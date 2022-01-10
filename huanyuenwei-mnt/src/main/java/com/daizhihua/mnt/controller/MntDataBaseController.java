package com.daizhihua.mnt.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.mnt.entity.MntDatabase;
import com.daizhihua.mnt.service.MntDatabaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Api(tags = "数据库管理")
@RestController
@RequestMapping(value = "database")
@Slf4j
public class MntDataBaseController implements BaseController<MntDatabase> {

    @Autowired
    private MntDatabaseService mntDatabaseService;

    @GetMapping
    @ApiOperation(value = "查询")
    public Resut list(Pageable pageable, QueryVo queryVo){

        return Resut.ok(mntDatabaseService.page(pageable,queryVo));
    }

    @ApiOperation(value = "新增")
    @Log(value = "新增数据库",type = LogActionType.ADD)
    @PostMapping
    @Override
    public Resut add(@RequestBody MntDatabase mntDatabase) {
        mntDatabase.setCreateBy(SecurityUtils.getCurrentUsername());
        mntDatabase.setUpdateBy(SecurityUtils.getCurrentUsername());
        mntDatabase.setCreateTime(DateUtils.getDateTime());
        mntDatabase.setUpdateTime(DateUtils.getDateTime());
        mntDatabase.setDbId(UUID.randomUUID().toString());
        return Resut.ok(mntDatabaseService.save(mntDatabase));
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @Log(value = "删除数据库",type = LogActionType.DELETE)
    @DeleteMapping
    @ApiOperation(value = "删除",notes = "删除数据库")
    public Resut delete(@RequestBody List<String> ids){
        log.info("删除的参数是:{}",ids);
//        mntDatabaseService.removeByIds(ids);
        for (String id : ids) {
            mntDatabaseService.removeById(id);
        }
        return Resut.ok();
    }

    @Log(value = "修改数据信息",type = LogActionType.UPDATE)
    @ApiOperation(value = "修改数据库")
    @PutMapping
    @Override
    public Resut update(@RequestBody MntDatabase mntDatabase) {
        mntDatabase.setUpdateTime(DateUtils.getDateTime());
        mntDatabase.setUpdateBy(SecurityUtils.getCurrentUsername());
        return Resut.ok(mntDatabaseService.updateById(mntDatabase));
    }

    @Log(value = "测试连接",type = LogActionType.SELECT)
    @PostMapping(value = "testConnect")
    @ApiOperation(value = "数据库连接",notes = "数据库连接测试")
    public Resut testContent(@RequestBody MntDatabase mntDatabase){
        log.info("参数是:{}",mntDatabase);
        return Resut.ok(mntDatabaseService.testConnect(mntDatabase));
    }


    @Log(value = "执行sql")
    @PostMapping(value = "runSql")
    @ApiOperation(value = "执行sql")
    public Resut runSql(@RequestBody MntDatabase mntDatabase){
        log.info("参数是:{}",mntDatabase);
        return Resut.ok(mntDatabaseService.runSql(mntDatabase));
    }

    @Log(value = "执行sql脚本",type = LogActionType.ADD)
    @PostMapping(value = "upload")
    @ApiOperation(value = "执行sql脚本",notes = "sql脚本")
    public Resut upload(MultipartFile file,MntDatabase database){
        log.info("{}",file.getOriginalFilename());
        log.info("参数:{}",database);
        return Resut.ok();
    }

    @GetMapping(value = "download")
    @ApiOperation(value = "导出数据库管理")
    public void download(Pageable pageable, HttpServletResponse response){
        mntDatabaseService.download(pageable,response);
    }


}
