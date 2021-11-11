package com.daizhihua.tools.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.oauth.util.SecurityUtils;
import com.daizhihua.tools.entity.SysQuartzJob;
import com.daizhihua.tools.service.SysQuartzJobService;
import com.daizhihua.tools.service.SysQuartzLogService;
import com.daizhihua.tools.util.QuartzManage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "任务管理")
@RestController
@Slf4j
@RequestMapping(value = "jobs")
public class JobsController implements BaseController<SysQuartzJob> {

    @Autowired
    private SysQuartzJobService sysQuartzJobService;

    @Autowired
    private SysQuartzLogService sysQuartzLogService;


    @Autowired
    private QuartzManage quartzManage;

    @ApiOperation(value = "任务查询",notes = "任务查询")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("参数是:{}",pageable);
        return Resut.ok(sysQuartzJobService.page(pageable,queryVo));
    }

    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    public Resut execution(@PathVariable Long id){
        sysQuartzJobService.execution(sysQuartzJobService.getById(id));
        return Resut.ok();
    }

    @ApiOperation(value = "状态修改")
    @PutMapping(value = "/{id}")
    public Resut updateIsPause(@PathVariable Long id){
        SysQuartzJob sysQurzJob = sysQuartzJobService.getById(id);
        if(sysQurzJob.getIsPause()){
            sysQurzJob.setIsPause(false);
            quartzManage.resumeJob(sysQurzJob);
        }else{
            sysQurzJob.setIsPause(true);
            quartzManage.pauseJob(sysQurzJob);
        }
        return Resut.ok(sysQuartzJobService.updateById(sysQurzJob));
    }

    @ApiOperation(value = "新增任务")
    @PostMapping
    @Override
    public Resut add(@RequestBody SysQuartzJob sysQuartzJob) {
        log.info("参数是:{}",sysQuartzJob);
        sysQuartzJob.setCreateBy(SecurityUtils.getCurrentUsername());
        sysQuartzJob.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysQuartzJob.setCreateTime(DateUtils.getDateTime());
        sysQuartzJob.setUpdateTime(DateUtils.getDateTime());
        boolean flag = sysQuartzJobService.save(sysQuartzJob);
        if(flag){
            quartzManage.addJob(sysQuartzJob);
        }
        return Resut.ok(flag);
    }

    @ApiOperation(value = "删除任务")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("删除的参数是:{}",ids);
        return Resut.ok(sysQuartzJobService.removeByIds(ids));
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @ApiOperation(value = "更新")
    @PutMapping
    @Override
    public Resut update(@RequestBody SysQuartzJob sysQuartzJob) {
        sysQuartzJob.setUpdateTime(DateUtils.getDateTime());
        sysQuartzJob.setUpdateBy(SecurityUtils.getCurrentUsername());
        return Resut.ok(sysQuartzJobService.updateById(sysQuartzJob));
    }

    @ApiOperation(value = "查询日志",notes = "查询日志详情信息")
    @GetMapping(value = "/logs")
    public Resut logs(Pageable pageable, QueryVo queryVo){
        log.info("分页参数是:{}",pageable);
        log.info("查询参数是:{}",queryVo);
        return Resut.ok( sysQuartzLogService.page(pageable,queryVo));
    }

}
