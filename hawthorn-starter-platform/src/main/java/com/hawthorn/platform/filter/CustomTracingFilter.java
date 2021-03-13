package com.hawthorn.platform.filter;

import brave.Span;
import brave.Tracer;
import com.hawthorn.component.constant.HeaderConstant;
import com.hawthorn.component.constant.MdcConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 自定义sleuth链路过滤器
 * @author: andy.ten@tom.com
 * @created: 3/11/21 3:27 PM
 * @lasted: 3/11/21 3:27 PM
 * @version v1.0.1
 */
@Component
@Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
@Slf4j
public class CustomTracingFilter extends GenericFilterBean
{

  private final Tracer tracer;

  CustomTracingFilter(Tracer tracer)
  {
    this.tracer = tracer;
  }

  /**
   * @remark: todo 自定义扩展
   * @param: request
   * @param: response
   * @param: chain
   * @return: void
   * <p>
   *   1. 给请求添加自定义的标记；
   *   2. 将请求 ID 添加到响应头返回给客户端
   * </p>
   * @author: andy.ten@tom.com
   * @date: 3/11/21 3:30 PM
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 3/11/21    andy.ten        v1.0.1             init
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException
  {
    Span currentSpan = this.tracer.currentSpan();
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    //log.debug("trace-path:{}", httpServletRequest.getServletPath());

    if (currentSpan == null)
    {
      chain.doFilter(request, response);
      return;
    }

    // 返回响应header中增加traceid
    ((HttpServletResponse) response).addHeader("TraceId", currentSpan.context().traceIdString());
    // 添加自定义字段到当前span当中
    currentSpan.tag("request.time", LocalDateTime.now().toString());
    // 方法名
    String method = httpServletRequest.getMethod();
    currentSpan.tag("request.method", method);

    // 添加参数信息
    //RequestWrapper wrapperRequest = new RequestWrapper(httpServletRequest);// 转换成代理类
    //String params = Pub.getRequestParameter(wrapperRequest);
    //currentSpan.tag("http.params", params);

    // 设置日志中，traceid和useraccount关联：%X{userAccount:-useraccount}
    MDC.put(MdcConstant.USER_ACCOUNT, httpServletRequest.getHeader(HeaderConstant.USER_ACCOUNT));
    try
    {
      chain.doFilter(request, response);
    } finally
    {
      MDC.clear();//must be,threadLocal
    }
  }
}
