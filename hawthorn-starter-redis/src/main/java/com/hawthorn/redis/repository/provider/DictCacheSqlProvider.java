package com.hawthorn.redis.repository.provider;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 字典缓存sql类
 * @author: andy.ten@tom.com
 * @created: 3/4/21 11:23 PM
 * @lasted: 3/4/21 11:23 PM
 * @version v1.0.1
 */
public class DictCacheSqlProvider implements ProviderMethodResolver
{
  public static String selectDict()
  {
    String sql = "select d.id, d.value, d.label, d.type";
    sql += " from sys_dict d ";
    sql += " where d.del_flag = 0 ";
    sql += " order by d.sort ";
    return sql;
  }
}
