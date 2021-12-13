package com.daizhihua.tools.service;

import com.daizhihua.core.entity.QueryVo;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

public interface TableService {

    Map<String,Object> listTables(Pageable pageable, QueryVo queryVo);


    /**
     * 同步表数据
     * @param columnInfos /
     * @param columnInfoList /
     */
//    @Async
//    void sync(List<ColumnInfo> columnInfos, List<ColumnInfo> columnInfoList);
}
