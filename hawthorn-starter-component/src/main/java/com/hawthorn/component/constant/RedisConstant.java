package com.hawthorn.component.constant;

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
  // redis expire的超时时间：1天
  public static final Long EXPIRE_TIME_ONE_DAY = 1000 * 60 * 60 * 24L;
  // redis expire的超时时间：1小时
  public static final Long EXPIRE_TIME_ONE_HOUR = 1000 * 60 * 60L;
  // redis expire的超时时间：30分钟
  public static final Long EXPIRE_TIME_ONE_HALFHOUR = 1000 * 60 * 30L;

  // public static final String JWT = "hawthorn:auth:jwt:{0}:{1}";
  public static final String JWT = "auth:jwt:{0}";
  // 定义access_token的redis中的key
  public static final String ACCESS_TOKEN_KEY = "at";
  // 定义refresh_token的redis中的key
  public static final String REFRESH_TOKEN_KEY = "rt";
  // 定义登录用户的uuid的redis中的key
  public static final String LOGIN_UUID_KEY = "uuid";
  // 定义登录用户的ipaddress的redis中的key
  public static final String LOGIN_IPADDRESS_KEY = "ip";
  
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
