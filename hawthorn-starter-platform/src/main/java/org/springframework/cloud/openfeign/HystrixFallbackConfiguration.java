package org.springframework.cloud.openfeign;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: TODO
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 2:19 下午
 * @version v1.0.1
 */
@Configuration
@ConditionalOnClass(Feign.class)
public class HystrixFallbackConfiguration
{
  /** 覆盖 FeignAutoConfiguration 中的默认配置 */
  @Configuration
  @ConditionalOnClass(name = "feign.hystrix.HystrixFeign")
  protected static class HystrixFeignTargeterConfiguration
  {
    @Bean
    @ConditionalOnMissingBean
    public Targeter feignTargeter()
    {
      return new HystrixFallbackTargeter();
    }
  }
}
