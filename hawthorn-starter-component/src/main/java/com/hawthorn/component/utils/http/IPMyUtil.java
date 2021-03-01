package com.hawthorn.component.utils.http;

import javax.servlet.http.HttpServletRequest;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: TODO 获取ip地址工具类
 * @author: andy.ten@tom.com
 * @created: 3/1/21 10:16 PM
 * @lasted: 3/1/21 10:16 PM
 * @version v1.0.2
 */
public class IPMyUtil
{
  public static String getIpAddr(HttpServletRequest request)
  {
    String ip = request.getHeader("x-forwarded-for");

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("x-forwarded-for");
      if (ip != null)
      {
        ip = ip.split(",")[0].trim();
      }
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("Proxy-Client-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("X-Real-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getRemoteAddr();
    }

    return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
  }
}
