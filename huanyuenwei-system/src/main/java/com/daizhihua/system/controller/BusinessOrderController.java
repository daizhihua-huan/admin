package com.daizhihua.system.controller;

import com.daizhihua.core.res.Resut;
import com.daizhihua.system.entity.BusinessVo;
import com.daizhihua.system.service.BusinessOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@Api(tags = "订单管理")
@RequestMapping(value = "businessOrder")
public class BusinessOrderController {

    @Resource
    private BusinessOrderService businessOrderService;

    @GetMapping
    @ApiOperation(value = "查询订单")
    public Resut list(BusinessVo businessVo,Pageable pageable){
        log.info("查询订单");
        return Resut.ok(businessOrderService.list(businessVo,pageable));
    }
}
