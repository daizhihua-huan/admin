package com.daizhihua.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.log.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
public interface SysLogService extends IService<SysLog> {
    void save(String name, String browser, String ip, ProceedingJoinPoint joinPoint, SysLog log);


    IPage<SysLog> page(Pageable pageable, QueryVo queryVo,String type);

    Boolean delete(String log_type);

    void download(Pageable pageable, HttpServletResponse response,String type);
}
