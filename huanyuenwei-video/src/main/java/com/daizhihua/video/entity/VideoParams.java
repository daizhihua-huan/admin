package com.daizhihua.video.entity;

import io.swagger.annotations.Api;
import lombok.Data;

@Data
@Api(value = "视频参数")
public class VideoParams {

    private String accessToken;

    private String deviceSerial;

    private String startTime;

    private String stopTime;

    private String type;
}
