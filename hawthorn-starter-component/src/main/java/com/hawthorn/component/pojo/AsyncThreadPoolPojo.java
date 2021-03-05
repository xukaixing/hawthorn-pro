package com.hawthorn.component.pojo;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 异步方法线程池pojo
 * @author: andy.ten@tom.com
 * @created: 3/4/21 10:27 PM
 * @lasted: 3/4/21 10:27 PM
 * @version v1.0.1
 */
public class AsyncThreadPoolPojo
{
  private int corePoolSize;
  private int maxPoolSize;
  private int queueCapacity;
  private int keepAliveSeconds;

  public AsyncThreadPoolPojo()
  {
    this.corePoolSize = 10;
    this.maxPoolSize = 300;
    this.queueCapacity = 999999;
    this.keepAliveSeconds = 600;
  }

  public AsyncThreadPoolPojo(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds)
  {
    this.corePoolSize = corePoolSize;
    this.maxPoolSize = maxPoolSize;
    this.queueCapacity = queueCapacity;
    this.keepAliveSeconds = keepAliveSeconds;
  }

  public void setCorePoolSize(int corePoolSize)
  {
    this.corePoolSize = corePoolSize;
  }

  public int getCorePoolSize()
  {
    if (corePoolSize == -1)
      return 10;
    else return corePoolSize;
  }

  public void setMaxPoolSize(int maxPoolSize)
  {
    this.maxPoolSize = maxPoolSize;
  }

  public int getMaxPoolSize()
  {
    if (maxPoolSize == -1)
      return 300;
    else return maxPoolSize;
  }

  public void setQueueCapacity(int queueCapacity)
  {
    this.queueCapacity = queueCapacity;
  }

  public int getQueueCapacity()
  {
    if (corePoolSize == -1)
      return 999999;
    else return corePoolSize;
  }

  public void setKeepAliveSeconds(int keepAliveSeconds)
  {
    this.keepAliveSeconds = keepAliveSeconds;
  }

  public int getKeepAliveSeconds()
  {
    if (corePoolSize == -1)
      return 600;
    else return corePoolSize;
  }
}
