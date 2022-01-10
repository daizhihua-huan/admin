package com.daizhihua.video.service;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.video.entity.VideoParams;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface VideoService {


    Map<String,Object> list(Pageable pageable, QueryVo queryVo);

    Map<String,String> play(VideoParams params);

    Map<String,String> playRtmp(VideoParams params);

    Map<String,Object> getDeviceStatuc(VideoParams params);
}
