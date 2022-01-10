package com.daizhihua.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.tools.entity.SysQuartzJob;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 定时任务 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-10
 */
public interface SysQuartzJobService extends IService<SysQuartzJob> {

    IPage<SysQuartzJob> page(Pageable pageable, QueryVo queryVo);


    /**
     * 立即执行定时任务
     * @param quartzJob /
     */
    void execution(SysQuartzJob quartzJob);

    /**
     * 执行子任务
     * @param tasks /
     * @throws InterruptedException /
     */
    void executionSubJob(String[] tasks) throws InterruptedException;


    boolean updateIsPause(Long id);

    Boolean saveJobs(SysQuartzJob sysQuartzJob);

    void download(Pageable pageable, HttpServletResponse response);



}
