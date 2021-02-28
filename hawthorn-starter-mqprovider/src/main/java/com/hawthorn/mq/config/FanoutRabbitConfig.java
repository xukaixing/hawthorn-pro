package com.hawthorn.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p>
 *   发送一次消息，全部队列都可以收到消息，发送者和消费者一对多
 * </p>
 * @remark: fanout rabbit mq 配置文件
 * @author: andy.ten@tom.com
 * @created: 1/18/21 2:56 PM
 * @lasted: 1/18/21 2:56 PM
 * @version v1.0.1
 */
@Configuration
public class FanoutRabbitConfig
{
  /**
   *  创建三个队列 ：fanout.A   fanout.B  fanout.C
   *  将三个队列都绑定在交换机 fanoutExchange 上
   *  因为是扇型交换机, 路由键无需配置,配置也不起作用
   */


  @Bean
  public Queue queueA()
  {
    return new Queue("fanout.A");
  }

  @Bean
  public Queue queueB()
  {
    return new Queue("fanout.B");
  }

  @Bean
  public Queue queueC()
  {
    return new Queue("fanout.C");
  }

  @Bean
  FanoutExchange fanoutExchange()
  {
    return new FanoutExchange("fanoutExchange");
  }

  @Bean
  Binding bindingExchangeA()
  {
    return BindingBuilder.bind(queueA()).to(fanoutExchange());
  }

  @Bean
  Binding bindingExchangeB()
  {
    return BindingBuilder.bind(queueB()).to(fanoutExchange());
  }

  @Bean
  Binding bindingExchangeC()
  {
    return BindingBuilder.bind(queueC()).to(fanoutExchange());
  }

}
