package com.daizhihua.manager.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.SysDictDetail;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.manager.service.DicDetailService;
import com.daizhihua.manager.service.DictService;
import com.daizhihua.core.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Api(tags = "数据字典详情")
@RestController
@Slf4j
@RequestMapping(value = "dictDetail")
public class DicDetailController implements BaseController<SysDictDetail> {

    @Autowired
    private DictService dictService;

    @Autowired
    private DicDetailService dicDetailService;

    @ApiOperation(value = "查询数据字典详情")
    @GetMapping(value = "/getForName")
    public Resut getDetail(String dictName, Pageable page){

        return Resut.ok(dictService.getDictDetailForName(dictName));
    }

    @ApiOperation(value = "查询",notes = "数据字典查询")
    @GetMapping
    public Resut list(Pageable pageable, String dictName){
        log.info("参数:{}",pageable);
        log.info("{}",dictName);
        return Resut.ok(dicDetailService.page(pageable,dictName));
    }

    @Log(value = "新增数据字典详情",type = LogActionType.ADD)
    @ApiOperation(value = "新增",notes = "新增数据字典详情")
    @PostMapping
    @Override
    public Resut add(@RequestBody SysDictDetail sysDictDetail) {
        log.info("参数是:{}",sysDictDetail);
        sysDictDetail.setCreateBy(SecurityUtils.getCurrentUsername());
        sysDictDetail.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysDictDetail.setCreateTime(DateUtils.getDateTime());
        sysDictDetail.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok( dicDetailService.save(sysDictDetail));
    }

    @Log(value = "删除数据字典详情",type = LogActionType.DELETE)
    @ApiOperation(value = "删除",notes = "删除数据字典详情")
    @Override
    @DeleteMapping(value = "/{id}")
    public Resut delete(@PathVariable Long id) {
        log.info("删除的参数是:{}",id);
        return Resut.ok(dicDetailService.removeById(id));
    }

//    @ApiOperation(value = "删除",notes = "删除数据字典详情")
//    @DeleteMapping
//    public Resut delete(@RequestBody List<Long> ids){
//        log.info("删除的参数是:{}",ids);
//        return Resut.ok(dicDetailService.removeByIds(ids));
//    }

    @Log(value = "修改数据字典详情",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新",notes = "更新数据字典详情")
    @PutMapping
    @Override
    public Resut update(@RequestBody SysDictDetail sysDictDetail) {
        log.info("更新的参数是:{}",sysDictDetail);
        sysDictDetail.setUpdateTime(DateUtils.getDateTime());
        sysDictDetail.setUpdateBy(SecurityUtils.getCurrentUsername());
        return Resut.ok(dicDetailService.updateById(sysDictDetail));
    }
}
