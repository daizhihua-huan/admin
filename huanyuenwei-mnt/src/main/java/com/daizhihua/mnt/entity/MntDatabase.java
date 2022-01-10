package com.daizhihua.mnt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据库管理
 * </p>
 *
 * @author 代志华
 * @since 2021-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MntDatabase对象", description="数据库管理")
public class MntDatabase extends Model<MntDatabase> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "db_id", type = IdType.ASSIGN_UUID)
    private String dbId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "jdbc连接")
    private String jdbcUrl;

    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @TableField(exist = false)
    private String sql;

    public String getId(){
        return this.dbId;
    }

    @Override
    protected Serializable pkVal() {
        return this.dbId;
    }

}
