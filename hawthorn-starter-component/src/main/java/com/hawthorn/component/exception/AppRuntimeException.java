package com.hawthorn.component.exception;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: runtime异常封装类
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 3:48 下午
 * @version v1.0.1
 */
public class AppRuntimeException extends AppException
{
  private static final long serialVersionUID = 1L;

  public AppRuntimeException(String message)
  {
    super(message);
  }

  public AppRuntimeException(Throwable cause)
  {
    super(cause);
  }

  public AppRuntimeException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public AppRuntimeException(String code, String message, Throwable cause)
  {
    super(code, message, cause);
  }

  public AppRuntimeException(String code, String message)
  {
    super(code, message);
  }

  /**
   * Return the detail message, including the message from the nested exception
   * if there is one.
   */
  public String getMessage()
  {
    return buildMessage(super.getMessage(), getCause());
  }


  /**
   * Retrieve the innermost cause of this exception, if any.
   * @return the innermost exception, or {@code null} if none
   * @since 2.0
   */
  public Throwable getRootCause()
  {
    Throwable rootCause = null;
    Throwable cause = getCause();
    while (cause != null && cause != rootCause)
    {
      rootCause = cause;
      cause = cause.getCause();
    }
    return rootCause;
  }

  /**
   * Build a message for the given base message and root cause.
   * @param message the base message
   * @param cause the root cause
   * @return the full exception message
   */
  protected String buildMessage(String message, Throwable cause)
  {
    if (cause != null)
    {
      StringBuilder sb = new StringBuilder();
      if (message != null)
      {
        sb.append(message).append("; ");
      }
      sb.append("nested exception is ").append(cause);
      return sb.toString();
    } else
    {
      return message;
    }
  }
}
