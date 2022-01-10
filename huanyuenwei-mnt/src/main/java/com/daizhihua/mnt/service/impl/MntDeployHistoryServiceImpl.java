package com.daizhihua.mnt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.mnt.entity.MntDeployHistory;
import com.daizhihua.mnt.mapper.MntDeployHistoryMapper;
import com.daizhihua.mnt.service.MntDeployHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部署历史管理 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-18
 */
@Service
@Slf4j
public class MntDeployHistoryServiceImpl extends ServiceImpl<MntDeployHistoryMapper, MntDeployHistory> implements MntDeployHistoryService {

    @Override
    public IPage<MntDeployHistory> page(Pageable pageable, QueryVo queryVo) {
        Page<MntDeployHistory> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<MntDeployHistory> queryWrapper = new QueryWrapper<>();
        return this.page(page,queryWrapper);
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        Page<MntDeployHistory> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<MntDeployHistory> records = this.page(page).getRecords();
        List<Map<String,Object>> list = new ArrayList<>();
        for (MntDeployHistory record : records) {
            Map<String,Object> map = new HashMap<>();
            map.put("应用名称",record.getAppName());
            map.put("部署IP",record.getIp());
            map.put("部署人员",record.getDeployUser());
            map.put("部署时间",record.getDeployDate());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
