package com.daizhihua.tools.entity;

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
 * 七牛云配置
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ToolQiniuConfig对象", description="七牛云配置")
public class ToolQiniuConfig extends Model<ToolQiniuConfig> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;

    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @ApiModelProperty(value = "Bucket 识别符")
    private String bucket;

    @ApiModelProperty(value = "外链域名")
    private String host;

    @ApiModelProperty(value = "secretKey")
    private String secretKey;

    @ApiModelProperty(value = "空间类型")
    private String type;

    @ApiModelProperty(value = "机房")
    private String zone;


    @Override
    protected Serializable pkVal() {
        return this.configId;
    }

}
