package com.daizhihua.log.aspect;

import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.core.util.ThrowableUtil;
import com.daizhihua.log.entity.SysLog;
import com.daizhihua.log.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 代志华
 * 签面
 * aop步骤
 * 1.配置切入点
 * 2.配置连接点
 * 3.配置通知类型
 * 4.完成
 */
@Component
@Slf4j
@Aspect
public class LogAspect {

    /**
     * 日志处理
     */
    @Autowired
    @Qualifier("mySysLogService")
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;
    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.daizhihua.log.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        SysLog log = new SysLog();
        log.setLogType("INFO");
        log.setTime(System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        sysLogService.save(SecurityUtils.getCurrentUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request),joinPoint, log);
        return result;
    }


    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog log = new SysLog();
        log.setLogType("ERROR");
        log.setTime(System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
        sysLogService.save(SecurityUtils.getCurrentUsername(),
                StringUtils.getBrowser(request), StringUtils.getIp(request), (ProceedingJoinPoint)joinPoint, log);
    }


}
