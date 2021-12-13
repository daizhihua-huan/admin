package com.daizhihua.manager.controller;

import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysMenu;
import com.daizhihua.core.entity.SysUser;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.manager.service.MenuService;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单")
@Slf4j
@RestController(value = "menuController")
@RequestMapping(value = "menus")
public class MenuController implements BaseController<SysMenu> {

    @Autowired
    private MenuService menuService;


    @ApiOperation(value = "获取菜单")
    @GetMapping
    public Resut getAllMenus(Pageable pageable, QueryVo queryVo){
        log.info("查询的参数:{}",pageable);
        return Resut.ok(menuService.buildTree( menuService.getAllMenusForPage(pageable,queryVo)));
    }


    /**
     * 查询当前所有的菜单
     * @return
     */
    @RequestMapping(value = "/listMenu",method = RequestMethod.POST)
    public Resut listMenu(){
        Long userId =  SecurityUtils.getCurrentUserId();
        List<SysMenu> sysMenus = menuService.listMenu(userId);
        return Resut.ok(menuService.buildMenus( menuService.buildTree(sysMenus)));
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Resut list() {
//        SysUser userDto =  SecurityUtils.getCurrentUser();
        List<SysMenu> sysMenus = menuService.listMenu(SecurityUtils.getCurrentUserId());
        return Resut.ok(menuService.buildTree(sysMenus));
    }

    @RequestMapping(value = "lazy",method = RequestMethod.GET)
    @ApiOperation(value = "加载菜单",notes = "加载菜单详情")
    public Resut lazy(String pid){
        return Resut.ok(menuService.buildTree( menuService.getAllMenus()));
    }



    @ApiOperation(value = "根据角色查询菜单id")
    @RequestMapping(value = "/listMenuIds",method = RequestMethod.GET)
    public Resut listMenuIds(Long roleId){

        return Resut.ok(menuService.listMenuIds(roleId));
    }

    @Log(value = "添加菜单",type = LogActionType.ADD)
    @ApiOperation(value = "添加菜单")
    @PostMapping
    @Override
    public Resut add(@RequestBody SysMenu sysMenu) {
        log.info("参数是:{}",sysMenu);
        if(sysMenu.getPid()==0){
            sysMenu.setPid(null);
        }
        sysMenu.setCreateTime(DateUtils.getDateTime());
        sysMenu.setUpdateTime(DateUtils.getDateTime());
        sysMenu.setCreateBy(SecurityUtils.getCurrentUsername());
        sysMenu.setUpdateBy(SecurityUtils.getCurrentUsername());
        return Resut.ok( menuService.save(sysMenu));
    }

    @Override
    public Resut delete(Long id) {
        return Resut.ok();
    }

    @Log(value = "删除菜单",type = LogActionType.DELETE)
    @ApiOperation(value = "删除菜单")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> ids) {
        log.info("删除：{}",ids);
        return Resut.ok(menuService.removeByIds(ids));
    }

    @Log(value = "更新菜单",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新菜单")
    @Override
    @PutMapping
    public Resut update(@RequestBody SysMenu sysMenu) {
        log.info("参数是:{}",sysMenu);
        if(sysMenu.getPid()==0){
            sysMenu.setPid(null);
        }
        sysMenu.setUpdateBy(SecurityUtils.getCurrentUsername());
        sysMenu.setUpdateTime(DateUtils.getDateTime());
        return Resut.ok(menuService.updateById(sysMenu));
    }
}
