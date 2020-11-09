package org.springframework.cloud.openfeign;

import com.hawthorn.component.constant.HttpStatusConstant;
import com.hawthorn.component.utils.common.Str2Util;
import com.hawthorn.platform.ret.RestResult;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import feign.RetryableException;

import java.lang.reflect.Method;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 默认的熔断fallback结果
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 2:29 下午
 * @version v1.0.1
 */
public class HystrixFallbackResults
{
  public static final HttpStatusConstant DEFAULT_FALLBACK_STATUS = HttpStatusConstant.SERVICE_UNAVAILABLE;
  public static final HttpStatusConstant TIMEOUT_FALLBACK_STATUS = HttpStatusConstant.REQUEST_TIMEOUT;
  public static final HttpStatusConstant SERVICE_RETRYABLE_ONEC = HttpStatusConstant.SERVICE_RETRYABLE_ONEC;

  public static RestResult defaultFallbackResult(Class<?> feignClientClass, Method method, Throwable cause)
  {
    if (cause instanceof HystrixTimeoutException)
      return RestResult.fail(TIMEOUT_FALLBACK_STATUS.value(), TIMEOUT_FALLBACK_STATUS.getReasonPhrase(), Str2Util.format("feign service call error: [{}.{}]| {}", feignClientClass.getName(), method.getName(), "timeout"));
    if (cause instanceof RetryableException)
      return RestResult.fail(SERVICE_RETRYABLE_ONEC.value(), SERVICE_RETRYABLE_ONEC.getReasonPhrase(), Str2Util.format("feign service call error: [{}.{}]| {}", feignClientClass.getName(), method.getName(), cause.getMessage()));
    String msg = cause.getMessage();
    if (msg.contains("available server"))
      return RestResult.fail(DEFAULT_FALLBACK_STATUS.value(), DEFAULT_FALLBACK_STATUS.getReasonPhrase(), Str2Util.format("feign service call error: [{}.{}]| {}", feignClientClass.getName(), method.getName(), cause.getMessage()));
    else if (msg.contains("timeout"))
      return RestResult.fail(TIMEOUT_FALLBACK_STATUS.value(), TIMEOUT_FALLBACK_STATUS.getReasonPhrase(), Str2Util.format("feign service call error: [{}.{}]| {}", feignClientClass.getName(), method.getName(), cause.getMessage()));
    else
      return RestResult.fail(DEFAULT_FALLBACK_STATUS.value(), DEFAULT_FALLBACK_STATUS.getReasonPhrase(), Str2Util.format("feign service call error: [{}.{}]| {}", feignClientClass.getName(), method.getName(), cause.getMessage()));
  }
}
