package com.daizhihua.tools.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.tools.entity.SysQuartzJob;
import com.daizhihua.tools.service.SysQuartzJobService;
import com.daizhihua.tools.service.SysQuartzLogService;
import com.daizhihua.tools.util.QuartzManage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

        return Resut.ok(sysQuartzJobService.updateIsPause(id));
    }

    @ApiOperation(value = "新增任务")
    @PostMapping
    @Override
    public Resut add(@RequestBody SysQuartzJob sysQuartzJob) {
        log.info("参数是:{}",sysQuartzJob);

        return Resut.ok(sysQuartzJobService.saveJobs(sysQuartzJob));
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
        if (!CronExpression.isValidExpression(sysQuartzJob.getCronExpression())){
            throw new BadRequestException("cron表达式格式错误");
        }
        if(StringUtils.isNotBlank(sysQuartzJob.getSubTask())){
            List<String> tasks = Arrays.asList(sysQuartzJob.getSubTask().split("[,，]"));
            if (tasks.contains(sysQuartzJob.getId().toString())) {
                throw new BadRequestException("子任务中不能添加当前任务ID");
            }
        }
        sysQuartzJob.setUpdateTime(DateUtils.getDateTime());
        sysQuartzJob.setUpdateBy(SecurityUtils.getCurrentUsername());
        boolean flag = sysQuartzJobService.updateById(sysQuartzJob);
        if(flag){
            quartzManage.updateJobCron(sysQuartzJob);
        }
        return Resut.ok(flag);
    }

    @ApiOperation(value = "查询日志",notes = "查询日志详情信息")
    @GetMapping(value = "/logs")
    public Resut logs(Pageable pageable, QueryVo queryVo,Boolean isSuccess){
        log.info("分页参数是:{}",pageable);
        log.info("查询参数是:{}",queryVo);
        log.info("{}",isSuccess);
        return Resut.ok( sysQuartzLogService.page(pageable,queryVo,isSuccess));
    }

}
