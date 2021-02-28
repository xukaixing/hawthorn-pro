package com.hawthorn.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
public class DirectReceiver
{
  @RabbitHandler
  @RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueu
  public void process(Map testMessage, Channel channel, Message message) throws IOException
  {
    try
    {
      log.info("DirectReceiver消费者收到消息: " + testMessage.toString());
      // 确认签收
      channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
      log.info("DirectReceiver消费者收到消息，处理成功.");
    } catch (Exception e)
    {
      log.error("TestDirectQueue Listener:", e);
      //签收失败
      /**
       * deliveryTag:该消息的index
       * multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
       * requeue：被拒绝的是否重新入队列
       */
      // 如果重新放进队列中还是会放在队列头部，继续消费者消费，如果一直消费一直错误就会产生堆积问题，理性使用
      channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);

      // channel.basicNack 与 channel.basicReject 的区别在于basicNack可以拒绝多条消息，而basicReject一次只能拒绝一条消息
    }

  }

}
