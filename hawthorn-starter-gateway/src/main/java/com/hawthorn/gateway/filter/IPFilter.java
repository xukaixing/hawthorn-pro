package com.hawthorn.gateway.filter;

import com.hawthorn.gateway.config.CustomGateWayFilterConfig;
import com.hawthorn.gateway.exception.BizCode;
import com.hawthorn.gateway.ret.RestResult;
import com.hawthorn.gateway.utils.http.IPMyUtil;
import com.hawthorn.gateway.utils.json.JacksonMyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo ip黑名单设置
 * @author: andy.ten@tom.com
 * @created: 2/24/21 5:26 PM
 * @lasted: 2/24/21 5:26 PM
 * @version v1.0.1
 */
@Component
public class IPFilter implements GlobalFilter, Ordered
{
  /**
   * ip黑名单地址
   */
  @Autowired
  private CustomGateWayFilterConfig customGateWayFilterConfig;

  /**
   * @remark: 拦截所有的请求头
   * @param: exchange
   * @param: chain
   * @return: reactor.core.publisher.Mono<java.lang.Void>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2/27/21 1:02 PM
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2/27/21    andy.ten        v1.0.1             init
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
  {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();
    if (denyIPs(IPMyUtil.getIpAddr(request)))
    {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return getVoidMono(response, BizCode.AUTH_IP_DENY);
    }
    return chain.filter(exchange);
  }

  /**
   * @remark: 返回结果
   * @param: serverHttpResponse
   * @param: bizcode
   * @return: reactor.core.publisher.Mono<java.lang.Void>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2/27/21 1:02 PM
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2/27/21    andy.ten        v1.0.1             init
   */
  private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, BizCode bizcode)
  {
    serverHttpResponse.getHeaders().add("ConteType", "application/json;charset=UTF-8");
    RestResult restResult = RestResult.fail(bizcode);
    DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JacksonMyUtil.object2Bytes(restResult));
    return serverHttpResponse.writeWith(Flux.just(dataBuffer));
  }

  @Override
  public int getOrder()
  {
    return -199;
  }

  /**
   * 黑名单
   * @param ips
   * @return
   */
  private boolean denyIPs(String ips)
  {
    List<String> denyIPs = customGateWayFilterConfig.getDenyIps();
    //遍历黑名单
    for (String denyIP : denyIPs)
    {
      //判断是否允许
      if (ips.startsWith(denyIP))
      {
        return true;
      }
    }
    return false;
  }

}
