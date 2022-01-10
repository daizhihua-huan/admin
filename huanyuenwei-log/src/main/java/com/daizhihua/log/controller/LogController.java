package com.daizhihua.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.log.entity.SysLog;
import com.daizhihua.log.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(value = "日志")
@RestController
@RequestMapping(value = "logs")
@Slf4j
public class LogController {

    @Autowired
    private SysLogService sysLogService;

    @ApiOperation(value = "查询日志",notes = "查询日志信息")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("配置的参数是:{}",pageable);
        log.info("查询参数:{}",queryVo);

        return Resut.ok(sysLogService.page(pageable,queryVo,"INFO"));
    }

    @ApiOperation(value = "异常日志查询")
    @GetMapping(value = "/error")
    public Resut error(Pageable pageable, QueryVo queryVo){
        log.info("{}",pageable);
        log.info("{}",queryVo);
        return Resut.ok(sysLogService.page(pageable,queryVo,"ERROR"));
    }
    @ApiOperation(value = "查询异常信息信息")
    @GetMapping(value = "/error/{id}")
    public Resut queryErrorLogs(@PathVariable Long id){

        return Resut.ok(sysLogService.getById(id));
    }


    @ApiOperation(value = "清空日志")
    @DeleteMapping(value = "del/info")
    public Resut del(){
        log.info("清空操作日志");
        return Resut.ok(sysLogService.delete("INFO"));
    }

    @ApiOperation(value = "情况异常日志")
    @DeleteMapping(value = "del/error")
    public Resut delError(){
        log.info("清空异常日志");
        return Resut.ok(sysLogService.delete("ERROR"));
    }

    @Log(value = "操作日志导出",type = LogActionType.SELECT)
    @GetMapping(value = "download")
    @ApiOperation(value = "下载")
    public void download(Pageable pageable, HttpServletResponse response){
        sysLogService.download(pageable,response,"INFO");
    }

    @Log(value = "异常日志导出")
    @GetMapping(value = "error/download")
    @ApiOperation(value = "导出异常日志")
    public void errorDownload(Pageable pageable, HttpServletResponse response){
        sysLogService.download(pageable,response,"ERROR");
    }

}
