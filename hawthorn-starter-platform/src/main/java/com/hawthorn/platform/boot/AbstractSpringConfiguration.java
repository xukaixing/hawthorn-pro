package com.hawthorn.platform.boot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: TODO
 * @author: andy.ten@tom.com
 * @date: 2020/10/31 5:10 下午
 * @version v1.0.1
 */
public class AbstractSpringConfiguration implements EnvironmentAware, ApplicationContextAware
{
  private Environment environment;

  private ApplicationContext applicationContext;

  public void setEnvironment(Environment environment)
  {
    this.environment = environment;
  }

  public Environment getEnvironment()
  {
    return environment;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
  {
    this.applicationContext = applicationContext;
  }

  public ApplicationContext getApplicationContext()
  {
    return applicationContext;
  }
}
