package com.hawthorn.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p>
 *  发送一次消息，多个符合的队列都可以收到消息，发送者和消费者一对多
 * </p>
 * @remark: topic rabbit mq 配置文件
 * @author: andy.ten@tom.com
 * @created: 1/11/21 10:23 PM
 * @lasted: 1/11/21 10:23 PM
 * @version v1.0.1
 */
@Configuration
public class TopicRabbitConfig
{
  //绑定键
  public final static String man = "topic.man";
  public final static String woman = "topic.women";

  @Bean
  public Queue firstQueue()
  {
    return new Queue(TopicRabbitConfig.man);
  }

  @Bean
  public Queue secondQueue()
  {
    return new Queue(TopicRabbitConfig.woman);
  }

  // TopicExchange创建的交换机默认是持久化的
  @Bean
  TopicExchange exchange()
  {
    return new TopicExchange("topicExchange");
  }


  //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
  //这样只要是消息携带的路由键是topic.man,才会分发到该队列
  @Bean
  Binding bindingExchangeMessage()
  {
    return BindingBuilder.bind(firstQueue()).to(exchange()).with(man);
  }

  //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
  // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
  @Bean
  Binding bindingExchangeMessage2()
  {
    return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic.");
  }

}
