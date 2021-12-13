package com.daizhihua.manager.service.imple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysDict;
import com.daizhihua.core.entity.SysDictDetail;
import com.daizhihua.core.mapper.SysDictMapper;
import com.daizhihua.manager.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
@Slf4j
@Service
public class DictServiceImple extends ServiceImpl<SysDictMapper, SysDict> implements DictService {

    @Autowired
    private SysDictMapper sysDictMapper;

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




}
