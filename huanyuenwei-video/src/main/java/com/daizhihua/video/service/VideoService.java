package com.daizhihua.video.service;

import com.daizhihua.core.entity.QueryVo;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface VideoService {

    String getAccessToken();

    Map<String,Object> list(Pageable pageable, QueryVo queryVo);
}
