package com.hawthorn.login.repository;

import com.hawthorn.component.utils.bean.QcBean;
import com.hawthorn.login.model.po.SysUserPO;
import com.hawthorn.login.model.pojo.UserInfo;
import com.hawthorn.platform.repository.DBMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Copyright: Copyright (c) 2020 andyten
 * @remark: SysUser Mapper类
 * @author:andy.ten@tom.com
 * @date:2020/8/13 11:47 上午
 * @version v1.0.1
 */
@Repository
public interface SysUserMapper extends DBMapper<SysUserPO>
{
  List<UserInfo> select(@Param("qc") Map<String, QcBean> qc);
}
