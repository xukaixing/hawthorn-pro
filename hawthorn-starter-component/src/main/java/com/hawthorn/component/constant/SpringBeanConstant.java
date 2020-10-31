package com.hawthorn.component.constant;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: Spring上下文中的bean常量
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:59 下午
 * @version v1.0.1
 */
public abstract class SpringBeanConstant<T> extends Constant<T>
{
  public SpringBeanConstant()
  {
    this(null, null);
  }

  public SpringBeanConstant(String name)
  {
    this(name, null);
  }

  public SpringBeanConstant(String name, T defaultValue)
  {
    super(name, defaultValue);
  }
}
