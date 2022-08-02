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
    public static final String CONTROLLER = "controller";
    public static final String SERVICE_NAME = "%sService";
    public static final String CONTROLLER_JAVA_VM = "/templates/controller.java.vm" ;

    //项目名称
    public static final String PROJECTNAME = "huanyuenwei-system";
    //包名称
    public static final String PACKAGENAME = "com.daizhihua.system";
    //表名
    public static final String[] TABLENAMES = {"business_order"};

    public static final long EXPIRATION_TIME = 7200;     // 2小时(以毫秒ms计)
    public static final String SECRET = "CodeSheepSecret";      // JWT密码
    public static final String TOKEN_PREFIX = "Bearer";         // Token前缀
    public static final String HEADER_STRING = "Authorization"; // 存放Token的Header Key

    public static final String PASSWORD = "123456789";

    /**
     * 分隔符
     */
    public static final String SEPARATOR = "/";
    /**
     * 减号
     */
    public static final String MINUS = "-";
    /**
     * 点
     */
    public static final String DOT = ".";
    /**
     * 上级目录
     */
    public static final String PARENT_DIRECTORY = "..";

    /** root用户 */
    public static final String USER_ROOT = "root";

    /**
     * 缓存字符长度
     */
    public static final int BUFFER_SIZE = 2048;

    /**
     * 随机生成uuid的key名
     */
    public static final String USER_UUID_KEY = "user_uuid";
    /**
     * 发送指令：连接
     */
    public static final String OPERATE_CONNECT = "connect";
    /**
     * 发送指令：命令
     */
    public static final String OPERATE_COMMAND = "command";
    /**
     * 发送指令：安全文件传送
     */
    public static final String OPERATE_SFTP = "sftp";


    /** 1kb */
    public static final long KB = 1024L;
    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";
    /**
     * win 系统
     */
    public static final String WIN = "win";

    /**
     * mac 系统
     */
    public static final String MAC = "mac";

    /**
     * 常用接口
     */
    public static class Url {
        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

    public static final String TOKEN = "https://open.ys7.com/api/lapp/token/get";

    public static final String LIST = "https://open.ys7.com/api/lapp/device/list";

    public static final String PLAY = "https://open.ys7.com/api/lapp/v2/live/address/get";

    public static final String DETECT = "https://open.ys7.com/api/lapp/intelligence/face/analysis/detect";


    /**
     *获取设备状态信息
     */
    public static final String DEVOICESTATUS = "https://open.ys7.com/api/lapp/device/status/get";

}
