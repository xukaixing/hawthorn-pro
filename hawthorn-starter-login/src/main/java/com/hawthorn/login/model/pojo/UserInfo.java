package com.hawthorn.login.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 安全认证用户信息类
 * @author:andy.ten@tom.com
 * @date:2020/10/11 3:31 下午
 * @version v1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "user_info", description = "用户信息")
public class UserInfo implements Serializable
{
  /**
   * 编号
   */
  @ApiModelProperty(value = "编号", name = "id")
  private String id;

  /**
   * 用户名
   */
  @ApiModelProperty(value = "用户名", name = "username")
  private String username;

  /**
   * 密码
   */
  @ApiModelProperty(value = "密码", name = "password")
  private String password;

  /**
   * 盐
   */
  @ApiModelProperty(value = "盐", name = "salt")
  private String salt;

}

