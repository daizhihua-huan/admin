package com.daizhihua.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.tools.entity.ToolQiniuContent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 七牛云文件存储 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
public interface ToolQiniuContentService extends IService<ToolQiniuContent> {


    IPage<ToolQiniuContent> page(Pageable pageable, QueryVo queryVo);

    void upload(MultipartFile multipartFile) throws IOException;

    void synchronize();

    void delete(List<Long> ids);

    ToolQiniuContent findByName(String name);

    void download(Pageable pageable, HttpServletResponse response);
}
