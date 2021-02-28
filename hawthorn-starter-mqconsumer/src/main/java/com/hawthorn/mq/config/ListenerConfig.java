package com.hawthorn.mq.config;

import org.springframework.context.annotation.Configuration;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: 消费者监听配置
 * @author: andy.ten@tom.com
 * @created: 1/18/21 5:29 PM
 * @lasted: 1/18/21 5:29 PM
 * @version v1.0.1
 */
@Configuration
public class ListenerConfig
{
  // @Autowired
  // private CachingConnectionFactory connectionFactory;
  // @Autowired
  // private MyAckReceiver myAckReceiver;//消息接收处理类
  //
  // @Bean
  // public SimpleMessageListenerContainer simpleMessageListenerContainer()
  // {
  //   SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
  //   container.setConcurrentConsumers(1);
  //   container.setMaxConcurrentConsumers(1);
  //   // RabbitMQ默认是自动确认，这里改为手动确认消息
  //   container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
  //
  //   // 设置一个队列
  //   container.setQueueNames("TestDirectQueue");
  //   // 如果同时设置多个如下： 前提是队列都是必须已经创建存在的
  //   //  container.setQueueNames("TestDirectQueue","TestDirectQueue2","TestDirectQueue3");
  //
  //
  //   // 另一种设置队列的方法,如果使用这种情况,那么要设置多个,就使用addQueues
  //   // container.setQueues(new Queue("TestDirectQueue",true));
  //   // container.addQueues(new Queue("TestDirectQueue2",true));
  //   // container.addQueues(new Queue("TestDirectQueue3",true));
  //
  //   container.setMessageListener(myAckReceiver);
  //
  //   return container;
  // }

}
