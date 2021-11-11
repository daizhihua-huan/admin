package com.daizhihua.core.controllers;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.config.ResponseData;
import com.daizhihua.core.util.SpringContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "代码生成器模块")
@RestController
@RequestMapping(value = "genrator")
public class Genrator {



    private AutoGenerator autoGenerator;

    @ApiOperation(value = "代码生成",notes = "根据表明生成增删改查的代码")
    @RequestMapping(value = "generationCode",method = RequestMethod.POST)
    @ApiImplicitParam(name = "tableNames",value = "数据库表的名字",required = true)
    public ResponseData generationCode (@RequestBody String[] tableNames){
        System.out.println(tableNames);
        autoGenerator = (AutoGenerator) SpringContextUtils.getBean("autoGenerator");
        StrategyConfig strategyConfig = (StrategyConfig) SpringContextUtils.getBean(Constant.STCONFIG);
        strategyConfig.setInclude(tableNames);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.execute();
        return new ResponseData(1);
    }


}
