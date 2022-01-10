package com.daizhihua.mnt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 部署管理
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MntDeploy对象", description="部署管理")
public class MntDeploy extends Model<MntDeploy> implements Serializable{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "deploy_id", type = IdType.AUTO)
    private Long deployId;

    @ApiModelProperty(value = "应用编号")
    private Long appId;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @TableField(exist = false)
    private List<MntServer> deploys;

    @TableField(exist = false)
    private  MntApp mntApp;

//    @TableField(exist = false)
//    private List<Long> deploys;
    @TableField(exist = false)
    private Long app;

    public Long getId(){
        return this.deployId;
    }

    @Override
    protected Serializable pkVal() {
        return this.deployId;
    }

}
