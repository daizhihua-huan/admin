package com.daizhihua.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.SecurityUtils;
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
 * @since 2022-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BusinessOrder对象", description="")
public class BusinessOrder extends Model<BusinessOrder> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "注解id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "电话/微信")
    private String number;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "报价")
    private String offer;

    @ApiModelProperty(value = "地区")
    private String address;

    @ApiModelProperty(value = "新/老客户")
    private String client;

    @ApiModelProperty(value = "客户来源")
    private String source;

    @ApiModelProperty(value = "电话类型")
    private String phoneType;

    @ApiModelProperty(value = "微信")
    private String weixin;

    @ApiModelProperty(value = "文件")
    private String file;

    @ApiModelProperty(value = "成交")
    private String done;

    @ApiModelProperty(value = "成交价")
    private String transactionPrice;

    @ApiModelProperty(value = "最后报价")
    private String lastPrice;

    @ApiModelProperty(value = "沟通记录")
    private String record;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "预付款")
    private String advance;

    @ApiModelProperty(value = "收款方式")
    private String pay;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "标底金额(万)")
    private String money;

    @ApiModelProperty(value = "交货时间")
    private String deliverTime;

    @ApiModelProperty(value = "开标时间")
    private String openTime;

    @ApiModelProperty(value = "投标文件")
    private String document;

    @ApiModelProperty(value = "客户需求")
    private String need;

    @ApiModelProperty(value = "不成交原因")
    private String cause;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "文件获取时间")
    private String fileTime;

    public BusinessOrder() {
    }
    public BusinessOrder(BusinessTemp businessTemp){
        this.number = businessTemp.getNumber();
        this.name = businessTemp.getName();
        this.address = businessTemp.getAddress();
        this.offer = businessTemp.getOffer();
        this.advance = businessTemp.getAdvance();
        this.client = businessTemp.getClient();
        this.source = businessTemp.getSource();
        this.phoneType = businessTemp.getPhoneType();
        this.weixin = businessTemp.getWeixin();
        this.file = businessTemp.getFile();
        this.done = businessTemp.getDone();
        this.transactionPrice = businessTemp.getTransactionPrice();
        this.lastPrice = businessTemp.getLastPrice();
        this.record = businessTemp.getRecord();
        this.projectName = businessTemp.getProjectName();
        this.pay = businessTemp.getPay();
        this.cause = businessTemp.getCause();
        this.projectType = businessTemp.getProjectType();
        this.money = businessTemp.getMoney();
        this.deliverTime = businessTemp.getDeliverTime();
        this.openTime = businessTemp.getOpenTime();
        this.document = businessTemp.getDocument();
        this.need = businessTemp.getNeed();
        this.userId = businessTemp.getUserId();
        this.createTime = DateUtils.getDateTime();
        this.updateTime = DateUtils.getDateTime();
        this.createBy = SecurityUtils.getCurrentUsername();
        this.updateBy = SecurityUtils.getCurrentUsername();
        this.status = "0";

    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
