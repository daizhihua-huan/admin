package com.daizhihua.tools.controller;

import cn.hutool.core.util.PageUtil;
import cn.hutool.extra.template.TemplateEngine;
import com.alibaba.druid.sql.PagerUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.config.ResponseData;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.SpringContextUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.tools.config.MyVelocityTemplateEngine;
import com.daizhihua.tools.entity.CodeColumnConfig;
import com.daizhihua.tools.mapper.TableMapper;
import com.daizhihua.tools.service.CodeColumnConfigService;
import com.daizhihua.tools.service.GenerateService;
import com.daizhihua.tools.service.TableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(tags = "代码生成器模块")
@RestController
@RequestMapping(value = "generator")
@Slf4j
public class GentorController {


    @Autowired
    private GenerateService generateService;

    @Autowired
    private CodeColumnConfigService codeColumnConfigService;

    @Autowired
    private TableService tableService;


    @ApiOperation(value = "查询表",notes = "查询数据库表信息")
    @GetMapping(value = "tables")
    public Resut tables(Pageable pageable, QueryVo queryVo){
        log.info("{}",pageable);
        log.info("{}",queryVo);
        Map<String,Object> map = tableService.listTables(pageable,queryVo);
        return Resut.ok(map);
    }

    @ApiOperation(value = "同步",notes = "同步信息")
    @PostMapping(value = "sync")
    public Resut sync(@RequestBody List<String> tables){
        log.info("{}",tables);
        return Resut.ok();
    }

    @ApiOperation(value = "预览,生成，下载")
    @PostMapping(value = "/{tableName}/{type}")
    public Resut codGenConfig(@PathVariable String tableName, @PathVariable Integer type){
        switch (type){
            case 0:
                return Resut.ok(generateService.generate(tableName));
            case 1:
              return  Resut.ok(generateService.privew(tableName));
        }
        return Resut.ok();
    }

    @Log(value = "代码配置")
    @ApiOperation(value = "下载配置代码",notes = "下载代码")
    @GetMapping(value = "dowload")
    public void dowload(String tableName){
        generateService.dowload(tableName);
    }

    @ApiOperation(value = "获取字段名称")
    @GetMapping(value = "columns")
    public Resut columns(Pageable pageable,String tableName){
        log.info("{}",pageable.getPageSize());
        log.info("{}",tableName);
        return Resut.ok(codeColumnConfigService.query(tableName));
    }


    @ApiOperation("保存或者更新字段数据")
    @PutMapping
    public Resut save(@RequestBody List<CodeColumnConfig> columnInfos){
        boolean flag = codeColumnConfigService.saveOrUpdateBatch(columnInfos);
        return Resut.ok(flag);
    }


}
