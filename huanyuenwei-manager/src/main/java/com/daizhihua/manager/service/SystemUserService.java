package com.daizhihua.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daizhihua.core.entity.SysUser;
import com.daizhihua.manager.entity.vo.UserPassVo;
import com.daizhihua.manager.entity.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SystemUserService extends IService<SysUser> {

    List<Map<String,Object>> listUser(int currentPage,int pageSize);

    List<Map<String,Object>> listUser(int currentPage,int pageSize,List<Long>depts);

    List<Map<String,Object>> listUser(int currentPage,int pageSize,List<Long> depts,String enabled,String username,
                                      String startTime,String endTime);

    void add(SysUser sysUser);

    int getCountUserForUserName(String userName);

    int deleteUserForUserId(Long userId);

    void updateForUser(SysUser sysUser);

    void update(long enabled,long uerId);

    int countUser(UserVo userVo,String statrTime,String endTime);

    void updateAvater(Long id,MultipartFile file);

    void download(List<SysUser> list, HttpServletResponse response) throws IOException;

    void updatePassword( UserPassVo userPassVo) throws Exception;



}
