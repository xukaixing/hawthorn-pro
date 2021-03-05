package com.hawthorn.redis.repository;

import com.hawthorn.platform.repository.DBMapper;
import com.hawthorn.redis.model.dto.usercache.UserCacheDTO;
import com.hawthorn.redis.model.po.UserCachePO;
import com.hawthorn.redis.repository.provider.UserCacheSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Copyright: Copyright (c) 2020 andyten
 * @remark: SysUser Mapper类
 * @author:andy.ten@tom.com
 * @date:2020/8/13 11:47 上午
 * @version v1.0.1
 */
@Repository
public interface UserCacheMapper extends DBMapper<UserCachePO>
{
  @SelectProvider(UserCacheSqlProvider.class)
  List<UserCacheDTO> selectUser();
}
