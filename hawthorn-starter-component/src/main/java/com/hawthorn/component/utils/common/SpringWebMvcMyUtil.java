package com.hawthorn.component.utils.common;

import com.hawthorn.component.constant.ApplicationConstant;
import com.hawthorn.component.utils.iassert.AssertMyUtil;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: SpringMVC的工具类
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:23 下午
 * @version v1.0.1
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringWebMvcMyUtil
{
  /**
   * 获取绑定在当前线程上下文下的HttpServletRequest对象
   * (通过{@link org.springframework.web.context.request.RequestContextListener}实现)
   * @return
   */
  public static HttpServletRequest getCurrentHttpServletRequest()
  {
    ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    if (requestAttributes != null)
    {
      return requestAttributes.getRequest();
    }
    return null;
  }

  /**
   * 获取绑定在当前线程上下文下的HttpServletRequest对象
   * (通过{@link org.springframework.web.context.request.RequestContextListener}实现)
   * @return
   */
  public static HttpServletResponse getCurrentHttpServletResponse()
  {
    ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    if (requestAttributes != null)
    {
      return requestAttributes.getResponse();
    }
    return null;
  }

  /**
   * 获取当前线程上下文下的HttpSession对象
   * @return
   */
  public static HttpSession getCurrentHttpSession()
  {
    HttpServletRequest request = getCurrentHttpServletRequest();
    if (request != null)
    {
      return request.getSession();
    }
    return null;
  }

  /**
   * 判断请求是否是异步请求
   * @param request
   * @param handler
   * @return
   */
  public static boolean isAsyncRequest(HttpServletRequest request, Object handler)
  {
    boolean isAsync = false;
    if (handler instanceof HandlerMethod)
    {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      isAsync = handlerMethod.hasMethodAnnotation(ResponseBody.class);
      if (!isAsync)
      {
        Class<?> controllerClass = handlerMethod.getBeanType();
        isAsync = controllerClass.isAnnotationPresent(ResponseBody.class) || controllerClass.isAnnotationPresent(RestController.class);
      }
      if (!isAsync)
      {
        isAsync = ResponseEntity.class.equals(handlerMethod.getMethod().getReturnType());
      }
    }
    if (!isAsync)
    {
      isAsync = ServletWebMyUtil.isAjaxRequest(request);
    }
    return isAsync;
  }

  /**
   * 通过SpringMVC的HttpMessageConverter来输出响应
   *
   * @param request        - 当前HttpServletRequest响应，如果过为空则从SpringMVC上下文中获取
   * @param response        - 当前HttpServletResponse响应，如果过为空则从SpringMVC上下文中获取
   * @param responseBody      - 输出的响应体
   * @param responseMediaTypes  - 输出的响应媒体类型(例如：application/json)
   * @throws IOException
   */
  public static void writeHttpMessage(HttpServletRequest request, HttpServletResponse response, Object responseBody, MediaType... responseMediaTypes) throws IOException
  {
    if (request == null)
    {
      request = getCurrentHttpServletRequest();
    }
    if (response == null)
    {
      response = getCurrentHttpServletResponse();
    }
    AssertMyUtil.notNull(request, "Parameter 'request' must be required!");
    AssertMyUtil.notNull(response, "Parameter 'response' must be required!");
    AssertMyUtil.state(!response.isCommitted(), "The http response has been committed!");
    if (responseBody != null)
    {
      HttpMessageConverters httpMessageConverters = ApplicationConstant.DEFAULT_HTTP_MESSAGE_CONVERTERS.get();

      List<MediaType> acceptedMediaTypes = null;
      if (CollectionUtils.isEmpty(acceptedMediaTypes) && !ArrayMyUtil.isEmpty(responseMediaTypes))
      { //如果参数指定了响应MediaType
        acceptedMediaTypes = Arrays.asList(responseMediaTypes); //使用参数指定的响应MediaType
      }
      if ((responseBody instanceof ResponseEntity))
      { //如果responseBody是ResponseEntity类型的
        ResponseEntity responseEntity = ((ResponseEntity) responseBody);
        if (responseEntity.getStatusCode() != null)
        {
          response.setStatus(responseEntity.getStatusCode().value());
        }
        if (CollectionUtils.isEmpty(acceptedMediaTypes))
        { //使用ResponseEntity中指定的响应MediaType
          HttpHeaders headers = responseEntity.getHeaders();
          if (headers != null && headers.getContentType() != null)
          {
            acceptedMediaTypes = Collections.singletonList(headers.getContentType());
          }
        }
        responseBody = responseEntity.getBody();
      }
      if (CollectionUtils.isEmpty(acceptedMediaTypes))
      { //其次从HttpServletResponse中获取响应MediaType
        try
        {
          MediaType mediaType = MediaType.parseMediaType(response.getContentType()); //
          if (mediaType != null)
          {
            acceptedMediaTypes = Collections.singletonList(mediaType);
          }
        } catch (Exception e)
        {
        }
      }
      if (CollectionUtils.isEmpty(acceptedMediaTypes))
      {
        acceptedMediaTypes = Collections.singletonList(ApplicationConstant.DEFAULT_RESPONSE_CONTENT_TYPE);
      }

      for (MediaType acceptedMediaType : acceptedMediaTypes)
      {
        for (HttpMessageConverter httpMessageConverter : httpMessageConverters)
        {
          if (httpMessageConverter.canWrite(responseBody.getClass(), acceptedMediaType))
          {
            try (ServletServerHttpResponse serverResponse = new ServletServerHttpResponse(response))
            {
              httpMessageConverter.write(responseBody, acceptedMediaType, serverResponse);
            }
            return;
          }
        }
      }
    } else
    {
      response.setStatus(HttpStatus.NO_CONTENT.value());
      response.flushBuffer();
    }
  }
}
