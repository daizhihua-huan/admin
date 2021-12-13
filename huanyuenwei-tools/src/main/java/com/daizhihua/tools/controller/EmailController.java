package com.daizhihua.tools.controller;

import com.daizhihua.core.res.Resut;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.tools.entity.vo.EmailVo;
import com.daizhihua.tools.entity.ToolEmailConfig;
import com.daizhihua.tools.service.ToolEmailConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "邮件设置")
@RestController
@RequestMapping(value = "email")
@Slf4j
public class EmailController {

    @Autowired
    private ToolEmailConfigService toolEmailConfigService;

    @ApiOperation(value = "获取email的配置")
    @GetMapping
    public Resut getConfig(){

        return Resut.ok(toolEmailConfigService.getOne(null));
    }
    @Log("发送邮件")
    @PostMapping
    @ApiOperation(value = "发送邮件")
    public Resut sendEmail(@Validated @RequestBody EmailVo emailVo){
        log.info("邮件的信息:{}",emailVo);
        toolEmailConfigService.send(emailVo);
        return Resut.ok();
    }

    @Log(value = "更新邮件配置",type = LogActionType.UPDATE)
    @PutMapping
    @ApiOperation(value = "更新邮件配置")
    public Resut updateEmailConfig(@RequestBody ToolEmailConfig toolEmailConfig){
        log.info("参数是：{}",toolEmailConfig);
        if(toolEmailConfig.getConfigId()==null){
            toolEmailConfigService.save(toolEmailConfig);
        }else{
            toolEmailConfigService.updateById(toolEmailConfig);
        }
        return Resut.ok();
    }
}
