package com.daizhihua.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.tools.entity.SysQuartzLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 * 定时任务日志 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-10
 */
public interface SysQuartzLogService extends IService<SysQuartzLog> {


   IPage<SysQuartzLog> page(Pageable pageable, QueryVo queryVo,Boolean isSuccess);



}
