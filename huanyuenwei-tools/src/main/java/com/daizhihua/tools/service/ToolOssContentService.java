package com.daizhihua.tools.service;

import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.tools.entity.ToolOssConfig;
import com.daizhihua.tools.entity.ToolOssContent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-15
 */
public interface ToolOssContentService extends IService<ToolOssContent> {

    IPage<ToolOssContent> page(Pageable pageable, QueryVo queryVo);

    ToolOssContent upload(OSS ossClient, ToolOssConfig toolOssConfig, MultipartFile multipartFile);


    ToolOssContent getOssContentByName(String name);

    void delete(OSS ossClinet,ToolOssConfig toolOssConfig, List<Long> ids);

    void download(OSS ossClient,ToolOssConfig toolOssConfig,Long id);

    void sync(OSS ossClient,ToolOssConfig toolOssConfig);

    void download(Pageable pageable, HttpServletResponse response);
}
