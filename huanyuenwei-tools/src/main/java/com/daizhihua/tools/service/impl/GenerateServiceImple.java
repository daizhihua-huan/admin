package com.daizhihua.tools.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.SpringContextUtils;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.tools.config.MyAutoGenerator;
import com.daizhihua.tools.config.MyVelocityTemplateEngine;
import com.daizhihua.tools.entity.CodeColumnConfig;
import com.daizhihua.tools.entity.CodeGenConfig;
import com.daizhihua.tools.service.CodeColumnConfigService;
import com.daizhihua.tools.service.CodeGenConfigService;
import com.daizhihua.tools.service.GenerateService;
import com.daizhihua.tools.util.ColUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.daizhihua.core.config.Constant.*;
import static com.daizhihua.core.util.FileUtil.SYS_TEM_DIR;

/**
 * 代码生成的逻辑操作类
 * @AUTO daizihua
 * @date 2021-12-03
 */
@Service
@Data
@Slf4j
public class GenerateServiceImple implements GenerateService {

    private static final String TIMESTAMP = "Timestamp";

    private static final String BIGDECIMAL = "BigDecimal";

    public static final String PK = "PRI";

    public static final String EXTRA = "auto_increment";

    @Autowired
    private CodeGenConfigService codeGenConfigService;

    @Autowired
    private CodeColumnConfigService codeColumnConfigService;

    @Autowired
    private MyAutoGenerator autoGenerator;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    // 获取模版数据
    @Override
    public  Map<String, Object> getGenMap(List<CodeColumnConfig> columnInfos, CodeGenConfig genConfig) {
        // 存储模版字段数据
        Map<String, Object> genMap = new HashMap<>(16);
        // 接口别名
        genMap.put("apiAlias", genConfig.getApiAlias());
        // 包名称
        genMap.put("package", genConfig.getPack());
        // 模块名称
        genMap.put("moduleName", genConfig.getModuleName());
        // 作者
        genMap.put("author", genConfig.getAuthor());
        // 创建日期
        genMap.put("date", LocalDate.now().toString());
        // 表名
        genMap.put("tableName", genConfig.getTableName());
        // 大写开头的类名
        String className = StringUtils.toCapitalizeCamelCase(genConfig.getTableName());
        // 小写开头的类名
        String changeClassName = StringUtils.toCamelCase(genConfig.getTableName());
        // 判断是否去除表前缀
        if (StringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = StringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(genConfig.getTableName(), genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StrUtil.removePrefix(genConfig.getTableName(), genConfig.getPrefix()));
        }
        // 保存类名
        genMap.put("className", className);
        // 保存小写开头的类名
        genMap.put("changeClassName", changeClassName);
        // 存在 Timestamp 字段
        genMap.put("hasTimestamp", false);
        // 查询类中存在 Timestamp 字段
        genMap.put("queryHasTimestamp", false);
        // 存在 BigDecimal 字段
        genMap.put("hasBigDecimal", false);
        // 查询类中存在 BigDecimal 字段
        genMap.put("queryHasBigDecimal", false);
        // 是否需要创建查询
        genMap.put("hasQuery", false);
        // 自增主键
        genMap.put("auto", false);
        // 存在字典
        genMap.put("hasDict", false);
        // 存在日期注解
        genMap.put("hasDateAnnotation", false);
        // 保存字段信息
        List<Map<String, Object>> columns = new ArrayList<>();
        // 保存查询字段的信息
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        // 存储字典信息
        List<String> dicts = new ArrayList<>();
        // 存储 between 信息
        List<Map<String, Object>> betweens = new ArrayList<>();
        // 存储不为空的字段信息
        List<Map<String, Object>> isNotNullColumns = new ArrayList<>();

        for (CodeColumnConfig column : columnInfos) {
            Map<String, Object> listMap = new HashMap<>(16);
            // 字段描述
            listMap.put("remark", column.getRemark());
            // 字段类型
            listMap.put("columnKey", column.getKeyType());
            // 主键类型
            String colType = ColUtil.cloToJava(column.getColumnType());
            // 小写开头的字段名
            String changeColumnName = StringUtils.toCamelCase(column.getColumnName());
            // 大写开头的字段名
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(column.getColumnName());
            if (PK.equals(column.getKeyType())) {
                // 存储主键类型
                genMap.put("pkColumnType", colType);
                // 存储小写开头的字段名
                genMap.put("pkChangeColName", changeColumnName);
                // 存储大写开头的字段名
                genMap.put("pkCapitalColName", capitalColumnName);
            }
            // 是否存在 Timestamp 类型的字段
            if (TIMESTAMP.equals(colType)) {
                genMap.put("hasTimestamp", true);
            }
            // 是否存在 BigDecimal 类型的字段
            if (BIGDECIMAL.equals(colType)) {
                genMap.put("hasBigDecimal", true);
            }
            // 主键是否自增
            if (EXTRA.equals(column.getExtra())) {
                genMap.put("auto", true);
            }
            // 主键存在字典
            if (StringUtils.isNotBlank(column.getDictName())) {
                genMap.put("hasDict", true);
                dicts.add(column.getDictName());
            }

            // 存储字段类型
            listMap.put("columnType", colType);
            // 存储字原始段名称
            listMap.put("columnName", column.getColumnName());
            // 不为空
            listMap.put("istNotNull", column.getNotNull());
            // 字段列表显示
            listMap.put("columnShow", column.getListShow());
            // 表单显示
            listMap.put("formShow", column.getFormShow());
            // 表单组件类型
            listMap.put("formType", StringUtils.isNotBlank(column.getFormType()) ? column.getFormType() : "Input");
            // 小写开头的字段名称
            listMap.put("changeColumnName", changeColumnName);
            //大写开头的字段名称
            listMap.put("capitalColumnName", capitalColumnName);
            // 字典名称
            listMap.put("dictName", column.getDictName());
            listMap.put("column_has_next",true);
            if(columnInfos.size()-1==columnInfos.indexOf(column)){
                listMap.put("column_has_next",false);
            }
            // 日期注解
            listMap.put("dateAnnotation", column.getDateAnnotation());
            if (StringUtils.isNotBlank(column.getDateAnnotation())) {
                genMap.put("hasDateAnnotation", true);
            }
            // 添加非空字段信息
            if (column.getNotNull()) {
                isNotNullColumns.add(listMap);
            }
            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if (!StringUtils.isBlank(column.getQueryType())) {
                // 查询类型
                listMap.put("queryType", column.getQueryType());
                // 是否存在查询
                genMap.put("hasQuery", true);
                if (TIMESTAMP.equals(colType)) {
                    // 查询中存储 Timestamp 类型
                    genMap.put("queryHasTimestamp", true);
                }
                if (BIGDECIMAL.equals(colType)) {
                    // 查询中存储 BigDecimal 类型
                    genMap.put("queryHasBigDecimal", true);
                }
                if ("between".equalsIgnoreCase(column.getQueryType())) {
                    betweens.add(listMap);
                } else {
                    // 添加到查询列表中
                    queryColumns.add(listMap);
                }
            }
            // 添加到字段列表中
            columns.add(listMap);
        }
        // 保存字段列表

        genMap.put("columns", columns);
        // 保存查询列表
        genMap.put("queryColumns", queryColumns);
        // 保存字段列表
        genMap.put("dicts", dicts);
        // 保存查询列表
        genMap.put("betweens", betweens);
        // 保存非空字段信息
        genMap.put("isNotNullColumns", isNotNullColumns);
        return genMap;
    }

    @Override
    public List<Map<String, Object>> privew(String tableName) {
        CodeGenConfig codeGenConfig = codeGenConfigService.getGenConfigForTableName(tableName);
        List<CodeColumnConfig> columnConfigs = codeColumnConfigService.query(tableName);
        MyVelocityTemplateEngine templateEngie = this.getVelocityTemplateEngie(codeGenConfig, columnConfigs, tableName);
        return templateEngie.getList();
    }

    /**
     * 代码生成
     * @param tableName
     * @return
     */
    @Override
    public Boolean generate(String tableName) {
        CodeGenConfig genConfigForTableName = codeGenConfigService.getGenConfigForTableName(tableName);
        List<CodeColumnConfig> columnConfigs = codeColumnConfigService.query(tableName);
        exe(genConfigForTableName,columnConfigs,tableName);
        return true;
    }

    @Override
    public void dowload(String tableName) {
        CodeGenConfig genConfigForTableName = codeGenConfigService.getGenConfigForTableName(tableName);
        List<CodeColumnConfig> columnConfigs = codeColumnConfigService.query(tableName);
        String dowload = this.dowload(genConfigForTableName, columnConfigs, tableName);
        File file = new File(dowload);
        String zipPath = file.getPath() + ".zip";
        log.info(file.getPath());
        ZipUtil.zip(file.getPath(), zipPath);
        FileUtil.downloadFile(request, response, new File(zipPath), true);
    }

    private String dowload(CodeGenConfig codeGenConfig ,
                           List<CodeColumnConfig> columnConfigs,
                           String tableName){
        this.setMap(codeGenConfig,columnConfigs);
        GlobalConfig config = (GlobalConfig) SpringContextUtils.getBean(Constant.CONFIG);
        String tempPath = SYS_TEM_DIR + "dadmin-gen-temp" + File.separator + tableName + File.separator;
        config.setActiveRecord(true) // 是否支持AR模式
                .setOpen(false)
                .setAuthor(codeGenConfig.getAuthor()) // 作者
                // "\\src\\main\\java"
                .setOutputDir(tempPath) // 生成路径，需要修改
                .setFileOverride(true)  // 文件覆盖
                .setIdType(IdType.ASSIGN_UUID) // 主键策略
                .setServiceName(SERVICE_NAME)  // 设置生成的service接口的名字的首字母是否为I
                .setSwagger2(true) //开启注解配置
                .setBaseResultMap(true)//生成基本的resultMap
                .setBaseColumnList(true);//生成基本的SQL片段
        StrategyConfig strategyConfig = (StrategyConfig) SpringContextUtils.getBean(Constant.STCONFIG);
        strategyConfig.setInclude(tableName);
        PackageConfig packageConfig = (PackageConfig) SpringContextUtils.getBean(PKCONFIG);
        packageConfig.setParent(codeGenConfig.getPack());
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setTemplateEngine(null);
        autoGenerator.setGlobalConfig(config);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.setConfig();
        autoGenerator.execute();
        return tempPath;

    }


    /**
     * 获取自定义的代码生成器
     * @param codeGenConfig
     * @param columnConfigs
     * @param tableName
     * @return
     */
    private MyVelocityTemplateEngine getVelocityTemplateEngie(CodeGenConfig codeGenConfig ,
                                                              List<CodeColumnConfig> columnConfigs,
                                                              String tableName){
        Map<String, Object> genMap = this.getGenMap(columnConfigs, codeGenConfig);
        autoGenerator = (MyAutoGenerator) SpringContextUtils.getBean("autoGenerator");
        StrategyConfig strategyConfig = (StrategyConfig) SpringContextUtils.getBean(Constant.STCONFIG);
        strategyConfig.setInclude(tableName);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setConfig();
        InjectionConfig cfg = autoGenerator.getCfg();
        cfg.setMap(genMap);
        autoGenerator.setTemplateEngine(new MyVelocityTemplateEngine());
        autoGenerator.setCfg(cfg);
        autoGenerator.execute();
        MyVelocityTemplateEngine templateEngine = (MyVelocityTemplateEngine) autoGenerator.getTemplateEngine();
        return templateEngine;
    }

    private void exe(CodeGenConfig codeGenConfig ,
                      List<CodeColumnConfig> columnConfigs,
                      String tableName){
        GlobalConfig config = (GlobalConfig) SpringContextUtils.getBean(Constant.CONFIG);
        this.setMap(codeGenConfig,columnConfigs);
        config.setActiveRecord(true) // 是否支持AR模式
                .setOpen(false)
                .setAuthor(codeGenConfig.getAuthor()) // 作者
                .setOutputDir(PATH + "\\" + codeGenConfig.getModuleName() + "\\src\\main\\java") // 生成路径，需要修改
                .setFileOverride(true)  // 文件覆盖
                .setIdType(IdType.ASSIGN_UUID) // 主键策略
                .setServiceName(SERVICE_NAME)  // 设置生成的service接口的名字的首字母是否为I
                .setSwagger2(true) //开启注解配置
                .setBaseResultMap(true)//生成基本的resultMap
                .setBaseColumnList(true);//生成基本的SQL片段
        StrategyConfig strategyConfig = (StrategyConfig) SpringContextUtils.getBean(Constant.STCONFIG);
        strategyConfig.setInclude(tableName);
        PackageConfig packageConfig = (PackageConfig) SpringContextUtils.getBean(PKCONFIG);
        packageConfig.setParent(codeGenConfig.getPack());
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setGlobalConfig(config);
        autoGenerator.setPackageInfo(packageConfig);

        autoGenerator.execute();
    }


    private void setMap(CodeGenConfig codeGenConfig ,
                        List<CodeColumnConfig> columnConfigs){
        Map<String, Object> genMap = this.getGenMap(columnConfigs, codeGenConfig);
        InjectionConfig cfg = autoGenerator.getCfg();
        cfg.setMap(genMap);
        autoGenerator.setCfg(cfg);
    }


}
