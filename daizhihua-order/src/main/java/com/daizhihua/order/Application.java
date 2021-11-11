package com.daizhihua.order;

import com.daizhihua.core.config.ResponseData;
import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages ={"com.daizhihua"} )
@RestController
@EnableMPP
public class Application {


    public static void main(String[] args) {


        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class);
        System.out.println(applicationContext.getBean("menuController"));
    }
    @RequestMapping("/")
    public ResponseData hello(){

        return  new ResponseData(1);
    }

    @RequestMapping("/helloword")
    @PreAuthorize("hasRole('user:list')")
    public ResponseData helloword(){

        return  new ResponseData(1);
    }

    @RequestMapping("/helloword1")
    @PreAuthorize("hasRole('TEST')")
    public ResponseData helloword1(){

        return  new ResponseData(1);
    }

    @PostConstruct
    public void init(){
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}
