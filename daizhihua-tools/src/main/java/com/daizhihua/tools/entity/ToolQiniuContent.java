package com.daizhihua.tools.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 七牛云文件存储
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ToolQiniuContent对象", description="七牛云文件存储")
public class ToolQiniuContent extends Model<ToolQiniuContent> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "content_id", type = IdType.AUTO)
    private Long contentId;

    @ApiModelProperty(value = "Bucket 识别符")
    private String bucket;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件大小")
    private String size;

    @ApiModelProperty(value = "文件类型：私有或公开")
    private String type;

    @ApiModelProperty(value = "文件url")
    private String url;

    @ApiModelProperty(value = "文件后缀")
    private String suffix;

    @ApiModelProperty(value = "上传或同步的时间")
    private String updateTime;

    public Long getId(){
        return this.contentId;
    }


    @Override
    protected Serializable pkVal() {
        return this.contentId;
    }

}
