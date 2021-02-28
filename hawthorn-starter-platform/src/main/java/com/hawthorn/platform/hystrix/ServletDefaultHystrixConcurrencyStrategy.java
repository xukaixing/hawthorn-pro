package com.hawthorn.platform.hystrix;

import com.hawthorn.component.utils.common.SpringWebMvcMyUtil;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自定义策略，解决将主线程的请求上下文参数(HttpServletRequest,HttpServletResponse)添加到hystrix执行子线程中去
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:14 下午
 * @version v1.0.1
 */
public class ServletDefaultHystrixConcurrencyStrategy extends AbstractHystrixConcurrencyStrategy
{
  public ServletDefaultHystrixConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy)
  {
    super(existingConcurrencyStrategy);
  }

  @Override
  protected HystrixConcurrencyContext detectContext()
  {
    HystrixConcurrencyContext context = HystrixConcurrencyContextHolder.getContext();
    if (context == null)
    {
      context = new HystrixConcurrencyContext();
    }

    //请求上下文
    context.put(HttpServletRequest.class.getName(), SpringWebMvcMyUtil.getCurrentHttpServletRequest());
    context.put(HttpServletResponse.class.getName(), SpringWebMvcMyUtil.getCurrentHttpServletResponse());
    return context;
  }
}
