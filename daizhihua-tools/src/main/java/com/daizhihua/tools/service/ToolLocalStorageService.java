package com.daizhihua.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.tools.entity.ToolLocalStorage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 本地存储 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
public interface ToolLocalStorageService extends IService<ToolLocalStorage> {

    void create(String name, MultipartFile file);

    IPage<ToolLocalStorage>page(Pageable pageable, QueryVo queryVo);

}
