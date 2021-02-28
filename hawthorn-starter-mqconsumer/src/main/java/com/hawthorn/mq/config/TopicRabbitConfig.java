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
 *   当一个队列的绑定键为 "#"（井号） 的时候，这个队列将会无视消息的路由键，接收所有的消息。
 *   当 * (星号) 和 # (井号) 这两个特殊字符都未在绑定键中出现的时候，此时主题交换机就拥有的直连交换机的行为。
 *   所以主题交换机也就实现了扇形交换机的功能，和直连交换机的功能，推荐使用。
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
    return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic.#");
  }

}
