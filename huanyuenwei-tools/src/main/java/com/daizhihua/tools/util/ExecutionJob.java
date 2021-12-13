/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.daizhihua.tools.util;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daizhihua.core.config.thread.ThreadPoolExecutorUtil;
import com.daizhihua.core.util.RedisUtil;
import com.daizhihua.core.util.SpringContextUtils;
import com.daizhihua.core.util.ThrowableUtil;
import com.daizhihua.tools.entity.SysQuartzJob;
import com.daizhihua.tools.entity.SysQuartzLog;
import com.daizhihua.tools.service.SysQuartzJobService;
import com.daizhihua.tools.service.SysQuartzLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 * @author /
 * @date 2019-01-07
 */
@Slf4j
// 异步调用
@Async
@SuppressWarnings({"unchecked","all"})
public class ExecutionJob extends QuartzJobBean {

    /** 该处仅供参考 */
    private final static ThreadPoolExecutor EXECUTOR = ThreadPoolExecutorUtil.getPoll();

    @Override
    public void executeInternal(JobExecutionContext context) {
        SysQuartzJob quartzJob = (SysQuartzJob) context.getMergedJobDataMap().get(SysQuartzJob.JOB_KEY);
        SysQuartzLogService quartzLogRepository = SpringContextUtils.getBean(SysQuartzLogService.class);
        SysQuartzJobService quartzJobService = SpringContextUtils.getBean(SysQuartzJobService.class);
        RedisUtil redisUtils = SpringContextUtils.getBean(RedisUtil.class);

        String uuid = quartzJob.getUuid();
        SysQuartzLog sysLog = new SysQuartzLog();
        sysLog.setJobName(quartzJob.getJobName());
        sysLog.setBeanName(quartzJob.getBeanName());
        sysLog.setMethodName(quartzJob.getMethodName());
        sysLog.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        sysLog.setCronExpression(quartzJob.getCronExpression());
        try {
//            // 执行任务
            log.info("--------------------------------------------------------------");
            log.info("任务开始执行，任务名称：" + quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(),
                    quartzJob.getParams());
            Future<?> future = EXECUTOR.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            sysLog.setTime(times);
            if (StringUtils.isNotBlank(uuid)) {
                redisUtils.set(uuid, true);
            }
            // 任务状态
            sysLog.setIsSuccess(true);
            log.info("任务执行完毕，任务名称：" + quartzJob.getJobName() + ", 执行时间：" + times + "毫秒");
            log.info("--------------------------------------------------------------");
//            // 判断是否存在子任务
            if (quartzJob.getSubTask() != null) {
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                // 执行子任务
                quartzJobService.executionSubJob(tasks);
            }
        } catch (Exception e) {
            if (StringUtils.isNotBlank(uuid)) {
                redisUtils.set(uuid, false);
            }
            log.info("任务执行失败，任务名称：" + quartzJob.getJobName());
            log.info("--------------------------------------------------------------");
            long times = System.currentTimeMillis() - startTime;
            sysLog.setTime(times);
            // 任务状态 0：成功 1：失败
            sysLog.setIsSuccess(false);
            sysLog.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(true);
//                //更新状态
                quartzJobService.updateById(quartzJob);
//            }
//            if(quartzJob.getEmail() != null){
//                EmailService emailService = SpringContextHolder.getBean(EmailService.class);
//                // 邮箱报警
//                EmailVo emailVo = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e));
//                emailService.send(emailVo, emailService.find());
//            }
            }
        }finally {
            quartzLogRepository.save(sysLog);
        }
    }
//
//    private EmailVo taskAlarm(QuartzJob quartzJob, String msg) {
//        EmailVo emailVo = new EmailVo();
//        emailVo.setSubject("定时任务【"+ quartzJob.getJobName() +"】执行失败，请尽快处理！");
//        Map<String, Object> data = new HashMap<>(16);
//        data.put("task", quartzJob);
//        data.put("msg", msg);
//        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
//        Template template = engine.getTemplate("email/taskAlarm.ftl");
//        emailVo.setContent(template.render(data));
//        List<String> emails = Arrays.asList(quartzJob.getEmail().split("[,，]"));
//        emailVo.setTos(emails);
//        return emailVo;
//    }
}
