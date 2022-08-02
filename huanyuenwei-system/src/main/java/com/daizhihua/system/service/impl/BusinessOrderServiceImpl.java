package com.daizhihua.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.util.SecurityUtils;
import com.daizhihua.system.entity.BusinessOrder;
import com.daizhihua.system.entity.BusinessVo;
import com.daizhihua.system.mapper.BusinessOrderMapper;
import com.daizhihua.system.service.BusinessOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2022-01-20
 */
@Service
public class BusinessOrderServiceImpl extends ServiceImpl<BusinessOrderMapper, BusinessOrder> implements BusinessOrderService {

    @Override
    public IPage<BusinessOrder> list(BusinessVo businessVo, Pageable pageable) {
        IPage<BusinessOrder> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<BusinessOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", SecurityUtils.getCurrentUserId());
        return this.page(page,queryWrapper);
    }
}
