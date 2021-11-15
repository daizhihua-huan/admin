package com.daizhihua.tools.controller;

import com.daizhihua.core.res.Resut;
import com.daizhihua.tools.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "服务监控")
@RestController
@RequestMapping(value = "monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @ApiOperation(value = "获取服务运行参数")
    @GetMapping
    public Resut server(){

        return Resut.ok(monitorService.getServer());
    }



}
