package com.hawthorn.gateway.utils.json;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: json转换异常封装类
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 3:19 下午
 * @version v1.0.1
 */
public class JacksonJsonException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public JacksonJsonException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public JacksonJsonException(String message)
  {
    super(message);
  }

  public JacksonJsonException(Throwable cause)
  {
    super(cause);
  }
}
