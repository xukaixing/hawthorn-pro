package com.hawthorn.claim.controller;

import com.hawthorn.platform.ret.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: hello测试
 * @author:andy.ten@tom.com
 * @date:2020/10/28 4:01 下午
 * @version v1.0.1
 */
@Api(tags = {"claim-hello"})
@RestController
@RequestMapping("/claim/hello")
public class HelloController
{
  /**
   * @author: andy.ten@tom.com
   * @date: 2020/10/28 4:04 下午
   * @version: 1.0.1
   */
  @ApiOperation(value = "hello->sayHello", notes = "")
  @ApiImplicitParams({
  })
  @GetMapping(value = "/sayHello")
  public RestResult sayHello()
  {

    return RestResult.success("hello claim ! ");
  }
}
