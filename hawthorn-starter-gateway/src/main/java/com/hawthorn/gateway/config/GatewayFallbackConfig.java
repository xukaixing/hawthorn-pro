package com.hawthorn.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 网关熔断配置
 * @author: andy.ten@tom.com
 * @date: 2020/11/22 下午8:52
 * @version v1.0.1
 */
@Configuration
public class GatewayFallbackConfig
{
  @Autowired
  private HystrixFallbackHandler hystrixFallbackHandler;

  @Bean
  public RouterFunction routerFunction()
  {
    return RouterFunctions.route(
        RequestPredicates.GET("/defaultfallback")
            .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler);
  }
}
