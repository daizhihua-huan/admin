package com.daizhihua.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户角色关联
 * </p>
 *
 * @author 代志华
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUsersRoles对象", description="用户角色关联")
public class SysUsersRoles extends Model<SysUsersRoles> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户ID")
      @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
