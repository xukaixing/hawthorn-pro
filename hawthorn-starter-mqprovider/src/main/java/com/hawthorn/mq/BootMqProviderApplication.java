package com.hawthorn.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
//@EnableSwagger2
@EnableOpenApi
@EnableDiscoveryClient
public class BootMqProviderApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootMqProviderApplication.class, args);
  }
}
