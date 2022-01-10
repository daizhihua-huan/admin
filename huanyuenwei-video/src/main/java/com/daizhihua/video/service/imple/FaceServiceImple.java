package com.daizhihua.video.service.imple;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.config.FileProperties;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.RedisUtil;
import com.daizhihua.video.service.FaceService;
import com.daizhihua.video.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.daizhihua.core.util.HttpUtils.doPostForm;
import static com.daizhihua.core.util.HttpUtils.imageToBase64;

@Service
@Slf4j
public class FaceServiceImple implements FaceService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FileProperties properties;

    @Override
    public Map<String, Object> detect(String file) {
        Map<String,String > map = new HashMap<>();
        String accessToken;
        if(redisUtil.hasKey("accessToken")){
            accessToken = redisUtil.get("accessToken").toString();
        }else{
            accessToken = tokenService.getAccessToken();
        }
        map.put("accessToken",accessToken);
        map.put("dataType","1");
        map.put("image",file);
        map.put("operation","gender,age,glass");
        JSONObject jsonObject = doPostForm(Constant.DETECT, map);
        String code = jsonObject.get("code").toString();
        if(code.equals("200")){
            log.info("{}",jsonObject.get("data"));
        }
        log.info("{}",jsonObject);
        return null;
    }

    private File upload(MultipartFile file){

        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file_data = FileUtil.upload(file, properties.getPath().getPath() + type +  File.separator);
        if(ObjectUtil.isNull(file_data)){
            throw new BadRequestException("上传失败");
        }
        return file_data;
    }
}
