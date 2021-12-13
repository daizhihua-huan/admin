package com.daizhihua.tools.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.daizhihua.tools.entity.ToolOssConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
public interface ToolOssConfigService extends IService<ToolOssConfig> {

    OSS getOssClient();

    ToolOssConfig getToolOssConfig();

}
