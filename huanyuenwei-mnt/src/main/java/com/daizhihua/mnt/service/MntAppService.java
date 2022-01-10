package com.daizhihua.mnt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.mnt.entity.MntApp;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 应用管理 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
public interface MntAppService extends IService<MntApp> {

    IPage<MntApp> page(Pageable pageable, QueryVo queryVo);

    void download(Pageable pageable, HttpServletResponse response);
}
