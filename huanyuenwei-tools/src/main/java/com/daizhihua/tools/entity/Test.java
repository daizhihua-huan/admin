package com.daizhihua.tools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author daizhihua
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("test")
@ApiModel(value="Test对象", description="")
public class Test extends Model<Test> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private Boolean id;

    @TableField("name")
    private String name;

    @TableField("time")
    private LocalDateTime time;

    @TableField("update")
    private LocalDateTime update;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
