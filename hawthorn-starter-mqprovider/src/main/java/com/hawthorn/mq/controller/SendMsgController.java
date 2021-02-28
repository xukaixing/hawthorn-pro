package com.hawthorn.mq.controller;

import com.hawthorn.component.ret.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: 发送消息控制类
 * @author: andy.ten@tom.com
 * @created: 1/12/21 10:18 AM
 * @lasted: 1/12/21 10:18 AM
 * @version v1.0.1
 */
@Api(tags = {"mq-provider-sendMsg"})
@RestController
@RequestMapping("/sendMsg")
public class SendMsgController
{
  @Autowired
  RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

  /**
   * @author: andy.ten@tom.com
   * @date: 12/21/20 4:31 PM
   * @version: 1.0.1
   */
  @ApiOperation(value = "发送->发送direct信息", notes = "RabbitMQ发送direct消息")
  @ApiImplicitParams({
  })
  @PutMapping("/sendDirectMessage")
  public RestResult sendDirectMessage()
  {
    String messageId = String.valueOf(UUID.randomUUID());
    String messageData = "test send rabbitmq message, hello!";
    String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    Map<String, Object> map = new HashMap<>();
    map.put("messageId", messageId);
    map.put("messageData", messageData);
    map.put("createTime", createTime);
    // 将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
    // mq管理控制台显示payload中，只有string可以显示字符，否则就是base64
    
    rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
    return RestResult.success("ok");
  }

  /**
   * @author: andy.ten@tom.com
   * @date: 1/15/21 10:17 AM
   * @version: 1.0.1
   */
  @ApiOperation(value = "发送->发送topic信息", notes = "RabbitMQ发送topic消息")
  @ApiImplicitParams({

  })
  @PutMapping("/sendTopicMessage1")
  public RestResult sendTopicMessage1()
  {
    String messageId = String.valueOf(UUID.randomUUID());
    String messageData = "message: M A N ";
    String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    Map<String, Object> manMap = new HashMap<>();
    manMap.put("messageId", messageId);
    manMap.put("messageData", messageData);
    manMap.put("createTime", createTime);
    rabbitTemplate.convertAndSend("topicExchange", "topic.man", manMap);
    return RestResult.success("ok");
  }

  /**
   * @author: andy.ten@tom.com
   * @date: 1/15/21 10:17 AM
   * @version: 1.0.1
   */
  @ApiOperation(value = "发送->发送topic信息2", notes = "RabbitMQ发送topic消息")
  @ApiImplicitParams({

  })
  @PutMapping("/sendTopicMessage2")
  public RestResult sendTopicMessage2()
  {
    String messageId = String.valueOf(UUID.randomUUID());
    String messageData = "message: woman is all ";
    String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    Map<String, Object> womanMap = new HashMap<>();
    womanMap.put("messageId", messageId);
    womanMap.put("messageData", messageData);
    womanMap.put("createTime", createTime);
    rabbitTemplate.convertAndSend("topicExchange", "topic.women", womanMap);
    return RestResult.success("ok");
  }

  /**
   * @author: andy.ten@tom.com
   * @date: 1/15/21 10:17 AM
   * @version: 1.0.1
   */
  @ApiOperation(value = "发送->发送fanout信息", notes = "RabbitMQ发送fanout消息")
  @ApiImplicitParams({

  })
  @PutMapping("/sendFanoutMessage")
  public RestResult sendFanoutMessage()
  {
    String messageId = String.valueOf(UUID.randomUUID());
    String messageData = "message: testFanoutMessage ";
    String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    Map<String, Object> map = new HashMap<>();
    map.put("messageId", messageId);
    map.put("messageData", messageData);
    map.put("createTime", createTime);
    rabbitTemplate.convertAndSend("fanoutExchange", null, map);
    return RestResult.success("ok");
  }

  /**
   * @author: andy.ten@tom.com
   * @date: 1/15/21 10:17 AM
   * @version: 1.0.1
   */
  @ApiOperation(value = "发送->发送错误回调信息", notes = "RabbitMQ发送消息错误回调测试")
  @ApiImplicitParams({

  })
  @PutMapping("/TestMessageAck")
  public RestResult TestMessageAck()
  {
    String messageId = String.valueOf(UUID.randomUUID());
    String messageData = "message: non-existent-exchange test message ";
    String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    Map<String, Object> map = new HashMap<>();
    map.put("messageId", messageId);
    map.put("messageData", messageData);
    map.put("createTime", createTime);
    // 未找到交换机
    //rabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", map);
    // 找到交换机，未找到队列
    rabbitTemplate.convertAndSend("lonelyDirectExchange", "TestDirectRouting", map);
    return RestResult.success("ok");
  }

}
