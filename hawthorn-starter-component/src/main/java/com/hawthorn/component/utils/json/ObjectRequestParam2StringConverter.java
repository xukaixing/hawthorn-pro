package com.hawthorn.component.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Set;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: feign-client在解析@RequestParam注解的复杂对象时，feign-client发起请求时将对象序列化为String的转换器
 * <p>
 *   示例：
 *   @GetMapping(value="/search")
 *   public Object searchList(@RequestParam Condition condition);
 *   feign-client在发起请求时的url: http://ip:port/search?condition={...}
 * </p>
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 3:55 下午
 * @version v1.0.1
 */
public class ObjectRequestParam2StringConverter implements ConditionalGenericConverter
{
  private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

  private final ObjectMapper objectMapper;

  public ObjectRequestParam2StringConverter()
  {
    super();
    this.objectMapper = JacksonMyUtil.createDefaultObjectMapper();
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes()
  {
    return Collections.singleton(new ConvertiblePair(Object.class, String.class));
  }

  @SneakyThrows
  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    return objectMapper.writeValueAsString(source);
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    if (STRING_TYPE_DESCRIPTOR.equals(targetType))
    {
      Class<?> clazz = sourceType.getObjectType();
      if (!BeanUtils.isSimpleProperty(clazz))
      {
        return sourceType.hasAnnotation(RequestParam.class);
      }
    }
    return false;
  }
}
