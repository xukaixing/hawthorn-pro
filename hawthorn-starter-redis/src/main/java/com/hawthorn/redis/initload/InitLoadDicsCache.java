package com.hawthorn.redis.initload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 初始化加载缓存
 * @author: andy.ten@tom.com
 * @created: 3/4/21 10:55 PM
 * @lasted: 3/4/21 10:55 PM
 * @version v1.0.1
 */
@Component
@Slf4j
public class InitLoadDicsCache implements ApplicationRunner
{
  @Override
  @Async
  public void run(ApplicationArguments arg)
  {
    // 加载用户信息缓存

    // 加载用户角色信息缓存
  }

}
