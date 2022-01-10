package com.daizhihua.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.controllers.BaseController;
import com.daizhihua.core.entity.QueryVo;
import com.daizhihua.core.entity.SysRole;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.manager.entity.vo.RoleVo;
import com.daizhihua.manager.service.RoleService;
import com.daizhihua.core.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "角色")
@RequestMapping(value = "roles")
@Slf4j
@RestController
public  class RoleController implements BaseController<SysRole> {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询角色列表",notes = "查询所有的角色列表")
    @GetMapping
//    @RequestMapping(value = "/listRoles",method = RequestMethod.GET)
    public Resut list(Pageable pageable, QueryVo queryVo) {
        log.info("参数是:{}",pageable);
        log.info("查询的参数是:{}",queryVo);
        return Resut.ok(roleService.listRoles(pageable,queryVo));
    }

    @ApiOperation(value = "查询所有角色",notes = "查询角色列表")
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public Resut getAll(){

        return Resut.ok(roleService.listRoles());
    }

    @ApiOperation(
            value = "查询角色",notes = "根据用户id查询角色"
    )
    @RequestMapping(value = "/getRoleForUserId",method = RequestMethod.GET)
    public Resut getRoleForUserId(Long userId){

        return Resut.ok(roleService.getUserRoles(userId));
    }


    @ApiOperation(value = "获取角色权限")
    @RequestMapping(value = "/level",method = RequestMethod.GET)
    public Resut getlevel(){

        return Resut.ok(getLevels(null));
    }


    @ApiOperation(value = "根据用户id查询角色")
    @RequestMapping(value = "/getUserRoles",method = RequestMethod.GET)
    public Resut getUserRoles(Long userId){

        return Resut.ok(roleService.getUserRoles(userId));
    }

    @Log(value = "添加角色",type = LogActionType.ADD)
    @PostMapping
    @ApiOperation(value = "添加角色",notes = "添加角色")
    @Override
    public Resut add(@RequestBody SysRole sysRole) {
        if(roleService.countRole(sysRole.getName())>0){
            return Resut.error("角色名称不能重复！");
        }else{
            roleService.addRole(sysRole);
        }

        return Resut.ok();
    }

    @Override
    public Resut delete(Long id) {
        return null;
    }

    @Log(value = "修改菜单",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新菜单")
    @RequestMapping(value = "menu",method = RequestMethod.POST)
    public Resut addMenu(@RequestBody RoleVo roleVo){
        log.info("查询id：{}",roleVo.getId());
        log.info("查询：{}",roleVo.getMenus());
        roleService.editMenuForRoleId(roleVo);
        return Resut.ok();
    }


    @ApiOperation(value = "删除角色")
    @DeleteMapping
    public Resut delete(@RequestBody  List<Long>ids) {
        for (Long id: ids){
            if( roleService.countRoleForId(id)>0){
                return Resut.error("当前角色和用户关联不能删除");
            }
            roleService.deleteRole(id);
        }
        return Resut.ok();
    }

    @Log(value = "更新角色",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新角色")
    @PutMapping
    @Override
    public Resut update(@RequestBody SysRole sysRole) {
        roleService.updateRole(sysRole);
        return Resut.ok();
    }

    @GetMapping(value = "download")
    @ApiOperation(value = "导出",notes = "导出角色信息")
    public void download(Pageable pageable, HttpServletResponse response){
        roleService.download(pageable,response);
    }

    /**
     * 获取用户的角色级别
     * @return /
     */
    private int getLevels(Integer level){
        List<Integer> levels = roleService.getRoleForId(SecurityUtils.getCurrentUserId()).stream().map(SysRole::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if(level != null){
            if(level < min){
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
