package com.hawthorn.platform.validatecode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 验证码生成接口
 * @author:andy.ten@tom.com
 * @date:2020/9/22 12:25 上午
 * @version v1.0.1
 */
public interface IVerifyCodeGen
{
  /**
   * 生成验证码并返回code，将图片写的os中
   *
   * @param width
   * @param height
   * @param os
   * @return
   * @throws IOException
   */
  String generate(int width, int height, OutputStream os) throws IOException;

  /**
   * 生成验证码对象
   *
   * @param width
   * @param height
   * @return
   * @throws IOException
   */
  VerifyCode generate(int width, int height) throws IOException;
}
