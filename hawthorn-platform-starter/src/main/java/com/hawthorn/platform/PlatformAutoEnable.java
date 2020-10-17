package com.hawthorn.platform;

import com.hawthorn.platform.config.PlatformAutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 自动装配类，platform工程（maven工程，BooApplication启动容器）无法自动装配注解问题；
 * @author:andy.ten@tom.com
 * @date:2020/8/25 10:03 下午
 * @version v1.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(PlatformAutoConfigurationPackage.class)
public @interface PlatformAutoEnable
{
}
