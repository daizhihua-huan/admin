package com.daizhihua.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysDict;
import com.daizhihua.core.entity.SysDictDetail;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<SysDict> {

    List<SysDictDetail> getDictDetailForName(String name);

    List<SysDictDetail> listAll();

    IPage<SysDict> page(Pageable pageable, QueryVo queryVo);


    void download(Pageable pageable, HttpServletResponse response);



}
