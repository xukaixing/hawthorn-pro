package com.hawthorn.login.service.impl;

import com.hawthorn.component.constant.AdminConstant;
import com.hawthorn.component.ret.RestResult;
import com.hawthorn.component.utils.common.StringMyUtil;
import com.hawthorn.login.model.pojo.AccessToken;
import com.hawthorn.login.model.pojo.JwtUserDetails;
import com.hawthorn.login.provider.JwtTokenProvider;
import com.hawthorn.login.security.GrantedAuthorityImpl;
import com.hawthorn.login.security.JwtAuthenticationToken;
import com.hawthorn.login.service.AuthService;
import com.hawthorn.login.service.SysUserService;
import com.hawthorn.platform.redis.RedisMyClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 认证权限服务实现类
 * @author:andy.ten@tom.com
 * @date:2020/10/12 11:30 上午
 * @version v1.0.1
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService
{
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @Autowired
  private SysUserService sysUserService;
  @Autowired
  private RedisMyClient redisMyClient;

  @Override
  public RestResult login(HttpServletRequest request, String loginAccount, String loginPassword)
  {
    log.info("====== 登录系统认证 ======");
    JwtAuthenticationToken usernameAuthentication = new JwtAuthenticationToken(loginAccount, loginPassword);
    usernameAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    // todo 认证
    Authentication authentication = authenticationManager.authenticate(usernameAuthentication);
    // todo 认证成功存储认证信息到上下文
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // todo 生成自定义令牌并返回给客户端
    JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
    AccessToken accessToken = jwtTokenProvider.createToken(userDetails);

    // todo 授予权限
    // 用户名和密码通过后，添加用户授权信息
    // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
    Set<String> permissions = sysUserService.findPermissions(loginAccount);
    List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
    userDetails.setAuthorities(grantedAuthorities);

    // todo 将令牌放入redis key为token值
    String sysJwt = StringMyUtil.placeHolder(AdminConstant.JWT, "login", accessToken.getToken());
    //redisMyClient.set(sysJwt, accessToken.getToken(), AdminConstant.EXPIRE_TIME_ONE_HOUR);
    // todo 将令牌放入redis key为userid
    //String sysJwtUserId = StringMyUtil.placeHolder(AdminConstant.JWT, "login", userDetails.getUserInfo().getId());
    //redisMyClient.set(sysJwtUserId, accessToken.getToken(), AdminConstant.EXPIRE_TIME_ONE_HOUR);
    // todo 将令牌放入redis key为username
    String sysJwtUserName = StringMyUtil.placeHolder(AdminConstant.JWT, "login", userDetails.getUsername());
    redisMyClient.set(sysJwtUserName, accessToken.getToken(), AdminConstant.EXPIRE_TIME_ONE_HOUR);
    return RestResult.success(accessToken);
  }

  @Override
  public boolean loginOut()
  {
    return false;
  }

  @Override
  public boolean refreshToken()
  {
    return false;
  }
}
