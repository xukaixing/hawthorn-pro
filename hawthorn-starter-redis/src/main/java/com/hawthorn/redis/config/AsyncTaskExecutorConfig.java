package com.hawthorn.redis.config;

import com.hawthorn.component.utils.thread.AsyncThreadPoolTaskMyUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 异步任务配置类
 * @author: andy.ten@tom.com
 * @created: 3/4/21 10:44 PM
 * @lasted: 3/4/21 10:44 PM
 * @version v1.0.1
 */
@Configuration
@EnableAsync
public class AsyncTaskExecutorConfig
{
  /**
   * 配置系统用户存储至redis
   * @return
   */
  @Bean("user2RedisExecutor")
  public Object user2RedisExecutor()
  {
    return AsyncThreadPoolTaskMyUtil.createAsyncTaskExecutor();
  }

  /**
   * 配置系统字典存储至redis
   * @return
   */
  @Bean("dict2RedisExecutor")
  public Object dict2RedisExecutor()
  {
    return AsyncThreadPoolTaskMyUtil.createAsyncTaskExecutor();
  }
}
