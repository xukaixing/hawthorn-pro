package com.hawthorn.gateway.exception;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: gateway自定义全局异常处理, 主要捕获访问服务的503、504等错误；
 * @author: andy.ten@tom.com
 * @date: 2020/11/23 下午10:26
 * @version v1.0.1
 */
@Slf4j
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler
{
  public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                              ErrorProperties errorProperties, ApplicationContext applicationContext)
  {
    super(errorAttributes, resourceProperties, errorProperties, applicationContext);
  }


  /**
   * @remark: 获取异常属性
   * @param: request
   * @param: includeStackTrace
   * @return: Map<String, Object>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/11/23 下午10:27
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/11/23    andy.ten        v1.0.1             init
   */
  @Override
  protected Map<String, Object> getErrorAttributes(ServerRequest webRequest, ErrorAttributeOptions options)
  {
    int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    Throwable error = super.getError(webRequest);

    if (error instanceof org.springframework.cloud.gateway.support.NotFoundException)
    {
      code = 503;
    } else if (error instanceof org.springframework.cloud.gateway.support.TimeoutException)
      code = 504;
    assert error instanceof ResponseStatusException;
    ResponseStatusException err2 = (ResponseStatusException) error;
    HttpStatus c = err2.getStatus();
    code = c.value();
    return response(code, this.buildMessage(webRequest, error));
  }

  /**
   * @remark: 指定响应处理方法为JSON而不是HTML处理的方法
   * @param: errorAttributes
   * @return: org.springframework.web.reactive.function.server.RouterFunction<org.springframework.web.reactive.function.server.ServerResponse>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/11/23 下午10:28
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/11/23    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("NullableProblems")
  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes)
  {
    // 返回HTML
    //return RouterFunctions.route(acceptsTextHtml(), this::renderErrorView).andRoute(RequestPredicates.all(), this::renderErrorResponse);
    // 返回JSON
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }


  /**
   * @remark: 根据code获取对应的HttpStatus
   * @param: errorAttributes
   * @return: org.springframework.http.HttpStatus
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/11/23 下午10:28
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/11/23    andy.ten        v1.0.1             init
   */
  @Override
  protected int getHttpStatus(Map<String, Object> errorAttributes)
  {
    int statusCode = 500;
    if (errorAttributes.get("code") != null)
      statusCode = (int) errorAttributes.get("code");
    return statusCode;
  }


  /**
   * @remark: 构建错误信息输出格式
   * @param: request
   * @param: ex
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/11/23 下午10:29
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/11/23    andy.ten        v1.0.1             init
   */
  private String buildMessage(ServerRequest request, Throwable ex)
  {
    StringBuilder message = new StringBuilder("Failed [");
    message.append(request.methodName());
    message.append(" ");
    message.append(request.uri());
    message.append("]");
    if (ex != null)
    {
      message.append(": ");
      message.append(ex.getMessage());
    }
    return message.toString();
  }

  /**
   * @remark: 构建返回的JSON数据格式
   * @param: status
   * @param: errorMessage
   * @return: java.util.Map<java.lang.String, java.lang.Object>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/11/23 下午10:51
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/11/23    andy.ten        v1.0.1             init
   */
  public static Map<String, Object> response(int status, String errorMessage)
  {
    Map<String, Object> map = new HashMap<>();
    map.put("code", status);
    map.put("status", "fail");
    map.put("message", errorMessage);
    map.put("data", new JSONObject());
    return map;
  }
}
