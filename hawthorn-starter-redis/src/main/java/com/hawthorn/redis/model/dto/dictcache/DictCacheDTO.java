package com.hawthorn.redis.model.dto.dictcache;

import com.hawthorn.platform.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 字典管理(sys_dict) dto实体类
 * @author: andy.ten@tom.com
 * @created: 3/5/21 9:50 PM
 * @lasted: 3/5/21 9:50 PM
 * @version v1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "sys_dict", description = "字典管理")
public class DictCacheDTO extends BaseDTO
{
  /**
   * 编号
   */
  @ApiModelProperty(value = "编号", name = "id")
  private Long id;
  /**
   * 字典代码
   */
  @ApiModelProperty(value = "字典值", name = "value")
  private String value;

  /**
   * 字典标签
   */
  @ApiModelProperty(value = "字典标签", name = "label")
  private String label;
  /**
   * 字典类型
   */
  @ApiModelProperty(value = "字典类型", name = "type")
  private String type;

  public DictCacheDTO()
  {
  }
}
