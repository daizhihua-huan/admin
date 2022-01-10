package com.daizhihua.manager.service.imple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysDept;
import com.daizhihua.core.mapper.SysDeptMapper;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.manager.entity.vo.DeptVo;
import com.daizhihua.manager.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class DeptServiceImple extends ServiceImpl<SysDeptMapper,SysDept> implements DeptService {

    @Autowired
    SysDeptMapper sysDeptMapper;
    private Map<String,Object> map = new HashMap<>();
    @Override
    public List<SysDept> listDept(QueryVo queryVo,String enable) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(queryVo.getBlurry())){
            queryWrapper.like("name",queryVo.getBlurry());
        }
        if(null!=queryVo.getCreateTime()&&queryVo.getCreateTime().size()==2){
            queryWrapper.between("create_time",queryVo.getCreateTime().get(0),queryVo.getCreateTime().get(1));
        }
        if(StringUtils.hasText(enable)){
            queryWrapper.eq("enabled",Boolean.valueOf(enable)?"1":"0");
        }
        queryWrapper.isNull("pid");
        List<SysDept> sysDepts = sysDeptMapper.selectList(queryWrapper);
        tree(sysDepts);
        return sysDepts;
    }

    @Override
    public List<Long> getDeptChildren(List<SysDept> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dep->{
            //部门存在并且启用
            if(dep!=null&&dep.getEnabled().equals("1")){
                map.put("pid",dep.getDeptId());
                List<SysDept> sysDepts = sysDeptMapper.selectByMap(map);
                if(sysDepts!=null&&sysDepts.size()>0){
                    list.addAll(getDeptChildren(sysDepts));
                }
                list.add(dep.getDeptId());
            }
        });
        return list;
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
//        IPage<SysDept> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<SysDept> deptList =this.list();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysDept deptDTO : deptList) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("部门名称", deptDTO.getName());
            map.put("部门状态", deptDTO.getEnabled()==1 ? "启用" : "停用");
            map.put("创建日期", deptDTO.getCreateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void tree( List<SysDept> list){
        for (SysDept sysDept : list) {
            map.put("pid",sysDept.getDeptId());
            DeptVo deptVo = new DeptVo();
            deptVo.setId(sysDept.getDeptId().toString());
            deptVo.setLabel(sysDept.getName());
            List<SysDept> sysDepts = sysDeptMapper.selectByMap(map);
            if(sysDepts.size()>0){
                sysDept.setChildren(sysDepts);
                tree(sysDepts);
            }

        }
    }
}
