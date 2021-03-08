package com.hawthorn.platform.utils.token;

import cn.hutool.core.date.DateUtil;
import com.hawthorn.component.utils.json.JacksonMyUtil;
import com.hawthorn.platform.config.JwtTokenConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: todo token工具类
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
public class JwtTokenMyUtil
{
  private final Base64 base64 = new Base64();

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
   * 从token中获取用户账号
   */
  public String getUserAccountFromToken(String token)
  {
    Claims claims = null;
    //if (claims == null)
    Map<String, Object> claimsMap = getClaimsFromToken(token);
    String status = (String) claimsMap.get("status");
    if ("ok".equals(status))
    {
      claims = (Claims) claimsMap.get("claim");
      if (claims != null)
      {
        return claims.getSubject();
      } else
      {
        return null;
      }
    } else
      return null;
  }

  /**
   * 从token中拿到负载信息
   */
  private Map<String, Object> getClaimsFromToken2(String token)
  {
    Map<String, Object> claimsMap = new HashMap<>();
    Claims claims = null;
    try
    {
      claims = Jwts.parser()
          .setSigningKey(jwtTokenConfig.getApiSecretKey())
          .parseClaimsJws(token)
          .getBody();
      claimsMap.put("status", "ok");
      claimsMap.put("claims", claims);
    } catch (ExpiredJwtException e)
    {
      log.error("token认证失败,token过期: {}", token);
      claimsMap.put("status", "fail");
      claimsMap.put("claims", "timeout");
    } catch (Exception e)
    {
      log.error("token认证失败: {}", token);
      claimsMap.put("status", "fail");
      claimsMap.put("claims", "no");
    }
    return claimsMap;
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
   * token负载部分解密
   * @param token
   * @return
   */
  @SuppressWarnings("rawtypes")
  public Map decryptToken(String token)
  {
    String payload = StringUtils.substringBetween(token, ".");
    byte[] b = base64.decode(payload);
    String json = new String(b);
    return JacksonMyUtil.json2Object(json, Map.class);
  }

  /**
   * 判断token是否已经过期
   */
  private boolean isTokenExpired(Claims claims)
  {
    return claims.getExpiration().before(new Date());
  }

  /**
   * 验证token是否还有效
   * @param token
   * @param publicKey
   * @return
   * @throws NoSuchAlgorithmException
   * @throws SignatureException
   * @throws InvalidKeySpecException
   * @throws InvalidKeyException
   */
  public boolean checkToken(String token, String publicKey) throws
      NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException
  {
    String preSignStr = StringUtils.substringBeforeLast(token, ".");
    String sign = StringUtils.substringAfterLast(token, ".");
    return checkSign(preSignStr, sign, publicKey);
  }

  public boolean checkSign(String preSignStr, String sign, String publicKey) throws
      InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException
  {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] encodedKey = base64.decode(publicKey);
    PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    Signature signature = Signature.getInstance("SHA256WithRSA");
    signature.initVerify(pubKey);
    signature.update(preSignStr.getBytes(StandardCharsets.UTF_8));
    return signature.verify(base64.decode(sign.getBytes(StandardCharsets.UTF_8)));
  }

  // 通过在application.yml中key-cloak配置scope校验
  public Boolean verifyScope(String scope, String scopeList)
  {
    String[] split = scopeList.split("\\s+");
    List<String> list = Arrays.asList(split);
    return list.contains(scope);
  }

  /**
   * @remark: 公共钥匙方式校验token
   * @param: token
   * @param: publicKey
   * @return: java.lang.Boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2/27/21 3:15 PM
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2/27/21    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("rawtypes")
  public Boolean verifyTokenByRsa(String token, String publicKey)
  {
    try
    {
      boolean verifyTokenBoolean = checkToken(token, publicKey);
      if (verifyTokenBoolean)
      {
        Map stringObjectMap = decryptToken(token);

        // 过期验证
        Integer exp = (Integer) stringObjectMap.get("exp");
        long now = System.currentTimeMillis();
        long expLong = exp.longValue() * 1000;

        if (expLong < now)
        {
          return false;
        }
      } else
      {
        return false;
      }
    } catch (Exception e)
    {
      log.error(e.toString());
      return false;
    }
    return true;
  }

  public Boolean verifyTokenByScope(String token, String publicKey, String scope)
  {
    try
    {
      Boolean verifyTokenBoolean = verifyTokenByRsa(token, publicKey);
      if (!verifyTokenBoolean)
      {
        return false;
      }
      Map stringObjectMap = decryptToken(token);
      String scopeStr = (String) stringObjectMap.get("scope");
      Boolean verifyScopeBoolean = verifyScope(scope, scopeStr);
      if (!verifyScopeBoolean)
      {
        return false;
      }
      // todo 过期验证
      Integer exp = (Integer) stringObjectMap.get("exp");
      long now = System.currentTimeMillis();
      long expLong = exp.longValue() * 1000;

      if (expLong < now)
      {
        return false;
      }
    } catch (Exception e)
    {
      log.error(e.toString());
      return false;
    }
    return true;
  }


  /**
   * @remark: todo 验证token
   * @param: token
   * @param: audience
   * @return: io.jsonwebtoken.Claims
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2/28/21 1:31 AM
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2/28/21    andy.ten        v1.0.1             init
   */
  public Map<String, Object> verifyToken(String token, String audience)
  {
    Claims claims = null;
    Map<String, Object> map = getClaimsFromToken2(token);

    String status = (String) map.get("status");
    if ("ok".equals(status))
    {
      claims = (Claims) map.get("claims");
      if (isTokenExpired(claims))
      {
        Map<String, Object> map2 = new HashMap<>();
        map2.put("status", "fail");
        map2.put("claims", "timeout");
        return map2;
      }
      return map;
    } else
      return map;
  }
}
