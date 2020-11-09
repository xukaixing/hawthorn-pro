package com.hawthorn.platform.config;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 全局feign自定义配置类
 * @author: andy.ten@tom.com
 * @date: 2020/10/28 9:27 下午
 * @version v1.0.1
 */

import com.hawthorn.component.utils.json.ObjectRequestParam2StringConverter;
import com.hawthorn.platform.hystrix.MyHystrixFallbackHandlerFactory;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.HystrixFallbackConfiguration;
import org.springframework.cloud.openfeign.HystrixFallbackFactory;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.format.FormatterRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

// 可以添加在主类下，但是不用添加@Configuration。
// 如果添加了@Configuration而且又放在了主类之下，那么就会所有Feign客户端实例共享，同Ribbon配置类一样父子上下文加载冲突；
// 如果一定添加@Configuration，就放在主类加载之外的包。建议还是不用加@Configuration
//@Configuration
@ConditionalOnClass(Feign.class)
@Import({HystrixFallbackConfiguration.class})
@Slf4j
//@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignGlobalConfig
{
  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;

  /**
   * @remark: 实现feign支持form表单提交
   * @param:
   * @return: Encoder
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 2:40 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  @Bean
  public Encoder feignEncoder()
  {
    return new SpringFormEncoder(new SpringEncoder(messageConverters));
  }

  /**
   * <p>
   *   feignContract配置的是使用Feign的默认注解
   *   注入这个配置bean之后，@FeignClient接口类就一定要使用@RequestLine这个注解才行（这个是Feign的注解）
   *   使用@RequestMapping会报Method findByNameEn not annotated with HTTP method type (ex. GET, POST)的异常
   * </p>
   */
  // @Bean
  // public Contract feignContract()
  // {
  //   return new feign.Contract.Default();
  // }

  /**
   * @remark: 配置feign输出日志级别；
   * @param:
   * @return: feign.Logger.Level
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/28 9:41 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/28    andy.ten        v1.0.1             init
   */
  @Bean
  Logger.Level feignLoggerLevel()
  {
    // 默认级别为NONE
    // NONE：不输出日志。
    // BASIC：只输出请求方法的 URL 和响应的状态码以及接口执行的时间。
    // HEADERS：将 BASIC 信息和请求头信息输出。
    // FULL：输出完整的请求信息。
    // feign只对日志级别为debug级别做出响应，因为生产环境大多是info级别；所以需要指定feignClient类级别为DEBUG
    // 用法：在配置文件中执行 Client 的日志级别才能正常输出日志，格式是“logging.level.client类地址=级别”。
    // logging:
    //   level:
    //     #需要将@FeignClient接口全路径写上
    //     com.hawthorn.admin.feign.HelloFeignService: DEBUG
    return Logger.Level.FULL;
  }

  /**
   * @remark: 增加自定义日志级别输出，解决生产环境info级别无法打印feign日志问题
   * <p>feign只能对debug级别打印</p>
   * <p>每个FeignClient都在Application.yml中配置过于繁琐</p>
   * @param:
   * @return: feign.Logger
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/28 10:14 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/28    andy.ten        v1.0.1             init
   */
  @Bean
  Logger InfoFeign()
  {
    return new FeignInfoLogger();
  }

  /**
   * @remark: feign接口超时时间配置
   * @param:
   * @return: feign.Request.Options
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/28 10:23 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/28    andy.ten        v1.0.1             init
   */
  @Bean
  public Request.Options options()
  {
    // 通过 Options 可以配置连接超时时间和读取超时时间（代码如下所示）
    // Options 的第一个参数是连接超时时间（ms），默认值是 10×1000；
    // 第二个是读取超时时间（ms），默认值是 60×1000。
    return new Request.Options(10000, 60000);
  }

  @Bean
  public HttpClient httpClient()
  {
    log.info("====== Apache httpclient strating... ======");
    log.info("====== Apache httpclient 初始化连接池开始 ======");
    // 生成默认请求配置
    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
    // 从连接池获取连接超时时间
    requestConfigBuilder.setConnectionRequestTimeout(5 * 1000);
    // 连接目标超时
    // 指的是连接目标url的连接超时时间，即客服端发送请求到与目标url建立起连接的最大时间
    requestConfigBuilder.setConnectTimeout(5 * 1000);
    // 等待响应超时（读取数据超时）
    // 连接上一个url后，获取response的返回等待时间 ，即在与目标url建立连接后，等待返回response的最大时间
    requestConfigBuilder.setSocketTimeout(15 * 1000);

    RequestConfig defaultRequestConfig = requestConfigBuilder.build();

    // 连接池配置
    // 长连接保持30秒
    final PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.MILLISECONDS);
    // 总连接数
    pollingConnectionManager.setMaxTotal(1000);
    // 同路由的并发数
    pollingConnectionManager.setDefaultMaxPerRoute(100);

    // httpclient 配置
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    // 保持长连接配置，需要在头添加Keep-Alive
    httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
    httpClientBuilder.setConnectionManager(pollingConnectionManager);
    httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
    HttpClient client = httpClientBuilder.build();

    // 启动定时器，定时回收过期的连接
    Timer timer = new Timer();
    timer.schedule(new TimerTask()
    {
      @Override
      public void run()
      {
        log.info("====== closeIdleConnections ======");
        pollingConnectionManager.closeExpiredConnections();
        pollingConnectionManager.closeIdleConnections(5, TimeUnit.SECONDS);
      }
    }, 10 * 1000, 5 * 1000); // 10秒后，每隔5秒调用
    log.info("====== Apache httpclient 初始化连接池完毕 ======");

    return client;
  }

  // @Bean
  // public okhttp3.OkHttpClient okHttpClient()
  // {
  //   log.info("====== Apache okhttp3 strating... ======");
  //   return new okhttp3.OkHttpClient.Builder()
  //       //设置连接超时
  //       .connectTimeout(10, TimeUnit.SECONDS)
  //       //设置读超时
  //       .readTimeout(10, TimeUnit.SECONDS)
  //       //设置写超时
  //       .writeTimeout(10, TimeUnit.SECONDS)
  //       //是否自动重连
  //       .retryOnConnectionFailure(true)
  //       .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
  //       .build();
  // }

  /**
   * @remark: 默认的全局fallback工厂配置
   * @param:
   * @return: DefaultHystrixFallbackFactory
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 2:42 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  @Bean
  @Scope("prototype")
  @ConditionalOnMissingBean
  public HystrixFallbackFactory hystrixFallbackFactory()
  {
    HystrixFallbackFactory hystrixFallbackFactory = new HystrixFallbackFactory();
    hystrixFallbackFactory.setFallbackHandlerFactory(new MyHystrixFallbackHandlerFactory());
    return hystrixFallbackFactory;
  }

  /**
   * @remark: 服务consumer端配置
   * @param:
   * @return: java.util.List<org.springframework.cloud.openfeign.FeignFormatterRegistrar>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 2:47 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  @Bean
  public List<FeignFormatterRegistrar> feignFormatterRegistrar()
  {
    return Collections.singletonList(new DefaultFeignFormatterRegistrar());
  }

  /**
   * @remark: 基于SpringMvc的服务provider端配置
   * @param: null
   * @return: null
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 2:47 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  // @Configuration
  // @ConditionalOnWebApplication(type = Type.SERVLET)
  // public static class DefaultFeignClientsWebMvcConfiguration implements WebMvcConfigurer
  // {
  //
  //   @Override
  //   public void addFormatters(FormatterRegistry registry)
  //   {
  //     registry.addConverter(new String2ObjectRequestParamConverter());
  //   }
  //
  // }

  /**
   * 基于Webflux的服务provider端配置
   */
  // @Configuration
  // @ConditionalOnWebApplication(type = Type.REACTIVE)
  // public static class DefaultFeignClientsWebFluxConfiguration
  // {
  //   //TODO
  // }

  public static class DefaultFeignFormatterRegistrar implements FeignFormatterRegistrar
  {

    @Override
    public void registerFormatters(FormatterRegistry registry)
    {
      registry.addConverter(new ObjectRequestParam2StringConverter());
    }

  }
}
