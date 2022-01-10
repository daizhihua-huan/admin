package com.daizhihua.mnt.service;

import com.daizhihua.mnt.entity.MntDeployServer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 应用与服务器关联 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
public interface MntDeployServerService extends IService<MntDeployServer> {

    boolean deleteByDeployId(Long deployId);
}
