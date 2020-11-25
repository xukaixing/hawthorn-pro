package com.hawthorn.platform.aspect;

import com.alibaba.fastjson.JSONObject;
import com.hawthorn.component.ret.RestResult;
import com.hawthorn.component.utils.json.JacksonUtil;
import com.hawthorn.platform.config.ResponseAdviceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @Copyright: Copyright (c) 2020 andyten

 * @remark: 在注解@ResponseBody将返回值处理成相应格式之前封装返回值
 * @author:andy.ten@tom.com
 * @date:2020/8/20 3:16 下午
 * @version v1.0.1
 */
@ControllerAdvice
@AutoConfigureAfter(ResponseAdviceConfig.class)
public class ResponseBodyAdviceImpl implements ResponseBodyAdvice<Object>
{
  @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
  @Autowired(required = false)
  ResponseAdviceConfig responseAdviceConfig;

  @SuppressWarnings("NullableProblems")
  @Override
  public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass)
  {
    // 定义哪些类需要拦截处理 return true 表示全部controller
    return true;
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public Object beforeBodyWrite(Object o, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response)
  {
    // 如果返回值是BaseResult类型，则直接返回
    if (o instanceof RestResult)
    {
      return o;
    }
    // 判断是否存在responseConfig 的实现类，如果存在实现类，则使用实现类进行判断
    if (responseAdviceConfig != null)
    {
      return responseAdviceConfig.sucessResponse(request, response, o, returnType);
    }
    String returnTypeName = returnType.getParameterType().getName();
    // 方法返回值是"void"
    if ("void".equals(returnTypeName))
    {
      return RestResult.success(new JSONObject());
    }
    // 方法返回值是"String"
    if (Objects.requireNonNull(returnType.getMethod()).getReturnType().equals(String.class))
    {
      return JacksonUtil.object2Json(RestResult.success(o));
    }
    // 屏蔽swagger接口api + Actuator监控路径请求
    if (request.getURI().toString().contains("/swagger") || request.getURI().toString().contains("/v2/api-docs") || request.getURI().toString().contains("/actuator"))
    {
      return o;
    }

    return RestResult.success(o);
  }
}
