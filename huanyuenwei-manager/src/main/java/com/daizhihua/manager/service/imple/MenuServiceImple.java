package com.daizhihua.manager.service.imple;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysMenu;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.manager.entity.vo.MenuMetaVo;
import com.daizhihua.manager.entity.vo.MenuVo;
import com.daizhihua.manager.service.MenuService;
import com.daizhihua.core.mapper.SysMenuMapper;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImple extends ServiceImpl<SysMenuMapper, SysMenu> implements MenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listMenu(Long userId) {

        return  sysMenuMapper.listMenu(userId);
    }

    @Override
    public List<SysMenu> buildTree(List<SysMenu> menuDtos) {
        List<SysMenu> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (SysMenu menuDTO : menuDtos) {
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (SysMenu it : menuDtos) {
                if (menuDTO.getMenuId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getMenuId());
                }
            }
        }
        if(trees.size() == 0){
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getMenuId())).collect(Collectors.toList());
        }
        return trees;

    }

    @Override
    public List<MenuVo> buildMenus(List<SysMenu> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<SysMenu> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(menuDTO.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() :menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if(!menuDTO.getIFrame()){
                            if(menuDTO.getPid() == null){
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                            }else if(!StrUtil.isEmpty(menuDTO.getComponent())){
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(),menuDTO.getIcon(),!menuDTO.getCache()));
                        if(menuDtoList !=null && menuDtoList.size()!=0){
                            menuVo.setAlwaysShow(true);
                            //重定向第一个子菜单的路径
//                            menuVo.setRedirect(menuVo.getPath()+"/"+menuDtoList.get(0).getPath());
                            menuVo.setRedirect("noRedirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if(menuDTO.getPid() == null){
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if(!menuDTO.getIFrame()){
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        if(StringUtils.hasText(menuVo.getPath())){
                            list.add(menuVo);
                        }

                    }
                }
        );
        return list;
    }

    @Override
    public List<Long> listMenuIds(Long roleId) {

        return  sysMenuMapper.listMenusIds(roleId);
    }

    @Override
    public List<SysMenu> listMenuForPid(Long pid) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        List<SysMenu> sysMenus ;
        if(pid==null){
            sysMenus = sysMenuMapper.getListMenusForPid();
        }else{
            sysMenus = sysMenuMapper.selectList(queryWrapper);
        }

        return sysMenus;
    }

    @Override
    public List<SysMenu> getAllMenus() {
        return sysMenuMapper.selectByMap(null);
    }

    @Override
    public List<SysMenu> getAllMenusForPage(Pageable pageable, QueryVo queryVo) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        IPage<SysMenu> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        if(StringUtils.hasText(queryVo.getBlurry())){
            queryWrapper.like("title",queryVo.getBlurry());
        }
        if(null!=queryVo.getCreateTime()&&queryVo.getCreateTime().size()==2){
            queryWrapper.between("create_time",queryVo.getCreateTime().get(0),queryVo.getCreateTime().get(1));
        }
        return  sysMenuMapper.selectList(queryWrapper);
    }

    @Override
    public void download(Pageable pageable, HttpServletResponse response) {
        IPage<SysMenu> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<SysMenu> records = this.page(page).getRecords();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysMenu menuDTO : records) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("菜单标题", menuDTO.getTitle());
            map.put("菜单类型", menuDTO.getType() == null ? "目录" : menuDTO.getType() == 1 ? "菜单" : "按钮");
            map.put("权限标识", menuDTO.getPermission());
            map.put("外链菜单", menuDTO.getIFrame() ? "是" : "否");
            map.put("菜单可见", menuDTO.getHidden() ? "否" : "是");
            map.put("是否缓存", menuDTO.getCache() ? "是" : "否");
            map.put("创建日期", menuDTO.getCreateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
