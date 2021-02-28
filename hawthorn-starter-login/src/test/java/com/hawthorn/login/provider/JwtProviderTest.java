package com.hawthorn.login.provider;

import com.hawthorn.component.utils.json.JacksonMyUtil;
import com.hawthorn.login.BootLoginApplication;
import com.hawthorn.login.model.pojo.AccessToken;
import com.hawthorn.platform.config.JwtTokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @copyright: Copyright (c)  andyten
 * <p></p>
 * @remark: TODO
 * @author: andy.ten@tom.com
 * @created: 2/27/21 3:43 PM
 * @lasted: 2/27/21 3:43 PM
 * @version v1.0.1
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootLoginApplication.class})
class JwtProviderTest
{
  @Autowired
  private JwtTokenConfig jwtTokenConfig;
  @Autowired
  private JwtProvider jwtProvider;

  @Test
  void createToken()
  {

    AccessToken at = jwtProvider.createToken("admin");
    log.info(JacksonMyUtil.object2Json(at));
  }
}
