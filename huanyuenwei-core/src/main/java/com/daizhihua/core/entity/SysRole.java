package com.daizhihua.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.daizhihua.core.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 代志华
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRole对象", description="角色表")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID=1L;

    public SysRole() {
        this.createTime = DateUtils.getDateTime();
        this.updateTime = DateUtils.getDateTime();
    }

    @ApiModelProperty(value = "ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "角色级别")
    private Integer level;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "数据权限")
    private String dataScope;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    public Long getId(){

        return roleId;
    }

    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

}
