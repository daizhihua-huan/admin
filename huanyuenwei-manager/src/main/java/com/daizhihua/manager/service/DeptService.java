package com.daizhihua.manager.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysDept;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DeptService extends IService<SysDept> {

    List<SysDept> listDept(QueryVo queryVo, String enable);

    /**
     * 查找子类部门
     * @param deptList
     * @return
     */
    List<Long> getDeptChildren(List<SysDept> deptList);

    void download(Pageable pageable, HttpServletResponse response);
}
