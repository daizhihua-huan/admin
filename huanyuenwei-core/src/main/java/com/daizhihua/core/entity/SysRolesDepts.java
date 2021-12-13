package com.daizhihua.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色部门关联
 * </p>
 *
 * @author 代志华
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysRolesDepts对象", description="角色部门关联")
public class SysRolesDepts extends Model<SysRolesDepts> {

    private static final long serialVersionUID=1L;
    @MppMultiId // 复合主键
    @TableField(value = "role_id")
    private Long roleId;
    @MppMultiId // 复合主键
    @TableField(value = "dept_id")
    private Long deptId;


    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

}
