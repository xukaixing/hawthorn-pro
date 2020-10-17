package com.hawthorn.login.provider;

import cn.hutool.core.date.DateUtil;
import com.hawthorn.login.config.JwtTokenConfig;
import com.hawthorn.login.model.pojo.AccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: token工具类
 * <p>一个完整的JwtToken由三部分组成：头部+负载信息+签名</p>
 * <p>header 存放JwtToken签名的算法 | token的类型：{"alg": "HS512","typ": "JWT"}</p>
 * <p>payload 主要存放用户名、创建时间、生成时间：{"sub":"wang","created":1489079981393,"exp":1489684781}</p>
 * <p>signature 生成算法：HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)</p>
 * @author:andy.ten@tom.com
 * @date:2020/10/11 10:45 下午
 * @version v1.0.1
 */
@Slf4j
@Component
public class JwtProvider
{
  @Autowired
  private JwtTokenConfig jwtTokenConfig;

  /**
   * 从请求中拿到token
   */
  public String getToken(HttpServletRequest request)
  {
    return request.getHeader(jwtTokenConfig.getRequestHeader());
  }

  /**
   * 根据用户信息生成token
   */
  public AccessToken createToken(UserDetails userDetails)
  {
    return createToken(userDetails.getUsername());
  }

  /**
   * 生成token
   * 参数是我们想放入token中的字符串（同样是用户登录账号）
   */
  public AccessToken createToken(String subject)
  {
    // 当前时间
    final Date now = new Date();
    // 过期时间
    final Date expirationDate = new Date(now.getTime() + jwtTokenConfig.getExpirationTime() * 1000);

    String token = jwtTokenConfig.getTokenPrefix() + Jwts.builder()
        //.setClaims(claims) // 设置用户的授予权限信息（角色信息）
        .setSubject(subject) // 设置主题
        .setIssuedAt(now) // 设置令牌签发日期
        .setExpiration(expirationDate) // 设置令牌过期时间
        .signWith(SignatureAlgorithm.HS512, jwtTokenConfig.getApiSecretKey()) // 设置token签名的加密算法以及追加的密钥
        .compact();
    return AccessToken.builder().loginAccount(subject).token(token).expirationTime(expirationDate).build();
  }

  /**
   * 验证token是否还有效
   * <p>
   * 反解析出token中信息，然后与参数中的信息比较，再校验过期时间
   *
   * @param token       客户端传入的token
   * @param userDetails 从数据库中查询出来的用户信息
   */
  public boolean validateToken(String token, UserDetails userDetails)
  {
    Claims claims = getClaimsFromToken(token);
    return claims.getSubject().equals(userDetails.getUsername()) && !isTokenExpired(claims);
  }


  /**
   * 刷新token
   * 过滤器会对请求进行验证，所以这里可以不必验证
   *
   * @param oldToken 带tokenHead的token
   */
  public AccessToken refreshToken(String oldToken)
  {
    String token = oldToken.substring(jwtTokenConfig.getTokenPrefix().length());

    // token反解析
    Claims claims = getClaimsFromToken(token);

    //如果token在30分钟之内刚刷新过，返回原token
    if (tokenRefreshJustBefore(claims))
    {
      return AccessToken.builder().loginAccount(claims.getSubject()).token(oldToken).expirationTime(claims.getExpiration()).build();
    } else
    {
      return createToken(claims.getSubject());
    }
  }

  /**
   * 判断token在指定时间内是否刚刚刷新过
   */
  private boolean tokenRefreshJustBefore(Claims claims)
  {
    Date refreshDate = new Date();
    //刷新时间在创建时间的指定时间内
    if (refreshDate.after(claims.getExpiration()) && refreshDate.before(DateUtil.offsetSecond(claims.getExpiration(), 1800)))
    {
      return true;
    }
    return false;
  }

  /**
   * 从token中拿到负载信息
   */
  private Claims getClaimsFromToken(String token)
  {
    Claims claims = null;
    try
    {
      claims = Jwts.parser()
          .setSigningKey(jwtTokenConfig.getApiSecretKey())
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e)
    {
      log.error("JWT反解析失败, token已过期或不正确, token : {}", token);
    }
    return claims;
  }


  /**
   * 从token中获取主题
   */
  public String getSubjectFromToken(String token)
  {
    Claims claims = getClaimsFromToken(token);
    if (claims != null)
    {
      return claims.getSubject();
    } else
    {
      return null;
    }
  }


  /**
   * 判断token是否已经过期
   */
  private boolean isTokenExpired(Claims claims)
  {
    return claims.getExpiration().before(new Date());
  }


}
