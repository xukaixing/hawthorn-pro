package com.hawthorn.platform.config;

import lombok.Data;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 读取druid的yml配置，动态设置属性
 * @author:andy.ten@tom.com
 * @date:2020/8/25 10:27 上午
 * @version v1.0.1
 */
@Data
//@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidDataSourceProperties
{
  // jdbc
  private String driverClassName;
  private String url;
  private String username;
  private String password;
  // jdbc connection pool
  private int initialSize;
  private int minIdle;
  private int maxActive = 100;
  private long maxWait;
  private long timeBetweenEvictionRunsMillis;
  private long minEvictableIdleTimeMillis;
  private String validationQuery;
  private long validationQueryTimeout;
  private boolean testWhileIdle;
  private boolean testOnBorrow;
  private boolean testOnReturn;
  private boolean poolPreparedStatements;
  private int maxPoolPreparedStatementPerConnectionSize;
  private boolean useGlobalDataSourceStat;
  // filter
  private String filters;

}
