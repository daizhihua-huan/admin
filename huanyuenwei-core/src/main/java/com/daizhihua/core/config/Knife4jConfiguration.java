package com.daizhihua.core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import springfox.documentation.service.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Knife4j的配置类
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        List<Parameter> params=new ArrayList<>();
        Parameter header= (Parameter) new ParameterBuilder().name("header_name").description("请求头")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).defaultValue("gtlx").build();
        params.add(header);
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //.title("swagger-bootstrap-ui-demo RESTful APIs")
                        .description("# 快速开发框架 RESTful APIs")
                        .termsOfServiceUrl("http://www.daizhihua.com/")
                        .contact("代志华")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("1.0版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.daizhihua"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(params)
                ;
        return docket;
    }
}
