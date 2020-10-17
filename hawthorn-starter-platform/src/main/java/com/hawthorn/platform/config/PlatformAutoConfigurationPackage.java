package com.hawthorn.platform.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自动装配类，platform工程（maven工程，BooApplication启动容器）无法自动装配注解问题；
 * @author:andy.ten@tom.com
 * @date:2020/8/25 10:20 下午
 * @version v1.0.1
 */
@Configuration
@EnableTransactionManagement
@ConditionalOnClass(PlatformProperties.class)
@EnableConfigurationProperties(PlatformProperties.class)//开启使用映射实体对象
//@ConditionalOnProperty(
//        prefix = "platform",//存在配置前缀
//        value = "enabled",//开启
//        matchIfMissing = true//缺失检查
//)
@ComponentScan(basePackages = {"com.hawthorn.platform"},
    excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.hawthorn\\" +
        ".platform\\.repository\\.DBMapper"), @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {DruidDataSourceAutoConfigure.class})}
)
//@MapperScan("com.hawthorn.platform.repository.mapper")
public class PlatformAutoConfigurationPackage
{
  public PlatformAutoConfigurationPackage()
  {
  }
}
