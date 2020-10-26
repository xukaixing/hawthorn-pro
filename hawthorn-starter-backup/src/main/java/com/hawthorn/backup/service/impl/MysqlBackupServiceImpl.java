package com.hawthorn.backup.service.impl;

import com.hawthorn.backup.service.MysqlBackupService;
import com.hawthorn.backup.utils.MySqlBackupRestoreUtil;
import org.springframework.stereotype.Service;

@Service
public class MysqlBackupServiceImpl implements MysqlBackupService
{

  @Override
  public boolean backup(String host, String userName, String password, String backupFolderPath, String fileName,
                        String database) throws Exception
  {
    return MySqlBackupRestoreUtil.backup(host, userName, password, backupFolderPath, fileName, database);
  }

  @Override
  public boolean restore(String restoreFilePath, String host, String userName, String password, String database)
      throws Exception
  {
    return MySqlBackupRestoreUtil.restore(restoreFilePath, host, userName, password, database);
  }

}
