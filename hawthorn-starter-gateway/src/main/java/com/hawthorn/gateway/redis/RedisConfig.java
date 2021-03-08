package com.hawthorn.gateway.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: redis配置类
 * @author:andy.ten@tom.com
 * @date:2020/10/17 3:36 下午
 * @version v1.0.1
 */
@Configuration
@EnableCaching
@ConditionalOnClass({JedisConnection.class, RedisOperations.class})
@Slf4j
public class RedisConfig extends CachingConfigurerSupport
{
  /**
   * @remark: 緩存管理器，不管用什么的缓存，都必须有缓存管理器
   * @param: factory
   * @return: org.springframework.cache.CacheManager
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/25 6:32 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/25    andy.ten        v1.0.1             init
   */
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory factory)
  {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofHours(1)); // 设置缓存有效期一小时
    return RedisCacheManager
        .builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
        .cacheDefaults(redisCacheConfiguration).build();

  }

  @SuppressWarnings("rawtypes")
  @Bean
  public RedisSerializer json2JsonRedisSerializer()
  {
    // 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    return jackson2JsonRedisSerializer;
  }

  /**
   * @remark: 配置自定义redisTemplate
   * @param: factory
   * @param: fastJson2JsonRedisSerializer
   * @return: org.springframework.data.redis.core.RedisTemplate
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/17 4:00 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/17    andy.ten        v1.0.1             init
   */
  @Bean
  @SuppressWarnings("rawtypes")
  public RedisTemplate redisTemplate(RedisConnectionFactory factory, RedisSerializer
      json2JsonRedisSerializer)
  {
    // 创建RedisTemplate对象
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
    // 设置连接工厂
    redisTemplate.setConnectionFactory(factory);
    // 设置键（key）的序列化采用StringRedisSerializer。
    redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
    // 设置值（value）的序列化采用Jackson2JsonRedisSerializer。 使用json，value会多一份双引号
    redisTemplate.setValueSerializer(json2JsonRedisSerializer);
    //redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());
    redisTemplate.setHashValueSerializer(json2JsonRedisSerializer);

    redisTemplate.afterPropertiesSet();

    return redisTemplate;
  }

  @Bean
  public KeyGenerator wiselyKeyGenerator()
  {
    return (target, method, params) ->
    {
      StringBuilder sb = new StringBuilder();
      sb.append("PAGE_DB_RESULT_CACHE_");
      sb.append(target.getClass().getSimpleName());
      sb.append("." + method.getName());
      for (Object obj : params)
      {
        sb.append(obj.toString());
      }
      if (log.isInfoEnabled())
      {
        log.info("[缓存key]{}", sb);
      }
      return sb.toString();
    };

  }

  @Bean
  @SuppressWarnings("rawtypes")
  public ZSetOperations redisZSetOps(RedisTemplate redisTemplate)
  {
    return redisTemplate.opsForZSet();
  }

  @Bean
  @SuppressWarnings("rawtypes")
  public SetOperations redisSetOps(RedisTemplate redisTemplate)
  {
    return redisTemplate.opsForSet();
  }

  @Bean
  @SuppressWarnings("rawtypes")
  public ListOperations redisListOps(RedisTemplate redisTemplate)
  {
    return redisTemplate.opsForList();
  }

  @Bean
  @SuppressWarnings("rawtypes")
  public HashOperations redisHashOps(RedisTemplate redisTemplate)
  {
    return redisTemplate.opsForHash();
  }

  @Bean
  @SuppressWarnings("rawtypes")
  public ValueOperations redisValueOps(RedisTemplate redisTemplate)
  {
    return redisTemplate.opsForValue();
  }

  // @Bean
  // public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory factory)
  // {
  //   return new StringRedisTemplate(factory);
  // }
}
