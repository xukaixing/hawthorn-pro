package com.hawthorn.redis.service.user.impl;

import com.hawthorn.component.constant.RedisConstant;
import com.hawthorn.component.utils.common.StringMyUtil;
import com.hawthorn.platform.redis.RedisMyClient;
import com.hawthorn.redis.model.dto.usercache.UserCacheDTO;
import com.hawthorn.redis.repository.UserCacheMapper;
import com.hawthorn.redis.service.user.UserCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 加载用户缓存服务实现类
 * @author: andy.ten@tom.com
 * @created: 3/4/21 11:05 PM
 * @lasted: 3/4/21 11:05 PM
 * @version v1.0.1
 */
@Service
@Slf4j
public class UserCacheServiceImpl implements UserCacheService
{
  @Autowired
  RedisMyClient redisMyClient;

  @Resource
  private UserCacheMapper userCacheMapper;

  public void init()
  {
    List<UserCacheDTO> userCache = userCacheMapper.selectUser();
    String username, usertype, deptid;
    Long userid;
    String key;
    // todo 先删除redis中的key
    redisMyClient.removeAll(StringMyUtil.placeHolder(RedisConstant.REDIS_KEY_USER_PREFIX, "*"));

    for (UserCacheDTO userCacheDTO : userCache)
    {
      username = userCacheDTO.getName();
      userid = userCacheDTO.getId();
      usertype = userCacheDTO.getUserType() == null ? "" : userCacheDTO.getUserType().toString();
      deptid = userCacheDTO.getDeptId() == null ? "" : userCacheDTO.getDeptId().toString();
      key = StringMyUtil.placeHolder(RedisConstant.REDIS_KEY_USER_PREFIX, userCacheDTO.getName());
      redisMyClient.hSet(key, RedisConstant.REDIS_KEY_USERID, String.valueOf(userid));
      redisMyClient.hSet(key, RedisConstant.REDIS_KEY_USERNAME, username);
      redisMyClient.hSet(key, RedisConstant.REDIS_KEY_USERTYPE, usertype);
      redisMyClient.hSet(key, RedisConstant.REDIS_KEY_DEPTID, deptid);
    }
    log.info("====== load userdata to redis success ======");
  }
}
