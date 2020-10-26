package com.hawthorn.backup.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 数据源属性配置类
 * @author:andy.ten@tom.com
 * @date:2020/10/18 10:43 下午
 * @version v1.0.1
 */
@Component
@ConfigurationProperties(prefix = "hawthorn.backup.datasource")
@Data
public class BackupDataSourceProperties
{
  private String host;
  private String userName;
  private String password;
  private String database;
}
