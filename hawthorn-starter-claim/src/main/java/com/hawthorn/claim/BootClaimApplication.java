package com.hawthorn.claim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableDiscoveryClient
@EnableOpenApi
@SpringBootApplication
public class BootClaimApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootClaimApplication.class, args);
  }
}
