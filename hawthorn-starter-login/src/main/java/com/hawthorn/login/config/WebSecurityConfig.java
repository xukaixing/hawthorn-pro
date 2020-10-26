package com.hawthorn.login.config;

import com.hawthorn.login.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.Arrays;
import java.util.List;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 认证配置类
 * @author:andy.ten@tom.com
 * @date:2020/10/9 9:01 下午
 * @version v1.0.1
 */
@Configuration
@EnableWebSecurity  // 开启Spring Security
/**
 * Spring Security 配置类
 * @EnableGlobalMethodSecurity 开启注解的权限控制，默认是关闭的。
 * prePostEnabled：使用表达式实现方法级别的控制，如：@PreAuthorize("hasRole('ADMIN')")
 * securedEnabled: 开启 @Secured 注解过滤权限，如：@Secured("ROLE_ADMIN")
 * jsr250Enabled: 开启 @RolesAllowed 注解过滤权限，如：@RolesAllowed("ROLE_ADMIN")
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // 开启权限注解，如：@PreAuthorize注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * @remark: 使用自定义身份验证组件
   * <p>重写安全认证，加入密码加密方式</p>
   * @param: auth
   * @return: void
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 12:08 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception
  {
    // 加入自定义的安全认证
    auth.userDetailsService(this.userDetailsService)
        //.passwordEncoder(passwordEncoder())
        .and()
        // 使用自定义身份验证组件 可以定义多个不同的provider
        //.authenticationProvider(smsAuthenticationProvider());
        .authenticationProvider(new UsernameAuthenticationProvider(userDetailsService));
  }

  /**
   * @remark: 设置 HTTP 验证规则
   * <p>复写这个方法来配置 {@link HttpSecurity}.</p>
   * <p>通常，子类不能通过调用 super 来调用此方法，因为它可能会覆盖其配置。 默认配置为：</p>
   * <p>http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();</p>
   * <p>匹配 "/" 路径，不需要权限即可访问</p>
   * <p>匹配 "/user" 及其以下所有路径，都需要 "USER" 权限</p>
   * <p>登录地址为 "/login"，登录成功默认跳转到页面 "/user"</p>
   * <p>退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"</p>
   * @param: http
   * @return: void
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 12:10 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http.authorizeRequests()
        // 跨域预检请求 放行所有OPTIONS请求
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        // web jars
        .antMatchers("/webjars/**").permitAll()
        // 查看SQL监控（druid）
        .antMatchers("/druid/**").permitAll()
        // 首页和登录页面 放行登录方法 于获取token的rest api要允许匿名访问
        .antMatchers("/auth/login").permitAll()
        // swagger
        .antMatchers("/swagger-ui.html").permitAll()
        .antMatchers("/swagger-resources/**").permitAll()
        .antMatchers("/v2/api-docs").permitAll()
        .antMatchers("/webjars/springfox-swagger-ui/**").permitAll()
        .antMatchers("/doc.html").permitAll()
        // 登录验证码
        .antMatchers("/auth/verifyCode").permitAll()
        // 服务监控
        .antMatchers("/instances").permitAll()
        .antMatchers("/actuator/**").permitAll()
        // 允许对于网站静态资源的无授权访问
        .antMatchers(
            HttpMethod.GET,
            "/",
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"
        ).permitAll()
        // 其他所有请求需要身份认证 其他请求都需要认证后才能访问
        .anyRequest().authenticated()
        // 将自定义的JWT过滤器放到过滤链中
        .and()

        // 开启form表单认证方式 即UsernamePasswordAuthenticationFilter
        .formLogin()
        .and()

        // 开启Basic认证，对应着Basic认证方式，即BasicAuthenticationFilter
        .httpBasic()
        // .and()
        // 使用自定义的 accessDecisionManager
        //.accessDecisionManager(accessDecisionManager())
        .and()

        // 添加未登录与权限不足异常处理器
        .exceptionHandling()
        .accessDeniedHandler(restfulAccessDeniedHandler())
        .authenticationEntryPoint(restAuthenticationEntryPoint())
        .and()

        // 将自定义的JWT过滤器放到过滤链中
        //.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
        .cors() // 打开Spring Security的跨域
        .and()
        .csrf().disable() // 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
        // 关闭Session机制 基于token，所以不需要session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // 解决不允许显示在iframe的问题
    http.headers().frameOptions().disable();
    // 退出登录处理器
    http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    // 记住我
    http.rememberMe().rememberMeParameter("remember-me")
        .userDetailsService(userDetailsService).tokenValiditySeconds(1000);
    // token验证过滤器
    http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public RestfulAccessDeniedHandler restfulAccessDeniedHandler()
  {
    return new RestfulAccessDeniedHandler();
  }

  /**
   * @remark: 用户未登录异常类
   * @param:
   * @return: com.hawthorn.login.security.RestAuthenticationEntryPoint
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/14 11:53 上午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/14    andy.ten        v1.0.1             init
   */
  @Bean
  public RestAuthenticationEntryPoint restAuthenticationEntryPoint()
  {
    return new RestAuthenticationEntryPoint();
  }

  @Bean
  public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception
  {
    return new JwtAuthenticationTokenFilter(authenticationManager());
  }

  // Spring Security在认证操作时会使用定义的加密器，本次会不使用@Autowired方式注入，使用new PasswordEncoder方式生明自定义的util类
  // @Bean
  // public PasswordEncoder passwordEncoder()
  // {
  //   return NoOpPasswordEncoder.getInstance();
  // }

  /**
   * @remark: 将Spring Security自带的authenticationManager声明成Bean
   * <p>声明它的作用是进行认证操作，调用这个Bean的authenticate方法会由Spring Security自动做认证</p>
   * @param:
   * @return: org.springframework.security.authentication.AuthenticationManager
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 1:50 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception
  {
    return super.authenticationManager();
  }

  @Bean
  public AccessDecisionVoter<FilterInvocation> accessDecisionProcessor()
  {
    return new AccessDecisionProcessor();
  }

  @Bean
  public AccessDecisionManager accessDecisionManager()
  {
    // 构造一个新的AccessDecisionManager 放入两个投票器
    List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(new WebExpressionVoter(), accessDecisionProcessor());
    return new UnanimousBased(decisionVoters);
  }
}
