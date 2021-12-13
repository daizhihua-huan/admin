package com.daizhihua.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daizhihua.core.entity.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("SELECT m.* from sys_user s \n" +
            "inner JOIN sys_users_roles sr on s.user_id = sr.user_id \n" +
            "\n" +
            "inner JOIN sys_role r on sr.role_id = r.role_id \n" +
            "\n" +
            "inner JOIN sys_roles_menus sm on sm.role_id = r.role_id\n" +
            "\n" +
            "inner JOIN sys_menu m on m.menu_id = sm.menu_id \n" +
            "WHERE s.user_id = #{userId} AND m.type in(0,1)  ORDER BY m.menu_sort")
    List<SysMenu> listMenu(Long userId);

    @Select("SELECT m.permission as permission \n" +
            "FROM sys_user s \n" +
            "JOIN sys_users_roles sr \n" +
            "ON s.user_id = sr.user_id \n" +
            "JOIN sys_role r \n" +
            "on r.role_id = sr.role_id\n" +
            "JOIN sys_roles_menus sm \n" +
            "ON sm.role_id = r.role_id\n" +
            "JOIN sys_menu m\n" +
            "ON m.menu_id = sm.menu_id\n" +
            "WHERE s.user_id = #{userId} and m.permission is not NULL")
    List<String> listPermission(Long userId);

    @Select("SELECT m.menu_id as menuId \n" +
            "FROM  sys_role r \n" +
            "JOIN sys_roles_menus sr \n" +
            "on r.role_id = sr.role_id\n" +
            "JOIN sys_menu m\n" +
            "ON m.menu_id = sr.menu_id\n" +
            "WHERE r.role_id = #{roleId}")
    List<Long> listMenusIds(Long roleId);

    @Select("SELECT menu_id,pid,sub_count,type,title,name,component,menu_sort," +
            "icon,path,i_frame,cache,hidden,permission,create_by,update_by," +
            "create_time,update_time FROM sys_menu WHERE pid is null")
    List<SysMenu> getListMenusForPid();

}
