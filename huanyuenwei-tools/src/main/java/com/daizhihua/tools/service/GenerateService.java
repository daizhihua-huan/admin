package com.daizhihua.tools.service;

import com.daizhihua.tools.entity.CodeColumnConfig;
import com.daizhihua.tools.entity.CodeGenConfig;

import java.util.List;
import java.util.Map;

public interface GenerateService {

    Map<String,Object> getGenMap(List<CodeColumnConfig> columnInfos, CodeGenConfig genConfig);

    /**
     * 预览
     * @param tableName
     * @return
     */
    List<Map<String,Object>> privew(String tableName);

    /**
     * 生成
     * @param tableName
     * @return
     */
    Boolean generate(String tableName);


    /**
     * 下载
     * @param tableName
     */
    void dowload(String tableName);
}
