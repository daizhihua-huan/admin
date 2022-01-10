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
 * 代码生成字段信息存储
 * </p>
 *
 * @author 代志华
 * @since 2021-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CodeColumnConfig对象", description="代码生成字段信息存储")
public class CodeColumnConfig extends Model<CodeColumnConfig> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "column_id", type = IdType.AUTO)
    private Long columnId;

    private String tableName;

    private String columnName;

    private String columnType;

    private String dictName;

    private String extra;

    private Boolean formShow;

    private String formType;

    private String keyType;

    private Boolean listShow;

    private Boolean notNull;

    private String queryType;

    private String remark;

    private String dateAnnotation;

    public CodeColumnConfig() {
        this.listShow = true;
        this.formShow = true;
    }

    @Override
    protected Serializable pkVal() {
        return this.columnId;
    }

}
