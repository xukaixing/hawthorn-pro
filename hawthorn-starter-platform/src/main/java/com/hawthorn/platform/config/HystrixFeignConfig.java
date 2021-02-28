package com.hawthorn.platform.config;

import com.netflix.hystrix.*;
import feign.Feign;
import feign.Target;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Method;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark:
 * <p>
 *   feign默认集成了Hystrix，通过该类配置，设置hystrix的默认值，而避免每个微服务去配置hystrix参数
 *   另外，也可以指定单个类的熔断配置（feign-hystrix默认只支持default和类名#方法名()的配置）
 * </p>
 * @author: andy.ten@tom.com
 * @date: 2020/11/9 4:01 下午
 * @version v1.0.1
 */
@Configuration
public class HystrixFeignConfig
{
  @Bean
  @Scope("prototype")
  @ConditionalOnProperty(name = "feign.hystrix.enabled") // 仅当feign的hystrix启用时有效
  public Feign.Builder feignHystrixBuilder()
  {

    // 使用自定义的默认配置
    return HystrixFeign.builder().setterFactory(new MyDefault());
  }

  /**
   * 修改源码
   */
  static final class MyDefault implements SetterFactory
  {

    @Override
    public HystrixCommand.Setter create(Target<?> target, Method method)
    {
      String groupKey = target.name();
      String commandKey = Feign.configKey(target.type(), method);

      // 配置文件中，指定单个feign接口的hystrix配置
      // hystrix:
      //   command:
      //     TestDemoApi:   ## 支持类级别
      if ("TestDemoApi".equals(target.type().getSimpleName()))
      {
        return HystrixCommand.Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
            .andCommandKey(HystrixCommandKey.Factory.asKey("TestDemoApi"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(commandKey))
            // ****** threadpool ******
            .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                // 线程池核心线程数，默认为10；并发执行的最大线程数；大多数时候，默认值为10的线程会很好
                .withCoreSize(10)
                // 最大排队长度。默认-1，使用SynchronousQueue。其他值则使用 LinkedBlockingQueue。如果要从-1换成其他值则需重启，即该值不能动态调整，若要动态调整，需要使用到下边这个配置
                .withMaximumSize(1000)
                // 即使maxQueueSize没有达到，达到queueSizeRejectionThreshold该值后，请求也会被拒绝，默认值5
                .withQueueSizeRejectionThreshold(800)
            )
            // ****** default ******
            .andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter()
                    // 是否开启回退方法
                    .withFallbackEnabled(true)
                    // 该配置用于 HystrixCommand 执行的超时时间设置，当 HystrixCommand 执行的时间超过了该配置所设置的数值后就会进入服务降级处理，单位是毫秒，默认值为 1000。
                    // 根据Ribbon的yml超时配置， hystrix的参考超时时间的计算：(1 + MaxAutoRetries ）*（1+ MaxAutoRetriesNextServer) *（ReadTimeout +ConnectTimeout） 。
                    .withExecutionTimeoutInMilliseconds(120 * 1000)
                    // ****** circuitBreaker ******
                    // 当在配置时间窗口内达到此数量的失败后，进行短路。默认20个;
                    .withCircuitBreakerRequestVolumeThreshold(50)
                    // 出错百分比阈值，当达到此阈值后，开始短路。默认50%）
                    .withCircuitBreakerErrorThresholdPercentage(50)
                    // 短路多久以后开始尝试是否恢复，默认5s）-单位ms
                    .withCircuitBreakerSleepWindowInMilliseconds(5 * 1000)
            );
      }

      // 配置文件中，全部feign默认的hystrix配置
      // hystrix:
      //   command:
      //     default:   ## 全局默认
      return HystrixCommand.Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
          .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
          // ****** threadpool ******
          .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
              // 线程池核心线程数，默认为10；并发执行的最大线程数；大多数时候，默认值为10的线程会很好
              .withCoreSize(10)
              // 最大排队长度。默认-1，使用SynchronousQueue。其他值则使用 LinkedBlockingQueue。如果要从-1换成其他值则需重启，即该值不能动态调整，若要动态调整，需要使用到下边这个配置
              .withMaximumSize(1000)
              // 即使maxQueueSize没有达到，达到queueSizeRejectionThreshold该值后，请求也会被拒绝，默认值5
              .withQueueSizeRejectionThreshold(800)
          )
          // ****** default ******
          .andCommandPropertiesDefaults(
              HystrixCommandProperties.Setter()
                  // 是否开启回退方法
                  .withFallbackEnabled(true)
                  // 该配置用于 HystrixCommand 执行的超时时间设置，当 HystrixCommand 执行的时间超过了该配置所设置的数值后就会进入服务降级处理，单位是毫秒，默认值为 1000。
                  // hystrix超时时间和feign定义的超时时间，哪个超时时间设置的短，则哪个设置生效
                  .withExecutionTimeoutInMilliseconds(30 * 1000)
                  // ****** circuitBreaker ******
                  // 当在配置时间窗口内达到此数量的失败后，进行短路。默认20个/1秒;如：每10s内请求失败数量达到20个，断路器开
                  .withCircuitBreakerRequestVolumeThreshold(20)
                  // 出错百分比阈值，当达到此阈值后，开始短路。默认50%）
                  .withCircuitBreakerErrorThresholdPercentage(50)
                  // 短路多久以后开始尝试是否恢复，默认5s）-单位ms 注意：只尝试恢复一次
                  .withCircuitBreakerSleepWindowInMilliseconds(5 * 1000)
          );
    }
  }
}
