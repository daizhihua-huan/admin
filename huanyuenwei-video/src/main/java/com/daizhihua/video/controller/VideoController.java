package com.daizhihua.video.controller;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.video.entity.VideoParams;
import com.daizhihua.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Api(tags = "视频监控")
@RestController
@RequestMapping(value = "video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    @ApiOperation(value = "查询设备列表")
    public Resut list(Pageable pageable, QueryVo queryVo){
        return Resut.ok( videoService.list(pageable,queryVo));
    }



    @ApiOperation(value = "查询播放的参数")
    @PostMapping(value = "play")
    public Resut play(VideoParams videoParams){

        return Resut.ok(videoService.play(videoParams));
    }

    @ApiOperation(value = "查询设备信息")
    @PostMapping(value = "getDeviceStatus")
    public Resut getStatus(@RequestBody VideoParams videoParams){
        videoService.getDeviceStatuc(videoParams);
        return Resut.ok();
    }

    @PostMapping(value = "face")
    public Resut face(HttpServletRequest request) throws IOException {
        File file = new File("D:/1.png");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        fileOutputStream.write(fileUint8Array);
        Object fileUint8Array = request.getAttribute("fileUint8Array");
        Object fileName = request.getAttribute("fileName");
        System.out.println(fileName);
        System.out.println(fileUint8Array);
        return Resut.ok();
    }

}
