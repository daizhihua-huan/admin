package com.daizhihua.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.core.entity.SysDept;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysDeptMapper extends BaseMapper<SysDept> {

    @Select("SELECT sd.* FROM sys_role r JOIN sys_roles_depts d ON r.role_id = d.role_id\n" +
            "JOIN sys_dept sd on d.dept_id = sd.dept_id WHERE r.role_id = #{roleId}")
    List<SysDept> getDeptForRoleID(Long roleId);


}
