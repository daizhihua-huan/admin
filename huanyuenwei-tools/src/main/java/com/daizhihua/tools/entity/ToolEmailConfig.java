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
 * 邮箱配置
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ToolEmailConfig对象", description="邮箱配置")
public class ToolEmailConfig extends Model<ToolEmailConfig> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "config_id", type = IdType.ASSIGN_UUID)
    private Long configId;

    @ApiModelProperty(value = "收件人")
    private String fromUser;

    @ApiModelProperty(value = "邮件服务器SMTP地址")
    private String host;

    @ApiModelProperty(value = "密码")
    private String pass;

    @ApiModelProperty(value = "端口")
    private String port;

    @ApiModelProperty(value = "发件者用户名")
    private String user;


    @Override
    protected Serializable pkVal() {
        return this.configId;
    }

}
