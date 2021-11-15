package com.daizhihua.tools.controller.store;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "oss存储")
@Slf4j
@RestController
@RequestMapping(value = "OssContent")
public class OssController {

    @ApiOperation(value = "获取oss存储")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("查询参数:{}",queryVo);
        log.info("分页参数是:{}",pageable);

        return Resut.ok();
    }

}
