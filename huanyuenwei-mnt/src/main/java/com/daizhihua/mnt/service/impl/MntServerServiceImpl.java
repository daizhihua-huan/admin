package com.daizhihua.mnt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.mnt.entity.MntServer;
import com.daizhihua.mnt.entity.vo.WebShellData;
import com.daizhihua.mnt.mapper.MntServerMapper;
import com.daizhihua.mnt.service.MntServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.mnt.util.JschUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 服务器管理 服务实现类
 * </p>
 *
 * @author 代志华
 * @since 2021-11-16
 */
@Service
public class MntServerServiceImpl extends ServiceImpl<MntServerMapper, MntServer> implements MntServerService {

    @Autowired
    private MntServerMapper mntServerMapper;

    @Override
    public IPage<MntServer> page(Pageable pageable, QueryVo queryVo) {
        Page<MntServer> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<MntServer> queryWrapper = new QueryWrapper<>();
        return  this.page(page,queryWrapper);
    }

    @Override
    public Boolean testContent(MntServer mntServer) {
        WebShellData webShellData = new WebShellData();
        webShellData.setUsername(mntServer.getAccount());
        webShellData.setPassword(mntServer.getPassword());
        webShellData.setPort(mntServer.getPort());
        webShellData.setHost(mntServer.getIp());
        JschUtil jschUtil = new JschUtil(webShellData);
        return jschUtil.isContent();
    }

    @Override
    public List<MntServer> getMntServerForDeployId(Long deployId) {
        return mntServerMapper.getMntServerForDeployId(deployId);
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        IPage<MntServer> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<MntServer> records = this.page(page).getRecords();
        List<Map<String,Object>> list = new ArrayList<>();
        for (MntServer deployDto : records) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("服务器名称", deployDto.getName());
            map.put("服务器IP", deployDto.getIp());
            map.put("端口", deployDto.getPort());
            map.put("账号", deployDto.getAccount());
            map.put("创建日期", deployDto.getCreateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
