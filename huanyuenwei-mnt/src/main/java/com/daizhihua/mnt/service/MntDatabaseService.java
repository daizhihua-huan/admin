package com.daizhihua.mnt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.mnt.entity.MntDatabase;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * <p>
 * 数据库管理 服务类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-19
 */
public interface MntDatabaseService extends IService<MntDatabase> {

    IPage<MntDatabase> page(Pageable pageable, QueryVo queryVo);

    boolean testConnect(MntDatabase database);

    HashMap<String,Object> runSql(MntDatabase mntDatabase);

    void download(Pageable pageable, HttpServletResponse response);
}
