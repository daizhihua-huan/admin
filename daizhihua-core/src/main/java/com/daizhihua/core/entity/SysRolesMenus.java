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
 * 角色菜单关联
 * </p>
 *
 * @author 代志华
 * @since 2021-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRolesMenus对象", description="角色菜单关联")
public class SysRolesMenus extends Model<SysRolesMenus> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "菜单ID")
      @TableId(value = "menu_id", type = IdType.ASSIGN_UUID)
    private Long menuId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;


    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }

}
