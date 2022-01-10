package com.daizhihua.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysJob;
import com.daizhihua.core.entity.SysUsersJobs;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface JobService extends IService<SysJob> {

    List<SysJob> listJob();

    List<SysUsersJobs> getUserJob(Long userId);

    IPage<SysJob> pageList(Pageable pageable, QueryVo queryVo);

    void download(Pageable pageable, HttpServletResponse response);
}
