package com.hawthorn.login.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawthorn.component.utils.bean.QcBean;
import com.hawthorn.login.model.dto.sysmenu.SysMenuDTO;

import java.util.List;
import java.util.Map;

/**
 * @Copyright: Copyright (c) 2020 andy.ten@tom.com
 * <p></p>
 * @remark: 菜单管理 service 接口
 * @author: andy.ten@tom.com
 * @date: 2020-10-15
 * @version v1.0.1
 */
public interface SysMenuService
{
  /**
   * generator->带分页查询
   */
  IPage<SysMenuDTO> select(Page<SysMenuDTO> page, Map<String, QcBean> qc);

  /**
   * generator->不带分页查询
   */
  List<SysMenuDTO> select(Map<String, QcBean> qc);

  /**
   * generator->新增方法
   */
  SysMenuDTO insert(SysMenuDTO sysMenuDTO);

  /**
   * generator->更新方法
   */
  SysMenuDTO update(SysMenuDTO sysMenuDTO);

  /**
   * generator->带乐观锁更新方法
   */
  SysMenuDTO updateByVersion(SysMenuDTO sysMenuDTO);

  /**
   * generator->根据id删除行记录
   */
  int deleteById(Long id);

  /**
   * @remark: 根据用户名查询用户授予的角色，再根据角色查询菜单
   * @param: userName
   * @return: java.util.List<com.hawthorn.login.model.dto.sysmenu.SysMenuDTO>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/15 5:31 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/15    andy.ten        v1.0.1             init
   */
  List<SysMenuDTO> findByUserName(String userName);
}
