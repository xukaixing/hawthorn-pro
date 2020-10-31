package com.hawthorn.login.security;

import com.hawthorn.component.exception.BizCode;
import com.hawthorn.component.exception.BizException;
import com.hawthorn.component.utils.iassert.AssertUtil;
import com.hawthorn.login.model.pojo.JwtUserDetails;
import com.hawthorn.login.utils.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自定义身份验证Provider
 * <p>UsernamePasswordAuthenticationToken的默认Provider是DaoAuthenticationProvider</p>
 * <p>该方法主要验证2点：1.通过账号是否能获取用户,即用户名是否正确；2.验证获取用户的密码是否匹配</p>
 * @author:andy.ten@tom.com
 * @date:2020/10/9 9:11 下午
 * @version v1.0.1
 */
public class UsernameAuthenticationProvider extends DaoAuthenticationProvider
{
  public UsernameAuthenticationProvider(UserDetailsService userDetailsService)
  {
    setUserDetailsService(userDetailsService);
  }

  /**
   * 附加的信息检查（密码检查）
   * @param userDetails
   * @param authentication
   */
  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
  {
    String loginUsername = authentication.getName();
    String loginPassword = (String) authentication.getCredentials();
    AssertUtil.notEmpty(loginUsername, BizCode.AUTH_LOGINACCOUNT_NOTEMPTY);
    AssertUtil.notEmpty(loginPassword, BizCode.AUTH_LOGINPASSWROD_NOTEMPTY);

    if (userDetails == null)
      throw new BizException(BizCode.AUTH_LOGINUSERNAME_INCORRECT);
    // 比较前端传入的密码(明文)和加密的密码是否相等,明文密码会和加密盐一起merge加密
    String salt = ((JwtUserDetails) userDetails).getSalt();
    if (!new PasswordEncoder(salt).matches(userDetails.getPassword(), loginPassword))
      throw new BizException(BizCode.AUTH_LOGINPASSWROD_INCORRECT);

    // 验证通过后，生成新的带用户权限信息的authentication
    //return new JwtAuthenticationToken(loginUsername, loginPassword, loadedUser.getAuthorities());
  }
}
