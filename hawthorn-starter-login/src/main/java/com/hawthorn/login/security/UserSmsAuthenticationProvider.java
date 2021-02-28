package com.hawthorn.login.security;

import com.hawthorn.component.exception.BizCode;
import com.hawthorn.component.exception.BizException;
import com.hawthorn.component.utils.iassert.AssertMyUtil;
import com.hawthorn.login.model.pojo.JwtUserDetails;
import com.hawthorn.login.utils.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 用户自定义身份认证, 短信验证码模式（预留）
 * <p>UsernamePasswordAuthenticationToken的默认Provider是DaoAuthenticationProvider</p>
 * @author:andy.ten@tom.com
 * @date:2020/10/9 9:11 下午
 * @version v1.0.1
 */
public class UserSmsAuthenticationProvider implements AuthenticationProvider
{
  private UserDetailsService userDetailsService;

  public UserSmsAuthenticationProvider(UserDetailsService userDetailsServices)
  {
  }

  /**
   * @remark: 认证处理，返回一个Authentication的实现类则代表认证成功，返回null则代表认证失败
   * @param: authentication
   * @return: org.springframework.security.core.Authentication
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/14 9:07 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/14    andy.ten        v1.0.1             init
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException
  {
    String loginUsername = authentication.getName();
    String smscode = (String) authentication.getCredentials();
    AssertMyUtil.notEmpty(loginUsername, BizCode.AUTH_LOGINACCOUNT_NOTEMPTY);
    AssertMyUtil.notEmpty(smscode, BizCode.AUTH_LOGINPASSWROD_NOTEMPTY);

    // 获取用户信息
    UserDetails loadedUser = userDetailsService.loadUserByUsername(loginUsername);
    if (loadedUser == null)
      throw new BizException(BizCode.AUTH_LOGINUSERNAME_INCORRECT);
    // 比较前端传入的密码明文和加密的密码是否相等
    String salt = ((JwtUserDetails) loadedUser).getSalt();
    if (!new PasswordEncoder(salt).matches(loadedUser.getPassword(), smscode))
      throw new BizException(BizCode.AUTH_LOGINPASSWROD_INCORRECT);

    // 验证通过后，生成新的带用户权限信息的authentication
    return new JwtAuthenticationToken(loginUsername, smscode, loadedUser.getAuthorities());
  }

  /**
   * @remark: 如果该AuthenticationProvider支持传入的Authentication对象，则返回true
   * @param: aClass
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/14 9:29 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/14    andy.ten        v1.0.1             init
   */
  @Override
  public boolean supports(Class<?> aClass)
  {
    return aClass.equals(UsernamePasswordAuthenticationToken.class);
  }
}
