package com.daizhihua.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysRole;
import com.daizhihua.core.entity.SysUsersRoles;
import com.daizhihua.manager.entity.vo.RoleVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    IPage<SysRole> listRoles(Pageable pageable, QueryVo queryVo);

    List<SysRole> listRoles();

    List<SysUsersRoles> getUserRoles(long userId);

    void addRole(SysRole sysRole);

    int deleteRole(Long roleId);

    int countRole(String name);

    int countRoleForId(Long roleId);

    int updateRole(SysRole sysRole);

    List<SysRole> getRoleForId(Long userId);

    void editMenuForRoleId(RoleVo roleVo);

}
