package org.springframework.cloud.openfeign;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: HystrixFallbackHandler工厂
 * @author: andy.ten@tom.com
 * @date: 2020/10/30 5:13 下午
 * @version v1.0.1
 */
public interface HystrixFallbackHandlerFactory
{
  HystrixFallbackHandler createHandler(Class<?> feignClientClass, Throwable cause);
}
