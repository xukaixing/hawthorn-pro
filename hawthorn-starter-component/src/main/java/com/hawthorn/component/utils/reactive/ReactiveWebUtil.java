package com.hawthorn.component.utils.reactive;

import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 基于Reactive的web应用工具类
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 8:15 下午
 * @version v1.0.1
 */
public class ReactiveWebUtil
{
  /**
   * 判断来的请求是否是ajax异步请求
   * @param request
   * @return
   */
  public static boolean isAjaxRequest(ServerRequest request)
  {
    return "XMLHttpRequest".equals(request.headers().asHttpHeaders().getFirst("X-Requested-With"));
  }
}
