package com.hawthorn.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: TODO 自定义gateway配置，在applation.yml中配置
 * @author: andy.ten@tom.com
 * @created: 3/1/21 12:22 AM
 * @lasted: 3/1/21 12:22 AM
 * @version v1.0.1
 */
@Configuration
@ConfigurationProperties("mygateway.filter")
@Data
public class CustomGateWayFilterConfig
{
  private List<String> allowPaths;
}
