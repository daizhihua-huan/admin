package com.daizhihua.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daizhihua.tools.entity.CodeGenConfig;
import com.daizhihua.tools.mapper.CodeGenConfigMapper;
import com.daizhihua.tools.service.CodeGenConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 代码生成器配置 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-12-03
 */
@Data
@Service
@Slf4j
public class CodeGenConfigServiceImpl extends ServiceImpl<CodeGenConfigMapper, CodeGenConfig> implements CodeGenConfigService {

    @Autowired
    private CodeGenConfigMapper codeGenConfigMapper;

    @Override
    public CodeGenConfig getGenConfigForTableName(String tableName) {
        QueryWrapper<CodeGenConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_name",tableName);
        List<CodeGenConfig> codeGenConfigs = codeGenConfigMapper.selectList(queryWrapper);
        if(codeGenConfigs.size()>0){
            return codeGenConfigs.get(0);
        }
        CodeGenConfig codeGenConfig = new CodeGenConfig();
        codeGenConfig.setTableName(tableName);
        codeGenConfigMapper.insert(codeGenConfig);
        return codeGenConfig;
    }
}
