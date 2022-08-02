package com.daizhihua.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class BusinessVo {

    private String status;
    /**
     * 创建日期
     */
    private List<String> createTime;

    private String source;

    private String phoneType;

    private String weixin;

    private String done;

    private String client;
}
