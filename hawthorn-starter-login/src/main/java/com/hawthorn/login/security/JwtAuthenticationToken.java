package com.hawthorn.login.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 根据登录页传递的username、password创建的未认证的token
 * @author:andy.ten@tom.com
 * @date:2020/10/9 9:22 下午
 * @version v1.0.1
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken
{

  private static final long serialVersionUID = 1L;

  private String token;

  public JwtAuthenticationToken(Object principal, Object credentials)
  {
    super(principal, credentials);
  }

  public JwtAuthenticationToken(Object principal, Object credentials, String token)
  {
    super(principal, credentials);
    this.token = token;
  }

  public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
  {
    super(principal, credentials, authorities);
  }

  public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token)
  {
    super(principal, credentials, authorities);
    this.token = token;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken(String token)
  {
    this.token = token;
  }

  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

}
