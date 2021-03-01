package com.hawthorn.gateway.utils.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;


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
  public static String getIpAddr(ServerHttpRequest request)
  {
    HttpHeaders headers = request.getHeaders();
    String ip = headers.getFirst("x-forwarded-for");

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = headers.getFirst("x-forwarded-for");
      if (ip != null)
      {
        ip = ip.split(",")[0].trim();
      }
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = headers.getFirst("Proxy-Client-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = headers.getFirst("WL-Proxy-Client-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = headers.getFirst("HTTP_CLIENT_IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = headers.getFirst("X-Real-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getRemoteAddress().getAddress().getHostAddress();
    }

    return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
  }
}
