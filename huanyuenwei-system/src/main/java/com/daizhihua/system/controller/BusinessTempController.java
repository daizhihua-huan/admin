package com.daizhihua.system.controller;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.system.entity.BusinessTemp;
import com.daizhihua.system.entity.BusinessVo;
import com.daizhihua.system.service.BusinessTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Api(tags = "我的业务")
@RequestMapping(value = "businessTemp")
@RestController
@Slf4j
public class BusinessTempController {

    @Autowired
    private BusinessTempService businessTempService;

    @GetMapping
    @ApiOperation(value = "查询我的业务")
    public Resut businesstemp(Pageable pageable, BusinessVo businessVo){

        return Resut.ok(businessTempService.page(pageable,businessVo));
    }

    @GetMapping(value = "total")
    @ApiOperation(value = "统计信息")
    public Resut businessTotal(){
        log.info("查询统计信息");
        return Resut.ok(businessTempService.total(SecurityUtils.getCurrentUserId()));
    }

    @PutMapping
    @ApiOperation(value = "修改我的业务")
    public Resut businesstempEdit(@RequestBody BusinessTemp businessTemp){
        log.info("查询的参数是:{}",businessTemp);
        businessTempService.updateById(businessTemp);
        return Resut.ok();
    }

    @PostMapping(value = "/issue")
    @ApiOperation(value = "建单")
    public Resut issue(@RequestBody BusinessTemp businessTemp){
        log.info("业务信息:{}",businessTemp);
        businessTempService.issue(businessTemp);
        return Resut.ok();
    }
}
