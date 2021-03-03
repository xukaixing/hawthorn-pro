package com.hawthorn.login.model.pojo;

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
  private String loginUuid;
  private String token;
  private Date expirationTime;
}
