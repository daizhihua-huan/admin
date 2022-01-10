package com.daizhihua.tools.service;

import com.daizhihua.tools.entity.CodeColumnConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 代码生成字段信息存储 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-12-06
 */
public interface CodeColumnConfigService extends IService<CodeColumnConfig> {

    List<CodeColumnConfig> query(String tableName);

    List<CodeColumnConfig> getColumnConfigForTableName(String tableName);
}
