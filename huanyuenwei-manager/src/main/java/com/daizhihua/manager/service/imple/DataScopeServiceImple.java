package com.daizhihua.manager.service.imple;

import com.daizhihua.core.entity.SysDept;
import com.daizhihua.core.entity.SysRole;
import com.daizhihua.core.entity.SysUser;
import com.daizhihua.core.enums.DataScopeEnum;
import com.daizhihua.core.mapper.SysDeptMapper;
import com.daizhihua.manager.service.DataScopeService;
import com.daizhihua.manager.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataScopeServiceImple implements DataScopeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private DeptService deptService;


    public List<Long> getDataScope(SysUser sysUser, SysRole sysRole){
        DataScopeEnum dataScopeEnum = DataScopeEnum.find(sysRole.getDataScope());
        Set<Long> depts = new HashSet<>();
        switch (dataScopeEnum){
            //全部
            case ALL:
                break;
                //自定义
            case CUSTOMIZE:
                getCustomize(depts,sysRole);
                break;
            case THIS_LEVEL:
                depts.add(sysUser.getDeptId());
                break;
            default:
                break;
        }
        return new ArrayList<>(depts);
    }

    /**
     * 获取自定义的数据权限
     * @param deptIds 部门ID
     * @param role 角色
     * @return 数据权限ID
     */
    public Set<Long> getCustomize(Set<Long> deptIds, SysRole role){
        List<SysDept> depts = sysDeptMapper.getDeptForRoleID(role.getRoleId());
        for (SysDept dept : depts) {
            deptIds.add(dept.getDeptId());
            Map<String,Object> map = new HashMap<>();
            map.put("pid",dept.getDeptId());
            List<SysDept> childenDepts = sysDeptMapper.selectByMap(map);
            if(childenDepts!=null&&childenDepts.size()>0){
                deptIds.addAll(deptService.getDeptChildren(childenDepts));
            }
        }
        return deptIds;
    }

}
