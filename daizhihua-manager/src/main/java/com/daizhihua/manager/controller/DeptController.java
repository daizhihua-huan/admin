package com.daizhihua.manager.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysDept;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.manager.service.DeptService;
import com.daizhihua.oauth.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "部门的方法")
@RequestMapping(value = "dept")
@Slf4j
public class DeptController implements BaseController<SysDept> {

    @Autowired
    DeptService deptService;

    @ApiOperation(value = "查询部门")
    @GetMapping
    public Resut listDept(QueryVo queryVo,String enabled){
        log.info("name是:{}",queryVo);
        log.info("{}",enabled);

        return Resut.ok(deptService.listDept(queryVo,enabled));
    }

    @ApiOperation(value = "部门新增",notes = "新增部门")
    @PostMapping
    @Override
    public Resut add(@RequestBody SysDept sysDept) {
        log.info("sysDept:{}",sysDept);
        sysDept.setCreateBy(SecurityUtils.getCurrentUsername());
        sysDept.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysDept.setCreateTime(DateUtils.getDateTime());
        sysDept.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok(deptService.save(sysDept));
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @ApiOperation(value = "删除id")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("删除ids:{}",ids);
        return Resut.ok(deptService.removeByIds(ids));
    }


    @ApiOperation(value = "修改部门")
    @PutMapping
    @Override
    public Resut update(@RequestBody SysDept sysDept) {
        log.info("参数是:{}",sysDept);
        return Resut.ok(deptService.updateById(sysDept));
    }
}
