package com.hawthorn.login.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.hawthorn.component.constant.SysConstant;
import com.hawthorn.component.exception.BizCode;
import com.hawthorn.component.utils.iassert.AssertUtil;
import com.hawthorn.login.model.pojo.AccessToken;
import com.hawthorn.login.service.AuthService;
import com.hawthorn.platform.ret.RestResult;
import com.hawthorn.platform.validatecode.IVerifyCodeGen;
import com.hawthorn.platform.validatecode.SimpleCharVerifyCodeGenImpl;
import com.hawthorn.platform.validatecode.VerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 认证控制类
 * @author:andy.ten@tom.com
 * @date:2020/9/22 12:05 上午
 * @version v1.0.1
 */
@Api(tags = {"authentication-login"})
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController
{
  @Autowired
  private AuthService authService;

  /**
   * @author: andy.ten@tom.com
   * @date: 2020/10/12 11:26 上午
   * @version: 1.0.1
   * @return
   */
  @ApiOperation(value = "登录->登录操作", notes = "登录页面响应api")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "loginInfoDTO", value = "loginInfoDTO")
  })
  @PostMapping("/login")
  public RestResult login(@RequestBody AccessToken.LoginInfoDTO loginInfoDTO, HttpServletRequest request)
  {
    String loginAccount = loginInfoDTO.getLoginAccount();
    String loginPassword = loginInfoDTO.getPassword();
    AssertUtil.notEmpty(loginAccount, BizCode.AUTH_LOGINACCOUNT_NOTEMPTY);
    AssertUtil.notEmpty(loginPassword, BizCode.AUTH_LOGINPASSWROD_NOTEMPTY);
    String verifyCode = loginInfoDTO.getVerifyCode();
    // 从session中获取之前保存的验证码跟前台传来的验证码进行匹配
    Object verifyCodeObj = request.getSession().getAttribute(SysConstant.SESSION_VERIFYCODE);
    // if (verifyCodeObj == null)
    // {
    //   return RestResult.fail(BizCode.VERIFY_CODE_INVALID);
    // }
    // if (!verifyCode.equals(verifyCodeObj))
    // {
    //   return RestResult.fail(BizCode.VERIFY_CODE_INCORRECT);
    // }
    return authService.login(request, loginAccount, loginPassword);
  }

  /**
   * @author: andy.ten@tom.com
   * @date: 2020/9/22 12:30 上午
   * @version: 1.0.1
   */
  @ApiOperation(value = "登录->图形验证码", notes = "登陆页验证码")
  @ApiImplicitParams({
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
      //将VerifyCode绑定session
      request.getSession().setAttribute(SysConstant.SESSION_VERIFYCODE, code);
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
