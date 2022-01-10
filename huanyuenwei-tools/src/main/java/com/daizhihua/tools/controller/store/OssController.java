package com.daizhihua.tools.controller.store;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.tools.entity.ToolOssConfig;
import com.daizhihua.tools.entity.ToolOssContent;
import com.daizhihua.tools.service.ToolOssConfigService;
import com.daizhihua.tools.service.ToolOssContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "oss存储")
@Slf4j
@RestController
@RequestMapping(value = "ossContent")
public class OssController {

    @Autowired
    private ToolOssContentService toolOssContentService;

    @Autowired
    private ToolOssConfigService toolOssConfigService;


    @ApiOperation(value = "获取oss存储")
    @GetMapping
    public Resut list(Pageable pageable, QueryVo queryVo){
        log.info("查询参数:{}",queryVo);
        log.info("分页参数是:{}",pageable);
        return Resut.ok(toolOssContentService.page(pageable,queryVo));
    }

    @Log(value = "上传文件",type = LogActionType.ADD)
    @ApiOperation(value = "上传文件到oss")
    @PostMapping
    public Resut upload(@RequestParam(value = "file") MultipartFile multipartFile){
        log.info("上传的文件是:{}",multipartFile.getOriginalFilename());
        return Resut.ok(toolOssContentService.upload(toolOssConfigService.getOssClient(),toolOssConfigService.getToolOssConfig(),multipartFile));
    }

    @Log(value = "删除文件",type = LogActionType.DELETE)
    @ApiOperation(value = "删除文件",notes = "删除OSS文件")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
        log.info("参数是:{}",ids);
        toolOssContentService.delete(toolOssConfigService.getOssClient(),toolOssConfigService.getToolOssConfig(),ids);
        return Resut.ok();
    }

    @Log(value = "修改文件",type = LogActionType.UPDATE)
    @ApiOperation(value = "修改文件",notes = "修改配置文件")
    @PutMapping
    public Resut update(@RequestBody ToolOssConfig toolOssConfig){
        log.info("修改的参数是:{}",toolOssConfig);
        if(toolOssConfig.getAccessKeyId()==null){
            toolOssConfigService.save(toolOssConfig);
        }else{
            toolOssConfigService.updateById(toolOssConfig);
        }
        return Resut.ok();
    }

    @Log(value = "下载文件")
    @ApiOperation(value = "下载文件")
    @GetMapping(value = "downloadFile")
    public void dowloadFile(Long id){

        toolOssContentService.download(toolOssConfigService.getOssClient(),toolOssConfigService.getToolOssConfig(),id);
//        return Resut.ok();
    }

    @Log(value = "同步oss")
    @ApiOperation(value = "同步",notes = "同步oss信息")
    @GetMapping(value = "/sync")
    public Resut sync(){
        toolOssContentService.sync(toolOssConfigService.getOssClient(),toolOssConfigService.getToolOssConfig());
        return Resut.ok();
    }


    @GetMapping(value = "getConfig")
    @ApiOperation(value = "获取配置",notes = "获取oss云配置")
    public Resut getConfig(){

        return Resut.ok(toolOssConfigService.getOne(null));
    }

    @Log(value = "导出oss存储")
    @GetMapping(value = "download")
    @ApiOperation(value = "导出oss存储")
    public void download(Pageable pageable, HttpServletResponse response){
        log.info("导出oss存储");
//        toolOssContentService.page(pageable);
        toolOssContentService.download(pageable,response);
    }

}
