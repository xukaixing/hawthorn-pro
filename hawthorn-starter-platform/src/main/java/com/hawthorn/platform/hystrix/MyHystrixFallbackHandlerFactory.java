package com.hawthorn.platform.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.HystrixFallbackHandler;
import org.springframework.cloud.openfeign.HystrixFallbackHandlerFactory;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: TODO
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 2:22 下午
 * @version v1.0.1
 */
@Slf4j
public class MyHystrixFallbackHandlerFactory implements HystrixFallbackHandlerFactory
{
  @Override
  public HystrixFallbackHandler createHandler(Class<?> feignClientClass, Throwable cause)
  {
    log.error("====== feign hystrix:[{}] | {} ===== ", feignClientClass.getName(), cause.getMessage());
    return new MyHystrixFallbackHandler(feignClientClass, cause);
  }
}
