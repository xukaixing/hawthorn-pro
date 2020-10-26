package com.hawthorn.backup.service;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: MySql命令行备份恢复服务
 * @author:andy.ten@tom.com
 * @date:2020/10/18 10:45 下午
 * @version v1.0.1
 */
public interface MysqlBackupService
{

  /**
   * @remark: 备份数据库
   * @param: host
   * @param: userName
   * @param: password
   * @param: backupFolderPath
   * @param: fileName
   * @param: database
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/18 10:45 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/18    andy.ten        v1.0.1             init
   */
  boolean backup(String host, String userName, String password, String backupFolderPath, String fileName, String database) throws Exception;

  /**
   * @remark: 恢复数据库
   * @param: restoreFilePath
   * @param: host
   * @param: userName
   * @param: password
   * @param: database
   * @return: boolean
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/18 10:45 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/18    andy.ten        v1.0.1             init
   */
  boolean restore(String restoreFilePath, String host, String userName, String password, String database) throws Exception;

}
