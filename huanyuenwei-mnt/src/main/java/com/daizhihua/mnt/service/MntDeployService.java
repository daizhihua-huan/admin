package com.daizhihua.mnt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.mnt.entity.MntDeploy;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 部署管理 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
public interface MntDeployService extends IService<MntDeploy> {

    IPage<MntDeploy> page(Pageable pageable, QueryVo queryVo);

    boolean mySave(MntDeploy mntDeploy);

    boolean myUpdate(MntDeploy mntDeploy);

    boolean delete(List<Long> ids);


    void upload(MultipartFile file,Long deployId,Long appId,Long userId);


    void stop(MntDeploy mntDeploy);

    void start(MntDeploy mntDeploy);

    void status(MntDeploy mntDeploy);

    void download(Pageable pageable, HttpServletResponse response);
}
