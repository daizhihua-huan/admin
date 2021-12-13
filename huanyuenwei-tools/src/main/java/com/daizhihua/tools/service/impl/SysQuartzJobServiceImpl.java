package com.daizhihua.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.tools.entity.SysQuartzJob;
import com.daizhihua.tools.mapper.SysQuartzJobMapper;
import com.daizhihua.tools.service.SysQuartzJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.tools.util.QuartzManage;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-10
 */
@Service
@Slf4j
public class SysQuartzJobServiceImpl extends ServiceImpl<SysQuartzJobMapper, SysQuartzJob> implements SysQuartzJobService {

    @Autowired
    private SysQuartzJobMapper sysQuartzJobMapper;

    @Autowired
    private QuartzManage quartzManage;

    @Override
    public IPage<SysQuartzJob> page(Pageable pageable, QueryVo queryVo) {
        IPage<SysQuartzJob> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<SysQuartzJob> quartzJobQueryWrapper = new QueryWrapper<>();
        return this.page(page,quartzJobQueryWrapper);
    }

    /**
     * 立即执行任务
     * @param quartzJob /
     */
    @Override
    public void execution(SysQuartzJob quartzJob) {
//        quartzManage.
        quartzManage.runJobNow(quartzJob);
    }

    @Override
    public void executionSubJob(String[] tasks) throws InterruptedException {

    }

    @Override
    public boolean updateIsPause(Long id) {
        SysQuartzJob sysQurzJob = sysQuartzJobMapper.selectById(id);
        if(sysQurzJob.getIsPause()){
            sysQurzJob.setIsPause(false);
            quartzManage.resumeJob(sysQurzJob);
        }else{
            sysQurzJob.setIsPause(true);
            quartzManage.pauseJob(sysQurzJob);
        }
        return sysQuartzJobMapper.updateById(sysQurzJob)==1;
    }

    @Override
    public Boolean saveJobs(SysQuartzJob sysQuartzJob) {
        sysQuartzJob.setCreateBy(SecurityUtils.getCurrentUsername());
        sysQuartzJob.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysQuartzJob.setCreateTime(DateUtils.getDateTime());
        sysQuartzJob.setUpdateTime(DateUtils.getDateTime());
        if (!CronExpression.isValidExpression(sysQuartzJob.getCronExpression())){
            throw new BadRequestException("cron表达式格式错误");
        }
        boolean flag = this.save(sysQuartzJob);
        if(flag){
            quartzManage.addJob(sysQuartzJob);
        }
        return flag;
    }
}
