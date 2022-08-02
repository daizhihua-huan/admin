package com.daizhihua.core;




import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootApplication
@MapperScan({"com.daizhihua.core.mapper",
        "com.daizhihua.manager.mapper",
        "com.daizhihua.tools.mapper",
        "com.daizhihua.log.mapper",
        "com.daizhihua.mnt.mapper",
        "com.daizhihua.system.mapper"
})
public class MpGenerator {


}