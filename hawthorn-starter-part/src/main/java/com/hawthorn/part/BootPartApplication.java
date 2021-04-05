package com.hawthorn.part;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableDiscoveryClient
@EnableOpenApi
@SpringBootApplication
public class BootPartApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootPartApplication.class, args);
  }
}
