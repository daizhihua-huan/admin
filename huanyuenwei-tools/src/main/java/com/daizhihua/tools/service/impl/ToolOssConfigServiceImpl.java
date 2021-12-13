package com.daizhihua.tools.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.tools.entity.ToolOssConfig;
import com.daizhihua.tools.mapper.ToolOssConfigMapper;
import com.daizhihua.tools.service.ToolOssConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
@Service
public class ToolOssConfigServiceImpl extends ServiceImpl<ToolOssConfigMapper, ToolOssConfig> implements ToolOssConfigService {



    @Override
    public OSS getOssClient() {
        ToolOssConfig toolOssConfig = getToolOssConfig();
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = toolOssConfig.getEndpoint();
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = toolOssConfig.getAccessKeyId();
        String accessKeySecret = toolOssConfig.getAccessKeySecret();

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }

    /**
     * 获取配置信息
     * @return
     */
    @Override
    public ToolOssConfig getToolOssConfig() {
        ToolOssConfig toolOssConfig = this.getOne(null);
        if(toolOssConfig==null){
            throw new BadRequestException("请先配置oss");
        }
        return toolOssConfig;
    }


}
