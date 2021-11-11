package com.daizhihua.manager.controller;


import com.daizhihua.core.entity.SysUser;
import com.daizhihua.core.res.Resut;

import com.daizhihua.manager.entity.vo.UserVo;
import com.daizhihua.manager.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "userIds",value = "用户主键集合")
    @DeleteMapping
    public Resut delete(@RequestBody List<Long> userIds) {
        for (Long userId : userIds) {
            systemUserService.deleteUserForUserId(userId);
        }
        return Resut.ok();
    }

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

}
