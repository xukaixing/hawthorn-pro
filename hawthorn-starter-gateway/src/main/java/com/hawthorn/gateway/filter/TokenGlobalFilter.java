package com.hawthorn.gateway.filter;

import com.hawthorn.gateway.config.CustomGateWayFilterConfig;
import com.hawthorn.gateway.config.JwtTokenConfig;
import com.hawthorn.gateway.constant.AdminConstant;
import com.hawthorn.gateway.exception.BizCode;
import com.hawthorn.gateway.provider.JwtTokenProvider;
import com.hawthorn.gateway.redis.RedisMyClient;
import com.hawthorn.gateway.ret.RestResult;
import com.hawthorn.gateway.utils.StringMyUtil;
import com.hawthorn.gateway.utils.json.JacksonMyUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: gateway全局token校验
 * @author: andy.ten@tom.com
 * @created: 2/24/21 5:26 PM
 * @lasted: 2/24/21 5:26 PM
 * @version v1.0.1
 */
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered
{
  @Autowired
  private JwtTokenConfig jwtTokenConfig;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @Autowired
  private RedisMyClient redisMyClient;

  /**
   * 不进行token校验的请求地址(白名单)
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
    // 获取请求url
    String requestUrl = request.getPath().toString();
    // 是否免校验
    boolean ignoreToken = false;

    // 请求方式是OPTIONS的时候 跳过
    String method = Objects.requireNonNull(request.getMethod()).name();
    // todo 忽略预检请求，可用于检测服务器允许的http方法
    if (RequestMethod.OPTIONS.name().equals(method))
    {
      ignoreToken = true;
    }

    if (!ignoreToken)
    {
      // todo 不进行token校验的请求地址,白名单
      if (customGateWayFilterConfig.getAllowPaths() != null)
        //ignoreToken = !(ignoreUrlList.parallelStream().filter(requestUrl::contains).count() == 0);
        ignoreToken = isAllowPath(request.getPath().toString());
    }
    if (ignoreToken)
    {
      return chain.filter(exchange);
    }

    // 获取Authorization请求头内的信息
    String authToken = jwtTokenProvider.getToken(request);

    // todo 验证token是空的情况
    if (StringMyUtil.isBlank(authToken))
    {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return getVoidMono(response, BizCode.AUTH_TOKEN_ISBLANK);
    }
    // 去掉token前缀(Bearer )，得到真实token
    authToken = authToken.substring(jwtTokenConfig.getTokenPrefix().length());

    // todo 验证token的前缀是否正确
    if (authToken.startsWith(jwtTokenConfig.getTokenPrefix()))
    {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return getVoidMono(response, BizCode.AUTH_TOKEN_INVALID);
    }
    // todo 检查Spring缓存中是否有此Token

    Claims claims = null;
    try
    {
      claims = jwtTokenProvider.verifyToken(authToken, "");
    } catch (Exception ex)
    {
      return getVoidMono(response, BizCode.UNKNOW_ERROR);
    }
    if (claims == null)
      return getVoidMono(response, BizCode.AUTH_TOKEN_INVALID);

    // 获取token里面的登录账号
    String loginAccount = claims.getSubject();
    // todo 验证loginAccount是空的情况
    if (StringMyUtil.isBlank(loginAccount))
    {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return getVoidMono(response, BizCode.AUTH_TOKEN_ACCOUNT_ISBLANK);
    }

    // todo 检查Redis中是否有此Token
    String sysJwt = StringMyUtil.placeHolder(AdminConstant.JWT, "login", loginAccount);
    if (!redisMyClient.hasKey(sysJwt))
    {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return getVoidMono(response, BizCode.AUTH_TOKEN_NOREDIS);
    }

    // todo 将用户信息放到header中
    ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("useraccount", loginAccount).build();
    ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
    return chain.filter(mutableExchange);

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
    return -200;
  }

  private boolean isAllowPath(String path)
  {
    List<String> allowPaths = customGateWayFilterConfig.getAllowPaths();
    //遍历白名单
    for (String allowPath : allowPaths)
    {
      //判断是否允许
      if (path.startsWith(allowPath))
      {
        return true;
      }
    }
    return false;
  }

}
