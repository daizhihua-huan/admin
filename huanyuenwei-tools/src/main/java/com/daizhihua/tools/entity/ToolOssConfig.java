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
 * 
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ToolOssConfig对象", description="")
public class ToolOssConfig extends Model<ToolOssConfig> {

    private static final long serialVersionUID=1L;

      @TableId(value = "oss_id", type = IdType.AUTO)
    private Integer ossId;

    @ApiModelProperty(value = "填写Bucket所在地域对应的Endpoint")
    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;


    @Override
    protected Serializable pkVal() {
        return this.ossId;
    }

}
