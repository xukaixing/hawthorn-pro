package com.hawthorn.platform.hystrix;

import org.springframework.core.NamedThreadLocal;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: Hystrix并发上下文Holder
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:18 下午
 * @version v1.0.1
 */
public class HystrixConcurrencyContextHolder
{
  private static final ThreadLocal<HystrixConcurrencyContext> hystrixConcurrencyContextHolder = new NamedThreadLocal<>("Hystrix Concurrency Context");

  public static void resetContext()
  {
    hystrixConcurrencyContextHolder.remove();
  }

  public static void setContext(HystrixConcurrencyContext context)
  {
    hystrixConcurrencyContextHolder.set(context);
  }

  public static HystrixConcurrencyContext getContext()
  {
    return hystrixConcurrencyContextHolder.get();
  }
}
