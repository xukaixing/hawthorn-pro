package com.hawthorn.redis.repository;

import com.hawthorn.platform.repository.DBMapper;
import com.hawthorn.redis.model.dto.dictcache.DictCacheDTO;
import com.hawthorn.redis.model.po.DictCachePO;
import com.hawthorn.redis.repository.provider.DictCacheSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo  SysDict Mapper类
 * @author: andy.ten@tom.com
 * @created: 3/5/21 9:54 PM
 * @lasted: 3/5/21 9:54 PM
 * @version v1.0.1
 */
@Repository
public interface DictCacheMapper extends DBMapper<DictCachePO>
{
  @SelectProvider(DictCacheSqlProvider.class)
  List<DictCacheDTO> selectDict();
}
