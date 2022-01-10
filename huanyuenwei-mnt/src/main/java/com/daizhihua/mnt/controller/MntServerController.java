package com.daizhihua.mnt.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.mnt.entity.MntServer;
import com.daizhihua.mnt.service.MntServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Service;
import java.util.List;

@Api(tags = "服务器")
@RestController
@RequestMapping(value = "/serverDeploy")
@Slf4j
public class MntServerController implements BaseController<MntServer> {

    @Autowired
    private MntServerService mntServerService;


    @ApiOperation(value = "查询服务器列表")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){

        log.info("分页的参数:{}",pageable);
        log.info("查询的参数是:{}",queryVo);
        return Resut.ok(mntServerService.page(pageable,queryVo));
    }


    @Log(value = "新增服务器",type = LogActionType.ADD)
    @ApiOperation(value = "新增服务器")
    @PostMapping
    @Override
    public Resut add(@RequestBody MntServer mntServer) {
        mntServer.setUpdateTime(DateUtils.getDateTime());
        mntServer.setCreateTime(DateUtils.getDateTime());
        mntServer.setCreateBy(SecurityUtils.getCurrentUsername());
        mntServer.setUpdateBy(SecurityUtils.getCurrentUsername());
        log.info("新增服务器的参数:{}",mntServer);
        mntServerService.save(mntServer);
        return Resut.ok();
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @Log(value = "更新服务器信息",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新服务器信息",notes = "更新服务器信息")
    @PutMapping
    @Override
    public Resut update(@RequestBody MntServer mntServer) {
        mntServer.setUpdateBy(SecurityUtils.getCurrentUsername());
        mntServer.setUpdateTime(DateUtils.getDateTime());
        log.info("参数是:{}",mntServer);
        return Resut.ok(mntServerService.updateById(mntServer));
    }

    @Log(value = "删除服务器信息")
    @ApiOperation(value = "删除服务器信息",notes = "删除服务器信息")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("删除的参数是:{}",ids);
        return Resut.ok(mntServerService.removeByIds(ids));
    }

    @PostMapping(value = "/testConnect")
    @ApiOperation(value = "测试服务器连接")
    public Resut testContent(@RequestBody MntServer mntServer){

        return Resut.ok(mntServerService.testContent(mntServer));
    }

    @Log(value = "导出服务器信息",type = LogActionType.SELECT)
    @GetMapping(value = "download")
    @ApiOperation(value = "导出服务器信息")
    public void download(Pageable pageable, HttpServletResponse response){
        mntServerService.download(pageable,response);
    }
}
