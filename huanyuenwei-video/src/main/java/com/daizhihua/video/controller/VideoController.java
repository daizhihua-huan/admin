package com.daizhihua.video.controller;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.video.service.VideoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "视频监控")
@RestController
@RequestMapping(value = "video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        return Resut.ok( videoService.list(pageable,queryVo));
    }





}
