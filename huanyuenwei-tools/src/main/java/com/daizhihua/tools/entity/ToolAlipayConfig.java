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
 * 支付宝配置类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ToolAlipayConfig对象", description="支付宝配置类")
public class ToolAlipayConfig extends Model<ToolAlipayConfig> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
      @TableId(value = "config_id", type = IdType.ASSIGN_UUID)
    private Long configId;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "编码")
    private String charset;

    @ApiModelProperty(value = "类型 固定格式json")
    private String format;

    @ApiModelProperty(value = "网关地址")
    private String gatewayUrl;

    @ApiModelProperty(value = "异步回调")
    private String notifyUrl;

    @ApiModelProperty(value = "私钥")
    private String privateKey;

    @ApiModelProperty(value = "公钥")
    private String publicKey;

    @ApiModelProperty(value = "回调地址")
    private String returnUrl;

    @ApiModelProperty(value = "签名方式")
    private String signType;

    @ApiModelProperty(value = "商户号")
    private String sysServiceProviderId;

    @ApiModelProperty(value = "类型")
    private String type;


    @Override
    protected Serializable pkVal() {
        return this.configId;
    }

}
