package com.daizhihua.mnt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 部署历史管理
 * </p>
 *
 * @author 代志华
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MntDeployHistory对象", description="部署历史管理")
public class MntDeployHistory extends Model<MntDeployHistory> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "history_id", type = IdType.ASSIGN_UUID)
    private String historyId;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "部署日期")
    private String deployDate;

    @ApiModelProperty(value = "部署用户")
    private String deployUser;

    @ApiModelProperty(value = "服务器IP")
    private String ip;

    @ApiModelProperty(value = "部署编号")
    private Long deployId;

    public String getId() {
        return historyId;
    }

    @Override
    protected Serializable pkVal() {
        return this.historyId;
    }

}
