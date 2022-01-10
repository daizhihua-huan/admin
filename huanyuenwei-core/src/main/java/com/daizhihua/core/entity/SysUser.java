package com.daizhihua.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.daizhihua.core.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author 代志华
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUser对象", description="系统用户")
@TableName(value = "sys_user")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID=1L;

    public SysUser(){
        isAdmin = false;
        createTime = DateUtils.getDateTime();
        updateTime = DateUtils.getDateTime();
        accountNonExpired = 1;
        accountNonLocked = 1;
        credentialsNonExpired = 1;
    }

    public void setUser(String username,String password ){
        createBy = username;
        updateBy = username;
        this.password = password;
    }

    @ApiModelProperty(value = "ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty(value = "部门名称")
    private Long deptId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像地址")
    private String avatarName;

    @ApiModelProperty(value = "头像真实路径")
    private String avatarPath;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "是否为admin账号")
    private Boolean isAdmin;

    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Long enabled;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新着")
    private String updateBy;

    @ApiModelProperty(value = "修改密码的时间")
    private String pwdResetTime;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "账户没有过期")
    @TableField("account_nonExpired")
    private int accountNonExpired;

    @ApiModelProperty(value = "过期时间")
    private String expiredTime;

    @ApiModelProperty(value = "锁定：1锁定、0没有")
    @TableField("account_nonLocked")
    private int accountNonLocked;

    @ApiModelProperty(value = "密码过期:1过期、0没有")
    @TableField("credentials_nonExpired")
    private int credentialsNonExpired;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private List<SysMenu> list;

    @TableField(exist = false)
    private List<Integer> roles;

    @TableField(exist = false)
    private List<Integer> jobs;
    @TableField(exist = false)
    private List<String> permissions;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }




}
