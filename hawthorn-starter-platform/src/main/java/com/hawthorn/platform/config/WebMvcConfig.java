package com.hawthorn.platform.config;

import com.hawthorn.platform.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p>
 *   springboot2.0之后配置拦截器WebMvcConfigurerAdapter过时，取而代之的是WebMvcConfigurationSupport（extends）
 *   WebMvcConfigurer是一个接口
 *   继承WebMvcConfigurationSupport会发现Spring Boot的WebMvc自动配置失效(WebMvcAutoConfiguration自动化配置)。
 *   导致无法视图解析器无法解析并返回到对应的视图
 *   一个项目中只能有一个继承WebMvcConfigurationSupport的@Configuration类
 *   本项目使用接口WebMvcConfigurer，WebMvcConfigurationSupport与Spring Security冲突
 * </p>
 * @remark: 全局拦截器配置
 * @author: andy.ten@tom.com
 * @created: 2/24/21 4:31 PM
 * @lasted: 2/24/21 4:31 PM
 * @version v1.0.1
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
  /**
   * token拦截器
   */
  @Autowired
  private TokenInterceptor tokenInterceptor;

  /**
   * 注册添加拦截器配置
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry)
  {
    // 多个拦截器组成一个拦截器链
    // addPathPatterns 用于添加拦截规则，/**表示拦截所有请求
    // excludePathPatterns 用户排除拦截
    // registry
    //     .addInterceptor(tokenInterceptor)
    //     .addPathPatterns("/**")
    //     .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    // super.addInterceptors(registry);
  }

  /**
   * 设置静态资源以及目录
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry)
  {
    // 添加文档对应的映射：swagger增加url映射
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    // 所有/webjars/开头的请求 都会去后面配置的路径下查找资源
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
