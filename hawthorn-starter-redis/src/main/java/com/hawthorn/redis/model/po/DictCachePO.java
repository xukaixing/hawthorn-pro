package com.hawthorn.redis.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hawthorn.platform.model.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @copyright: Copyright (c) 2021 andyten
 * <p></p>
 * @remark: todo 字典管理(sys_dict) po实体类
 * @author: andy.ten@tom.com
 * @created: 3/5/21 9:40 PM
 * @lasted: 3/5/21 9:40 PM
 * @version v1.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_dict")
public class DictCachePO extends BasePO
{
  /**
   * 编号
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  /**
   * 字典标签
   */
  @TableField("label")
  private String label;
  /**
   * 字典值
   */
  @TableField("value")
  private String value;
  /**
   * 字典类型
   */
  @TableField("type")
  private String type;

  public DictCachePO()
  {
  }
}
