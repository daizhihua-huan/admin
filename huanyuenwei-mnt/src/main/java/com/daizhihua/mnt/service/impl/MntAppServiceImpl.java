package com.daizhihua.mnt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.mnt.entity.MntApp;
import com.daizhihua.mnt.mapper.MntAppMapper;
import com.daizhihua.mnt.service.MntAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用管理 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-17
 */
@Slf4j
@Service
public class MntAppServiceImpl extends ServiceImpl<MntAppMapper, MntApp> implements MntAppService {

    @Override
    public IPage<MntApp> page(Pageable pageable, QueryVo queryVo) {
        Page<MntApp> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<MntApp> queryWrapper = new QueryWrapper<>();
        return this.page(page,queryWrapper);
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        Page<MntApp> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<MntApp> records = this.page(page).getRecords();
        List<Map<String,Object>> list = new ArrayList<>();
        for (MntApp record : records) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("应用名称", record.getName());
            map.put("端口号",record.getDeployPort());
            map.put("上传目录",record.getUploadPath());
            map.put("部署目录",record.getDeployPath());
            map.put("备份目录",record.getBackupPath());
            map.put("创建日期",record.getCreateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
