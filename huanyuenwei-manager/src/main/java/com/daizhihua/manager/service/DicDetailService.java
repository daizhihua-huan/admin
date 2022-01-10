package com.daizhihua.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.core.entity.SysDictDetail;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DicDetailService extends IService<SysDictDetail> {

    IPage<SysDictDetail> page(Pageable pageable, String dictName);

    List<SysDictDetail> getDicDetailForDictId(Long dictId);
}
