package com.hawthorn.component.ret;

import com.hawthorn.component.utils.http.HttpStatusMyUtil;

/**
 * @Copyright: Copyright (c) 2020 andyten

 * @remark: 定义结果集code
 * @author:andy.ten@tom.com
 * @date:2020/8/11 5:27 下午
 * @version v1.0.1
 */
public enum RetCode
{
  // 成功
  SUCCESS(HttpStatusMyUtil.SC_OK),

  // 失败
  FAIL(HttpStatusMyUtil.SC_BAD_REQUEST),

  // 未认证（签名错误）
  UNAUTHORIZED(HttpStatusMyUtil.SC_UNAUTHORIZED),

  // 接口不存在
  NOT_FOUND(HttpStatusMyUtil.SC_NOT_FOUND),

  // 客户端发送的实体主体部分比服务器能够或者希望处理的要大
  Entity_TOOLARGE(HttpStatusMyUtil.SC_REQUEST_TOO_LONG),

  // 服务器内部错误
  INTERNAL_SERVER_ERROR(HttpStatusMyUtil.SC_INTERNAL_SERVER_ERROR);

  public int code;

  RetCode(int code)
  {
    this.code = code;
  }
}
