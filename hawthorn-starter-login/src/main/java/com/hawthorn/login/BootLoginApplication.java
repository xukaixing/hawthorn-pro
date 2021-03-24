package com.hawthorn.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@SpringBootApplication
//@EnableSwagger2
@EnableOpenApi
@MapperScan("com.hawthorn.login.repository")
public class BootLoginApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootLoginApplication.class, args);
  }
}
