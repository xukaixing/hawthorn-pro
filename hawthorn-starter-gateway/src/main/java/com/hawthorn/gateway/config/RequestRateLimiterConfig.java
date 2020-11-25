package com.hawthorn.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: gateway令牌桶限流配置
 * @author: andy.ten@tom.com
 * @date: 2020/11/18 下午9:45
 * @version v1.0.1
 */
@Configuration
public class RequestRateLimiterConfig
{
  @Bean
  @Primary
  KeyResolver apiKeyResolver()
  {
    // 按URL限流,即以每秒内请求数按URL分组统计，超出限流的url请求都将返回429状态
    return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
  }

  @Bean
  KeyResolver userKeyResolver()
  {
    // 按用户限流
    return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getQueryParams().getFirst("user")));
  }

  @Bean
  KeyResolver ipKeyResolver()
  {
    // 按IP来限流
    return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName());
  }
}
