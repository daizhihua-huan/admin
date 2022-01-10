package com.daizhihua.video.controller;

import com.daizhihua.core.res.Resut;
import com.daizhihua.video.entity.FaceParams;
import com.daizhihua.video.service.FaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "人脸识别")
@RestController
@RequestMapping(value = "face")
@Slf4j
public class FaceController {

    @Autowired
    private FaceService faceService;

    @ApiOperation(value = "创建人脸识别的集合")
    @PostMapping(value = "createFace")
    public Resut createFace(@RequestBody String name){

        return Resut.ok();
    }

    @PostMapping(value = "faceDetect")
    @ApiOperation(value = "人脸检测")
    public Resut faceDetect(String file){
        log.info("{}",file);
        faceService.detect(file);
        return Resut.ok();
    }

}
