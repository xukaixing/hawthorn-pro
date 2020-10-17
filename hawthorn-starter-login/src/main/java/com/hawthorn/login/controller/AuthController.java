package com.hawthorn.login.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.hawthorn.component.exception.BizCode;
import com.hawthorn.platform.validatecode.IVerifyCodeGen;
import com.hawthorn.platform.validatecode.SimpleCharVerifyCodeGenImpl;
import com.hawthorn.platform.validatecode.VerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 登录控制类
 * @author:andy.ten@tom.com
 * @date:2020/9/22 12:05 上午
 * @version v1.0.1
 */
@Api(tags = {"login-syslogin"})
@RestController
@Slf4j
public class LoginController
{
  /**
   * @author: andy.ten@tom.com
   * @date: 2020/10/12 11:16 上午
   * @version: 1.0.1
   */
  @ApiOperation(value = "", notes = "")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "request", value = "", required = false, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "response", value = "", required = false, dataType = "string", paramType = "query")
  })
  @PostMapping("/login")
  public void login(@RequestBody LoginBean loginBean, HttpServletRequest request)
  {

  }

  /**
   * @author: andy.ten@tom.com
   * @date: 2020/9/22 12:30 上午
   * @version: 1.0.1
   */
  @ApiOperation(value = "登录->图形验证码", notes = "登陆页验证码")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "request", value = "", required = false, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "response", value = "", required = false, dataType = "string", paramType = "query")
  })
  @GetMapping("/verifyCode")
  public void verifyCode(HttpServletRequest request, HttpServletResponse response)
  {
    IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
    try
    {
      //设置长宽
      VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
      String code = verifyCode.getCode();
      log.info(code);
      //将VerifyCode绑定session
      request.getSession().setAttribute("VerifyCode", code);
      //设置响应头
      response.setHeader("Pragma", "no-cache");
      //设置响应头
      response.setHeader("Cache-Control", "no-cache");
      //在代理服务器端防止缓冲
      response.setDateHeader("Expires", 0);
      //设置响应内容类型
      response.setContentType("image/jpeg");
      response.getOutputStream().write(verifyCode.getImgBytes());
      response.getOutputStream().flush();
    } catch (IOException e)
    {
      log.error("生成验证码异常[" + BizCode.VERIFY_CODE_GEN_FAIL.getCode() + "] : " + ExceptionUtil.getRootCauseMessage(e));
    }
  }
}
