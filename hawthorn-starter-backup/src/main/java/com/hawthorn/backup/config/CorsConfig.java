package com.hawthorn.backup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 跨域配置
 * @author:andy.ten@tom.com
 * @date:2020/10/25 9:20 下午
 * @version v1.0.1
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer
{

  @Override
  public void addCorsMappings(CorsRegistry registry)
  {
    registry.addMapping("/**")  // 允许跨域访问的路径
        .allowedOrigins("*")  // 允许跨域访问的源
        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")  // 允许请求方法
        .maxAge(168000)  // 预检间隔时间
        .allowedHeaders("*")  // 允许头部设置
        .allowCredentials(true);  // 是否发送cookie
  }
}
