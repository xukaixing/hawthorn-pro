package com.hawthorn.hystrixdashboard;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTurbine
// 启动hystrix监控
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class BootHystrixDashboardApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootHystrixDashboardApplication.class, args);
  }
}
