package com.hawthorn.component.utils.json;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hawthorn.component.utils.common.StringMyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 封装了Jackson的json工具类，用于代替JSONObject
 * @author:andy.ten@tom.com
 * @date:2020/10/11 11:05 下午
 * @version v1.0.1
 */
@Slf4j
public class JacksonMyUtil
{
  private static final ConversionService DEFAULT_CONVERSION_SERVICE = new DefaultConversionService();

  public static final String DEFAULT_EMPTY_JSON_OBJECT = "{}";

  public static final String DEFAULT_EMPTY_JSON_ARRAY = "[]";

  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private static final ObjectMapper DEFAULT_OBJECT_MAPPER = createDefaultObjectMapper();

  public static <T> String object2Json(T object)
  {
    return object2Json(DEFAULT_OBJECT_MAPPER, object);
  }

  /**
   * @remark: Java对象转JSON字符串
   * @param: objectMapper
   * @param: object
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:17 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static String object2Json(ObjectMapper objectMapper, Object object)
  {
    try
    {
      if (object != null)
      {
        if (object instanceof String)
        {
          return object.toString();
        }
        return objectMapper.writeValueAsString(object);
      }
      return null;
    } catch (JsonProcessingException e)
    {
      log.error("The JacksonUtil toJsonString is error : \n", e);
      throw new JacksonJsonException(e);
    }
  }

  /**
   * Java对象转JSON字符串 - 美化输出
   *
   * @param object
   * @return
   */
  public static String object2JsonWithPretty(Object object)
  {
    try
    {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e)
    {
      log.error("The JacksonUtil toJsonString is error : \n", e);
      throw new RuntimeException();
    }
  }


  /**
   * Java对象转byte数组
   *
   * @param object
   * @return
   */
  public static byte[] object2Bytes(Object object)
  {
    try
    {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsBytes(object);
    } catch (JsonProcessingException e)
    {
      log.error("The JacksonUtil toJsonBytes is error : \n", e);
      throw new RuntimeException();
    }
  }

  // json转javabean
  public static <T> T json2Object(String json, Class<T> clazz)
  {
    return json2Object(DEFAULT_OBJECT_MAPPER, json, clazz);
  }

  /**
   * @remark: json字符串转普通javabean对象(pojo)
   * @param: objectMapper
   * @param: json
   * @param: clazz - 注：clazz所指对象存在泛型,例如 RestResult<User> 则转换后User的实际类型是个Map,此类情况应该使用TypeReference进行转换
   * @return: T
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:24 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static <T> T json2Object(ObjectMapper objectMapper, String json, Class<T> clazz)
  {
    try
    {
      if (!StringMyUtil.isEmpty(json))
      {
        return objectMapper.readValue(json, clazz);
      }
      return null;
    } catch (Exception e)
    {
      log.error("The JacksonUtil jons2Object is error, json str is {}, class name is {} \n", json, clazz.getName(), e);
      throw new JacksonJsonException(e);
    }
  }

  /**
   * @remark: json字符串转泛型类对象
   * <p>示例： List<User> userList = json2Object("[{"username":"jack","accounts":[{"accountId":"","amount":1200.00},...]},...]", new TypeReference<List<User>>(){});
   * 	        RestResult<User> result = json2Object("{"success": true, "message": "OK", data: {"userId": 12345, "userName": "jack"}}", new TypeReference<RestResult<User>>(){});
   * </p>
   * @param: json
   * @param: typeReference
   * @return: T
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:26 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static <T> T json2Object(String json, TypeReference<T> typeReference)
  {
    return json2Object(DEFAULT_OBJECT_MAPPER, json, typeReference);
  }

  /**
   * @remark: json字符串转泛型类对象
   * @param: json
   * @param: converter
   * @return: T
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:30 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static <T> T json2Object(String json, Json2ObjectConverter<T> converter)
  {
    try
    {
      if (!StringMyUtil.isEmpty(json))
      {
        return converter.convert(DEFAULT_OBJECT_MAPPER, DEFAULT_OBJECT_MAPPER.readTree(json));
      }
    } catch (Exception e)
    {
      throw new JacksonJsonException(e);
    }
    return null;
  }

  /**
   * @remark: json字符串转泛型类对象
   * <p>示例： List<User> userList = json2Object("[{"username":"jack","accounts":[{"accountId":"","amount":1200.00},...]},...]", new TypeReference<List<User>>(){});
   * 	  		  Result<User> result = json2Object("{"success": true, "message": "OK", data: {"userId": 12345, "userName": "jack"}}", new TypeReference<Result<User>>(){});
   * </p>
   * @param: objectMapper
   * @param: json
   * @param: typeReference
   * @return: T
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:32 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static <T> T json2Object(ObjectMapper objectMapper, String json, TypeReference<T> typeReference)
  {
    try
    {
      if (!StringMyUtil.isEmpty(json))
      {
        return objectMapper.readValue(json, typeReference);
      }
      return null;
    } catch (Exception e)
    {
      throw new JacksonJsonException(e);
    }
  }

  /**
   * @remark: todo json对象转String字符串
   * @param: obj
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 3/8/21 7:59 PM
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 3/8/21    andy.ten        v1.0.1             init
   */
  public static String json2Str(Object obj)
  {
    if (null == obj)
    {
      return null;
    }
    if (obj instanceof CharSequence)
    {
      return StringMyUtil.str((CharSequence) obj);
    }
    return json2Str(object2Json(obj));
  }

  public static String json2Str(JSON json)
  {
    if (null == json)
    {
      return null;
    }
    return json.toJSONString(0);
  }

  /**
   * JSON字符串转List集合
   *
   * @param json
   * @param elementClasses
   * @param <T>
   * @return
   */
  @SafeVarargs
  public static <T> List<T> parseList(String json, Class<T>... elementClasses)
  {
    try
    {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json, getCollectionType(objectMapper, List.class, elementClasses));
    } catch (Exception e)
    {
      log.error("The JacksonUtil parseList is error, json str is {}, element class name is {} \n",
          json, elementClasses.getClass().getName(), e);
      throw new RuntimeException();
    }
  }


  /**
   * JSON字符串转Set集合
   *
   * @param json
   * @param elementClasses
   * @param <T>
   * @return
   */
  @SafeVarargs
  public static <T> Set<T> parseSet(String json, Class<T>... elementClasses)
  {
    try
    {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json, getCollectionType(objectMapper, Set.class, elementClasses));
    } catch (Exception e)
    {
      log.error("The JacksonUtil parseSet is error, json str is {}, element class name is {} \n",
          json, elementClasses.getClass().getName(), e);
      throw new RuntimeException();
    }
  }

  /**
   * JSON字符串转Collection集合
   *
   * @param json
   * @param elementClasses
   * @param <T>
   * @return
   */
  @SafeVarargs
  public static <T> Collection<T> parseCollection(String json, Class<T>... elementClasses)
  {
    try
    {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json, getCollectionType(objectMapper, Collection.class, elementClasses));
    } catch (Exception e)
    {
      log.error("The JacksonUtil parseCollection is error, json str is {}, element class name is {} \n",
          json, elementClasses.getClass().getName(), e);
      throw new RuntimeException();
    }
  }

  /**
   * 获取泛型的Collection Type
   *
   * @param collectionClass 泛型的Collection
   * @param elementClasses  元素类
   * @return JavaType Java类型
   * @since 1.0
   */
  public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses)
  {
    return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }

  /**
   * @remark: 创建默认配置的ObjectMapper
   * @param:
   * @return: com.fasterxml.jackson.databind.ObjectMapper
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:12 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static ObjectMapper createDefaultObjectMapper()
  {
    ObjectMapper objectMapper = new CustomObjectMapper();
    //通过以下三项配置来开启仅以属性字段来序列化和反序列化对象(忽略get方法)
    //objectMapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
    //objectMapper.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
    //objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    //将被序列化对象的类名作为一个字段(字段名@class)输出到序列化后的JSON字符串中
    //objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    // 建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用
    //defaultObjectMapper.setSerializationInclusion(Include.NON_DEFAULT);
    //去掉默认的时间戳格式
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //objectMapper.getDeserializationConfig().getDateFormat().setTimeZone(zone);
    objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    //单引号处理,允许单引号
    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    //允许wrap/unwrap
    objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
    objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  /**
   * @remark: 创建根JsonNode
   * @param: json
   * @return: com.fasterxml.jackson.databind.JsonNode
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:33 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static JsonNode createRootJsonNode(String json)
  {
    return createRootJsonNode(DEFAULT_OBJECT_MAPPER, json);
  }

  /**
   * @remark: 创建根JsonNode
   * @param: objectMapper
   * @param: json
   * @return: com.fasterxml.jackson.databind.JsonNode
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:34 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static JsonNode createRootJsonNode(ObjectMapper objectMapper, String json)
  {
    try
    {
      return objectMapper.readTree(json);
    } catch (IOException e)
    {
      throw new JacksonJsonException(e.getMessage(), e);
    }
  }

  /**
   * @remark: 判断json字符串是否是JSON对象
   * @param: json
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:34 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static boolean isJsonObject(String json)
  {
    if (json != null)
    {
      return json.startsWith("{") && json.endsWith("}");
    }
    return false;
  }

  /**
   * @remark: 判断json字符串是否是JSON数组
   * @param: json
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:34 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static boolean isJsonArray(String json)
  {
    if (json != null)
    {
      return json.startsWith("[") && json.endsWith("]");
    }
    return false;
  }

  /**
   * @remark: 判断json字符串是否是JSON Object数组
   * @param: json
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/31 3:38 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/31    andy.ten        v1.0.1             init
   */
  public static boolean isJsonObjectArray(String json)
  {
    boolean b = isJsonArray(json);
    if (b)
    {
      json = StringUtils.trim(StringUtils.strip(json, "[]"));
      return json.startsWith("{") && json.endsWith("}");
    }
    return false;
  }

  public static String toString(JsonNode parentNode, String fieldName)
  {
    JsonNode jsonNode = parentNode.get(fieldName);
    if (jsonNode != null)
    {
      return jsonNode.toString();
    }
    return null;
  }

  public static String getString(JsonNode parentNode, String fieldName)
  {
    return getString(parentNode, fieldName, null);
  }

  public static String getString(JsonNode parentNode, String fieldName, String defaultValue)
  {
    JsonNode jsonNode = parentNode.get(fieldName);
    if (jsonNode != null)
    {
      return StringUtils.defaultIfEmpty(jsonNode.asText(), defaultValue);
    }
    return null;
  }

  public static Boolean getBoolean(JsonNode parentNode, String fieldName)
  {
    return getBoolean(parentNode, fieldName, null);
  }

  public static Boolean getBoolean(JsonNode parentNode, String fieldName, Byte defaultValue)
  {
    String fieldValue = getString(parentNode, fieldName, defaultValue != null ? defaultValue.toString() : null);
    if (fieldValue != null)
    {
      return DEFAULT_CONVERSION_SERVICE.convert(fieldValue, Boolean.class);
    }
    return null;
  }

  public static Byte getByte(JsonNode parentNode, String fieldName)
  {
    return getByte(parentNode, fieldName, null);
  }

  public static Byte getByte(JsonNode parentNode, String fieldName, Byte defaultValue)
  {
    String fieldValue = getString(parentNode, fieldName, defaultValue != null ? defaultValue.toString() : null);
    if (fieldValue != null)
    {
      return DEFAULT_CONVERSION_SERVICE.convert(fieldValue, Byte.class);
    }
    return null;
  }

  public static Short getShort(JsonNode parentNode, String fieldName)
  {
    return getShort(parentNode, fieldName, null);
  }

  public static Short getShort(JsonNode parentNode, String fieldName, Short defaultValue)
  {
    String fieldValue = getString(parentNode, fieldName, defaultValue != null ? defaultValue.toString() : null);
    if (fieldValue != null)
    {
      return DEFAULT_CONVERSION_SERVICE.convert(fieldValue, Short.class);
    }
    return null;
  }

  public static Integer getInteger(JsonNode parentNode, String fieldName)
  {
    return getInteger(parentNode, fieldName, null);
  }

  public static Integer getInteger(JsonNode parentNode, String fieldName, Integer defaultValue)
  {
    String fieldValue = getString(parentNode, fieldName, defaultValue != null ? defaultValue.toString() : null);
    if (fieldValue != null)
    {
      return DEFAULT_CONVERSION_SERVICE.convert(fieldValue, Integer.class);
    }
    return null;
  }

  public static Float getFloat(JsonNode parentNode, String fieldName)
  {
    return getFloat(parentNode, fieldName, null);
  }

  public static Float getFloat(JsonNode parentNode, String fieldName, Float defaultValue)
  {
    String fieldValue = getString(parentNode, fieldName, defaultValue != null ? defaultValue.toString() : null);
    if (fieldValue != null)
    {
      return DEFAULT_CONVERSION_SERVICE.convert(fieldValue, Float.class);
    }
    return null;
  }

  public static Double getDouble(JsonNode parentNode, String fieldName)
  {
    return getDouble(parentNode, fieldName, null);
  }

  public static Double getDouble(JsonNode parentNode, String fieldName, Double defaultValue)
  {
    String fieldValue = getString(parentNode, fieldName, defaultValue != null ? defaultValue.toString() : null);
    if (fieldValue != null)
    {
      return DEFAULT_CONVERSION_SERVICE.convert(fieldValue, Double.class);
    }
    return null;
  }

  public static Long getLong(JsonNode parentNode, String fieldName)
  {
    return getLong(parentNode, fieldName, null);
  }

  public static Long getLong(JsonNode parentNode, String fieldName, Long defaultValue)
  {
    String fieldValue = getString(parentNode, fieldName, defaultValue != null ? defaultValue.toString() : null);
    if (fieldValue != null)
    {
      return DEFAULT_CONVERSION_SERVICE.convert(fieldValue, Long.class);
    }
    return null;
  }

  public static ObjectMapper getDefaultObjectMapper()
  {
    return DEFAULT_OBJECT_MAPPER;
  }

  @FunctionalInterface
  public static interface Json2ObjectConverter<T>
  {
    public T convert(ObjectMapper objectMapper, JsonNode rootNode) throws Exception;
  }
}
