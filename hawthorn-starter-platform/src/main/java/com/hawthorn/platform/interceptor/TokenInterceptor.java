package com.hawthorn.platform.interceptor;

import com.hawthorn.component.constant.RedisConstant;
import com.hawthorn.component.exception.BizCode;
import com.hawthorn.component.ret.RestResult;
import com.hawthorn.component.utils.common.StringMyUtil;
import com.hawthorn.component.utils.json.JacksonMyUtil;
import com.hawthorn.platform.annotation.CheckToken;
import com.hawthorn.platform.annotation.NoCheckToken;
import com.hawthorn.platform.config.JwtTokenConfig;
import com.hawthorn.platform.redis.RedisMyClient;
import com.hawthorn.platform.utils.token.JwtTokenMyUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo token校验controller拦截器
 * @author: andy.ten@tom.com
 * @created: 3/8/21 7:26 PM
 * @lasted: 3/8/21 7:26 PM
 * @version v1.0.1
 */
@Component
@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter
{
  @Autowired
  private JwtTokenConfig jwtTokenConfig;

  @Autowired
  private JwtTokenMyUtil jwtTokenMyUtil;

  @Autowired
  private RedisMyClient redisMyClient;

  /**
   * 单点登录key-cloak
   */
  //@Autowired
  //private KeyCloakProperties keyCloakProperties;
  @SuppressWarnings("NullableProblems")
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    log.info("====== 执行token校验 ======");
    // 如果不是映射到方法直接通过
    if (!(handler instanceof HandlerMethod))
    {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    // todo 检查是否有nochecktoken注解，有则跳过认证
    if (method.isAnnotationPresent(NoCheckToken.class))
    {
      NoCheckToken noCheckToken = method.getAnnotation(NoCheckToken.class);
      if (noCheckToken.required())
      {
        return true;
      }
    }
    // todo 检查是否有checktoken注解，有则进行认证
    if (method.isAnnotationPresent(CheckToken.class))
    {
      CheckToken checkToken = method.getAnnotation(CheckToken.class);
      if (checkToken.required()) // 执行认证
      {
        //String sLoginAccount = request.getHeader(HeaderConstant.USER_ACCOUNT);
        //String sLoginUuid = request.getHeader(HeaderConstant.LOGIN_UUID);
        // 获取请求url
        String requestUrl = request.getRequestURI();
        boolean ignoreToken = false;

        // 请求方式是OPTIONS的时候 跳过
        String requestMethod = request.getMethod();
        // todo 忽略预检请求，可用于检测服务器允许的http方法
        if (RequestMethod.OPTIONS.name().equals(requestMethod))
        {
          ignoreToken = true;
        }

        if (ignoreToken)
        {
          return true;
        }

        // // todo 校验用户账号是否为空
        // if (StringMyUtil.isBlank(sLoginAccount))
        // {
        //   response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //   return tokenInvalid(response, BizCode.AUTH_LOGINACCOUNT_NOTEMPTY);
        // }
        // // todo 校验uuid是否为空
        // if (StringMyUtil.isBlank(sLoginUuid))
        // {
        //   response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //   return tokenInvalid(response, BizCode.AUTH_LOGIUUID_NOTEMPTY);
        // }

        // 获取Authorization请求头内的信息
        String authToken = request.getHeader(jwtTokenConfig.getRequestHeader());

        // todo 验证token是空的情况
        if (StringMyUtil.isBlank(authToken))
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return tokenInvalid(response, BizCode.AUTH_TOKEN_ISBLANK);
        }
        // 去掉token前缀(Bearer )，得到真实token
        String authTokenNoPrefix = authToken.substring(jwtTokenConfig.getTokenPrefix().length());

        // todo 验证token的前缀是否正确
        if (authTokenNoPrefix.startsWith(jwtTokenConfig.getTokenPrefix()))
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return tokenInvalid(response, BizCode.AUTH_TOKEN_INVALID);
        }
        // todo 检查Spring缓存中是否有此Token

        // todo 验证token有效性
        Map<String, Object> verifyMap = new HashMap<>();
        try
        {
          verifyMap = jwtTokenMyUtil.verifyToken(authTokenNoPrefix, "");
        } catch (Exception ex)
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return tokenInvalid(response, BizCode.UNKNOW_ERROR);
        }

        String status = (String) verifyMap.get("status");
        Claims claims = null;
        if ("ok".equals(status))
        {
          claims = (Claims) verifyMap.get("claims");
        } else
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          String msg = (String) verifyMap.get("claims");
          if (msg.equals("timeout"))
          {
            // 过期 ,判断是否超过设置的会话时间
            return tokenInvalid(response, BizCode.AUTH_TOKEN_TIMEOUT);
          } else
            return tokenInvalid(response, BizCode.AUTH_TOKEN_INVALID);
        }

        if (claims == null)
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return tokenInvalid(response, BizCode.AUTH_TOKEN_INVALID);
        }

        // todo 验证token中的账号和登录账号是否匹配
        String loginAccount = claims.getSubject();
        // if (!loginAccount.equals(sLoginAccount))
        // {
        //   response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //   return tokenInvalid(response, BizCode.AUTH_TOKEN_NOT_MATCH_ACCOUNT);
        // }

        // todo 验证loginAccount是空的情况
        if (StringMyUtil.isBlank(loginAccount))
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return tokenInvalid(response, BizCode.AUTH_TOKEN_ACCOUNT_ISBLANK);
        }

        // todo 根据loginAccount，获取userid
        Object userobj = redisMyClient.hGet(StringMyUtil.placeHolder(RedisConstant.REDIS_KEY_USER_PREFIX, loginAccount), RedisConstant.REDIS_KEY_USERID);
        String userid = "";
        if (userobj != null)
          userid = userobj.toString();
        else
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return tokenInvalid(response, BizCode.AUTH_TOKEN_NOREDIS);
        }
        //redisMyClient.hGet();
        // todo 检查Redis中是否有此Token
        String sysJwtUserId = StringMyUtil.placeHolder(RedisConstant.JWT, userid);
        Object redisTokenObj = redisMyClient.hGet(sysJwtUserId, RedisConstant.ACCESS_TOKEN_KEY);
        String redisToken = "";
        if (redisTokenObj == null)
          redisToken = "";
        else
          redisToken = redisTokenObj.toString();
        if ("".equals(redisToken)) // 是否存在
        {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return tokenInvalid(response, BizCode.AUTH_TOKEN_NOREDIS);
        } else
        {
          // todo 检查redis存储的token是否与传递的token一致
          if (!redisToken.equals(authToken))
          {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return tokenInvalid(response, BizCode.AUTH_TOKEN_NOT_MATCH_REDIS);
          }
          // todo 检查Redis中的token是否过期(待实现)
        }

        // todo 校验是否有多个账户同时登录
        // String loginUuid = claims.getId();
        // if (!sLoginUuid.equals(loginUuid))
        // {
        //   response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //   return tokenInvalid(response, BizCode.AUTH_TOKEN_LOGINED);
        // }

        // todo 将用户信息放到header中
      }
    }
    return super.preHandle(request, response, handler);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         Object handler, ModelAndView modelAndView)
  {

  }

  @SuppressWarnings("NullableProblems")
  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse,
                              Object handler, Exception e)
  {
  }

  /**
   * 返回结果
   * @param response
   * @return
   */
  private boolean tokenInvalid(HttpServletResponse response, BizCode bizCode) throws IOException
  {
    RestResult restResult = RestResult.fail(bizCode);
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().write(JacksonMyUtil.object2Json(restResult));
    return false;
  }

}
