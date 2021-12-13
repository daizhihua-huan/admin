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
 * 岗位
 * </p>
 *
 * @author 代志华
 * @since 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysJob对象", description="岗位")
public class SysJob extends Model<SysJob> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    @ApiModelProperty(value = "岗位名称")
    private String name;

    @ApiModelProperty(value = "岗位状态")
    private Boolean enabled;

    @ApiModelProperty(value = "排序")
    private Integer jobSort;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    public Long getId(){
        return jobId;
    }

    @Override
    protected Serializable pkVal() {
        return this.jobId;
    }

}
