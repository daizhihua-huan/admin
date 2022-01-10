package com.daizhihua.manager.service.imple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysJob;
import com.daizhihua.core.entity.SysUsersJobs;
import com.daizhihua.core.mapper.SysJobMapper;
import com.daizhihua.core.mapper.SysUsersJobsMapper;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.manager.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class JobServiceImple extends ServiceImpl<SysJobMapper, SysJob> implements JobService {

    @Autowired
    private SysJobMapper sysJobMapper;

    @Autowired
    private SysUsersJobsMapper sysUsersJobsMapper;

    @Override
    public List<SysJob> listJob() {
        QueryWrapper<SysJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enabled","1");
        return sysJobMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysUsersJobs> getUserJob(Long userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userId);
        return sysUsersJobsMapper.selectByMap(map);
    }

    @Override
    public IPage<SysJob> pageList(Pageable pageable, QueryVo queryVo) {
        IPage<SysJob> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<SysJob> queryWrapper = new QueryWrapper<>();
        IPage<SysJob> pageJob = this.page(page, queryWrapper);
        return pageJob;
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        IPage<SysJob> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<SysJob> records = this.page(page).getRecords();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysJob jobDTO : records) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("岗位名称", jobDTO.getName());
            map.put("岗位状态", jobDTO.getEnabled() ? "启用" : "停用");
            map.put("创建日期", jobDTO.getCreateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
