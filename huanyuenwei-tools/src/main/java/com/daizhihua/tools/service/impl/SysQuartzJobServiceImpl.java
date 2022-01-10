package com.daizhihua.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.tools.entity.SysQuartzJob;
import com.daizhihua.tools.entity.SysQuartzLog;
import com.daizhihua.tools.mapper.SysQuartzJobMapper;
import com.daizhihua.tools.service.SysQuartzJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.tools.service.SysQuartzLogService;
import com.daizhihua.tools.util.QuartzManage;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private SysQuartzLogService sysQuartzLogService;

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

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        IPage<SysQuartzLog> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<SysQuartzLog> records = sysQuartzLogService.page(page).getRecords();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysQuartzLog quartzLog : records) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzLog.getJobName());
            map.put("Bean名称", quartzLog.getBeanName());
            map.put("执行方法", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("表达式", quartzLog.getCronExpression());
            map.put("异常详情", quartzLog.getExceptionDetail());
            map.put("耗时/毫秒", quartzLog.getTime());
            map.put("状态", quartzLog.getIsSuccess() ? "成功" : "失败");
            map.put("创建日期", quartzLog.getCreateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
