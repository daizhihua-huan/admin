package com.daizhihua.tools.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.*;

public class MyVelocityTemplateEngine  extends AbstractTemplateEngine {

    private static final String DOT_VM = ".vm";
    private VelocityEngine velocityEngine;

    private String API = "api";

    private String INDEX = "index";

    private List<Map<String,Object>> list;

    @Override
    public MyVelocityTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOAD_PATH_KEY, ConstVal.VM_LOAD_PATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
            velocityEngine = new VelocityEngine(p);
        }
        list = new ArrayList<>();
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isBlank(templatePath)) {
            return;
        }
        logger.info("路径是:{}",templatePath);
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
         //实例化一个StringWrite
         StringWriter stringWriter = new StringWriter();
         template.merge(new VelocityContext(objectMap),stringWriter);
//             template.merge(new VelocityContext(objectMap), writer);
         Map<String,Object> map = new HashMap<>();
         if(templatePath.contains(API)){
             map.put("name",API);
             map.put("content",stringWriter.toString());
         }
         if(templatePath.contains(INDEX)){
             map.put("name",INDEX);
             map.put("content",stringWriter.toString());
         }
         if(templatePath.contains(ConstVal.TEMPLATE_ENTITY_JAVA)){
             map.put("name",ConstVal.ENTITY);
             map.put("content",stringWriter.toString());
         }
         if(templatePath.contains(ConstVal.TEMPLATE_MAPPER)){
             map.put("name",ConstVal.MAPPER);
             map.put("content",stringWriter.toString());
         }
         if(templatePath.contains(ConstVal.TEMPLATE_SERVICE)){
             map.put("name",ConstVal.SERVICE);
             map.put("content",stringWriter.toString());
         }
         if(templatePath.contains(ConstVal.TEMPLATE_SERVICE_IMPL)){
             map.put("name",ConstVal.SERVICE_IMPL);
             map.put("content",stringWriter.toString());
         }
         if(templatePath.contains(ConstVal.TEMPLATE_CONTROLLER)){
             map.put("name",ConstVal.CONTROLLER);
             map.put("content",stringWriter.toString());
         }
         list.add(map);
    }

    public List<Map<String,Object>> getList(){
        return this.list;
    }

    @Override
    public String templateFilePath(String filePath) {
        if (null == filePath || filePath.contains(DOT_VM)) {
            return filePath;
        }
        return filePath + DOT_VM;
    }
}
