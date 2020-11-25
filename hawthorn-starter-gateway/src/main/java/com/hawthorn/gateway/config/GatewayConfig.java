package com.hawthorn.gateway.config;

import com.hawthorn.gateway.filter.factory.CustomRetryGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: gateway配置类
 * @author: andy.ten@tom.com
 * @date: 2020/11/16 下午12:04
 * @version v1.0.1
 */
@Configuration
public class GatewayConfig
{
  /**
   * @remark: 自定义路由转发规则，作用与yml中定义作用一致
   * @param: builder
   * @return: org.springframework.cloud.gateway.route.RouteLocator
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/11/15 下午5:20
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/11/15    andy.ten        v1.0.1             init
   */
  // @Bean
  // public RouteLocator customRouteLocator(RouteLocatorBuilder builder)
  // {
  //   return builder.routes()
  //       // 转发到claim的服务中
  //       .route("service-claim", r -> r.path("/claim/**")
  //           // 定义熔断
  //           //.filters()
  //           .uri("lb://hawthorn-claim"))
  //       .build();
  // }

  /**
   * @remark: 自定义重试
   * @param:
   * @return: CustomRetryGatewayFilterFactory
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/11/23 下午11:03
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/11/23    andy.ten        v1.0.1             init
   */
  @Bean
  public CustomRetryGatewayFilterFactory customRetryGatewayFilterFactory()
  {
    return new CustomRetryGatewayFilterFactory();
  }
}
