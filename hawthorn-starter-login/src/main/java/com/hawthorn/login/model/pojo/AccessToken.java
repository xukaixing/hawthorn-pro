package com.hawthorn.login.model.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自定义令牌返回类
 * @author:andy.ten@tom.com
 * @date:2020/10/15 4:38 下午
 * @version v1.0.1
 */
@Data
@Builder
public class AccessToken
{
  private String loginAccount;
  private String token;
  private Date expirationTime;

  @Data
  @ApiModel(value = "登录信息")
  public static class LoginInfoDTO
  {

    /**
     * 登录账号
     */
    private String loginAccount;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 登录验证码
     */
    private String verifyCode;
  }
}
