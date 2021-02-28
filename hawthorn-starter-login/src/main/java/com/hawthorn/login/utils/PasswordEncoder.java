package com.hawthorn.login.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 密码加密工具类
 * @author:andy.ten@tom.com
 * @date:2020/10/11 1:04 下午
 * @version v1.0.1
 */
public class PasswordEncoder
{
  private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
      "e", "f"};

  private final static String MD5 = "MD5";
  private final static String SHA = "SHA";

  private Object salt;
  private String algorithm;

  public PasswordEncoder(Object salt)
  {
    this(salt, MD5);
  }

  public PasswordEncoder(Object salt, String algorithm)
  {
    this.salt = salt;
    this.algorithm = algorithm;
  }

  /**
   * @remark: 密码md5加密
   * @param: rawPass 明文
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 1:07 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  public String encode(String rawPass)
  {
    String result = null;
    try
    {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      // 加密后的字符串
      result = byteArray2HexStr(md.digest(mergePasswordAndSalt(rawPass).getBytes(StandardCharsets.UTF_8)));
    } catch (Exception ignored)
    {
    }
    return result;
  }

  /**
   * @remark: 密码匹配验证
   * @param: encPass 密文
   * @param: rawPass 明文
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 1:09 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  public boolean matches(String encPass, String rawPass)
  {
    String pass1 = "" + encPass;
    String pass2 = encode(rawPass);

    return pass1.equals(pass2);
  }

  /**
   * @remark: 在明文密码字符串后增加盐 格式： 明文{盐}
   * @param: password
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 1:32 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  private String mergePasswordAndSalt(String password)
  {
    if (password == null)
    {
      password = "";
    }

    if ((salt == null) || "".equals(salt))
    {
      return password;
    } else
    {
      return password + "{" + salt.toString() + "}";
    }
  }

  /**
   * @remark: 转换字节数组为16进制字串
   * @param: b 字节数组
   * @return: java.lang.String 16进制字串
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 1:10 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  private String byteArray2HexStr(byte[] b)
  {
    StringBuilder resultSb = new StringBuilder();
    for (byte value : b)
    {
      resultSb.append(byte2HexStr(value));
    }
    return resultSb.toString();
  }

  /**
   * @remark: 将字节转换为16进制
   * @param: b
   * @return: java.lang.String
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/11 1:10 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/11    andy.ten        v1.0.1             init
   */
  private static String byte2HexStr(byte b)
  {
    int n = b;
    if (n < 0)
      n = 256 + n;
    int d1 = n / 16;
    int d2 = n % 16;
    return hexDigits[d1] + hexDigits[d2];
  }

  // tests
  public static void main(String[] args)
  {
    // 数据库中保存的加密盐
    String salt = "YzcmCZNvbXocrsz9dm8e";
    PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
    String encode = encoderMd5.encode("123"); // 默认密码123，admin默认密码admin
    System.out.println("MD5-->" + encode);
    //boolean passwordValid = encoderMd5.validPassword("1bd98ed329aebc7b2f89424b5a38926e" , "test" );
    //System.out.println(passwordValid);

    PasswordEncoder encoderSha = new PasswordEncoder(salt, "SHA");
    String pass2 = encoderSha.encode("123");
    System.out.println("SHA-->" + pass2);
    //boolean passwordValid2 = encoderSha.validPassword("1bd98ed329aebc7b2f89424b5a38926e" , "test" );
    //System.out.println(passwordValid2);
  }
}
