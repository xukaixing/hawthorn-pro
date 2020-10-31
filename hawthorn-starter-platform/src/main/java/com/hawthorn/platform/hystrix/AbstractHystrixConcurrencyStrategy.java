package com.hawthorn.platform.hystrix;


import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自定义策略, 解决将主线程的请求上下文参数(HttpServletRequest, HttpServletResponse, Authentication等)添加到hystrix执行子线程中去
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:15 下午
 * @version v1.0.1
 */
public abstract class AbstractHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy
{
  private HystrixConcurrencyStrategy existingConcurrencyStrategy;

  public AbstractHystrixConcurrencyStrategy(
      HystrixConcurrencyStrategy existingConcurrencyStrategy)
  {
    this.existingConcurrencyStrategy = existingConcurrencyStrategy;
  }

  @Override
  public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize)
  {
    return existingConcurrencyStrategy != null
        ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
        : super.getBlockingQueue(maxQueueSize);
  }

  @Override
  public <T> HystrixRequestVariable<T> getRequestVariable(
      HystrixRequestVariableLifecycle<T> rv)
  {
    return existingConcurrencyStrategy != null
        ? existingConcurrencyStrategy.getRequestVariable(rv)
        : super.getRequestVariable(rv);
  }

  @Override
  public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                          HystrixProperty<Integer> corePoolSize,
                                          HystrixProperty<Integer> maximumPoolSize,
                                          HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                          BlockingQueue<Runnable> workQueue)
  {
    return existingConcurrencyStrategy != null
        ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize,
        maximumPoolSize, keepAliveTime, unit, workQueue)
        : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
        keepAliveTime, unit, workQueue);
  }

  @Override
  public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                          HystrixThreadPoolProperties threadPoolProperties)
  {
    return existingConcurrencyStrategy != null
        ? existingConcurrencyStrategy.getThreadPool(threadPoolKey,
        threadPoolProperties)
        : super.getThreadPool(threadPoolKey, threadPoolProperties);
  }

  @Override
  public <T> Callable<T> wrapCallable(Callable<T> callable)
  {
    HystrixConcurrencyContext context = detectContext();
    Callable<T> delegate = new DelegatingCallable<T>(callable, context);
    return existingConcurrencyStrategy != null ? existingConcurrencyStrategy.wrapCallable(delegate) : super.wrapCallable(delegate);
  }

  protected abstract HystrixConcurrencyContext detectContext();
}
