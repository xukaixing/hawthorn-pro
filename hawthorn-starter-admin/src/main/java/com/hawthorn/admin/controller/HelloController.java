package com.hawthorn.admin.controller;

import com.hawthorn.admin.feign.HelloFeignService;
import com.hawthorn.component.ret.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Copyright: Copyright (c) 2020 andyten

 * @remark: helloController测试类
 * @author:andy.ten@tom.com
 * @date:2020/8/13 8:10 下午
 * @version v1.0.1
 */
@Api(tags = {"demo-hello-controller"})
@RestController
@RequestMapping("/feign")
@Slf4j
public class HelloController
{
  @Autowired
  HelloFeignService helloFeignService;

  /**
   * @author: andy.ten@tom.com
   * @date: 2020/10/28 4:04 下午
   * @version: 1.0.1
   */
  @ApiOperation(value = "feign->/hello->sayHello", notes = "feign接口调用demo")
  @GetMapping(value = "/hello/sayHello")
  public RestResult sayHello()
  {
    log.info("====== feign 调用 ======");
    return helloFeignService.sayHello();
  }
}
