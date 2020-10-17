package com.hawthorn.login.security;

import cn.hutool.core.util.StrUtil;
import com.hawthorn.login.config.JwtTokenConfig;
import com.hawthorn.login.model.pojo.JwtUserDetails;
import com.hawthorn.login.provider.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 该filter会拦截除登录请求之外的请求，并进行token解析验证
 * @author:andy.ten@tom.com
 * @date:2020/10/9 9:17 下午
 * @version v1.0.1
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter
{

  @Autowired
  public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager)
  {
    super(authenticationManager);
  }

  @Autowired
  private JwtProvider jwtProvider;
  @Autowired
  private JwtTokenConfig jwtTokenConfig;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException
  {
    // 拿到Authorization请求头内的信息
    String authToken = jwtProvider.getToken(request);

    // 判断一下内容是否为空
    if (StrUtil.isNotEmpty(authToken) && authToken.startsWith(jwtTokenConfig.getTokenPrefix()))
    {
      // 去掉token前缀(Bearer )，拿到真实token
      authToken = authToken.substring(jwtTokenConfig.getTokenPrefix().length());

      // 拿到token里面的登录账号
      String loginAccount = jwtProvider.getSubjectFromToken(authToken);

      if (StrUtil.isNotEmpty(loginAccount) && SecurityContextHolder.getContext().getAuthentication() == null)
      {
        // 从内存中查询用户
        JwtUserDetails userDetails = null;

        // 拿到用户信息后验证用户信息与token
        if (userDetails != null && jwtProvider.validateToken(authToken, userDetails))
        {

          // 组装authentication对象，构造参数是Principal Credentials 与 Authorities
          // 后面的拦截器里面会用到 grantedAuthorities 方法
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

          // 将authentication信息放入到上下文对象中
          SecurityContextHolder.getContext().setAuthentication(authentication);

          log.info("JWT过滤器通过校验请求头token自动登录成功, user : {}", userDetails.getUsername());
        }
      }
    }

    chain.doFilter(request, response);
  }
}
