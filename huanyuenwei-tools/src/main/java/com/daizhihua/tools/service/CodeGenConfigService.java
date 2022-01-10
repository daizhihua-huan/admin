package com.daizhihua.tools.service;

import com.daizhihua.tools.entity.CodeGenConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 代码生成器配置 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-12-03
 */
public interface CodeGenConfigService extends IService<CodeGenConfig> {
    CodeGenConfig getGenConfigForTableName(String tableName);
}
