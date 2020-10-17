package com.hawthorn.login.model.dto.sysmenu;

import com.hawthorn.platform.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @copyright: Copyright (c) 2020 andy.ten@tom.com
 * <p></p>
 * @remark: 菜单管理(sys_menu) dto实体类
 * @author: andy.ten@tom.com
 * @date: 2020-10-15
 * @version v1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "sys_menu", description = "菜单管理")
public class SysMenuDTO extends BaseDTO
{
  /**
   * 编号
   */
  @ApiModelProperty(value = "编号", name = "id")
  private Long id;
  /**
   * 菜单名称
   */
  @ApiModelProperty(value = "菜单名称", name = "name")
  private String name;
  /**
   * 父菜单ID，一级菜单为0
   */
  @ApiModelProperty(value = "父菜单ID，一级菜单为0", name = "parentId")
  private Long parentId;
  /**
   * 菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址)
   */
  @ApiModelProperty(value = "菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址)", name = "url")
  private String url;
  /**
   * 授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)
   */
  @ApiModelProperty(value = "授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)", name = "perms")
  private String perms;
  /**
   * 类型   0：目录   1：菜单   2：按钮
   */
  @ApiModelProperty(value = "类型   0：目录   1：菜单   2：按钮", name = "type")
  private Integer type;
  /**
   * 菜单图标
   */
  @ApiModelProperty(value = "菜单图标", name = "icon")
  private String icon;
  /**
   * 排序
   */
  @ApiModelProperty(value = "排序", name = "orderNum")
  private Integer orderNum;

  public SysMenuDTO()
  {
  }

}
