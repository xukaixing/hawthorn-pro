package com.hawthorn.gateway.config;

import com.hawthorn.gateway.ret.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: TODO
 * @author: andy.ten@tom.com
 * @date: 2020/11/22 下午9:00
 * @version v1.0.1
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse>
{

  @SuppressWarnings("NullableProblems")
  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest)
  {
    AtomicReference<String> originalUrl = new AtomicReference<>("");
    try
    {
      serverRequest.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
          .ifPresent(originalUrls -> originalUrl.set(originalUrls.toString()));
    } catch (Exception e)
    {
      e.printStackTrace();
    }

    log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUrl);
    return ServerResponse
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(RestResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "网关执行请求失败,hystrix服务降级处理", originalUrl)));
  }
}

