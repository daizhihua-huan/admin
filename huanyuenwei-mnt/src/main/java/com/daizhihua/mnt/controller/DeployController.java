package com.daizhihua.mnt.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.mnt.entity.MntDeploy;
import com.daizhihua.mnt.service.MntDeployService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "部署管理")
@RestController
@Slf4j
@RequestMapping(value = "deploy")
public class DeployController implements BaseController<MntDeploy> {

    @Autowired
    private MntDeployService mntDeployService;

    @ApiOperation(value = "获取部署管理")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("分页参数是:{}",pageable);
        log.info("查询参数是:{}",queryVo);
        return Resut.ok(mntDeployService.page(pageable,queryVo));
    }

    @Log(value = "新增部署管理",type = LogActionType.ADD)
    @PostMapping
    @ApiOperation(value = "新增部署管理")
    @Override
    public Resut add(@RequestBody MntDeploy mntDeploy) {
        log.info("新增的参数是:{}",mntDeploy);
        return Resut.ok(mntDeployService.mySave(mntDeploy));
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @Log(value = "删除部署管理",type = LogActionType.DELETE)
    @ApiOperation(value = "删除部署管理")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("参数是:{}",ids);
        return Resut.ok(mntDeployService.delete(ids));
    }

    @Log(value = "更新部署管理",type = LogActionType.UPDATE)
    @PutMapping
    @ApiOperation(value = "更新部署管理")
    @Override
    public Resut update(@RequestBody MntDeploy mntDeploy) {

        return Resut.ok(mntDeployService.myUpdate(mntDeploy));
    }

    @Log(value = "一键部署上传")
    @ApiOperation(value = "一键部署上传")
    @PostMapping(value = "upload")
    public Resut upload(@RequestParam(value = "file") MultipartFile file, Long deployId,Long appId, Long userId,HttpServletRequest request){
        log.info("文件信息是:{}",file.getOriginalFilename());
        log.info("{}",deployId);
        log.info("{}",appId);
        mntDeployService.upload(file,deployId,appId,userId);
        return Resut.ok();
    }

    @ApiOperation(value = "停止",notes = "发送停止命令")
    @PostMapping(value = "stopServer")
    public Resut stop(@RequestBody MntDeploy mntDeploy){
        log.info("发生停止命令的参数是：{}",mntDeploy);
        mntDeployService.stop(mntDeploy);
        return Resut.ok();
    }

    @ApiOperation(value = "状态",notes = "发送状态命令")
    @PostMapping(value = "serverStatus")
    public Resut status(@RequestBody MntDeploy mntDeploy){
        log.info("发送状态命令的参数是:{}",mntDeploy);
        mntDeployService.status(mntDeploy);
        return Resut.ok();
    }

    @ApiOperation(value = "开始",notes = "发送开始命令")
    @PostMapping(value = "startServer")
    public Resut start(@RequestBody MntDeploy mntDeploy){
        log.info("发送开始命令的参数是:{}",mntDeploy);
        mntDeployService.start(mntDeploy);
        return Resut.ok();
    }

    @ApiOperation(value = "导出部署管理")
    @GetMapping(value = "download")
    public void download(Pageable pageable, HttpServletResponse response){
        mntDeployService.download(pageable,response);
    }




}
