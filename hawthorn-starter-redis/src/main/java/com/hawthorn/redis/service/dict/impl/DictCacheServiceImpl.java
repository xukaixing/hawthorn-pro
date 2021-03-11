package com.hawthorn.redis.service.dict.impl;

import com.hawthorn.component.constant.RedisConstant;
import com.hawthorn.component.utils.common.StringMyUtil;
import com.hawthorn.platform.redis.RedisMyClient;
import com.hawthorn.redis.model.dto.dictcache.DictCacheDTO;
import com.hawthorn.redis.repository.DictCacheMapper;
import com.hawthorn.redis.service.dict.DictCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 加载字典缓存服务实现类
 * @author: andy.ten@tom.com
 * @created: 3/4/21 11:05 PM
 * @lasted: 3/4/21 11:05 PM
 * @version v1.0.1
 */
@Service
@Slf4j
public class DictCacheServiceImpl implements DictCacheService
{
  @Autowired
  RedisMyClient redisMyClient;

  @Resource
  private DictCacheMapper dictCacheMapper;

  public void init()
  {
    List<DictCacheDTO> dictCache = dictCacheMapper.selectDict();
    String value, type, label;
    String key;
    // todo 先删除redis中的key
    redisMyClient.removeAll(StringMyUtil.placeHolder(RedisConstant.REDIS_KEY_DICT_PREFIX, "*"));

    for (DictCacheDTO dictCacheDTO : dictCache)
    {
      value = dictCacheDTO.getValue();
      type = dictCacheDTO.getType();
      label = dictCacheDTO.getLabel();
      key = StringMyUtil.placeHolder(RedisConstant.REDIS_KEY_DICT_PREFIX, dictCacheDTO.getType());
      redisMyClient.hSet(key, RedisConstant.REDIS_KEY_DICVALUE, value);
      redisMyClient.hSet(key, RedisConstant.REDIS_KEY_DICLABEL, label);
      redisMyClient.hSet(key, RedisConstant.REDIS_KEY_DICTYPE, type);
    }

    log.info("====== load dictdata to redis success ======");
  }
}
