package com.daizhihua.core.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.daizhihua.core.config.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

import static com.daizhihua.core.config.Constant.*;

@Component
@Slf4j
public class MyBatisPlusGenerator implements BeanPostProcessor,BeanFactoryPostProcessor {

    @Value("${server.port}")
    private String url;
    @Autowired
    DataSourceProperties dataSourceProperties;

    private ConfigurableListableBeanFactory configurableListableBeanFactory;

//    @Autowired
//    private Constant dataSouce;

    private  String COM_MYSQL_JDBC_DRIVER = "";
    private  String URL = "";
    private  String USERNAME = "";
    private  String PASSWORD = "";
    private static final String AUTHOR = "代志华";
    //需要生成在什么地方


    public static void main(String[] args) throws FileNotFoundException {
        PATH = System.getProperty("user.dir");
        System.out.println(PATH);
        //1. 全局配置
        GlobalConfig config =  new MyBatisPlusGenerator().getGlobalConfig();

        //2. 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setDbType(DbType.MYSQL)  // 设置数据库类型
                .setDriverName("com.mysql.jdbc.Driver").setUrl("jdbc:mysql://39.104.177.194:3306/huang90cnm?useUnicode=true&characterEncoding=utf-8&useSSL=false")
                .setUsername("daizhihua")
                .setPassword("daizhihua1996");

        //3. 策略配置globalConfiguration中
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true) //全局大写命名
                .setEntityLombokModel(true)
                //.setDbColumnUnderline(true)  // 指定表名 字段名是否使用下划线
                .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                //.setTablePrefix("tbl_")

                .setInclude(TABLENAMES);// 生成的表，需要修改

        //4. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent(PACKAGENAME).setService(SERVICE)//servcie
                .setEntity(ENTITY)//entity
        .setMapper(MAPPER)
       .setController(CONTROLLER);//controller
//        .setXml(XML);//mapper.xml*/

        //5. 整合配置
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(config).
                setDataSource(dsConfig).
                setStrategy(stConfig)
                .setPackageInfo(pkConfig);

        TemplateConfig tc = new TemplateConfig();

        tc.setController(CONTROLLER_JAVA_VM);
        tc.setController("");
        tc.setXml("");
//        tc.setService("");
//        tc.setServiceImpl("");
//        tc.setMapper(MAPPER);
        ag.setTemplate(tc);


        //6. 执行
        ag.execute();


    }



    public  GlobalConfig getGlobalConfig(){
        PATH = System.getProperty("user.dir");
        System.out.println(PATH);
        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true) // 是否支持AR模式
                .setOpen(false)
                .setAuthor(AUTHOR) // 作者
                .setOutputDir(PATH + "\\" + PROJECTNAME + "\\src\\main\\java") // 生成路径，需要修改
                .setFileOverride(true)  // 文件覆盖
                .setIdType(IdType.ASSIGN_UUID) // 主键策略
                .setServiceName(SERVICE_NAME)  // 设置生成的service接口的名字的首字母是否为I
                .setSwagger2(true) //开启注解配置
                .setBaseResultMap(true)//生成基本的resultMap
                .setBaseColumnList(true);//生成基本的SQL片段
        return config;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties")){
            DataSourceProperties  dataSourceProperties = (DataSourceProperties) bean;
            URL = dataSourceProperties.getUrl();
            USERNAME = dataSourceProperties.getUsername();
            PASSWORD = dataSourceProperties.getPassword();
            COM_MYSQL_JDBC_DRIVER = dataSourceProperties.getDriverClassName();

            GlobalConfig config = getGlobalConfig();
            this.configurableListableBeanFactory.registerSingleton(CONFIG,config);
            //数据库配置
            DataSourceConfig dsConfig = new DataSourceConfig();
            dsConfig.setDbType(DbType.MYSQL)  // 设置数据库类型
                    .setDriverName(COM_MYSQL_JDBC_DRIVER).setUrl(URL)
                    .setUsername(USERNAME)
                    .setPassword(PASSWORD);
            this.configurableListableBeanFactory.registerSingleton(DSCONFIG,dsConfig);
            //3. 策略配置globalConfiguration中
            StrategyConfig stConfig = new StrategyConfig();
            stConfig.setCapitalMode(true) //全局大写命名
                    .setEntityLombokModel(true)
                    .setEntityTableFieldAnnotationEnable(true)
                    //.setDbColumnUnderline(true)  // 指定表名 字段名是否使用下划线
                    .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                    //.setTablePrefix("tbl_")
                    .setInclude(TABLENAMES);// 生成的表，需要修改

            this.configurableListableBeanFactory.registerSingleton(STCONFIG,stConfig);
            //        //4. 包名策略配置
            PackageConfig pkConfig = new PackageConfig();
            pkConfig.setParent(PACKAGENAME).setService(SERVICE)//servcie
                    .setEntity(ENTITY)//entity
                    .setMapper(MAPPER)
                    .setController(CONTROLLER);//controller
            //        .setXml(XML);//mapper.xml*/
            this.configurableListableBeanFactory.registerSingleton(PKCONFIG,pkConfig);
            //5. 整合配置
            AutoGenerator ag = new AutoGenerator();
            ag.setGlobalConfig(config).
                    setDataSource(dsConfig).
                    setStrategy(stConfig)
                    .setPackageInfo(pkConfig);
            //模板引擎的配置
            TemplateConfig tc = new TemplateConfig();
            tc.setController(CONTROLLER_JAVA_VM);
//            tc.setController("");
            tc.setXml("");
//                    tc.setService("");
//                    tc.setServiceImpl("");
                    tc.setMapper("");
            ag.setTemplate(tc);

            this.configurableListableBeanFactory.registerSingleton(AUTOGENERATOR,ag);
            this.configurableListableBeanFactory.registerSingleton(TCONFIG,tc);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.configurableListableBeanFactory = beanFactory;
//        GenericBeanDefinition bd = new GenericBeanDefinition();
//        bd.setBeanClass(AutoGenerator.class);
//        ((DefaultListableBeanFactory) beanFactory)
//                .registerBeanDefinition("autoGenerator", bd);
    }


//    @Override
//    public void afterPropertiesSet() {
//        log.info("获取的url是:{}",url);
//        System.out.println(dataSouce.getPassword());
//        System.out.println("@PostConstruct");
//    }
}
