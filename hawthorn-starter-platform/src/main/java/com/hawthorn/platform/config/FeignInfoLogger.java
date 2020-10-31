package com.hawthorn.platform.config;

import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 重写feign.logger，将默认debug输出级别改为info
 * @author: andy.ten@tom.com
 * @date: 2020/10/28 10:06 下午
 * @version v1.0.1
 */
public class FeignInfoLogger extends feign.Logger
{
  private final Logger logger;

  public FeignInfoLogger()
  {
    this(feign.Logger.class);
  }

  public FeignInfoLogger(Class<?> clazz)
  {
    this(LoggerFactory.getLogger(clazz));
  }

  public FeignInfoLogger(String name)
  {
    this(LoggerFactory.getLogger(name));
  }

  FeignInfoLogger(Logger logger)
  {
    this.logger = logger;
  }


  @Override
  protected void logRequest(String configKey, Level logLevel, Request request)
  {
    if (logger.isInfoEnabled())
    {
      super.logRequest(configKey, logLevel, request);
    }
  }

  @Override
  protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime)
      throws IOException
  {
    if (logger.isInfoEnabled())
    {
      return super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
    }
    return response;
  }

  @Override
  protected void log(String configKey, String format, Object... args)
  {
    // Not using SLF4J's support for parameterized messages (even though it
    // would be more efficient) because it would
    // require the incoming message formats to be SLF4J-specific.
    if (logger.isInfoEnabled())
    {
      // 将debug改为info
      //logger.debug(String.format(methodTag(configKey) + format, args));
      logger.info(String.format(methodTag(configKey) + format, args));
    }
  }
}
