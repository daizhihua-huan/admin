package com.daizhihua.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 数据字典详情
 * </p>
 *
 * @author 代志华
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysDictDetail对象", description="数据字典详情")
public class SysDictDetail extends Model<SysDictDetail> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "detail_id", type = IdType.AUTO)
    private Long detailId;

    @ApiModelProperty(value = "字典id")
    private Long dictId;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer dictSort;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;


    public Long getId(){
        return this.detailId;
    }

    @Override
    protected Serializable pkVal() {
        return this.detailId;
    }

}
