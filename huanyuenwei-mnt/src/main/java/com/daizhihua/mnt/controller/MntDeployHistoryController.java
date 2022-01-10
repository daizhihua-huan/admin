package com.daizhihua.mnt.controller;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.mnt.service.MntDeployHistoryService;
import com.daizhihua.mnt.service.MntDeployServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "备份管理")
@RestController
@RequestMapping(value = "deployHistory")
@Slf4j
public class MntDeployHistoryController {

    @Autowired
    private MntDeployHistoryService mntDeployHistoryService;

    @GetMapping
    @ApiOperation(value = "查询部署备份信息")
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("参数是:{}",pageable);
        log.info("分页参数是:{}",queryVo);
        return Resut.ok(mntDeployHistoryService.page(pageable,queryVo));
    }

    @ApiOperation(value = "删除部署备份的信息")
    @DeleteMapping
    public Resut delete(@RequestBody List<String> ids){
        log.info("id参数是:{}",ids);
        mntDeployHistoryService.removeByIds(ids);
        return Resut.ok();
    }

    @GetMapping(value = "download")
    @ApiOperation(value = "导出部署备份信息")
    public void download(Pageable pageable, HttpServletResponse response){
        mntDeployHistoryService.download(pageable,response);
    }
}
