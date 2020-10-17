package com.hawthorn.login.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawthorn.component.utils.bean.QcBean;
import com.hawthorn.login.model.dto.sysmenu.SysMenuDTO;
import com.hawthorn.login.model.po.SysMenuPO;
import com.hawthorn.platform.repository.DBMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @copyright: Copyright (c) 2020 andy.ten@tom.com
 * <p></p>
 * @remark: 菜单管理(sys_menu) Mapper 接口
 * @author: andy.ten@tom.com
 * @date: 2020-10-15
 * @version v1.0.1
 */
@Repository
public interface SysMenuMapper extends DBMapper<SysMenuPO>
{
  IPage<SysMenuDTO> select(Page<SysMenuDTO> page, Map<String, QcBean> qc);

  List<SysMenuDTO> select(Map<String, QcBean> qc);

  List<SysMenuDTO> findByUserName(@Param(value = "userName") String userName);

}
