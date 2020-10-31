package com.hawthorn.component.exception;

import com.hawthorn.component.constant.GlobalConstant;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 应用异常基类
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:50 下午
 * @version v1.0.1
 */
public class AppException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  private String code = String.valueOf(GlobalConstant.RESULT_CODE_FAILURE);

  public AppException(String message)
  {
    super(message);
  }

  public AppException(Throwable cause)
  {
    super(cause);
  }

  public AppException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public AppException(String code, String message)
  {
    super(message);
    setCode(code);
  }

  public AppException(String code, String message, Throwable cause)
  {
    super(message, cause);
    setCode(code);
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }
}
