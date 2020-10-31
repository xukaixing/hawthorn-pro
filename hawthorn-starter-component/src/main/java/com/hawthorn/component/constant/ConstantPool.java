package com.hawthorn.component.constant;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 常量池
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 7:32 下午
 * @version v1.0.1
 */
public interface ConstantPool<T>
{
  /**
   * 从常量池中获取常量值
   * @param constant
   * @return
   */
  public T get(Constant<T> constant);
}
