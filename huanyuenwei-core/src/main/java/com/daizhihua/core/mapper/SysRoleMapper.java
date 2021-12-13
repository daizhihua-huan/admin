package com.daizhihua.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.core.entity.SysRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("SELECT r.* FROM sys_user s JOIN sys_users_roles sr \n" +
            "ON s.user_id = sr.user_id JOIN sys_role r on sr.role_id = r.role_id\n" +
            "WHERE s.user_id = #{userId}")
    List<SysRole> selectRoleForUserId(Long userId);

    @Select("SELECT count(*) FROM sys_user s JOIN sys_users_roles sr \n" +
            "ON s.user_id = sr.user_id JOIN sys_role r on sr.role_id = r.role_id\n" +
            "WHERE r.role_id = #{roleId}")
    int countRoleForRoleId(Long roleId);


}
