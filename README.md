# 说明

  `hawthorn-pro` hawthorn微服务框架平台   
  作者:andy.ten@tom.com
      xukaixing@hotmail.com
  
## 版本
> v1.0.9 : 2020.10.31    
>> 增加feign接口配置    
>> 增加hystrix全局统一fallback配置              
>> 升级JackUtil工具  

---

> v1.0.8 : 2020.10.26    
>> 增加SpringBoot Admin监控    
>> 增加注册中心consul       

---

> v1.0.7 : 2020.10.17    
>> 重构包结构  
>> 新增login工程      
>> 实现jwt认证和鉴权      
>> 增加redis配置  
       
## 环境

- SpringBoot版本：2.3.2.RELEASE
- JDK版本：Jdk1.8+
- Maven版本：3.5.0+
- 数据库：mysql5.7，数据库用户名：hawthorn
- 数据库开发工具：Navicat
- Java开发工具：IntelliJ IDEA
- 组件
  - `Slf4j+logback-spring`：日志插件
  - `Lombok`：生成Setter、Getter插件
  - `HuTool`: java工具包
  - `configuration-processor`: 自定义属性配置插件
  - `exectime`: 增加aop的执行时间插件（自定义）
  - `mybatis`: 增加mybatis依赖
  - `mybatis-plus`: 增加mybatis-plus依赖
  - `mybatis-plus generator`: 增加mybatis-plus generator插件
  - `fastjson`: 增加阿里json处理插件
  - `consul`: 增加注册中心服务
- 自定义工具
  - `Str2Util`: 增加处理字符串工具类
  - `Map2ObjectUtil`: 、mapper与obj互相转换工具类
  - `AssertUtil`: 增加断言工具类
  - `PropertiesUtil、YmlUtil`: 增加.property、.yml文件读取工具类
  - `Redis Client`: redis封装工具类
  
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
├── hawthorn-starter-component
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── hawthorn
│           │           └── component
│           │               ├── constant
│           │               ├── exception
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
│           │   └── com
│           │       └── hawthorn
│           │           └── platform
│           │               ├── PlatformAutoEnable.java
│           │               ├── annotation
│           │               ├── aspect
│           │               ├── config
│           │               ├── exception
│           │               ├── generator
│           │               ├── model
│           │               ├── redis
│           │               ├── repository
│           │               ├── ret
│           │               ├── service
│           │               ├── utils
│           │               └── validatecode
│           └── resources
│               ├── META-INF
│               │   └── spring.factories
│               └── rebel.xml
└── pom.xml

98 directories, 63 files

```

## 主页

- 欢迎访问个人 [github-xukaixing](https://github.com/xukaixing) 主页.
- 欢迎访问个人 [gitee-xukaixing](https://gitee.com/xukaixing) 主页.
