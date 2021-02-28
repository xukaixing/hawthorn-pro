package com.hawthorn.gateway.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自定义redis-cli工具类
 * @author:andy.ten@tom.com
 * @date:2020/10/17 4:02 下午
 * @version v1.0.1
 */
@SuppressWarnings("ConstantConditions")
@Component
@Slf4j
public class RedisMyClient
{
  @Autowired
  RedisTemplate<Object, Object> redisTemplate;

  public RedisMyClient(RedisTemplate<Object, Object> redisTemplate)
  {
    this.redisTemplate = redisTemplate;
  }

  @SuppressWarnings("rawtypes")
  @Resource(name = "redisValueOps")
  public ValueOperations valueOps;
  @SuppressWarnings("rawtypes")
  @Resource(name = "redisHashOps")
  public HashOperations hashOps;
  @SuppressWarnings("rawtypes")
  @Resource(name = "redisListOps")
  public ListOperations listOps;
  @SuppressWarnings("rawtypes")
  @Resource(name = "redisSetOps")
  private SetOperations setOps;
  @SuppressWarnings("rawtypes")
  @Resource(name = "redisZSetOps")
  private ZSetOperations zSetOps;

  /* ********************************************* 字符串类型 ********************************************* */

  /**
   * @remark: 设置key值
   * @param: key
   * @param: value
   * @return: void
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:35 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public void set(String key, Object value)
  {
    try
    {
      valueOps.set(key, value);
    } catch (Exception e)
    {
      log.error("redis set error e = {}", e.getMessage());
    }
  }

  /**
   * @remark: 设置key并设置失效时间
   * @param: key
   * @param: value
   * @param: milliseconds: 失效时间（毫秒），time要大于0 如果time小于等于0 将设置无限期
   * @return: void
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:36 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public void set(String key, Object value, long milliseconds)
  {
    try
    {
      valueOps.set(key, value);
      if (milliseconds > 0)
        expire(key, milliseconds);
    } catch (Exception e)
    {
      log.error("redis set error e={}", e.getMessage());
    }
  }

  /**
   * @remark: 递增
   * @param: key: 必须是数字
   * @param: value: 递增幅度必须要大于0
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:40 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public void incrby(String key, long value)
  {
    try
    {
      valueOps.increment(key, value);
    } catch (Exception e)
    {
      log.error("redis incrby error e={}", e.getMessage());
    }
  }

  /**
   * @remark: 递减
   * @param: key: 必须是数字
   * @param: value: 递减幅度必须要大于0
   * @return: void
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:43 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  // @SuppressWarnings("unchecked")
  // public void decrby(String key, long value)
  // {
  //   try
  //   {
  //     valueOps.decrement(key, value);
  //   } catch (Exception e)
  //   {
  //     log.error("redis decrby error e={}", e.getMessage());
  //   }
  // }

  /**
   * @remark: key获取
   * @param: key
   * @return: java.lang.Object
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:31 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  public Object get(String key)
  {
    try
    {
      return valueOps.get(key);
    } catch (Exception e)
    {
      log.error("redis get error = {}", e.getMessage());
    }
    return null;
  }

  /**
   * @remark: 删除key
   * @param: keys: 可以是一个，也可以是多个
   * @return: long:
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:23 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public long del(String... keys)
  {
    try
    {
      return (long) redisTemplate.execute((RedisCallback) connection ->
      {
        long result = 0;
        for (String key : keys)
        {
          result += connection.del(key.getBytes());
        }
        return result;
      });
    } catch (Exception e)
    {
      log.error("redis del error ={}", e.getMessage());
    }
    return 0;
  }

  /* ********************************************* 列表类型 ********************************************* */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void setList(String key, Collection values, long milliseconds)
  {
    try
    {
      listOps.remove(key, -1, null);
      listOps.leftPushAll(key, values);
      redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
    } catch (Exception e)
    {
      log.error("redis setList error ={}", e.getMessage());
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List getListAll(String key)
  {
    try
    {
      return listOps.range(key, 0L, -1L);
    } catch (Exception e)
    {
      log.error("redis getListAll error ={}", e.getMessage());
    }
    return Collections.emptyList();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void rightPushAll(String key, Collection values, long milliseconds)
  {
    try
    {
      listOps.rightPushAll(key, values);
      redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
    } catch (Exception e)
    {
      log.error("redis rightPushAll error ={}", e.getMessage());
    }
  }
  /* ********************************************* 散列类型 ********************************************* */

  /**
   * @remark: 获取散列类型HashSet指定key.field的值
   * @param: key: 不能为null
   * @param: field: 不能为null
   * @return: java.lang.Object
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:43 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public Object hGet(String key, String field)
  {
    try
    {
      return hashOps.get(key, field);
    } catch (Exception e)
    {
      log.error("redis hget error = {}", e.getMessage());
    }
    return null;
  }

  /**
   * @remark: 判断hash表中是否有该项的值
   * @param: key
   * @param: field
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:55 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public boolean hHasKey(String key, String field)
  {
    try
    {
      return hashOps.hasKey(key, field);
    } catch (Exception e)
    {
      log.error("redis hhasKey error = {}", e.getMessage());
    }
    return false;
  }

  /**
   * @remark: hash递增 如果不存在,就会创建一个 并把新增后的值返回
   * @param: key
   * @param: item
   * @param: by
   * @return: double
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:56 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked"})
  public double hincr(String key, String item, double by)
  {
    try
    {
      return hashOps.increment(key, item, by);
    } catch (Exception e)
    {
      log.error("redis hincr error = {}", e.getMessage());
      return 0.00;
    }
  }

  /**
   * @remark: hash递减
   * @param: key
   * @param: item
   * @param: by
   * @return: double
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:56 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked"})
  public double hdecr(String key, String item, double by)
  {
    try
    {
      return hashOps.increment(key, item, -by);
    } catch (Exception e)
    {
      log.error("redis hdecr error = {}", e.getMessage());
      return 0.00;
    }
  }

  /**
   * @remark: 获取hashKey对应的所有键值
   * @param: key
   * @return: java.util.Map<java.lang.Object, java.lang.Object>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:48 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Map hmGet(String key)
  {
    try
    {
      return hashOps.entries(key);
    } catch (Exception e)
    {
      log.error("redis hmGet error = {}", e.getMessage());
    }
    return null;
  }

  /**
   * @remark: 设置hashKey对应的所有键值
   * @param: key
   * @param: map
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:49 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked"})
  public boolean hmSet(String key, Map<String, Object> map)
  {
    try
    {
      hashOps.putAll(key, map);
      return true;
    } catch (Exception e)
    {
      log.error("redis hmSet error = {}", e.getMessage());
      return false;
    }
  }

  /**
   * @remark: 设置hashKey对应的所有键值和失效时间
   * @param: key
   * @param: map
   * @param: time
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:50 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked"})
  public boolean hmSet(String key, Map<String, Object> map, long miniseconds)
  {
    try
    {
      hashOps.putAll(key, map);
      if (miniseconds > 0)
      {
        expire(key, miniseconds);
      }
      return true;
    } catch (Exception e)
    {
      log.error("redis hmSet error = {}", e.getMessage());
      return false;
    }
  }

  /**
   * @remark: 向一张hash表中放入数据, 如果不存在将创建
   * @param: key
   * @param: field
   * @param: value
   * @return: void
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:51 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public boolean hSet(String key, String field, String value)
  {
    try
    {
      hashOps.put(key, field, value);
      return true;
    } catch (Exception e)
    {
      log.error("redis hset error = {}", e.getMessage());
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  public boolean hSet(String key, String field, String value, Long miniseconds)
  {
    try
    {
      hashOps.put(key, field, value);
      if (miniseconds > 0)
      {
        expire(key, miniseconds);
      }
      return true;
    } catch (Exception e)
    {
      log.error("redis hset error = {}", e.getMessage());
      return false;
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public List hMultiGet(String h, List keys)
  {
    try
    {
      return hashOps.multiGet(h, keys);
    } catch (Exception e)
    {
      log.error("redis multiGet error = {}", e.getMessage());
    }
    return Collections.emptyList();
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  public Map hGetAll(String key)
  {
    try
    {
      return hashOps.entries(key);
    } catch (Exception e)
    {
      log.error("redis hGetAll error = {}", e.getMessage());
    }
    return null;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void hPutAll(String key, Map map, long miniseconds)
  {
    try
    {
      hashOps.putAll(key, map);
      redisTemplate.expire(key, miniseconds, TimeUnit.MILLISECONDS);
    } catch (Exception e)
    {
      log.error("redis hPutAll error = {}", e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void hSetExpire(String key, String field, String value, long milliseconds)
  {
    try
    {
      hashOps.put(key, field, value);
      redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
    } catch (Exception e)
    {
      log.error("redis putAll error = {}", e.getMessage());
    }
  }

  /**
   * @remark: 删除hash表中的key.field的值
   * @param: key
   * @param: field
   * @return: long
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:54 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public long hDel(String key, String field)
  {
    try
    {
      return hashOps.delete(key, field);
    } catch (Exception e)
    {
      log.error("redis hdel error = {}", e.getMessage());
    }
    return 0;
  }

  /* ********************************************* 集合类型 ********************************************* */

  /**
   * @remark: 根据key获取Set中的所有值
   * @param: key
   * @return: java.util.Set<java.lang.Object>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:59 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set sGet(String key)
  {
    try
    {
      return setOps.members(key);
    } catch (Exception e)
    {
      log.error("redis sGet error = {}", e.getMessage());
      return null;
    }
  }

  /**
   * @remark: 将数据放入set缓存
   * @param: key
   * @param: values
   * @return: long
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 5:00 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked"})
  public long sSet(String key, Object... values)
  {
    try
    {
      return setOps.add(key, values);
    } catch (Exception e)
    {
      log.error("redis sSet error = {}", e.getMessage());
      return 0;
    }
  }

  /**
   * @remark: 根据value从一个set中查询, 是否存在
   * @param: key
   * @param: value
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 5:00 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings({"unchecked"})
  public boolean sHasKey(String key, Object value)
  {
    try
    {
      return setOps.isMember(key, value);
    } catch (Exception e)
    {
      log.error("redis sHasKey error = {}", e.getMessage());
      return false;
    }
  }

  /**
   * @remark: 将set数据放入缓存(带失效时间)
   * @param: key
   * @param: time
   * @param: values
   * @return: long
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 5:01 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public long sSetAndTime(String key, long miniseconds, Object... values)
  {
    try
    {
      Long count = setOps.add(key, values);
      if (miniseconds > 0)
      {
        expire(key, miniseconds);
      }
      return count;
    } catch (Exception e)
    {
      log.error("redis sSetAndTime error = {}", e.getMessage());
      return 0;
    }
  }

  /**
   * @remark: 获取set缓存的长度
   * @param: key
   * @return: long
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 5:02 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public long sGetSetSize(String key)
  {
    try
    {
      return setOps.size(key);
    } catch (Exception e)
    {
      log.error("redis sGetSetSize error = {}", e.getMessage());
      return 0;
    }
  }

  /**
   * @remark: 移除值为value的
   * @param: key
   * @param: values
   * @return: long
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 5:03 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @SuppressWarnings("unchecked")
  public long sRemove(String key, Object... values)
  {
    try
    {
      return setOps.remove(key, values);
    } catch (Exception e)
    {
      log.error("redis sRemove error = {}", e.getMessage());
      return 0;
    }
  }

  @SuppressWarnings({"unchecked"})
  public long sAdd(Object key, Object value, long milliseconds)
  {
    try
    {
      long n = setOps.add(key, value);
      redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
      return n;
    } catch (Exception e)
    {
      log.error("redis sAdd error = {}", e.getMessage());
    }
    return 0;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set sMembers(Object key)
  {
    try
    {
      return setOps.members(key);
    } catch (Exception e)
    {
      log.error("redis sMembers error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  @SuppressWarnings({"unchecked"})
  public long sMembersCount(Object key)
  {
    try
    {
      return setOps.size(key);
    } catch (Exception e)
    {
      log.error("redis sMembersCount error = {}", e.getMessage());
    }
    return 0;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set sIntersect(String key1, String key2)
  {
    try
    {
      return setOps.intersect(key1, key2);
    } catch (Exception e)
    {
      log.error("redis putAll error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set sIntersect(String key1, String... key2)
  {
    try
    {
      return setOps.intersect(key1, key2);
    } catch (Exception e)
    {
      log.error("redis sIntersect error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set sIntersect(String... keys)
  {
    try
    {
      if (keys.length == 0)
        return Collections.emptySet();
      else if (keys.length == 1)
      {
        String key1 = keys[0];
        return setOps.members(key1);
      } else
      {
        String key1 = keys[0];
        int i = 0;
        Set otherKeys = new HashSet<>();
        for (String s : keys)
        {
          if (i > 0)
            otherKeys.add(s);
          i++;
        }
        return setOps.intersect(key1, otherKeys);
      }
    } catch (Exception e)
    {
      log.error("redis sIntersect error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  @SuppressWarnings("unchecked")
  public long sIntersectAndStore(String key1, String key2, String key3)
  {
    try
    {
      return setOps.intersectAndStore(key1, key2, key3);
    } catch (Exception e)
    {
      log.error("redis sIntersectAndStore error = {}", e.getMessage());
    }
    return 0;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set sUnion(String key1, String key2)
  {
    try
    {
      return setOps.union(key1, key2);
    } catch (Exception e)
    {
      log.error("redis sUnion error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Set sDifference(String key1, String key2)
  {
    try
    {
      return setOps.difference(key1, key2);
    } catch (Exception e)
    {
      log.error("redis sDifference error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  /* ********************************************* 有序集合类型 ********************************************* */
  @SuppressWarnings("unchecked")
  public void zsAdd(String key, Object val, Double score)
  {
    try
    {
      zSetOps.add(key, val, score);
    } catch (Exception e)
    {
      log.error("redis zsAdd error = {}", e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public Double zsCore(String key, Object val)
  {
    try
    {
      return zSetOps.score(key, val);
    } catch (Exception e)
    {
      log.error("redis zsCore error = {}", e.getMessage());
    }
    return (double) -1;
  }

  @SuppressWarnings("unchecked")
  public long zsCount(String key, Double minScore, Double maxScore)
  {
    try
    {
      return zSetOps.count(key, minScore, maxScore);
    } catch (Exception e)
    {
      log.error("redis zsCount error = {}", e.getMessage());
    }
    return -1;
  }

  @SuppressWarnings("unchecked")
  public long zsSize(String key)
  {
    try
    {
      return zSetOps.size(key);
    } catch (Exception e)
    {
      log.error("redis zsSize error = {}", e.getMessage());
    }
    return -1;
  }

  @SuppressWarnings("unchecked")
  public long zsRemove(String key, Object val)
  {
    try
    {
      return zSetOps.remove(key, val);
    } catch (Exception e)
    {
      log.error("redis zsRemove error = {}", e.getMessage());
    }
    return -1;
  }

  @SuppressWarnings("unchecked")
  public long zsRemoveByScore(String key, Double minScore, Double maxScore)
  {
    try
    {
      return zSetOps.removeRangeByScore(key, minScore, maxScore);
    } catch (Exception e)
    {
      log.error("redis zsRemoveByScore error = {}", e.getMessage());
    }
    return -1;
  }

  @SuppressWarnings("unchecked")
  public long zsRank(String key, Object val)
  {
    try
    {
      return zSetOps.rank(key, val);
    } catch (Exception e)
    {
      log.error("redis zsRank error = {}", e.getMessage());
    }
    return -1;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Set zsRange(String key, long val1, long val2)
  {
    try
    {
      return zSetOps.range(key, val1, val2);
    } catch (Exception e)
    {
      log.error("redis zsRange error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  @SuppressWarnings({"unchecked"})
  public Set<ZSetOperations.TypedTuple<String>> zsRangeByScoreWithScores(String key, double val1, double val2)
  {
    try
    {
      return zSetOps.rangeByScoreWithScores(key, val1, val2);
    } catch (Exception e)
    {
      log.error("redis zsRangeByScoreWithScores error = {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  /**
   * @remark: 判断key是否存在
   * @param: key
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:22 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  public boolean hasKey(String key)
  {
    try
    {
      return redisTemplate.hasKey(key);
    } catch (Exception e)
    {
      log.error("redis hasKey error = {}", e.getMessage());
    }
    return false;
  }

  /**
   * @remark: 指定key的失效时间
   * @param: key 键
   * @param: miniseconds 失效时间(毫秒)
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:09 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  public void expire(String key, long miniseconds)
  {
    try
    {
      if (miniseconds > 0)
      {
        redisTemplate.expire(key, miniseconds, TimeUnit.MILLISECONDS);
      }
    } catch (Exception e)
    {
      log.error("redis expire error = {}", e.getMessage());
    }
  }

  /**
   * @remark: 根据key获取过期时间
   * @param: key
   * @return: long: 失效时间（毫秒）
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:21 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  public long getExpire(String key)
  {
    return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
  }
}
