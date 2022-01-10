package com.daizhihua.manager.service.imple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.core.entity.SysDict;
import com.daizhihua.core.entity.SysDictDetail;
import com.daizhihua.core.mapper.SysDictDetailMapper;
import com.daizhihua.core.mapper.SysDictMapper;
import com.daizhihua.manager.service.DicDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DicDetailServiceImple extends ServiceImpl<SysDictDetailMapper, SysDictDetail> implements DicDetailService {

    @Autowired
    private SysDictDetailMapper sysDictDetailMapper;

    @Autowired
    private SysDictMapper sysDictMapper;


    @Override
    public IPage<SysDictDetail> page(Pageable pageable, String dictName) {

        IPage<SysDictDetail> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",dictName);
        List<SysDict> sysDicts = sysDictMapper.selectList(queryWrapper);
        QueryWrapper<SysDictDetail> sysDictDetailQueryWrapper = new QueryWrapper<>();
        List<Long> list = new ArrayList<>();
        if(null!=sysDicts&&sysDicts.size()>0){
            for (SysDict sysDict : sysDicts) {
                Long dictId = sysDict.getDictId();
               list.add(dictId);
            }
        }
        sysDictDetailQueryWrapper.in("dict_id",list);
        return this.page(page,sysDictDetailQueryWrapper);
    }

    @Override
    public List<SysDictDetail> getDicDetailForDictId(Long dictId) {
        QueryWrapper<SysDictDetail> sysDictDetailQueryWrapper = new QueryWrapper<>();
        sysDictDetailQueryWrapper.in("dict_id",dictId);
        return this.list(sysDictDetailQueryWrapper);
    }
}
