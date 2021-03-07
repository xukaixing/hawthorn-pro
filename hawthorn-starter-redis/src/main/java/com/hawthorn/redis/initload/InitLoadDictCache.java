package com.hawthorn.redis.initload;

import com.hawthorn.redis.service.dict.DictCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 初始化加载字典缓存
 * @author: andy.ten@tom.com
 * @created: 3/4/21 10:55 PM
 * @lasted: 3/4/21 10:55 PM
 * @version v1.0.1
 */
@Component
@Slf4j
public class InitLoadDictCache implements ApplicationRunner
{
  @Autowired
  DictCacheService dictCacheService;


  @Override
  @Async("dict2RedisExecutor")
  public void run(ApplicationArguments arg)
  {
    // 加载字典信息缓存
    dictCacheService.init();
  }

}
