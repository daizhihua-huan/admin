package com.daizhihua.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysRole;
import com.daizhihua.core.entity.SysUsersRoles;
import com.daizhihua.manager.entity.vo.RoleVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RoleService extends IService<SysRole> {

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

    void download(Pageable pageable, HttpServletResponse response);

}
