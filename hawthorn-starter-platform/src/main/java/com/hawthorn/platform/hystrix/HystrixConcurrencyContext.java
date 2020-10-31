package com.hawthorn.platform.hystrix;

import java.util.HashMap;
import java.util.Map;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: Hystrix并发上下文
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:17 下午
 * @version v1.0.1
 */
public class HystrixConcurrencyContext extends HashMap<String, Object>
{
  private static final long serialVersionUID = 1L;

  public HystrixConcurrencyContext()
  {
    super();
  }

  public HystrixConcurrencyContext(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }

  public HystrixConcurrencyContext(int initialCapacity)
  {
    super(initialCapacity);
  }

  public HystrixConcurrencyContext(Map<? extends String, ?> m)
  {
    super(m);
  }
}
