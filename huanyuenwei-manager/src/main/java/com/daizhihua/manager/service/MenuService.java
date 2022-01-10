package com.daizhihua.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysMenu;
import com.daizhihua.manager.entity.vo.MenuVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MenuService extends IService<SysMenu> {




    List<SysMenu> listMenu(Long userId);

    /**
     * 构建菜单树
     * @param menuDtos 原始数据
     * @return /
     */
    List<SysMenu> buildTree(List<SysMenu> menuDtos);

    /**
     * 构建菜单树
     * @param menuDtos /
     * @return /
     */
    List<MenuVo> buildMenus(List<SysMenu> menuDtos);

    List<Long> listMenuIds(Long roleId);

    List<SysMenu> listMenuForPid(Long pid);

    List<SysMenu> getAllMenus();

    List<SysMenu> getAllMenusForPage(Pageable pageable, QueryVo queryVo);

    void download(Pageable pageable, HttpServletResponse response);

}
