package com.daizhihua.log.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.log.entity.SysLog;
import com.daizhihua.log.mapper.SysLogMapper;
import com.daizhihua.log.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-12
 */
@Service(value = "mySysLogService")
@Slf4j
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 保存日志
     * 1、获取方法名
     * 2、获取参数
     * @param name
     * @param browser
     * @param ip
     * @param joinPoint
     * @param log
     */
    @Override
    public void save(String name, String browser, String ip, ProceedingJoinPoint joinPoint, SysLog log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.daizhihua.log.annotation.Log aopLog = method.getAnnotation(com.daizhihua.log.annotation.Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        List<Object> argValues = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
        //参数名称
        for (Object argValue : argValues) {
            params.append(argValue).append(" ");
        }
        // 描述
        if (log != null) {
            log.setDescription(aopLog.value());
        }
        assert log != null;
        log.setRequestIp(ip);

        String loginPath = "login";
        if (loginPath.equals(signature.getName())) {
            try {
                name = new JSONObject(argValues.get(0)).get("username").toString();
            } catch (Exception e) {
                SysLogServiceImpl.log.error(e.getMessage(), e);
            }
        }
        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(name);
        log.setParams(params.toString() + " }");
        log.setBrowser(browser);
        log.setCreateTime(DateUtils.getDateTime());
        this.save(log);
    }

    @Override
    public IPage<SysLog> page(Pageable pageable, QueryVo queryVo, String type) {
        IPage<SysLog> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(queryVo.getBlurry())){
            queryWrapper.eq("username",queryVo.getBlurry());
        }
        if(null!=queryVo.getCreateTime()&&queryVo.getCreateTime().size()==2){
            queryWrapper.between("create_time",queryVo.getCreateTime().get(0),queryVo.getCreateTime().get(1));
        }
        queryWrapper.eq("log_type",type);
        return  this.page(page, queryWrapper);
    }

    @Override
    public Boolean delete(String log_type) {
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_type","INFO");
        return this.remove(queryWrapper);
    }
}
