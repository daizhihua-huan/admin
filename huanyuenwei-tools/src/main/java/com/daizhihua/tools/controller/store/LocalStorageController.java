package com.daizhihua.tools.controller.store;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.tools.entity.ToolLocalStorage;
import com.daizhihua.tools.service.ToolLocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "localStorage")
@Api(value = "本地存储")
public class LocalStorageController {

    @Autowired
    private ToolLocalStorageService localStorageService;

    @ApiOperation(value = "查询本地存储")
    @GetMapping
    public Resut lisyLocalStroage(Pageable pageable, QueryVo queryVo){
        log.info("分页参数是:{}",pageable);
        log.info("查询参数是:{}",queryVo);

        return Resut.ok(localStorageService.page(pageable,queryVo));
    }


    @Log(value = "删除文件",type = LogActionType.DELETE)
    @ApiOperation(value = "删除本地存储")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){

        return Resut.ok(localStorageService.removeByIds(ids));
    }

    @Log(value = "上传文件",type = LogActionType.ADD)
    @ApiOperation(value = "上传文件")
    @PostMapping
    public Resut dowload(@RequestParam String name, @RequestParam("file") MultipartFile file){
        log.info("name是:{}",name);
        log.info("file是:{}",file.getOriginalFilename());
        localStorageService.create(name,file);
        return Resut.ok();
    }

    @Log(value = "修改图片",type = LogActionType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改图片名称")
    public Resut update(@RequestBody ToolLocalStorage toolLocalStorage){
        toolLocalStorage.setUpdateTime(DateUtils.getDateTime());
        toolLocalStorage.setUpdateBy(SecurityUtils.getCurrentUsername());
        return Resut.ok(localStorageService.updateById(toolLocalStorage));
    }

    @Log(value = "邮件上传图片")
    @PostMapping(value = "pictures")
    @ApiOperation(value = "邮件上传图片")
    public Resut picture(@RequestParam(value = "file") MultipartFile file){
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if(!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))){
            throw new BadRequestException("只能上传图片");
        }

        return Resut.ok( localStorageService.create(file.getOriginalFilename(),file));
    }

    @ApiOperation(value = "导出本地图片")
    @GetMapping(value = "download")
    @Log(value = "导出本地图片")
    public void download(Pageable pageable, HttpServletResponse response){
        localStorageService.download(pageable,response);
    }

}
