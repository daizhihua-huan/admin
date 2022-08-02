package com.daizhihua.manager.controller;

import cn.hutool.core.map.MapUtil;

import cn.hutool.json.JSONException;
import com.daizhihua.core.annotion.AnonymousAccess;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.TLSSigAPIvUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api
@RestController
@RequestMapping(value = "im")
@Slf4j
public class IMController {


    /**
     * 生成userSign
     * @return
     * @throws JSONException
     */
    @AnonymousAccess
    @GetMapping(value = "getUserSign")
    public Resut getUserSign(@RequestBody Map<String,Object> map) throws JSONException {
        String userId = MapUtil.getStr(map, "userId");
        TLSSigAPIvUtil tlsSigAPIvUtil = new TLSSigAPIvUtil(1400449515,
                "b67a138cb0d7c8d0a44bfee728cb6314f42a99ee80466ae660b6fe85b7b56f32");
        String sign = tlsSigAPIvUtil.genUserSig(userId, 3 * 10000);
        return Resut.ok(sign);
    }

}
