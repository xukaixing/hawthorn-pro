package org.springframework.cloud.openfeign;

import com.hawthorn.component.utils.common.Str2Util;
import com.hawthorn.platform.ret.RestResult;
import org.springframework.http.HttpStatus;

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
  public static final HttpStatus DEFAULT_FALLBACK_STATUS = HttpStatus.SERVICE_UNAVAILABLE;

  public static RestResult defaultFallbackResult()
  {
    return RestResult.fail(DEFAULT_FALLBACK_STATUS.value(), Str2Util.format("请求失败: {}", DEFAULT_FALLBACK_STATUS.getReasonPhrase()));
  }
}
