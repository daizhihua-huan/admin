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
 * 本地存储
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ToolLocalStorage对象", description="本地存储")
public class ToolLocalStorage extends Model<ToolLocalStorage> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "storage_id", type = IdType.AUTO)
    private Long storageId;

    @ApiModelProperty(value = "文件真实的名称")
    private String realName;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "后缀")
    private String suffix;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "大小")
    private String size;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "创建日期")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    private String url;


    public Long getId(){
        return this.storageId;
    }

    public ToolLocalStorage(String realName, String name, String suffix, String path, String type, String size) {
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.size = size;
    }

    public ToolLocalStorage(Long storageId, String realName, String name, String suffix, String path, String type, String size, String createBy, String updateBy, String createTime, String updateTime) {
        this.storageId = storageId;
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.size = size;
        this.createBy = createBy;
        this.updateBy = updateBy;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ToolLocalStorage() {
    }

    @Override
    protected Serializable pkVal() {
        return this.storageId;
    }

}
