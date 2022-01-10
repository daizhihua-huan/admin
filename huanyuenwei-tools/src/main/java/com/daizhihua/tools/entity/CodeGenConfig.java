package com.daizhihua.tools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 代码生成器配置
 * </p>
 *
 * @author 代志华
 * @since 2021-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("code_gen_config")
@ApiModel(value="CodeGenConfig对象", description="代码生成器配置")
public class CodeGenConfig extends Model<CodeGenConfig> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;

    @ApiModelProperty(value = "表名")
    @TableField("table_name")
    private String tableName;

    @ApiModelProperty(value = "作者")
    @TableField("author")
    private String author;

    @ApiModelProperty(value = "是否覆盖")
    @TableField("cover")
    private Boolean cover;

    @ApiModelProperty(value = "模块名称")
    @TableField("module_name")
    private String moduleName;

    @ApiModelProperty(value = "至于哪个包下")
    @TableField("pack")
    private String pack;

    @ApiModelProperty(value = "前端代码生成的路径")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "前端Api文件路径")
    @TableField("api_path")
    private String apiPath;

    @ApiModelProperty(value = "表前缀")
    @TableField("prefix")
    private String prefix;

    @ApiModelProperty(value = "接口名称")
    @TableField("api_alias")
    private String apiAlias;


    @Override
    protected Serializable pkVal() {
        return this.configId;
    }

}
