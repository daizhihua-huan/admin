package com.daizhihua.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daizhihua.tools.entity.CodeColumnConfig;
import com.daizhihua.tools.mapper.CodeColumnConfigMapper;
import com.daizhihua.tools.mapper.TableMapper;
import com.daizhihua.tools.service.CodeColumnConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 代码生成字段信息存储 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-12-06
 */
@Service
@Slf4j
public class CodeColumnConfigServiceImpl extends ServiceImpl<CodeColumnConfigMapper, CodeColumnConfig> implements CodeColumnConfigService {

    @Autowired
    private CodeColumnConfigMapper codeColumnConfigMapper;

    @Autowired
    private TableMapper tableMapper;

    @Override
    public List<CodeColumnConfig> query(String tableName) {
        List<CodeColumnConfig> columnConfig = this.getColumnConfigForTableName(tableName);
        if(columnConfig==null){
          columnConfig = tableMapper.query(tableName);
          this.saveBatch(columnConfig);
        }
        return columnConfig;
    }

    @Override
    public List<CodeColumnConfig> getColumnConfigForTableName(String tableName) {
        QueryWrapper<CodeColumnConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_name",tableName);
        if(codeColumnConfigMapper.selectList(queryWrapper).size()>0){
            return codeColumnConfigMapper.selectList(queryWrapper);
        }
        return null;
    }


}
