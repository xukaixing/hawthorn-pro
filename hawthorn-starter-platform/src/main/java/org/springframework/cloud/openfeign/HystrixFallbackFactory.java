package org.springframework.cloud.openfeign;

import feign.hystrix.FallbackFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 默认的服务服务熔断|降级实现工厂类,通过Cglib动态代理来实现统一的服务熔断|降级逻辑
 * @author: andy.ten@tom.com
 * @date: 2020/10/30 5:04 下午
 * @version v1.0.1
 */
// 每次获得bean都会生成一个新的对象
// 如果在spring配置文件内未指定scope属性，默认为singleton（表示在spring容器中的单例，通过spring容器获得该bean时总是返回唯一的实例）
@Scope("prototype")
public class HystrixFallbackFactory implements FallbackFactory<FeignFallback>
{

  private Class<?> feignClientClass;

  private HystrixFallbackHandlerFactory fallbackHandlerFactory;

  @Override
  public FeignFallback create(Throwable cause)
  {
    //在create之前需要设置feignClientClass，见#
    Assert.notNull(feignClientClass, "Property 'feignClientClass' must be required!");
    Assert.notNull(fallbackHandlerFactory, "Property 'fallbackHandlerFactory' must be required!");
    return doCreate(cause);
  }

  protected FeignFallback doCreate(Throwable cause)
  {
    Enhancer enhancer = new Enhancer();
    enhancer.setClassLoader(Thread.currentThread().getContextClassLoader());
    enhancer.setSuperclass(getFeignClientClass());
    enhancer.setCallback(getFallbackHandlerFactory().createHandler(getFeignClientClass(), cause));
    return (FeignFallback) enhancer.create();
  }

  public HystrixFallbackHandlerFactory getFallbackHandlerFactory()
  {
    return fallbackHandlerFactory;
  }

  public void setFallbackHandlerFactory(HystrixFallbackHandlerFactory fallbackHandlerFactory)
  {
    this.fallbackHandlerFactory = fallbackHandlerFactory;
  }

  public Class<?> getFeignClientClass()
  {
    return feignClientClass;
  }

  public void setFeignClientClass(Class<?> feignClientClass)
  {
    this.feignClientClass = feignClientClass;
  }

}
