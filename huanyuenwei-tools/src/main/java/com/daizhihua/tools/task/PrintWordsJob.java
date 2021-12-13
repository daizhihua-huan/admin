package com.daizhihua.tools.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 定时任务
 */
@Slf4j
public class PrintWordsJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("{}",jobExecutionContext);
        String printTime = new SimpleDateFormat("yy-MM-dd HH-mm-ss").format(new Date());
        System.out.println("PrintWordsJob start at:" + printTime + ", prints: Hello Job-" + new Random().nextInt(100));
//通过Map中key和value的方式获取JobDataMap中的值。
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        System.out.println(key.getName()+key.getGroup()+key.getClass());
        TriggerKey key1 = jobExecutionContext.getTrigger().getKey();
        System.out.println(key1.getGroup()+key1.getName()+key1.getClass());
        System.out.println("获取JobDataMap");



    }
}
