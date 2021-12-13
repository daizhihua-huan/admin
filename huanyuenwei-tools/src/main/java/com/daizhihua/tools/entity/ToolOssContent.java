package com.daizhihua.tools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ToolOssContent对象", description="")
public class ToolOssContent extends Model<ToolOssContent> {

    private static final long serialVersionUID=1L;

      @TableId(value = "content_id", type = IdType.AUTO)
    private Long contentId;

    @ApiModelProperty(value = "路径")
    private String path;

    private String size;

    private String name;

    @ApiModelProperty(value = "版本信息")
    private String version;

    private String etag;

    private String updateTime;

    @TableField(value = "bucket_name")
    private String bucketName;

    private String suffix;

    public Long getId(){
        return this.contentId;
    }

    @Override
    protected Serializable pkVal() {
        return this.contentId;
    }

}
