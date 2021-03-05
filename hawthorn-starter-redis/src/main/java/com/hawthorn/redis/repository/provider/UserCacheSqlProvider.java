package com.hawthorn.redis.repository.provider;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 用户缓存sql类
 * @author: andy.ten@tom.com
 * @created: 3/4/21 11:23 PM
 * @lasted: 3/4/21 11:23 PM
 * @version v1.0.1
 */
public class UserCacheSqlProvider implements ProviderMethodResolver
{
  public static String selectUser()
  {
    String sql = "select u.id, u.name, u.user_type, u.dept_id";
    sql += " from sys_user u ";
    sql += " where u.status= 1";

    return sql;
  }
}
