package com.hawthorn.platform.wrapper;

import com.hawthorn.component.utils.common.StringMyUtil;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 请求封装类
 * @author: andy.ten@tom.com
 * @created: 3/12/21 7:56 PM
 * @lasted: 3/12/21 7:56 PM
 * @version v1.0.1
 */
public class RequestWrapper extends HttpServletRequestWrapper
{
  private final byte[] body;

  public RequestWrapper(HttpServletRequest request) throws IOException
  {
    super(request);
    String sessionStream = getBodyString(request);
    body = sessionStream.getBytes(Charset.forName("UTF-8"));
  }

  /**
   * 获取请求Body
   *
   * @param request
   * @return
   */
  public String getBodyString(final ServletRequest request)
  {
    String contentType = request.getContentType();
    String bodyString = "";

    if (StringMyUtil.isNotBlank(contentType) &&
        (contentType.contains("multipart/form-data") ||
            contentType.contains("x-www-form-urlencoded")))
    {

      Enumeration<String> pars = request.getParameterNames();

      while (pars.hasMoreElements())
      {

        String n = pars.nextElement();

        bodyString += n + "=" + request.getParameter(n) + "&";

      }

      bodyString = bodyString.endsWith("&") ? bodyString.substring(0, bodyString.length() - 1) : bodyString;

      return bodyString;

    }

    try
    {
      byte[] byteArray = StreamUtils.copyToByteArray(request.getInputStream());
      bodyString = new String(byteArray, "UTF-8");
    } catch (IOException e)
    {
    }

    return bodyString;
  }

  @Override
  public BufferedReader getReader() throws IOException
  {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  @Override
  public ServletInputStream getInputStream() throws IOException
  {

    final ByteArrayInputStream bais = new ByteArrayInputStream(body);

    return new ServletInputStream()
    {

      @Override
      public int read() throws IOException
      {
        return bais.read();
      }

      @Override
      public boolean isFinished()
      {
        return false;
      }

      @Override
      public boolean isReady()
      {
        return false;
      }

      @Override
      public void setReadListener(ReadListener readListener)
      {
      }
    };
  }
}
