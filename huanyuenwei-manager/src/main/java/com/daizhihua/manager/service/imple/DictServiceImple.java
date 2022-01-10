package com.daizhihua.manager.service.imple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysDict;
import com.daizhihua.core.entity.SysDictDetail;
import com.daizhihua.core.mapper.SysDictDetailMapper;
import com.daizhihua.core.mapper.SysDictMapper;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.manager.service.DicDetailService;
import com.daizhihua.manager.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DictServiceImple extends ServiceImpl<SysDictMapper, SysDict> implements DictService {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private DicDetailService dicDetailService;

    @Override
    public List<SysDictDetail> getDictDetailForName(String name) {

        return  sysDictMapper.getDicDetailForName(name);
    }

    @Override
    public List<SysDictDetail> listAll() {
        return sysDictMapper.listAll();
    }

    @Override
    public IPage<SysDict> page(Pageable pageable, QueryVo queryVo) {
        IPage<SysDict> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(queryVo.getBlurry())){
            queryWrapper.like("name",queryVo.getBlurry());
        }
        return this.page(page,queryWrapper);
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        IPage<SysDict> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<Map<String, Object>> list = new ArrayList<>();
        List<SysDict> records = this.page(page).getRecords();
        for (SysDict record : records) {
            Long dictId = record.getDictId();
            List<SysDictDetail> sysDictDetails = dicDetailService.getDicDetailForDictId(dictId);
            if(sysDictDetails==null&&sysDictDetails.size()<=0){
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("字典名称", record.getName());
                map.put("字典描述", record.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", record.getCreateTime());
                list.add(map);
            }else{
                for (SysDictDetail dictDetail : sysDictDetails) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    map.put("字典名称", record.getName());
                    map.put("字典描述", record.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            }
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
