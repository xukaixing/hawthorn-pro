package com.hawthorn.platform.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 不校验token注解
 * @author: andy.ten@tom.com
 * @created: 3/8/21 5:45 PM
 * @lasted: 3/8/21 5:45 PM
 * @version v1.0.1
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface NoCheckToken
{
  boolean required() default true;
}
