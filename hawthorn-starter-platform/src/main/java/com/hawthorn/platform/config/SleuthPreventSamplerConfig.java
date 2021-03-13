package com.hawthorn.platform.config;

import brave.http.HttpAdapter;
import brave.http.HttpSampler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.instrument.web.ServerSampler;
import org.springframework.cloud.sleuth.instrument.web.SkipPatternProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo sleuth过滤url调用跟踪,不知道为什么不起作用
 * @author: andy.ten@tom.com
 * @created: 3/12/21 8:25 PM
 * @lasted: 3/12/21 8:25 PM
 * @version v1.0.1
 */
@Configuration
@Slf4j
class SleuthPreventSamplerConfig
{

  @Bean(name = ServerSampler.NAME)
  HttpSampler myHttpSampler(SkipPatternProvider provider)
  {
    Pattern pattern = provider.skipPattern();
    return new HttpSampler()
    {

      @Override
      public <Req> Boolean trySample(HttpAdapter<Req, ?> adapter, Req request)
      {
        String url = adapter.path(request);
        Pattern p = Pattern
            .compile(".*swagger.*|.*api-docs.*|.*metrics.*|.*hystrix.*|.*monitor.*|.*actuator.*");
        boolean shouldSkip = p.matcher(url).matches();
        if (shouldSkip)
        {
          return false;
        }
        return null;
      }
    };
  }
}
