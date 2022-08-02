package com.daizhihua.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.system.entity.BusinessOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.system.entity.BusinessVo;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 代志华
 * @since 2022-01-20
 */
public interface BusinessOrderService extends IService<BusinessOrder> {
    IPage<BusinessOrder> list(BusinessVo businessVo, Pageable pageable);
}
