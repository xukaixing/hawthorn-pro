# 说明

  `hawthorn-pro` hawthorn微服务框架平台   
  作者: 徐开兴 ｜
      andy.ten@tom.com ｜
      xukaixing@hotmail.com
  
## 版本
> v1.1.8 : 2021.03.23            
>> 增加Dockerfile打包文件      
>> 调整component包结构    
>> 调整pom.xml的maven打包插件         
  
---

> v1.1.7 : 2021.03.18            
>> swagger2.0升级为3.0    
>> 增加Knife4j增强ui
>> 增加logstash日志配置                
  
---

> v1.1.6 : 2021.03.11          
>> 增加sleuth链路追踪  
>> 增加zipkin链路展示
>> 优化yml配置文件内容  
>> 日志文件增加traceid配置  
      
## 环境

- SpringBoot版本：2.3.2.RELEASE
- SpringIcloud版本：Hoxton.RELEASE
- JDK版本：OpenJdk 8.0+
- Maven版本：3.5.0+
- 数据库：mysql5.7，数据库用户名：hawthorn
- 数据库开发工具：Navicat Premium
- Java IDE工具：IntelliJ IDEA
- Redis工具：Redis Desktop Manager
- Git管理工具：SourceTree
- 调试服务工具：Postman
- 文本编辑工具：Sublime Text3 、 Xcode
- 依赖组件
  - `Slf4j+logback-spring`：日志组件
  - `Swagger`: swagger组件
  - `Lombok`：生成Setter、Getter组件
  - `HuTool`: java工具包
  - `Configuration-processor`: 自定义属性配置组件
  - `Druid`: 阿里druid连接池组件
  - `Mybatis`: mybatis组件
  - `Mybatis-plus`: mybatis-plus组件
  - `Mybatis-plus generator`: mybatis-plus generator组件
  - `Fastjson`: 阿里json处理组件
  - `Feign`: feign服务调用组件
  - `Ribbon`: ribbon负载均衡组件
  - `Consul`: 注册中心服务
  - `Gateway`: gateway网关
  - `RequestRateLimiter`: 网关限流组件
  - `Spring Boot Admin`: 监控组件
  - `Spring Security`: 安全组件
  - `Jwt`: token组件
  - `Hystrix Dashboard`: 可视化监控RequestRateLimiter限流熔断服务组件
  - `Feign Httpclient`: feign连接池组件
  - `Turbine`: 可视化turbine多服务监控组件
  - `Redis`: redis缓存组件
  - `Amqp`: rabbit mq消息组件
  - `Sleuth`: 链路跟踪组件
  - `Zipkin`: 链路跟踪展示组件
  
- 自定义工具
  - `Exectime`: aop的执行时间注解
  - `CheckToken`: aop的校验token注解
  - `NoCheckToken`: aop的非校验token注解
  - `StringMyUtil`: 处理字符串工具类
  - `Map2ObjectUtil`: 、mapper与obj互相转换工具类
  - `AssertMyUtil`: 断言工具类
  - `PropertiesMyUtil、YmlMyUtil`: .property、.yml文件读取工具类
  - `IPMyUtil`: 获取请求ip地址工具类 
  - `RedisClient`: redis封装工具类
  - `JwtProvider`: jwt工具类
  - `JwtTokenMyUtil`: 平台token校验工具类
  - `CollectionUtil`: 处理集合工具类
  - `FileMyUtil`: 处理文件工具类
  - `JacksonMyUtil`: 处理json工具类
  - `ThreadPooMylUtil`: 线程池工具类
  - `GlobalExceptionHandler`: 全局异常处理类
  - `DBMapper`: 扩展封装mybatis-plus的BaseMapper类
  - `RestResult`: 封装全局返回结果类
  - `MyPropsConfig`: 读取application.yml自定义属性工具类
  - `MPCodeGenerator`: 代码生成工具类
  - `HystrixFallbackFactory`: 熔断fallback工具类
  - `RandomUtil`: 生成随机数工具类
  - `AsyncThreadPoolTaskMyUtil`: 异步方法工具类  
  - `RequestWrapper、ResponseWrapper`: 请求和响应封装类
  - `CustomTracingFilterr`: 自定义sleuth链路过滤器
  
- 工程端口说明
  - `hawthorn-starter-monitor`: 4000
  - `hawthorn-starter-gateway`: 4001 
  - `hawthorn-starter-claim`: 4501  
  - `hawthorn-starter-part`: 4601  
  - `hawthorn-starter-admin`: 4701  
  - `hawthorn-starter-login`: 4702    
  - `hawthorn-starter-mqprovider`: 4703  
  - `hawthorn-starter-mqconsumer`: 4704  
  - `hawthorn-starter-redis`: 4705       
  - `hawthorn-starter-hystrixdashboard`: 4901
  - `hawthorn-starter-backup`: 4902  
## 运行

```java


```

## 目录

``` 目录
.
├── hawthorn-starter-admin
│   ├── db
│   │   └── hawthorn.sql
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── admin
│       │   │               ├── BootAdminApplication.java
│       │   │               ├── config
│       │   │               ├── controller
│       │   │               ├── feign
│       │   │               ├── model
│       │   │               ├── repository
│       │   │               └── service
│       │   └── resources
│       │       ├── META-INF
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── generator.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       ├── mapper
│       │       │   ├── SysDeptMapper.xml
│       │       │   └── SysUserMapper.xml
│       │       └── rebel.xml
│       └── test
│           └── java
│               └── com
│                   └── hawthorn
│                       └── admin
│                           ├── Generator.java
│                           └── HawthornAdminApplicationTests.java
├── hawthorn-starter-backup
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── backup
│       │   │               ├── BootBackupApplication.java
│       │   │               ├── config
│       │   │               ├── constant
│       │   │               ├── controller
│       │   │               ├── service
│       │   │               └── utils
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       └── rebel.xml
│       └── test
│           └── java
├── hawthorn-starter-claim
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── claim
│       │   │               ├── BootClaimApplication.java
│       │   │               └── controller
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── generator.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       └── rebel.xml
│       └── test
│           └── java
├── hawthorn-starter-component
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── hawthorn
│           │           └── component
│           │               ├── boot
│           │               ├── constant
│           │               ├── exception
│           │               ├── pojo
│           │               ├── ret
│           │               └── utils
│           └── resources
│               ├── rebel.xml
│               └── template
│                   ├── myController.java.vm
│                   ├── myControllerTest.java.vm
│                   ├── myDto.java.vm
│                   ├── myEntity.java.vm
│                   ├── myMapper.java.vm
│                   ├── myMapper.xml.vm
│                   ├── myService.java.vm
│                   ├── myServiceImpl.java.vm
│                   └── myServiceImplTest.java.vm
├── hawthorn-starter-gateway
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── gateway
│       │   │               ├── BootGatewayApplication.java
│       │   │               ├── config
│       │   │               ├── constant
│       │   │               ├── exception
│       │   │               ├── filter
│       │   │               ├── provider
│       │   │               ├── redis
│       │   │               ├── ret
│       │   │               └── utils
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       └── rebel.xml
│       └── test
│           └── java
├── hawthorn-starter-hystrixdashboard
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── hystrixdashboard
│       │   │               └── BootHystrixDashboardApplication.java
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       └── rebel.xml
│       └── test
│           └── java
├── hawthorn-starter-login
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── login
│       │   │               ├── BootLoginApplication.java
│       │   │               ├── config
│       │   │               ├── controller
│       │   │               ├── model
│       │   │               ├── provider
│       │   │               ├── repository
│       │   │               ├── security
│       │   │               ├── service
│       │   │               └── utils
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── generator.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       ├── mapper
│       │       │   ├── SysMenuMapper.xml
│       │       │   └── SysUserMapper.xml
│       │       └── rebel.xml
│       └── test
│           └── java
│               └── com
│                   └── hawthorn
│                       └── login
│                           └── provider
├── hawthorn-starter-monitor
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── monitor
│       │   │               └── BootMonitorApplication.java
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       └── rebel.xml
│       └── test
│           └── java
├── hawthorn-starter-mqconsumer
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── mq
│       │   │               ├── BootMqConsumerApplication.java
│       │   │               ├── config
│       │   │               ├── controller
│       │   │               └── listener
│       │   └── resources
│       │       ├── META-INF
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       ├── mapper
│       │       └── rebel.xml
│       └── test
│           └── java
│               └── com
│                   └── hawthorn
├── hawthorn-starter-mqprovider
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── mq
│       │   │               ├── BootMqProviderApplication.java
│       │   │               ├── config
│       │   │               └── controller
│       │   └── resources
│       │       ├── META-INF
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       ├── mapper
│       │       └── rebel.xml
│       └── test
│           └── java
│               └── com
│                   └── hawthorn
├── hawthorn-starter-part
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── part
│       │   │               ├── BootPartApplication.java
│       │   │               └── controller
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       └── rebel.xml
│       └── test
│           └── java
├── hawthorn-starter-platform
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           │   ├── com
│           │   │   └── hawthorn
│           │   │       └── platform
│           │   │           ├── PlatformAutoEnable.java
│           │   │           ├── annotation
│           │   │           ├── aspect
│           │   │           ├── config
│           │   │           ├── exception
│           │   │           ├── generator
│           │   │           ├── hystrix
│           │   │           ├── interceptor
│           │   │           ├── model
│           │   │           ├── redis
│           │   │           ├── repository
│           │   │           ├── service
│           │   │           ├── utils
│           │   │           └── validatecode
│           │   ├── model
│           │   └── org
│           │       └── springframework
│           │           └── cloud
│           │               └── openfeign
│           └── resources
│               ├── META-INF
│               │   └── spring.factories
│               └── rebel.xml
├── hawthorn-starter-redis
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── hawthorn
│       │   │           └── redis
│       │   │               ├── BootRedisApplication.java
│       │   │               ├── config
│       │   │               ├── constant
│       │   │               ├── initload
│       │   │               ├── model
│       │   │               ├── repository
│       │   │               └── service
│       │   └── resources
│       │       ├── META-INF
│       │       ├── application-dev.yml
│       │       ├── application-prod.yml
│       │       ├── application.yml
│       │       ├── banner.txt
│       │       ├── bootstrap.yml
│       │       ├── generator.yml
│       │       ├── logback-spring-dev.xml
│       │       ├── logback-spring-prod.xml
│       │       ├── mapper
│       │       │   ├── DictCacheMapper.xml
│       │       │   └── UserCacheMapper.xml
│       │       └── rebel.xml
│       └── test
│           └── java
└── pom.xml

213 directories, 138 files

```

## 主页

- 欢迎访问个人 [github-xukaixing](https://github.com/xukaixing) 主页.
- 欢迎访问个人 [gitee-xukaixing](https://gitee.com/xukaixing) 主页.
