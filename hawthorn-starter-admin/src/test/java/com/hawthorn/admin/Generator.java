package com.hawthorn.admin;

import com.hawthorn.platform.generator.MPCodeGenerator;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: mp代码生成
 * @author:andy.ten@tom.com
 * @date:2020/8/26 3:35 下午
 * @version v1.0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootAdminApplication.class})
public class Generator
{
  //@Test
  public static void main(String[] args)
  {
    MPCodeGenerator.generate();
  }
}
