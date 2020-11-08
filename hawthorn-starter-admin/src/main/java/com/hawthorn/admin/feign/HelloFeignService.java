package com.hawthorn.admin.feign;

import com.hawthorn.platform.config.FeignGlobalConfig;
import com.hawthorn.platform.ret.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignFallback;
import org.springframework.cloud.openfeign.HystrixFallbackFactory;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: feign接口服务
 * @author: andy.ten@tom.com
 * @date: 2020/10/28 7:42 下午
 * @version v1.0.1
 */
@FeignClient(name = "hawthorn-claim", path = "/claim/hello", configuration = FeignGlobalConfig.class, fallbackFactory = HystrixFallbackFactory.class)
public interface HelloFeignService extends FeignFallback
{
  @GetMapping(value = "/sayHello")
  RestResult sayHello();
}
