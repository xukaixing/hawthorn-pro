package com.hawthorn.platform.hystrix;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.HystrixFallbackConfiguration;
import org.springframework.cloud.openfeign.HystrixFallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: TODO
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 4:06 下午
 * @version v1.0.1
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import({HystrixFallbackConfiguration.class})
//启用配置
public class MyFeignClientsConfiguration
{
  @Bean
  @Scope("prototype")
  public HystrixFallbackFactory hystrixFallbackFactory()
  {
    HystrixFallbackFactory hystrixFallbackFactory = new HystrixFallbackFactory();
    hystrixFallbackFactory.setFallbackHandlerFactory(new MyHystrixFallbackHandlerFactory());
    return hystrixFallbackFactory;
  }
}
