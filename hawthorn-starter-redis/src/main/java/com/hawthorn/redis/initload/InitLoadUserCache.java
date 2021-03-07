package com.hawthorn.redis.initload;

import com.hawthorn.redis.service.user.UserCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 初始化加载用户缓存
 * @author: andy.ten@tom.com
 * @created: 3/4/21 10:55 PM
 * @lasted: 3/4/21 10:55 PM
 * @version v1.0.1
 */
@Component
@Slf4j
public class InitLoadUserCache implements ApplicationRunner
{
  @Autowired
  UserCacheService userCacheService;

  @Override
  @Async("user2RedisExecutor")
  public void run(ApplicationArguments arg)
  {
    // 加载用户信息缓存
    userCacheService.init();
    // 加载用户角色信息缓存
  }

}
