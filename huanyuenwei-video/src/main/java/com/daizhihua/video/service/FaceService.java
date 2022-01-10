package com.daizhihua.video.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FaceService  {

    Map<String,Object> detect(String file);
}
