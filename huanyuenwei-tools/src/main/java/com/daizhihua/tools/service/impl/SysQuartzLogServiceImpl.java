package com.daizhihua.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.tools.entity.SysQuartzLog;
import com.daizhihua.tools.mapper.SysQuartzLogMapper;
import com.daizhihua.tools.service.SysQuartzLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 定时任务日志 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-10
 */
@Service
@Slf4j
public class SysQuartzLogServiceImpl extends ServiceImpl<SysQuartzLogMapper, SysQuartzLog> implements SysQuartzLogService {

    @Override
    public IPage<SysQuartzLog> page(Pageable pageable, QueryVo queryVo,Boolean isSuccess) {
        IPage<SysQuartzLog> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<SysQuartzLog> quartzLogQueryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(queryVo.getBlurry())){
            quartzLogQueryWrapper.like("job_name",queryVo.getBlurry());
        }
        if(null!=queryVo.getCreateTime()&&queryVo.getCreateTime().size()==2){
            quartzLogQueryWrapper.between("create_time",queryVo.getCreateTime().get(0),queryVo.getCreateTime().get(1));
        }
        if(isSuccess!=null){
            quartzLogQueryWrapper.eq("is_success",isSuccess);
        }
        return  this.page(page,quartzLogQueryWrapper);
    }
}
