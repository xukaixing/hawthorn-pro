package com.hawthorn.admin.controller;

import com.hawthorn.admin.feign.HelloFeignService;
import com.hawthorn.component.ret.RestResult;
import com.hawthorn.platform.redis.RedisMyClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo
 * @author: andy.ten@tom.com
 * @created: 4/5/21 6:02 PM
 * @lasted: 4/5/21 6:02 PM
 * @version v1.0.1
 */
@Api(tags = {"demo-hello-controller"})
@RestController
@RequestMapping("/hello")
@Slf4j
@RefreshScope
public class HelloController
{
  @Autowired
  HelloFeignService helloFeignService;

  @Autowired
  private RedisMyClient redisMyClient;

  @Value("${hello:nothing}")
  private String hello;

  /**
   * @author: andy.ten@tom.com
   * @date: 4/5/21 11:07 AM
   * @version: 1.0.1
   */
  @ApiOperation(value = "/hello->loadHello", notes = "读取配置中心内容")
  @ApiImplicitParams({
  })
  @GetMapping(value = "/loadHello")
  public RestResult loadConfig()
  {
    log.info("====== load config ======");
    log.info("redis:{}", redisMyClient.hGet("cache:dict:sf", "val"));
    return RestResult.success(this.hello);
  }

  /**
   * @author: andy.ten@tom.com
   * @date: 2020/10/28 4:04 下午
   * @version: 1.0.1
   */
  @ApiOperation(value = "feign->/hello->sayHello", notes = "feign接口调用demo")
  @GetMapping(value = "/feign/sayHello")
  public RestResult sayHello()
  {
    log.info("====== feign 调用 ======");
    return helloFeignService.sayHello();
  }
}
