package com.hawthorn.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p>
 *   ①消息推送到server，但是在server里找不到交换机：触发ConfirmCallbackd回调函数
 *   ②消息推送到server，找到交换机了，但是没找到队列：触发ConfirmCallback和RetrunCallback两个回调函数，ConfirmCallback返回true
 *   ③消息推送到sever，交换机和队列啥都没找到：触发ConfirmCallback 回调函数
 *   ④消息推送成功，触发ConfirmCallback 回调函数，返回结果true
 *   可以随意推送消息到一个不存在的交换机或者队列，然后即可进行异常测试
 * </p>
 * @remark: 消息提供者推送消息的消息确认调用回调机制配置
 * @author: andy.ten@tom.com
 * @created: 1/18/21 4:22 PM
 * @lasted: 1/18/21 4:22 PM
 * @version v1.0.1
 */
@Configuration
@Slf4j
public class RabbitConfig
{
  @Autowired
  private Environment env;

  @Autowired
  private CachingConnectionFactory connectionFactory;

  @Autowired
  private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;


  /**
   * 单一消费者
   */
  @Bean(name = "singleListenerContainer")
  public SimpleRabbitListenerContainerFactory singleListenerContainer()
  {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    // 自定义message converter
    // factory.setMessageConverter(new Jackson2JsonMessageConverter());
    // 单一消费者

    // factory.setConcurrentConsumers(1);
    // factory.setMaxConcurrentConsumers(1);
    // factory.setPrefetchCount(1);
    return factory;
  }

  /**
   * 多个消费者
   */
  @Bean(name = "multiListenerContainer")
  public SimpleRabbitListenerContainerFactory multiListenerContainer()
  {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factoryConfigurer.configure(factory, connectionFactory);
    // 自定义message converter
    //factory.setMessageConverter(new Jackson2JsonMessageConverter());
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

    //factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency", int.class));
    //factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency", int.class));
    //factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch", int.class));
    return factory;
  }


  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)
  {
    RabbitTemplate rabbitTemplate = new RabbitTemplate();
    rabbitTemplate.setConnectionFactory(connectionFactory);
    // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
    rabbitTemplate.setMandatory(true);

    // 消息确认监听器ConfirmCallback
    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
    {
      log.info("ConfirmCallback: -{}:{}", "correlationData", correlationData);
      log.info("ConfirmCallback: -{}:{}", "ack", ack);
      log.info("ConfirmCallback: -{}:{}", "cause", cause);
      // 消息确认失败
      if (!ack)
      {
        log.error("消息处理失败: {}", cause);
      } else
      {
        // 处理成功后，删除队列中的消息
        // if (!StrUtil.isEmpty(stringRedisTemplate.opsForValue().get(correlationData.toString())))
        // {
        //   stringRedisTemplate.delete(correlationData.toString());
        // }
      }
    });

    // 消息发送失败返回监听器returnedMessage
    rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
    {
      log.error("ReturnCallback: -{}:{}", "message", message);
      log.error("ReturnCallback: -{}:{}", "replyCode", replyCode);
      log.error("ReturnCallback: -{}:{}", "replyText", replyText);
      log.error("ReturnCallback: -{}:{}", "exchange", exchange);
      log.error("ReturnCallback: -{}:{}", "routingKey", routingKey);
    });

    return rabbitTemplate;
  }
}
