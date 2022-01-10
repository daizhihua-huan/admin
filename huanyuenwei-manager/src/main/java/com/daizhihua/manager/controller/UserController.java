package com.daizhihua.manager.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daizhihua.core.config.FileProperties;
import com.daizhihua.core.entity.SysJob;
import com.daizhihua.core.entity.SysUser;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.res.Resut;

import com.daizhihua.core.util.*;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.log.annotation.LogActionType;
import com.daizhihua.manager.entity.vo.UserPassVo;
import com.daizhihua.manager.entity.vo.UserVo;
import com.daizhihua.manager.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户信息")
@RestController
@Slf4j
@RequestMapping(value = "users")
public class UserController  {

    @Autowired
    private SystemUserService systemUserService;



    /**
     * 获取用户列表分三种情况
     * 1、直接获取分页的列表
     * 2、通过部门选择获取用户分页的列表
     * 3、搜索获取用户的列表
     *
     * 后期优化
     * 1、不需要每次获取部门id 将部门id更新到redis 同时维护
     * 2、mysql 增加索引提交访问速度
     * @return
     */

    @ApiOperation(value = "获取用户",notes = "获取所有用户列表")
    @GetMapping
    public Resut list(UserVo userVo,Pageable pageable){
        log.info("{}",pageable);
        log.info("搜索的参数是:{}",userVo);
        Map<String,Object> map = new HashMap<>();
        List<String> createTime = userVo.getCreateTime();
        String starTime = null;
        String endTime = null;
        if(createTime!=null&&createTime.size()==2){
            starTime = createTime.get(0);
            endTime = createTime.get(1);
        }
        if(userVo.getEnabled()!=null){
            map.put("records", systemUserService.
                    listUser(pageable.getPageNumber(),pageable.getPageSize(),
                            userVo.getDeptId(),userVo.getEnabled()?"1":"0",userVo.getBlurry(),
                            starTime,endTime));
        }else{
            map.put("records", systemUserService.
                    listUser(pageable.getPageNumber(),pageable.getPageSize(),
                            userVo.getDeptId(),null,userVo.getBlurry(),
                            starTime,endTime));
        }

        map.put("total", systemUserService.countUser(userVo,starTime,endTime));
        return Resut.ok(map);
    }



    @Log(value = "用户新增",type = LogActionType.ADD)
    @ApiOperation(value = "添加")
    @PostMapping
    public Resut add(@RequestBody SysUser sysUser){
        if(systemUserService.getCountUserForUserName(sysUser.getUsername())>0){
            return Resut.error("用户名不能重复");
        }else{
            systemUserService.add(sysUser);
            return Resut.ok();
        }

    }

    @Log(value = "删除用户",type = LogActionType.DELETE)
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "userIds",value = "用户主键集合")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> userIds) {
        for (Long userId : userIds) {
            systemUserService.deleteUserForUserId(userId);
        }
        return Resut.ok();
    }

    @Log(value = "更新用户",type = LogActionType.UPDATE)
    @ApiOperation(value = "更新")
    @PutMapping
    public Resut update(@RequestBody SysUser sysUser) {
        log.info("参数是:{}",sysUser);
        systemUserService.updateForUser(sysUser);
        return Resut.ok();
    }
    @ApiOperation(value = "更新禁用和激活")
    @RequestMapping(value = "updateEnable",method = RequestMethod.POST)
    public Resut updateEnable(Long enabled, Long userId){
        systemUserService.update(enabled,userId);
        return Resut.ok();
    }

    @ApiOperation(value = "修改在线头像")
    @PostMapping(value = "updateAvatar")
    public Resut updateAvatar(@RequestParam("id")Long id, @RequestParam("avatar") MultipartFile avatar){
        log.info("avater是:{}",avatar);
        log.info("用户的id是:{}",id);
        systemUserService.updateAvater(id,avatar);
        return Resut.ok();
    }

    @ApiOperation(value = "修改用户信息")
    @PutMapping(value = "center")
    public Resut center(@RequestBody SysUser sysUser){
        log.info("用户信息是:{}",sysUser);
        return Resut.ok(systemUserService.updateById(sysUser));
    }

    @ApiOperation(value = "修改密码")
    @PostMapping(value = "updatePass")
    public Resut updatePass(@RequestBody UserPassVo userPassVo) throws Exception {
        systemUserService.updatePassword(userPassVo);
        return Resut.ok();
    }

    @ApiOperation(value = "导出用户",notes = "导出用户的excle")
    @GetMapping(value = "download")
    public void download(Pageable pageable, HttpServletResponse response){
        log.info("{}",pageable);
        IPage<SysUser> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        List<SysUser> userPage = systemUserService.page(page).getRecords();
        try {
            systemUserService.download(userPage,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
