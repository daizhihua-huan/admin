package com.daizhihua.mnt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.mnt.entity.MntServer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 服务器管理 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-16
 */
public interface MntServerService extends IService<MntServer> {

    IPage<MntServer> page(Pageable pageable, QueryVo queryVo);

    Boolean testContent(MntServer mntServer);

    List<MntServer> getMntServerForDeployId(Long deployId);

    void download(Pageable pageable, HttpServletResponse response);
}
