package com.hawthorn.gateway.constant;

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
  // user redis
  public static final String REDIS_KEY_USER_PREFIX = "cache:user:{0}";
  public static final String REDIS_KEY_USERNAME = "name";
  public static final String REDIS_KEY_USERID = "id";
  public static final String REDIS_KEY_DEPTID = "deptid";
  public static final String REDIS_KEY_USERTYPE = "type";

  // dict redis
  public static final String REDIS_KEY_DICT_PREFIX = "cache:dict:{0}";
  public static final String REDIS_KEY_DICVALUE = "val";
  public static final String REDIS_KEY_DICID = "id";
  public static final String REDIS_KEY_DICLABEL = "label";
  public static final String REDIS_KEY_DICTYPE = "type";
}
