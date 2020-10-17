package com.hawthorn.platform.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 使用alibaba的fastjson包redis序列化工具
 * @author:andy.ten@tom.com
 * @date:2020/10/17 3:48 下午
 * @version v1.0.1
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private Class<T> clazz;

  public FastJson2JsonRedisSerializer(Class<T> clazz)
  {
    super();
    this.clazz = clazz;
  }

  @Override
  public byte[] serialize(T t)
  {
    if (t == null)
    {
      return new byte[0];
    }
    return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
  }

  @Override
  public T deserialize(byte[] bytes)
  {
    if (bytes == null || bytes.length <= 0)
    {
      return null;
    }
    String str = new String(bytes, DEFAULT_CHARSET);

    return JSON.parseObject(str, clazz);
  }
}
