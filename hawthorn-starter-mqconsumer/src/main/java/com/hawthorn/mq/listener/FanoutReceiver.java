package com.hawthorn.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 消息消费监听
 * @author: andy.ten@tom.com
 * @created: 1/13/21 10:16 AM
 * @lasted: 1/13/21 10:16 AM
 * @version v1.0.1
 */
@Component
@Slf4j
public class FanoutReceiver
{
  @RabbitHandler
  @RabbitListener(queues = "fanout.A")//监听的队列名称
  public void processA(Map testMessage)
  {
    log.info("FanoutReceiverA消费者收到消息  : " + testMessage.toString());
  }

  @RabbitHandler
  @RabbitListener(queues = "fanout.B")//监听的队列名称
  public void processB(Map testMessage)
  {
    log.info("FanoutReceiverB消费者收到消息  : " + testMessage.toString());
  }

  @RabbitHandler
  @RabbitListener(queues = "fanout.C")//监听的队列名称
  public void processC(Map testMessage)
  {
    log.info("FanoutReceiverC消费者收到消息  : " + testMessage.toString());
  }

}
