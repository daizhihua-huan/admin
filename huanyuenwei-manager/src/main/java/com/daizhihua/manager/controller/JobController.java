package com.daizhihua.manager.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysJob;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.manager.service.JobService;
import com.daizhihua.core.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "岗位接口")
@Slf4j
@RestController
@RequestMapping(value = "job")
public class JobController implements BaseController<SysJob> {

    @Autowired
    private JobService jobService;
    @ApiOperation(value = "查询所有岗位")
    @PreAuthorize("@el.check('dept:list')")
    @RequestMapping(value = "listJob",method = RequestMethod.GET)
    public Resut listJob(){
        return Resut.ok(jobService.listJob());
    }

    @ApiOperation(value = "查询岗位",notes = "分页查询岗位")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("分页参数:{}",pageable);
        log.info("查询参数:{}",queryVo);

        return Resut.ok(jobService.pageList(pageable,queryVo));
    }


    @RequestMapping(value = "getUserJob",method = RequestMethod.GET)
    public Resut getUserJob(Long userId){
        return Resut.ok(jobService.getUserJob(userId));
    }

    @ApiOperation(value = "新增岗位")
    @Log(value = "新增岗位",type = LogActionType.ADD)
    @PostMapping
    @Override
    public Resut add(@RequestBody SysJob sysJob) {
        log.info("参数是:{}",sysJob);
        sysJob.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysJob.setCreateBy(SecurityUtils.getCurrentUsername());
        sysJob.setCreateTime(DateUtils.getDateTime());
        sysJob.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok(jobService.save(sysJob));
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @Log(value = "删除岗位",type = LogActionType.DELETE)
    @ApiOperation(value = "删除岗位",notes = "删除岗位可以是多个ids")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("删除的ids是:{}",ids);
        return Resut.ok(jobService.removeByIds(ids));
    }

    @Log(value = "修改岗位",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新岗位")
    @PutMapping
    @Override
    public Resut update(@RequestBody SysJob sysJob) {
        log.info("修改的参数是:{}",sysJob);
        sysJob.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysJob.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok(jobService.updateById(sysJob));
    }

    @GetMapping(value = "download")
    @ApiOperation(value = "导出",notes = "导出岗位")
    @Log(value = "导出岗位",type = LogActionType.SELECT)
    public void download(Pageable pageable, HttpServletResponse response){
        jobService.download(pageable,response);
    }
}
