package com.hawthorn.platform.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自定义策略, 解决将主线程的请求上下文(HttpServletRequest, HttpServletResponse, Authentication)参数添加到hystrix执行子线程中去
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 7:48 下午
 * @version v1.0.1
 */
public class ServletSecurityHystrixConcurrencyStrategy extends ServletDefaultHystrixConcurrencyStrategy
{
  public ServletSecurityHystrixConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy)
  {
    super(existingConcurrencyStrategy);
  }

  @Override
  protected HystrixConcurrencyContext detectContext()
  {
    HystrixConcurrencyContext context = super.detectContext();

    //spring-security
    //context.put(Authentication.class.getName(), SpringSecurityUtil.getCurrentAuthentication());
    return context;
  }

}
