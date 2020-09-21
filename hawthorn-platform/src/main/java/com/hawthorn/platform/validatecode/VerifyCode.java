package com.hawthorn.platform.validatecode;

import lombok.Data;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 验证码实体类
 * @author:andy.ten@tom.com
 * @date:2020/9/22 12:24 上午
 * @version v1.0.1
 */
@Data
public class VerifyCode
{
  private String code;

  private byte[] imgBytes;

  private long expireTime;
}
