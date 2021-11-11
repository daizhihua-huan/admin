package com.daizhihua.manager.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@Data
public class UserVo {

    private List<Long> deptId;

    private Boolean enabled;

    private String blurry;

    private List<String> createTime;

}
