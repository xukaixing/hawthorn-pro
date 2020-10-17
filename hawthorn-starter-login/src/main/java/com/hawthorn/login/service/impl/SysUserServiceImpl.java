package com.hawthorn.login.service.impl;

import com.hawthorn.component.utils.bean.QcBean;
import com.hawthorn.login.model.dto.sysmenu.SysMenuDTO;
import com.hawthorn.login.model.pojo.UserInfo;
import com.hawthorn.login.repository.SysUserMapper;
import com.hawthorn.login.service.SysMenuService;
import com.hawthorn.login.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Copyright: Copyright (c) 2020 andyten
 * @remark: 系统用户service
 * @author:andy.ten@tom.com
 * @date:2020/8/13 3:56 下午
 * @version v1.0.1
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService
{
  @Autowired
  private SysUserMapper sysUserMapper;

  @Autowired
  private SysMenuService sysMenuService;

  public List<UserInfo> selectSysUserByUserName(Map<String, QcBean> qc)
  {
    return sysUserMapper.select(qc);
  }

  public Set<String> findPermissions(String userName)
  {
    Set<String> perms = new HashSet<>();
    List<SysMenuDTO> sysMenus = sysMenuService.findByUserName(userName);
    for (SysMenuDTO sysMenu : sysMenus)
    {
      if (sysMenu.getPerms() != null && !"".equals(sysMenu.getPerms()))
      {
        perms.add(sysMenu.getPerms());
      }
    }
    return perms;
  }
}

