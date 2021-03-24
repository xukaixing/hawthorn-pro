package com.hawthorn.platform.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo swagger配置类
 * @author: andy.ten@tom.com
 * @created: 9/12/20 11:56 AM
 * @lasted: 3/17/21 11:56 AM
 * @version v1.0.2
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
// 启动swagger
// @EnableSwagger2
// 自动扫描，该注解会扫描指定路径下的所有的配置，默认扫描该类所在包下面的所有配置文件
// @ComponentScan("com.hawthorn.**.config")
// 注意类名不能更改
public class SwaggerConfig
{
  /*
   * 创建API应用
   * apiInfo() 增加API相关信息
   * select() 选择哪些路径和api会生成document
   * apis() 对所有api进行监控
   * paths() 对所有路径进行监控
   */
  @Bean
  public Docket createRestApi()
  {
    // 设置全局响应状态码
    List<Response> responseMessageList = new ArrayList<>();
    responseMessageList.add(new ResponseBuilder().code("200").description("成功").build());
    responseMessageList.add(new ResponseBuilder().code("400").description("请求返回错误").build());
    responseMessageList.add(new ResponseBuilder().code("404").description("找不到资源").build());
    responseMessageList.add(new ResponseBuilder().code("409").description("业务逻辑异常").build());
    responseMessageList.add(new ResponseBuilder().code("422").description("参数校验异常").build());
    responseMessageList.add(new ResponseBuilder().code("500").description("服务器内部错误").build());
    responseMessageList.add(new ResponseBuilder().code("503").description("Hystrix异常").build());

    return new Docket(DocumentationType.OAS_30) // swagger3需要将SWAGGER_2改成OAS_30
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        //.apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))
        .apis(RequestHandlerSelectors.basePackage("com.hawthorn"))
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .globalResponses(HttpMethod.GET, responseMessageList)
        .globalResponses(HttpMethod.POST, responseMessageList)
        .globalResponses(HttpMethod.DELETE, responseMessageList)
        .globalResponses(HttpMethod.PUT, responseMessageList);
  }

  /*
   * 创建该API的基本信息
   * title:访问界面的标题
   * description：描述
   * version：版本号
   */
  private ApiInfo apiInfo()
  {
    return new ApiInfoBuilder()
        .title("Swagger 接口服务API文档")
        .description("hawthorn-pro")
        .version("1.0")
        .license("version1.0.1")
        .contact(new Contact("andy.ten", "https://github.com/xukaixing/", "xukaixing@hotmail.com"))
        .build();
  }
}
