package com.daizhihua.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 常量类
 */
public class Constant {


    //代码生成器的名字
    public static final String  AUTOGENERATOR = "autoGenerator";


    //全局配置的名字
    public static final String CONFIG = "config";
    //数据库配置的名字
    public static final String DSCONFIG = "dsConfig";

    //策略配置名字
    public static final String STCONFIG = "stConfig";

    //包配置的名字
    public static final String PKCONFIG = "pkConfig";

    //模板引擎配置
    public static final String TCONFIG = "tconfig";

    public static  String PATH = "";
    public static final String SERVICE = "service";
    public static final String ENTITY = "entity";
    public static final String MAPPER = "mapper";
    public static final String XML = "mapper";
    public static final String CONTROLLER = "controllers";
    public static final String SERVICE_NAME = "%sService";
    public static final String CONTROLLER_JAVA_VM = "/templates/controller.java.vm" ;

    //项目名称
    public static final String PROJECTNAME = "daizhihua-tools";
    //包名称
    public static final String PACKAGENAME = "com.daizhihua.tools";
    //表名
    public static final String[] TABLENAMES = {"sys_quartz_log"};

    public static final long EXPIRATION_TIME = 7200;     // 2小时(以毫秒ms计)
    public static final String SECRET = "CodeSheepSecret";      // JWT密码
    public static final String TOKEN_PREFIX = "Bearer";         // Token前缀
    public static final String HEADER_STRING = "Authorization"; // 存放Token的Header Key

    public static final String PASSWORD = "123456789";

}
