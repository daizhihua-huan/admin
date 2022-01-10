package com.daizhihua.mnt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daizhihua.mnt.entity.MntDeployServer;
import com.daizhihua.mnt.mapper.MntDeployServerMapper;
import com.daizhihua.mnt.service.MntDeployServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用与服务器关联 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
@Service
public class MntDeployServerServiceImpl extends ServiceImpl<MntDeployServerMapper, MntDeployServer> implements MntDeployServerService {

    @Override
    public boolean deleteByDeployId(Long deployId) {
        QueryWrapper<MntDeployServer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deploy_id",deployId);
        return this.remove(queryWrapper);
    }
}
