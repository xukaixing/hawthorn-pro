package com.hawthorn.platform.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 返回值封装类
 * @author: andy.ten@tom.com
 * @created: 3/12/21 7:55 PM
 * @lasted: 3/12/21 7:55 PM
 * @version v1.0.1
 */
public class ResponseWrapper extends HttpServletResponseWrapper
{
  private ByteArrayOutputStream buffer;

  private ServletOutputStream out;

  public ResponseWrapper(HttpServletResponse httpServletResponse)
  {
    super(httpServletResponse);
    buffer = new ByteArrayOutputStream();
    out = new WrapperOutputStream(buffer);
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException
  {
    return out;
  }

  @Override
  public void flushBuffer() throws IOException
  {
    if (out != null)
    {
      out.flush();
    }
  }

  public byte[] getContent() throws IOException
  {
    flushBuffer();
    return buffer.toByteArray();
  }

  class WrapperOutputStream extends ServletOutputStream
  {
    private ByteArrayOutputStream bos;

    public WrapperOutputStream(ByteArrayOutputStream bos)
    {
      this.bos = bos;
    }

    @Override
    public void write(int b) throws IOException
    {
      bos.write(b);
    }

    @Override
    public boolean isReady()
    {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void setWriteListener(WriteListener arg0)
    {
      // TODO Auto-generated method stub
    }
  }
}
