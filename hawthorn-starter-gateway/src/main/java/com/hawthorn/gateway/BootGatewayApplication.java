package com.hawthorn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: gateway与springboot-web有jar包冲突，所以独立建一个工程
 * @author: andy.ten@tom.com
 * @date: 2020/11/8 11:20 上午
 * @version v1.0.1
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BootGatewayApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootGatewayApplication.class, args);
  }
}
