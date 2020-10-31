package com.hawthorn.platform.config;


import com.hawthorn.component.boot.AbstractSpringConfiguration;
import com.hawthorn.platform.hystrix.ReactiveDefaultHystrixConcurrencyStrategy;
import com.hawthorn.platform.hystrix.ReactiveSecurityHystrixConcurrencyStrategy;
import com.hawthorn.platform.hystrix.ServletDefaultHystrixConcurrencyStrategy;
import com.hawthorn.platform.hystrix.ServletSecurityHystrixConcurrencyStrategy;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

//import org.springframework.security.core.context.SecurityContext;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 默认的springcloud-hystrix配置
 * <p>
 *   在Servlet环境下，将HttpServletRequest、HttpServletResponse、Authentication(如果引入了Spring-Security)绑定到HystrixConcurrencyContext中去，供在Hystrix的执行上下文中获取
 *   在Reactive环境下，将ServerWebExchange、Authentication(如果引入了Spring-Security)绑定到HystrixConcurrencyContext中去，供在Hystrix的执行上下文中获取
 * </p>
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:04 下午
 * @version v1.0.1
 */
@Configuration
@Conditional(HystrixConfig.HystrixConcurrencyCondition.class)
@ConditionalOnClass({Hystrix.class})
@Slf4j
public class HystrixConfig extends AbstractSpringConfiguration
{
  @Configuration
  @ConditionalOnWebApplication(type = Type.SERVLET)
  @ConditionalOnMissingClass({"org.springframework.security.core.context.SecurityContext"})
  public static class ServletDefaultHystrixConfiguration
  {

    public ServletDefaultHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy)
    {
      registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ServletDefaultHystrixConcurrencyStrategy::new);
    }

  }

  @Configuration
  @ConditionalOnWebApplication(type = Type.SERVLET)
  // @ConditionalOnClass({SecurityContext.class})
  public static class ServletSecurityHystrixConfiguration
  {

    public ServletSecurityHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy)
    {
      registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ServletSecurityHystrixConcurrencyStrategy::new);
    }

  }

  @Configuration
  @ConditionalOnWebApplication(type = Type.REACTIVE)
  @ConditionalOnMissingClass({"org.springframework.security.core.context.SecurityContext"})
  public static class ReactiveDefaultHystrixConfiguration
  {

    public ReactiveDefaultHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy)
    {
      registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ReactiveDefaultHystrixConcurrencyStrategy::new);
    }

  }

  @Configuration
  @ConditionalOnWebApplication(type = Type.REACTIVE)
  //@ConditionalOnClass({SecurityContext.class})
  public static class ReactiveSecurityHystrixConfiguration
  {

    public ReactiveSecurityHystrixConfiguration(ObjectProvider<HystrixConcurrencyStrategy> existingConcurrencyStrategy)
    {
      registerConcurrencyStrategy(existingConcurrencyStrategy.getIfAvailable(), ReactiveSecurityHystrixConcurrencyStrategy::new);
    }

  }

  protected static void registerConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy, Function<HystrixConcurrencyStrategy, HystrixConcurrencyStrategy> concurrencyStrategyProvider)
  {
    // Keeps references of existing Hystrix plugins.
    HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
        .getEventNotifier();
    HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
        .getMetricsPublisher();
    HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
        .getPropertiesStrategy();
    HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance()
        .getCommandExecutionHook();
    HystrixConcurrencyStrategy concurrencyStrategy = detectRegisteredConcurrencyStrategy(existingConcurrencyStrategy);

    concurrencyStrategy = concurrencyStrategyProvider.apply(concurrencyStrategy);

    log.info(">>> concurrencyStrategy = " + concurrencyStrategy);

    HystrixPlugins.reset();

    // Registers existing plugins excepts the Concurrent Strategy plugin.
    HystrixPlugins.getInstance().registerConcurrencyStrategy(concurrencyStrategy);
    HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
    HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
    HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
    HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
  }

  protected static HystrixConcurrencyStrategy detectRegisteredConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy)
  {
    HystrixConcurrencyStrategy registeredStrategy = HystrixPlugins.getInstance()
        .getConcurrencyStrategy();
    if (existingConcurrencyStrategy == null)
    {
      return registeredStrategy;
    }
    // Hystrix registered a default Strategy.
    if (registeredStrategy instanceof HystrixConcurrencyStrategyDefault)
    {
      return existingConcurrencyStrategy;
    }
    // If registeredStrategy not the default and not some use bean of
    // existingConcurrencyStrategy.
    if (!existingConcurrencyStrategy.equals(registeredStrategy))
    {
      log.warn(
          "Multiple HystrixConcurrencyStrategy detected. Bean of HystrixConcurrencyStrategy was used.");
    }
    return existingConcurrencyStrategy;
  }

  static class HystrixConcurrencyCondition extends AllNestedConditions
  {

    HystrixConcurrencyCondition()
    {
      super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(name = "hystrix.shareConcurrencyContext", havingValue = "true", matchIfMissing = true)
    static class ShareConcurrencyContext
    {

    }
  }
}
