package com.hawthorn.login.service;

import com.hawthorn.component.utils.bean.QcBean;
import com.hawthorn.login.model.pojo.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Copyright: Copyright (c) 2020 andyten
 * * <p></p>
 * @remark: 系统用户interface
 * @author:andy.ten@tom.com
 * @date:2020/8/13 3:57 下午
 * @version v1.0.1
 */
public interface SysUserService
{
  List<UserInfo> selectSysUserByUserName(Map<String, QcBean> qc);

  Set<String> findPermissions(String userName);
}
