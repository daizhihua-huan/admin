package com.daizhihua.tools.controller.store;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.tools.entity.ToolQiniuConfig;
import com.daizhihua.tools.entity.ToolQiniuContent;
import com.daizhihua.tools.service.ToolQiniuConfigService;
import com.daizhihua.tools.service.ToolQiniuContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Api(value = "七牛云")
@Slf4j
@RequestMapping(value = "qiNiuContent")
public class QiNiuController {

    @Autowired
    private ToolQiniuContentService toolQiniuContentService;

    @Autowired
    private ToolQiniuConfigService toolQiniuConfigService;

    @ApiOperation(value = "查询七牛云")
    @GetMapping
    public Resut listQiNiu(Pageable pageable, QueryVo queryVo){
        log.info("{}",pageable);
        log.info("{}",queryVo);


        return Resut.ok(toolQiniuContentService.page(pageable,queryVo));
    }

    @Log(value = "查询七牛云配置",type = LogActionType.SELECT)
    @ApiOperation(value = "查询七牛云存储配置")
    @GetMapping(value = "config")
    public Resut config(){
        List<ToolQiniuConfig> list = toolQiniuConfigService.list();
        if(list!=null&&list.size()>0){
            return Resut.ok(list.get(0));
        }
        return Resut.ok();
    }

    @Log(value = "修改七牛云配置",type = LogActionType.UPDATE)
    @ApiOperation(value = "修改七牛云配置")
    @PutMapping(value = "config")
    public Resut putConfig(@RequestBody ToolQiniuConfig toolQiniuConfig){
        log.info("修改的参数是:{}",toolQiniuConfig);
        if(toolQiniuConfig.getConfigId()==null){
            return Resut.ok(toolQiniuConfigService.save(toolQiniuConfig));
        }
        return Resut.ok( toolQiniuConfigService.updateById(toolQiniuConfig));
    }

    @Log(value = "七牛云上传",type = LogActionType.ADD)
    @ApiOperation(value = "上传文件",notes = "上传文件到七牛云")
    @PostMapping
    public Resut upload(@RequestParam(value = "file")  MultipartFile file){
        log.info("file是:{}",file.getOriginalFilename());
        try {
            toolQiniuContentService.upload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Resut.ok();
    }

    @Log(value = "七牛云同步")
    @ApiOperation(value = "同步",notes = "同步七牛云的文件")
    @PostMapping(value = "synchronize")
    public Resut synchronize(){
        toolQiniuContentService.synchronize();
        return Resut.ok();
    }

    @Log(value = "删除七牛云",type = LogActionType.DELETE)
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids){
//        toolQiniuContentService
        toolQiniuContentService.delete(ids);
        return Resut.ok();
    }
}
