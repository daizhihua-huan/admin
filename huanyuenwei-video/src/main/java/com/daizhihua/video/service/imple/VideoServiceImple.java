package com.daizhihua.video.service.imple;

import cn.hutool.json.JSONObject;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.RedisUtil;
import com.daizhihua.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.daizhihua.core.util.HttpUtils.doPostForm;

@Service
@Slf4j
public class VideoServiceImple implements VideoService {

    @Value("${AppKey}")
    private String appkey;

    @Value("${Secret}")
    private String sercet;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getAccessToken() {
        Map<String, String> params = new HashMap<>();
        params.put("appKey",appkey);
        params.put("appSecret",sercet);
        JSONObject jsonObject = doPostForm(Constant.TOKEN, params);
        log.info("{}",jsonObject);
        JSONObject data = (JSONObject) jsonObject.get("data");
        System.out.println(data.get("accessToken"));
        redisUtil.set("accessToken",data.get("accessToken"));
        return redisUtil.get("accessToken").toString();
    }

    @Override
    public Map<String, Object> list(Pageable pageable, QueryVo queryVo) {
        String accessToken = "";
        if(redisUtil.hasKey("accessToken")){
            accessToken = redisUtil.get("accessToken").toString();
        }else{
            accessToken = getAccessToken();
        }
        log.info("{}",pageable);
        Map<String, String> params = new HashMap<>();
        params.put("accessToken",accessToken);
        params.put("pageStart",String.valueOf(pageable.getPageNumber()));
        params.put("pageSize",String.valueOf(pageable.getPageSize()));
        JSONObject jsonObject = doPostForm(Constant.LIST, params);
        log.info("{}",jsonObject);
        if(jsonObject.get("code").toString().equals("200")){
            JSONObject page = (JSONObject) jsonObject.get("page");
            Map<String,Object> map = new HashMap<>();
            map.put("records",jsonObject.get("data"));
            map.put("total",page.get("total"));
            return map;
        }
        return null;
    }
}
