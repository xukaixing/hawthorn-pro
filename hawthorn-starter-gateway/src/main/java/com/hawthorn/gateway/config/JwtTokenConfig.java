package com.hawthorn.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: JWT Token 配置类
 * @author:andy.ten@tom.com
 * @date:2020/10/11 4:31 下午
 * @version v1.0.1
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenConfig
{
  /**
   * 签发者
   */
  private String issuer = "hawthorn";

  /**
   * 密匙
   */
  private String apiSecretKey = "hawthorn-jwt";

  /**
   * 过期时间-默认半个小时，单位 秒
   */
  private Long expirationTime = 1800L;

  /**
   * 默认存放token的请求头
   */
  private String requestHeader = "Authorization";

  /**
   * 默认token前缀
   */
  private String tokenPrefix = "Bearer ";
}
