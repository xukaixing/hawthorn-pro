package com.hawthorn.login.service.impl;

import com.hawthorn.component.exception.BizCode;
import com.hawthorn.component.exception.BizException;
import com.hawthorn.component.utils.bean.QcBean;
import com.hawthorn.login.model.pojo.JwtUserDetails;
import com.hawthorn.login.model.pojo.UserInfo;
import com.hawthorn.login.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: DaoAuthenticationProvider的ProviderManager的父类authenticate认证方法使用
 * <p>通过loadUserByUsername方法，加载用户信息，并判断用户信息是否正确</p>
 * @author:andy.ten@tom.com
 * @date:2020/10/11 1:56 下午
 * @version v1.0.1
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
  @Autowired
  private SysUserService sysUserService;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
  {
    log.info("====== 装载用户实体信息，用户名为: {} ======", userName);

    // 因为用户在admin工程中已经存在，可以考虑采用服务间调用获取user信息

    // 如果将user信息放入到redis中，可以考虑不读取数据库，而从redis中读取用户信息，然后返回userDetial

    // 根据用户名验证用户
    QcBean qc = new QcBean();
    qc.setId("userName");
    qc.setOp("=");
    qc.setDs("name");
    qc.setValue(userName);
    HashMap<String, QcBean> hm = new HashMap<>();
    hm.put(qc.getId(), qc);
    List<UserInfo> userInfoList = sysUserService.selectSysUserByUserName(hm);

    if (userInfoList.size() == 0)
    {
      throw new BizException(BizCode.USER_NOT_EXISTED);
    }

    if (userInfoList.size() > 1)
    {
      throw new BizException(BizCode.USER_MULTI_EXISTED);
    }

    // 构建UserDetail对象
    JwtUserDetails userDetail = new JwtUserDetails();
    userDetail.setUserInfo(userInfoList.get(0));
    
    return userDetail;
  }
}
