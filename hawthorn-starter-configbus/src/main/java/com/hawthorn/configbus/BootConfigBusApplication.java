package com.hawthorn.configbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import springfox.documentation.oas.annotations.EnableOpenApi;


@EnableDiscoveryClient
@EnableConfigServer
@EnableOpenApi
@SpringBootApplication
public class BootConfigBusApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootConfigBusApplication.class, args);
  }
}
