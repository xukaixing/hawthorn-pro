package com.hawthorn.login.service;

import com.hawthorn.component.ret.RestResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @Copyright: Copyright (c) 2020 andyten
 * * <p></p>
 * @remark: 认证权限服务
 * @author:andy.ten@tom.com
 * @date:2020/8/13 3:57 下午
 * @version v1.0.1
 */
public interface AuthService
{
  RestResult login(HttpServletRequest request, String loginAccount, String loginPassword);

  boolean loginOut();

  boolean refreshToken();
}
