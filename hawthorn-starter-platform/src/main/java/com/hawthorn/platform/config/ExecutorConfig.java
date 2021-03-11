package com.hawthorn.platform.config;

import com.hawthorn.component.pojo.AsyncThreadPoolPojo;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 自定义线程池配置类
 * @author: andy.ten@tom.com
 * @created: 3/11/21 2:38 PM
 * @lasted: 3/11/21 2:38 PM
 * @version v1.0.1
 */
@Configuration
public class ExecutorConfig extends AsyncConfigurerSupport
{
  @Autowired
  BeanFactory beanFactory;

  @Override
  public Executor getAsyncExecutor()
  {
    AsyncThreadPoolPojo asyncThreadPoolPojo = new AsyncThreadPoolPojo();
    // ThreadPoolExecutor是线程池的真正实现，通过构造方法的一系列参数，来构成不同配置的线程池
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // ExecutorService service = Executors.newCachedThreadPool();//包含2个线程对象
    //配置核心线程数
    executor.setCorePoolSize(asyncThreadPoolPojo.getCorePoolSize());
    //配置最大线程数
    executor.setMaxPoolSize(asyncThreadPoolPojo.getMaxPoolSize());
    //配置队列大小
    executor.setQueueCapacity(asyncThreadPoolPojo.getQueueCapacity());
    //线程池维护线程所允许的空闲时间
    executor.setKeepAliveSeconds(asyncThreadPoolPojo.getKeepAliveSeconds());
    //配置线程池中的线程的名称前缀
    executor.setThreadNamePrefix("async-service-");
    /**
     * rejection-policy：当pool已经达到max size的时候，
     * AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 默认策略
     * DiscardPolicy：也是丢弃任务，但是不抛出异常。
     * DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * CallerRunsPolicy：不在新线程中执行任务，而是有调用者所在的线程来执行
     */
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    //执行初始化
    executor.initialize();

    // return executor;
    // 使用LazyTraceExecutor，Sleuth 会为这个调用新创建一个 Span，否则会导致无法新创建一个 Span
    return new LazyTraceExecutor(this.beanFactory, executor);
  }

  public Executor getAsyncExecutor(AsyncThreadPoolPojo asyncThreadPoolPojo)
  {
// ThreadPoolExecutor是线程池的真正实现，通过构造方法的一系列参数，来构成不同配置的线程池
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // ExecutorService service = Executors.newCachedThreadPool();//包含2个线程对象
    //配置核心线程数
    executor.setCorePoolSize(asyncThreadPoolPojo.getCorePoolSize());
    //配置最大线程数
    executor.setMaxPoolSize(asyncThreadPoolPojo.getMaxPoolSize());
    //配置队列大小
    executor.setQueueCapacity(asyncThreadPoolPojo.getQueueCapacity());
    //线程池维护线程所允许的空闲时间
    executor.setKeepAliveSeconds(asyncThreadPoolPojo.getKeepAliveSeconds());
    //配置线程池中的线程的名称前缀
    executor.setThreadNamePrefix("async-service-");
    /**
     * rejection-policy：当pool已经达到max size的时候，
     * AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 默认策略
     * DiscardPolicy：也是丢弃任务，但是不抛出异常。
     * DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * CallerRunsPolicy：不在新线程中执行任务，而是有调用者所在的线程来执行
     */
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    //执行初始化
    executor.initialize();

    //return executor;
    // 使用LazyTraceExecutor，Sleuth 会为这个调用新创建一个 Span，否则会导致无法新创建一个 Span
    return new LazyTraceExecutor(this.beanFactory, executor);
  }
}
