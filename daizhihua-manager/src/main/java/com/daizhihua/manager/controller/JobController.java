package com.daizhihua.manager.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysJob;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.manager.service.JobService;
import com.daizhihua.oauth.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "岗位接口")
@Slf4j
@RestController
@RequestMapping(value = "job")
public class JobController implements BaseController<SysJob> {

    @Autowired
    private JobService jobService;
    @ApiOperation(value = "查询所有岗位")
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

    @ApiOperation(value = "删除岗位",notes = "删除岗位可以是多个ids")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("删除的ids是:{}",ids);
        return Resut.ok(jobService.removeByIds(ids));
    }

    @ApiOperation(value = "更新岗位")
    @PutMapping
    @Override
    public Resut update(@RequestBody SysJob sysJob) {
        log.info("修改的参数是:{}",sysJob);
        sysJob.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysJob.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok(jobService.updateById(sysJob));
    }
}
