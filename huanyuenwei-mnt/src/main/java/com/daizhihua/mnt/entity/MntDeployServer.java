package com.daizhihua.mnt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应用与服务器关联
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MntDeployServer对象", description="应用与服务器关联")
public class MntDeployServer extends Model<MntDeployServer> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "部署ID")
      @TableId(value = "deploy_id", type = IdType.ASSIGN_UUID)
    private Long deployId;

    @ApiModelProperty(value = "服务ID")
    private Long serverId;


    @Override
    protected Serializable pkVal() {
        return this.deployId;
    }

}
