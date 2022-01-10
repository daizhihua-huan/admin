package com.daizhihua.mnt.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.mnt.entity.MntApp;
import com.daizhihua.mnt.service.MntAppService;
import com.daizhihua.mnt.service.MntServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "应用管理")
@RequestMapping(value = "app")
public class MntAppController implements BaseController<MntApp> {

    @Autowired
    private MntAppService mntAppService;

    @GetMapping
    @ApiOperation(value = "查询应用管理")
    @ApiResponses({
            @ApiResponse(code = 200,message = "应用管理列表")
    })
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("分页参数是:{}",pageable);
        log.info("查询参数是:{}",queryVo);
        return Resut.ok(mntAppService.page(pageable,queryVo));
    }

    @ApiOperation(value = "新增应用管理")
    @PostMapping
    @Override
    public Resut add(@RequestBody MntApp mntApp) {
        mntApp.setCreateBy(SecurityUtils.getCurrentUsername());
        mntApp.setUpdateBy(SecurityUtils.getCurrentUsername());
        mntApp.setCreateTime(DateUtils.getDateTime());
        mntApp.setUpdateTime(DateUtils.getDateTime());
        mntAppService.save(mntApp);
        return Resut.ok();
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @DeleteMapping
    @ApiOperation(value = "删除应用管理")
    public Resut delete(@RequestBody List<Long> ids){
        log.info("参数是:{}",ids);
        mntAppService.removeByIds(ids);
        return Resut.ok();
    }

    @ApiOperation(value = "修改应用管理")
    @PutMapping
    @Override
    public Resut update(@RequestBody MntApp mntApp) {
        mntApp.setUpdateTime(DateUtils.getDateTime());
        mntApp.setUpdateBy(SecurityUtils.getCurrentUsername());
        mntAppService.updateById(mntApp);
        return Resut.ok();
    }

    @ApiOperation(value = "导出应用管理")
    @GetMapping(value = "download")
    public void download(Pageable pageable, HttpServletResponse response){
        log.info("导出应用管理");
        mntAppService.download(pageable,response);
    }
}
