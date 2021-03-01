package com.hawthorn.platform.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: token校验controller拦截器
 * @author: andy.ten@tom.com
 * @created: 2/24/21 4:34 PM
 * @lasted: 2/24/21 4:34 PM
 * @version v1.0.1
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter
{
  public static final String ACCESS_TOKEN = "access_token";

  /**
   * 单点登录key-cloak
   */
  //@Autowired
  //private KeyCloakProperties keyCloakProperties;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    /**
     * token 校验
     */
    //String accessToken = request.getHeader(ACCESS_TOKEN);


    return super.preHandle(request, response, handler);
  }

  private boolean tokenInvalid(HttpServletResponse response) throws Exception
  {
    // BaseOutDTO outDTO = BaseOutDTO.builder().errorCode(BaseServiceError.TOKEN_ERROR.getCode()).
    //     errorMessage(BaseServiceError.TOKEN_ERROR.getMessage()).build();
    // response.setContentType("application/json");
    // response.getWriter().write(JSONUtil.toJsonStr(outDTO));
    return false;
  }
}
