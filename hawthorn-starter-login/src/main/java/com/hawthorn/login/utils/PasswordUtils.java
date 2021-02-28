package com.hawthorn.login.utils;

import java.util.UUID;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 密码工具类（MD5单向散列加密）
 * @author:andy.ten@tom.com
 * @date:2020/10/12 10:55 上午
 * @version v1.0.1
 */
public class PasswordUtils
{

  /**
   * @remark: 校验密码是否匹配
   * @param: salt 盐
   * @param: rawPass 明文
   * @param: encPass 密文（格式为：明文加密 + 盐加密）
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/12 10:55 上午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/12    andy.ten        v1.0.1             init
   */
  public static boolean matches(String salt, String rawPass, String encPass)
  {
    return new PasswordEncoder(salt).matches(encPass, rawPass);
  }

  /**
   * @remark: 明文密码加密
   * @param: rawPass 明文
   * @param: salt 盐
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/12 11:00 上午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/12    andy.ten        v1.0.1             init
   */
  public static String encode(String rawPass, String salt)
  {
    return new PasswordEncoder(salt).encode(rawPass);
  }

  /**
   * @remark: 获取盐，每次获取盐使用随机数
   * @param:
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/12 11:01 上午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/12    andy.ten        v1.0.1             init
   */
  public static String getSalt()
  {
    return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
  }

  // test
  public static void main(String[] args)
  {
    System.out.println(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20));
  }
}
