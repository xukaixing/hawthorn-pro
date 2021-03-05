package com.hawthorn.redis.model.dto.usercache;

import com.hawthorn.platform.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @copyright: Copyright (c) 2020 andyten
 * <p></p>
 * @remark: 用户管理(sys_user) dto实体类
 * @author: andy.ten@tom.com
 * @date: 2020-08-23
 * @version v1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "sys_user", description = "用户管理")
public class UserCacheDTO extends BaseDTO
{
  /**
   * 编号
   */
  @ApiModelProperty(value = "编号", name = "id")
  private Long id;
  /**
   * 用户名
   */
  @ApiModelProperty(value = "用户名", name = "name")
  private String name;

  /**
   * 用户类型
   */
  @ApiModelProperty(value = "用户类型", name = "userType")
  private Integer userType;
  /**
   * 机构ID
   */
  @ApiModelProperty(value = "机构ID", name = "deptId")
  private Long deptId;

  public UserCacheDTO()
  {
  }
}
