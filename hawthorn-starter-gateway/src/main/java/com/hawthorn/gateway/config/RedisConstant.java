package com.hawthorn.gateway.config;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo redis缓存常量类
 * @author: andy.ten@tom.com
 * @created: 3/4/21 11:32 PM
 * @lasted: 3/4/21 11:32 PM
 * @version v1.0.1
 */
public class RedisConstant
{
  public static final String REDIS_KEY_USER_PREFIX = "cache:user:{0}";
  public static final String REDIS_KEY_USERNAME = "username";
  public static final String REDIS_KEY_USERID = "userid";
  public static final String REDIS_KEY_DEPTID = "deptid";
  public static final String REDIS_KEY_USERTYPE = "usertype";
}
