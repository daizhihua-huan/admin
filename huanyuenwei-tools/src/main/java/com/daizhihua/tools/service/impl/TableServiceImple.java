package com.daizhihua.tools.service.impl;

import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.PageUtil;
import com.daizhihua.tools.mapper.TableMapper;
import com.daizhihua.tools.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TableServiceImple implements TableService {
    @Autowired
    private TableMapper tableMapper;

    @Override
    public Map<String, Object> listTables(Pageable pageable, QueryVo queryVo) {
        List<Map> maps = tableMapper.listTable();
        List list = PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(), maps);
        return  PageUtil.toPage(list,maps.size());
    }
}
