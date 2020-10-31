package com.hawthorn.platform.hystrix;

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
public class MyHystrixFallbackHandlerFactory implements HystrixFallbackHandlerFactory
{
  @Override
  public HystrixFallbackHandler createHandler(Class<?> feignClientClass, Throwable cause)
  {
    return new MyHystrixFallbackHandler(feignClientClass, cause);
  }
}
