package com.hawthorn.platform.common;

import com.hawthorn.component.utils.common.StringMyUtil;
import com.hawthorn.platform.wrapper.RequestWrapper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 公共类
 * @author: andy.ten@tom.com
 * @created: 3/12/21 7:59 PM
 * @lasted: 3/12/21 7:59 PM
 * @version v1.0.1
 */
public class Pub
{

  /**
   * @remark: todo 获取请求参数包括form表单和json参数
   * @param: wrapperRequest
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 3/12/21 8:00 PM
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 3/12/21    andy.ten        v1.0.1             init
   */
  public static String getRequestParameter(RequestWrapper wrapperRequest)
  {
    if (null == wrapperRequest)
    {
      return null;
    }
    String params = null;
    String method = wrapperRequest.getMethod();
    if (StringMyUtil.isNotBlank(method) && "GET".equals(method.toUpperCase()))
    {
      // 获取请求体中的字符串(get)
      params = wrapperRequest.getQueryString();
      try
      {
        if (StringMyUtil.isNotBlank(params))
          params = URLDecoder.decode(params, "UTF-8");
      } catch (UnsupportedEncodingException e)
      {
        // Logger.error("获取到的请求参数解码错误 : {}", e.getMessage());
      }
      return params;
    } else
    {
      return wrapperRequest.getBodyString(wrapperRequest);
    }
  }

}
