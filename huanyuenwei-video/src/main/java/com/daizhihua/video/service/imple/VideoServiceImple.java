package com.daizhihua.video.service.imple;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.RedisUtil;
import com.daizhihua.video.entity.VideoParams;
import com.daizhihua.video.entity.YsEnum;
import com.daizhihua.video.service.TokenService;
import com.daizhihua.video.service.VideoService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.daizhihua.core.util.HttpUtils.doPostForm;

@Service
@Slf4j
public class VideoServiceImple implements VideoService {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TokenService tokenService;

    @Override
    public Map<String, Object> list(Pageable pageable, QueryVo queryVo) {
        Map<String,Object> map = new HashMap<>();
        log.info("{}",pageable);
        Map<String, String> params = new HashMap<>();
        params.put("accessToken",getToken());
        params.put("pageStart",String.valueOf(pageable.getPageNumber()));
        params.put("pageSize",String.valueOf(pageable.getPageSize()));
        JSONObject jsonObject = doPostForm(Constant.LIST, params);
        log.info("{}",jsonObject);
        if(jsonObject.get("code").toString().equals("200")){
            JSONObject page = (JSONObject) jsonObject.get("page");
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            map.put("records",jsonArray);
            map.put("total",page.get("total"));
        }else if(jsonObject.get("code").toString().equals("10002")){
            redisUtil.del("accessToken");
           list(pageable,queryVo);
        }
        return map;
    }

    @Override
    public Map<String,String> play(VideoParams params) {

        Map<String,String> map = new HashMap<>();
        map.put("accessToken",getToken());
        map.put("deviceSerial",params.getDeviceSerial());
//        map.put("protocol","1");
//        map.put("quality","2");
        JSONObject jsonObject = doPostForm(Constant.PLAY, map);
        String url = "";
        if(jsonObject.get("code").toString().equals("200")){
            JSONObject data = (JSONObject) jsonObject.get("data");
            url = data.get("url").toString();
        }else if(jsonObject.get("code").toString().equals("10002")){
            redisUtil.del("accessToken");
            play(params);
        }
        map.put("url",url);
        return map;
    }

    @Override
    public Map<String, String> playRtmp(VideoParams params) {
        Map<String,String> map = new HashMap<>();
        map.put("accessToken",getToken());
        map.put("deviceSerial",params.getDeviceSerial());
        map.put("protocol","3");
//        map.put("quality","2");
        String url = "";
        JSONObject jsonObject = doPostForm(Constant.PLAY, map);
        if(jsonObject.get("code").toString().equals("200")){
            JSONObject data = (JSONObject) jsonObject.get("data");
            url = data.get("url").toString();
        }else if(jsonObject.get("code").toString().equals("10002")){
            redisUtil.del("accessToken");
            play(params);
        }
        map.put("url",url);
        return map;
    }

    @Override
    public Map<String, Object> getDeviceStatuc(VideoParams params) {
        Map<String,String> map = new HashMap<>();
        map.put("accessToken",getToken());
        map.put("deviceSerial",params.getDeviceSerial());
        JSONObject jsonObject = doPostForm(Constant.DEVOICESTATUS, map);
        int code = Integer.parseInt(jsonObject.get("code").toString());
        switch (YsEnum.getStructureEnum(code)){
            case SUCCESS:
                JSONObject reuslt = (JSONObject) jsonObject.get("data");
                log.info("{}",reuslt);
                break;
            case EXPIERTOKEN:
                break;
        }
        return null;
    }

    /**
     * 获取萤石云的请求头
     * @return
     */
    private String getToken(){
        String accessToken = "";
        if(redisUtil.hasKey("accessToken")){
            accessToken = redisUtil.get("accessToken").toString();
        }else{
            accessToken = tokenService.getAccessToken();
        }
        return accessToken;
    }
}
