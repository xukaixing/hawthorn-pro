package com.hawthorn.login.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawthorn.component.constant.SysConstant;
import com.hawthorn.component.utils.bean.QcBean;
import com.hawthorn.login.model.dto.sysmenu.SysMenuDTO;
import com.hawthorn.login.model.po.SysMenuPO;
import com.hawthorn.login.repository.SysMenuMapper;
import com.hawthorn.login.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Copyright: Copyright (c) 2020 andy.ten@tom.com
 * <p></p>
 * @remark: 菜单管理 serviceImp 实现类
 * @author: andy.ten@tom.com
 * @date: 2020-10-15
 * @version v1.0.1
 */
@Service
public class SysMenuServiceImpl implements SysMenuService
{
  @Resource
  private SysMenuMapper sysMenuMapper;

  /**
   * generator->带分页查询
   */
  @Transactional(readOnly = true)
  public List<SysMenuDTO> select(Map<String, QcBean> qc)
  {
    return sysMenuMapper.select(qc);
  }

  /**
   * generator->不带分页查询
   */
  @Transactional(readOnly = true)
  public IPage<SysMenuDTO> select(Page<SysMenuDTO> page, Map<String, QcBean> qc)
  {
    return sysMenuMapper.select(page, qc);
  }

  /**
   * generator->新增方法
   */
  @Transactional(rollbackFor = Exception.class)
  public SysMenuDTO insert(SysMenuDTO sysMenuDTO)
  {
    SysMenuPO sysMenuPO = sysMenuDTO.transDto2Po(SysMenuPO.class);
    int b = sysMenuMapper.insert(sysMenuPO);

    return sysMenuDTO;
  }

  /**
   * generator->新增方法
   */
  @Transactional(rollbackFor = Exception.class)
  public SysMenuDTO update(SysMenuDTO sysMenuDTO)
  {
    // 更新null值情况
    //UpdateWrapper<SysMenuPO> uw = new UpdateWrapper<>();
    //uw.eq("id", sysMenuDTO.getId());
    //uw.set("xx", null);
    //boolean b = dbService.update(uw);
    SysMenuPO sysMenuPO = sysMenuDTO.transDto2Po(SysMenuPO.class);
    int b = sysMenuMapper.updateById(sysMenuPO);

    return sysMenuDTO;
  }

  /**
   * generator->带乐观锁更新方法
   */
  @Transactional(rollbackFor = Exception.class)
  public SysMenuDTO updateByVersion(SysMenuDTO sysMenuDTO)
  {
    SysMenuPO sysMenuPO = sysMenuMapper.selectById(sysMenuDTO.getId());
    int b = sysMenuMapper.updateById(sysMenuPO);

    return sysMenuDTO;
  }

  /**
   * generator->根据id删除行记录
   */
  @Transactional(rollbackFor = Exception.class)
  public int deleteById(Long id)
  {
    return sysMenuMapper.deleteById(id);
  }

  /**
   * @remark:
   * @param: userName
   * @return: java.util.List<com.hawthorn.login.model.dto.sysmenu.SysMenuDTO>
   * <p></p>
   * @author: andy.ten@tom.com
   * @date: 2020/10/15 5:32 下午
   * @version: 1.0.1
   * Modification History:
   * Date         Author          Version            Description
   * -----------------------------------------------------------
   * 2020/10/15    andy.ten        v1.0.1             init
   */
  public List<SysMenuDTO> findByUserName(String userName)
  {
    if (userName == null || "".equals(userName) || SysConstant.ADMIN.equalsIgnoreCase(userName))
    {
      Map<String, QcBean> qc = null;
      return sysMenuMapper.select(null);
    }
    return sysMenuMapper.findByUserName(userName);
  }
}
