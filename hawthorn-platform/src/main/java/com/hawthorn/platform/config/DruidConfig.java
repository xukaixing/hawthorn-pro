package com.hawthorn.platform.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;

/**
 * @Copyright: Copyright (c) 2020 andyten
 * @remark: DruidConfig配置类, 增加@Configuration注解后，会与druid自带的自动装载冲突
 * @author:andy.ten@tom.com
 * @date:2020/8/14 2:01 上午
 * @version v1.0.1
 */
//@Configuration
//@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class DruidConfig
{
  @Bean
  @ConfigurationProperties("spring.datasource.druid")
  public DataSource dataSource()
  {
    return DruidDataSourceBuilder.create().build();
  }

  // private final DruidDataSourceProperties properties;
  //
  // @Autowired
  // public DruidConfig(DruidDataSourceProperties properties)
  // {
  //   this.properties = properties;
  // }

  // @Bean
  // @ConditionalOnMissingBean
  // public DataSource druidDataSource()
  // {
  //   DruidDataSource druidDataSource = new DruidDataSource();
  //   druidDataSource.setDriverClassName(properties.getDriverClassName());
  //   druidDataSource.setUrl(properties.getUrl());
  //   druidDataSource.setUsername(properties.getUsername());
  //   druidDataSource.setPassword(properties.getPassword());
  //   druidDataSource.setInitialSize(properties.getInitialSize());
  //   druidDataSource.setMinIdle(properties.getMinIdle());
  //   druidDataSource.setMaxActive(properties.getMaxActive());
  //   druidDataSource.setMaxWait(properties.getMaxWait());
  //   druidDataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
  //   druidDataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
  //   druidDataSource.setValidationQuery(properties.getValidationQuery());
  //   druidDataSource.setTestWhileIdle(properties.isTestWhileIdle());
  //   druidDataSource.setTestOnBorrow(properties.isTestOnBorrow());
  //   druidDataSource.setTestOnReturn(properties.isTestOnReturn());
  //   druidDataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
  //   druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
  //
  //   try
  //   {
  //     druidDataSource.setFilters(properties.getFilters());
  //     druidDataSource.init();
  //   } catch (SQLException e)
  //   {
  //     e.printStackTrace();
  //   }
  //
  //   return druidDataSource;
  // }

  /**
   * 注册Servlet信息， 配置监控视图
   *
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public ServletRegistrationBean<Servlet> druidServlet()
  {
    ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

    //白名单：
//        servletRegistrationBean.addInitParameter("allow","127.0.0.1,139.196.87.48");
    //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
    servletRegistrationBean.addInitParameter("deny", "192.168.1.119");
    //登录查看信息的账号密码, 用于登录Druid监控后台
    servletRegistrationBean.addInitParameter("loginUsername", "admin");
    servletRegistrationBean.addInitParameter("loginPassword", "admin");
    //是否能够重置数据.
    servletRegistrationBean.addInitParameter("resetEnable", "true");
    return servletRegistrationBean;

  }

  /**
   * 注册Filter信息, 监控拦截器
   *
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public FilterRegistrationBean<Filter> filterRegistrationBean()
  {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new WebStatFilter());
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    return filterRegistrationBean;
  }
}
