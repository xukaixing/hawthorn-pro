package com.hawthorn.login.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 用户权限（角色）实现类
 * @author:andy.ten@tom.com
 * @date:2020/10/9 9:23 下午
 * @version v1.0.1
 */
public class GrantedAuthorityImpl implements GrantedAuthority
{

  private static final long serialVersionUID = 1L;

  private String authority;

  public GrantedAuthorityImpl(String authority)
  {
    this.authority = authority;
  }

  public void setAuthority(String authority)
  {
    this.authority = authority;
  }

  @Override
  public String getAuthority()
  {
    return this.authority;
  }
}

