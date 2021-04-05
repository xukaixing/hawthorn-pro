package com.hawthorn.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
// 开启监控服务
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class BootMonitorApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootMonitorApplication.class, args);
  }
}

// 如果应用以war形式部署，启动文件做如下改变
/*
 public class BootMonitorApplication extends SpringBootServletInitializer
 {
 @Override protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
 {
 return builder.sources(BootMonitorApplication.class);
 }

 public static void main(String[] args)
 {
 SpringApplication.run(BootMonitorApplication.class, args);
 }
 }
 */
