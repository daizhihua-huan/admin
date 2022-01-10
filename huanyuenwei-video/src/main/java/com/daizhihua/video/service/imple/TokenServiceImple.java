package com.daizhihua.video.service.imple;

import cn.hutool.json.JSONObject;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.util.RedisUtil;
import com.daizhihua.video.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.daizhihua.core.util.HttpUtils.doPostForm;

/**
 * token类操作
 */
@Service
@Slf4j
public class TokenServiceImple implements TokenService {
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
}
