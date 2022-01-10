package com.daizhihua.mnt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.mnt.entity.MntDeployHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 部署历史管理 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-18
 */
public interface MntDeployHistoryService extends IService<MntDeployHistory> {

    IPage<MntDeployHistory> page(Pageable pageable, QueryVo queryVo);

    void download(Pageable pageable, HttpServletResponse response);
}
