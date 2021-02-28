package com.hawthorn.component.utils.json;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Set;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: eign-client在解析@RequestParam注解的复杂对象时，在spring-mvc收到请求时将String反序列化为对象的转换器
 * <p>示例：spring-mvc收到请求时的url: http://ip:port/search?condition={...}</p>
 * <p>@GetMapping(value="/search")
 *    public Object searchList(@RequestParam Condition condition) { //该Converter将JSON字符串形式的Condition转换为对象Condition
 *  	  ...
 *    }
 *  </p>
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 2:54 下午
 * @version v1.0.1
 */
public class String2ObjectRequestParamConverter implements ConditionalGenericConverter
{
  private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

  public String2ObjectRequestParamConverter()
  {
    super();
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes()
  {
    return Collections.singleton(new ConvertiblePair(String.class, Object.class));
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    if (source != null && JacksonMyUtil.isJsonObject(source.toString()))
    {
      return JacksonMyUtil.json2Object(source.toString(), targetType.getObjectType());
    }
    return null;
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
  {
    if (STRING_TYPE_DESCRIPTOR.equals(sourceType))
    {
      Class<?> clazz = targetType.getObjectType();
      if (!BeanUtils.isSimpleProperty(clazz))
      {
        return targetType.hasAnnotation(RequestParam.class);
      }
    }
    return false;
  }
}
