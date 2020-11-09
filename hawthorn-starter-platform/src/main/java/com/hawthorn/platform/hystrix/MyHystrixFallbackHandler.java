package com.hawthorn.platform.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.HystrixFallbackHandler;
import org.springframework.cloud.openfeign.HystrixFallbackResults;

import java.lang.reflect.Method;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 默认的服务服务熔断|降级实现工厂类,通过Cglib动态代理来实现统一的服务熔断|降级逻辑
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 2:22 下午
 * @version v1.0.1
 */
@Slf4j
public class MyHystrixFallbackHandler extends HystrixFallbackHandler
{
  public MyHystrixFallbackHandler(Class<?> feignClientClass, Throwable cause)
  {
    super(feignClientClass, cause);
  }

  @Override
  protected Object handle(Object proxy, Method method, Object[] args)
  {
    Class<?> returnType = method.getReturnType();

    return HystrixFallbackResults.defaultFallbackResult(super.getFeignClientClass(), method, super.getCause());
  }
}
