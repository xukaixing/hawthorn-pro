package com.hawthorn.gateway.constant;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 定义系统级常量
 * @author:andy.ten@tom.com
 * @date:2020/8/23 5:29 下午
 * @version v1.0.1
 */
public class AdminConstant
{
  // session保存的登录页图片验证码
  public static final String SESSION_VERIFYCODE = "VerifyCode";
  // admin权限的默认账号名
  public static final String ADMIN = "admin";
  // superman权限默认的账号名
  public static final String SUPERMAN = "superman";
  // redis expire的超时时间：1天
  public static final Long EXPIRE_TIME_ONE_DAY = 1000 * 60 * 60 * 24L;
  // redis expire的超时时间：1小时
  public static final Long EXPIRE_TIME_ONE_HOUR = 1000 * 60 * 60L;
  // redis expire的超时时间：30分钟
  public static final Long EXPIRE_TIME_ONE_HALFHOUR = 1000 * 60 * 30L;
  public static final String JWT = "hawthorn:auth:jwt:{0}:{1}";
  //定义token的名称
  public static final String TOKEN_NAME = "jwt";

  private AdminConstant()
  {
  }
}
