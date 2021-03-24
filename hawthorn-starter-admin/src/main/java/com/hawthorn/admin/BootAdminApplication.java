package com.hawthorn.admin;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
// @EnableSwagger2
@EnableOpenApi
// 在启动类上为@EnableFeignClients注解添加defaultConfiguration配置
//@EnableFeignClients(defaultConfiguration = FeignGlobalConfig.class)
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@MapperScan("com.hawthorn.admin.repository")
public class BootAdminApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(BootAdminApplication.class, args);
  }

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate()
  {
    return new RestTemplate();
  }

  // 此配置是为了服务监控而配置，与服务容错本身无关，
  // ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
  // 只要在自己的项目里配置上下面的servlet就可以了
  // 版本1.5之前是不需要进行配置的 但是2.x之后是需要对Hystrix进行配置的
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Bean
  public ServletRegistrationBean getServlet()
  {
    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
    registrationBean.setLoadOnStartup(1);
    registrationBean.addUrlMappings("/hystrix.stream");
    registrationBean.setName("HystrixMetricsStreamServlet");
    return registrationBean;
  }
}
