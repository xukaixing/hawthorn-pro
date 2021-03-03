package com.hawthorn.login.model.dto.sysmenu;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo login的dto
 * @author: andy.ten@tom.com
 * @created: 3/3/21 8:52 PM
 * @lasted: 3/3/21 8:52 PM
 * @version v1.0.1
 */
@Data
@ApiModel(value = "登录信息")
public class LoginInfoDTO
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
