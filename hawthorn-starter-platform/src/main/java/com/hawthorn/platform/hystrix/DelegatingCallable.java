package com.hawthorn.platform.hystrix;

import com.hawthorn.component.utils.iassert.AssertUtil;

import java.util.concurrent.Callable;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 代理的Callable, 解决将主线程的请求上下文参数添加到hystrix执行子线程中去
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:19 下午
 * @version v1.0.1
 */
public class DelegatingCallable<V> implements Callable<V>
{
  private final Callable<V> delegate;

  private final HystrixConcurrencyContext context;

  public DelegatingCallable(Callable<V> delegate, HystrixConcurrencyContext context)
  {
    super();
    AssertUtil.notNull(delegate, "Parameter 'delegate' can not be null!");
    AssertUtil.notNull(context, "Parameter 'context' can not be null!");
    this.delegate = delegate;
    this.context = context;
  }

  @Override
  public V call() throws Exception
  {
    try
    {
      HystrixConcurrencyContextHolder.setContext(context);
      return delegate.call();
    } finally
    {
      HystrixConcurrencyContextHolder.resetContext();
    }
  }
}
