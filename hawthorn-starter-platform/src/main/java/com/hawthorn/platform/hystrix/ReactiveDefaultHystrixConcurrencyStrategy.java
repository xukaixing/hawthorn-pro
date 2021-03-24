package com.hawthorn.platform.hystrix;

import com.hawthorn.platform.utils.common.SpringWebFluxMyUtil;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.springframework.web.server.ServerWebExchange;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自定义策略, 解决将主线程的请求上下文参数(HttpServletRequest, HttpServletResponse)添加到hystrix执行子线程中去
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 8:08 下午
 * @version v1.0.1
 */
public class ReactiveDefaultHystrixConcurrencyStrategy extends AbstractHystrixConcurrencyStrategy
{
  public ReactiveDefaultHystrixConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy)
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
    context.put(ServerWebExchange.class.getName(), SpringWebFluxMyUtil.getCurrentServerWebExchange());
    return context;
  }
}
