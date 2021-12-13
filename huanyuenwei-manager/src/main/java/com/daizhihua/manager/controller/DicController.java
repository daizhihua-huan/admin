package com.daizhihua.manager.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysDict;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.manager.service.DictService;
import com.daizhihua.core.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "字典")
@RestController
@RequestMapping(value = "dict")
@Slf4j
public class DicController implements BaseController<SysDict> {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "查询数据字典")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("获取分页参数:{}",pageable);
        log.info("参数:{}",queryVo);
        return Resut.ok(dictService.page(pageable,queryVo));
    }

    @Log(value = "新增数据字典",type = LogActionType.ADD)
    @ApiOperation(value = "添加",notes = "增加数据字典")
    @PostMapping
    @Override
    public Resut add(@RequestBody SysDict sysDict) {
        log.info("参数是:{}",sysDict);
        sysDict.setCreateBy(SecurityUtils.getCurrentUsername());
        sysDict.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysDict.setCreateTime(DateUtils.getDateTime());
        sysDict.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok(dictService.save(sysDict));
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @ApiOperation(value = "查询数据字典")
    @GetMapping("all")
    public Resut all(){

        return Resut.ok( dictService.list());
    }

    @Log(value = "删除数据字典",type = LogActionType.DELETE)
    @ApiOperation(value = "删除",notes = "删除字典")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("参数是:{}",ids);
        return Resut.ok(dictService.removeByIds(ids));
    }

    @Log(value = "修改数据字典",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新",notes = "修改数据字典")
    @PutMapping
    @Override
    public Resut update(@RequestBody SysDict sysDict) {
        log.info("参数是:{}",sysDict);
        sysDict.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysDict.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok(dictService.updateById(sysDict));
    }
}
