package com.daizhihua.tools.controller;

import com.daizhihua.core.res.Resut;
import com.daizhihua.tools.entity.CodeGenConfig;
import com.daizhihua.tools.service.CodeGenConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "参数配置")
@RestController
@RequestMapping(value = "genConfig")
@Slf4j
public class GentorConfigController {


    @Autowired
    private CodeGenConfigService codeGenConfigService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "/{tableName}")
    public Resut codeConfig(@PathVariable String tableName){
        log.info("查询表名称:{}",tableName);
        return Resut.ok(codeGenConfigService.getGenConfigForTableName(tableName));
    }

    @ApiOperation(value = "修改配置信息",notes = "修改配置信息")
    @PutMapping
    public Resut update(@RequestBody CodeGenConfig codeGenConfig){
        log.info("配置参数是:{}",codeGenConfig);
        codeGenConfigService.saveOrUpdate(codeGenConfig);
        return Resut.ok(codeGenConfig);
    }
}
