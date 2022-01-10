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
 * @since 2022-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Message对象", description="")
public class Message extends Model<Message> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "消息类型")
    private String type;

    @ApiModelProperty(value = "设备序列号")
    private String deviceId;

    @ApiModelProperty(value = "设备通道号")
    private String channelNo;

    @ApiModelProperty(value = "消息唯一ID")
    private String messageId;

    @ApiModelProperty(value = "设备通道类型: 1-视频通道信息; 2-IO通道")
    private String channelType;

    @ApiModelProperty(value = "告警类型, 见 附录:萤石设备告警消息类型")
    private String alarmType;

    @ApiModelProperty(value = "设备自己生成的UUID, 用来标识唯一的告警,统一告警的开始、结束采用统一alarmI")
    private String alarmId;

    @ApiModelProperty(value = "告警关联ID，由发起联动方产生，用来表示联动的关联关系")
    private String relationId;

    @ApiModelProperty(value = "告警状态 1-开始")
    private String status;

    @ApiModelProperty(value = "告警位置信息：长度不能超过80字节")
    private String location;

    @ApiModelProperty(value = "告警描述，需要推送给客户的信息")
    private String describe;

    @ApiModelProperty(value = "告警时间，格式： yyyy-MM-ddTHH:mm:ss")
    private String alarmTime;

    @ApiModelProperty(value = "自定义协议类型，命名规则：设备型号_协议标识 如：CS-A1-32W_XX")
    private String customType;

    @ApiModelProperty(value = "图片加密类型：0-不加密，1-用户加密，2-平台加密")
    private String crypt;

    @ApiModelProperty(value = "服务端记录的请求时间")
    private String requestTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
