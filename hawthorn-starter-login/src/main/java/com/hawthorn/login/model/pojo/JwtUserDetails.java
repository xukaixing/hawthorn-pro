package com.hawthorn.login.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: TODO
 * @author:andy.ten@tom.com
 * @date:2020/10/11 3:30 下午
 * @version v1.0.1
 */
@Data
public class JwtUserDetails implements Serializable, UserDetails
{
  private UserInfo userInfo;
  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return authorities;
  }

  @Override
  public String getUsername()
  {
    return this.userInfo.getUsername();
  }

  public String getUserId()
  {
    return this.userInfo.getId();
  }

  @JsonIgnore
  @Override
  public String getPassword()
  {
    return this.userInfo.getPassword();
  }

  @JsonIgnore
  public String getSalt()
  {
    return this.userInfo.getSalt();
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired()
  {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked()
  {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled()
  {
    return true;
  }
}
