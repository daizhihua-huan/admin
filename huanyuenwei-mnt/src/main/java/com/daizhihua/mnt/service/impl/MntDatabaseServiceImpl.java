package com.daizhihua.mnt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.mnt.entity.MntDatabase;
import com.daizhihua.mnt.mapper.MntDatabaseMapper;
import com.daizhihua.mnt.service.MntDatabaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.mnt.util.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 数据库管理 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-19
 */
@Service
@Slf4j
public class MntDatabaseServiceImpl extends ServiceImpl<MntDatabaseMapper, MntDatabase> implements MntDatabaseService {

    @Override
    public IPage<MntDatabase> page(Pageable pageable, QueryVo queryVo) {
        Page<MntDatabase> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<MntDatabase> queryWrapper = new QueryWrapper<>();
        return this.page(page,queryWrapper);
    }

    @Override
    public boolean testConnect(MntDatabase database) {

        return   SqlUtils.testConnection(database.getJdbcUrl(),database.getUserName(),database.getPwd());
    }

    @Override
    public HashMap<String, Object> runSql(MntDatabase mntDatabase) {
        HashMap hashMap =
                SqlUtils.runSql(mntDatabase.getJdbcUrl(), mntDatabase.getUserName(), mntDatabase.getPwd(), mntDatabase.getSql());
        return hashMap;
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        Page<MntDatabase> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<MntDatabase> records = this.page(page).getRecords();
        List<Map<String,Object>> list = new ArrayList<>();
        for (MntDatabase record : records) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("数据库名称",record.getName());
            map.put("连接地址",record.getJdbcUrl());
            map.put("用户名",record.getUserName());
            map.put("密码",record.getPwd());
            map.put("创建日志",record.getCreateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
