package com.daizhihua.manager.service.imple;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysRole;
import com.daizhihua.core.entity.SysRolesMenus;
import com.daizhihua.core.entity.SysUsersRoles;
import com.daizhihua.core.mapper.SysRoleMapper;
import com.daizhihua.core.mapper.SysRolesMenusMapper;
import com.daizhihua.core.mapper.SysUsersRolesMapper;
import com.daizhihua.manager.entity.vo.RoleVo;
import com.daizhihua.manager.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RoleServiceImple implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUsersRolesMapper sysUsersRolesMapper;

    @Autowired
    private SysRolesMenusMapper sysRolesMenusMapper;


    @Override
    public IPage<SysRole> listRoles(Pageable pageable, QueryVo queryVo) {
        System.out.println(pageable.getSort());
        IPage<SysRole> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        Sort sort = pageable.getSort();
        Iterator<Sort.Order> iterator = sort.iterator();
        String value = "";
        while (iterator.hasNext()){
//            System.out.println(iterator.next().getProperty());
            value =iterator.next().getProperty();
        }
//        String substring = value.substring(0, value.lastIndexOf(","));
        queryWrapper.orderByAsc(value);
        if(StringUtils.hasText(queryVo.getBlurry())){
            queryWrapper.like("name",queryVo.getBlurry());
        }
        if(queryVo.getCreateTime()!=null&&queryVo.getCreateTime().size()==2){
            queryWrapper.between("create_time",queryVo.getCreateTime().get(0),queryVo.getCreateTime().get(1));
        }
        return sysRoleMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<SysRole> listRoles() {
        return sysRoleMapper.selectByMap(null);
    }

    @Override
    public List<SysUsersRoles> getUserRoles(long userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userId);
        return sysUsersRolesMapper.selectByMap(map);
    }

    @Override
    public void addRole(SysRole sysRole) {

      sysRoleMapper.insert(sysRole);
    }

    @Override
    public int deleteRole(Long roleId) {
        return sysRoleMapper.deleteById(roleId);
    }

    @Override
    public int countRole(String name) {
        Wrapper<SysRole> queryWrapper = new QueryWrapper<>();
        ((QueryWrapper<SysRole>) queryWrapper).eq("name",name);
        return sysRoleMapper.selectCount(queryWrapper);
    }

    @Override
    public int countRoleForId(Long roleId) {

        return sysRoleMapper.countRoleForRoleId(roleId);
    }

    @Override
    public int updateRole(SysRole sysRole) {
        return sysRoleMapper.updateById(sysRole);
    }

    @Override
    public List<SysRole> getRoleForId(Long userId) {
        return sysRoleMapper.selectRoleForUserId(userId);
    }

    @Override
    public void editMenuForRoleId(RoleVo roleVo) {
        QueryWrapper<SysRolesMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleVo.getId());
        sysRolesMenusMapper.delete(queryWrapper);
        List<Long> menus = roleVo.getMenus();
        for (Long menuId : menus) {
            SysRolesMenus sysRolesMenus = new SysRolesMenus();
            sysRolesMenus.setRoleId(roleVo.getId());
            sysRolesMenus.setMenuId(menuId);
            sysRolesMenusMapper.insert(sysRolesMenus);
        }
    }
}
