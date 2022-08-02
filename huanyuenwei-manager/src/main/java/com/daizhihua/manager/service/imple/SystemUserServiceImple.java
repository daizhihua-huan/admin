package com.daizhihua.manager.service.imple;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.config.FileProperties;
import com.daizhihua.core.entity.*;
import com.daizhihua.core.exception.BadRequestException;
import com.daizhihua.core.mapper.*;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.core.util.RsaUtils;
import com.daizhihua.manager.entity.vo.UserPassVo;
import com.daizhihua.manager.entity.vo.UserVo;
import com.daizhihua.manager.service.DataScopeService;
import com.daizhihua.manager.service.DeptService;
import com.daizhihua.manager.service.JobService;
import com.daizhihua.manager.service.SystemUserService;
import com.daizhihua.core.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SystemUserServiceImple extends ServiceImpl<SysUserMapper,SysUser> implements SystemUserService {
    /**
     * 用户
     */
    @Autowired
    private SysUserMapper sysUserMapper;
    //密码加密
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${rsa.key}")
    private String privateKey;

    //角色
    @Autowired
    private SysUsersJobsMapper sysUsersJobsMapper;
    //岗位
    @Autowired
    private SysUsersRolesMapper sysUsersRolesMapper;

    @Autowired
    private DeptService  deptService;

    @Autowired
    private JobService jobService;

    //角色
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private DataScopeService dataScopeService;
    @Autowired
    private SysRoleDeptsMapper sysRoleDeptsMapper;
    @Autowired
    private FileProperties properties;

    @Override
    public List<Map<String,Object>> listUser(int currentPage,int pageSize) {

        return  sysUserMapper.listUserForDepids(getDepIds(),(currentPage)*pageSize,pageSize);
    }

    @Override
    public List<Map<String, Object>> listUser(int currentPage, int pageSize, List<Long> depts) {

        return sysUserMapper.listUserForDepids(depts,(currentPage)*pageSize,pageSize);
    }

    @Override
    public List<Map<String, Object>> listUser(int currentPage, int pageSize, List<Long> depts, String enabled, String username,
                                              String statrTime,String endTime) {
        if(StringUtils.hasText(username)){
            username = "%"+username+"%";
        }
        if(null!=depts&&depts.size()>0){
            return sysUserMapper.listUserForDepidsLikeSearch(depts,(currentPage)*pageSize,pageSize,enabled,
                    username,statrTime,endTime);
        }else{
            return sysUserMapper.listUserForDepidsLikeSearch(getDepIds(),(currentPage)*pageSize,pageSize,enabled,
                    username,statrTime,endTime);
        }
    }

    @Override
    public void add(SysUser sysUser) {
        String password = passwordEncoder.encode(Constant.PASSWORD);
        String name = SecurityUtils.getCurrentUser().getUsername();
        sysUser.setUser(name,password);
        sysUserMapper.insert(sysUser);
        List<Integer> roles = sysUser.getRoles();
        for (Integer role : roles) {
            SysUsersRoles sysUsersRoles = new SysUsersRoles();
            sysUsersRoles.setRoleId(Long.valueOf(role));
            sysUsersRoles.setUserId(sysUser.getUserId());
            sysUsersRolesMapper.insert(sysUsersRoles);
        }
        List<Integer> jobs = sysUser.getJobs();
        for (Integer job : jobs) {
            SysUsersJobs sysUsersJobs = new SysUsersJobs();
            sysUsersJobs.setJobId(Long.valueOf(job));
            sysUsersJobs.setUserId(sysUser.getUserId());
            sysUsersJobsMapper.insert(sysUsersJobs);
        }
    }

    @Override
    public int getCountUserForUserName(String userName) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",userName);
        return sysUserMapper.selectCount(queryWrapper);
    }

    @Override
    public int deleteUserForUserId(Long userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userId);
        sysUsersJobsMapper.deleteByMap(map);
        sysUsersRolesMapper.deleteByMap(map);
        return sysUserMapper.deleteById(userId);
    }

    @Override
    public void updateForUser(SysUser sysUser) {
        sysUserMapper.updateById(sysUser);
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",sysUser.getUserId());
        List<Integer> jobs = sysUser.getJobs();
        sysUsersJobsMapper.deleteByMap(map);
        if(null!=jobs){
            for (Integer job : jobs) {
                SysUsersJobs sysUsersJobs = new SysUsersJobs();
                sysUsersJobs.setUserId(sysUser.getUserId());
                sysUsersJobs.setJobId(Long.valueOf(job));
                sysUsersJobsMapper.insert(sysUsersJobs);
            }
        }
        List<Integer> roles = sysUser.getRoles();
        if(null!=roles){
            sysUsersRolesMapper.deleteByMap(map);
            for (Integer role : roles) {
                SysUsersRoles sysUsersRoles = new SysUsersRoles();
                sysUsersRoles.setUserId(sysUser.getUserId());
                sysUsersRoles.setRoleId(Long.valueOf(role));
                sysUsersRolesMapper.insert(sysUsersRoles);
                SysRolesDepts sysRolesDepts = new SysRolesDepts();
                sysRolesDepts.setDeptId(sysUser.getDeptId());
                sysRolesDepts.setRoleId(Long.valueOf(role));
                map.clear();
                map.put("role_id",role);
                sysRoleDeptsMapper.deleteByMap(map);
                sysRoleDeptsMapper.insert(sysRolesDepts);
            }
        }


    }

    @Override
    public void update(long enabled,long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setEnabled(enabled);
        sysUserMapper.updateById(sysUser);
    }

    @Override
    public int countUser(UserVo userVo,String statrTime,String endTime) {
        String enbale = null;
        if(userVo.getEnabled()!=null){
            enbale = userVo.getEnabled()?"1":"0";
        }

        return sysUserMapper.count(userVo.getDeptId(),userVo.getBlurry(),enbale,statrTime,endTime);
    }

    @Override
    public void updateAvater(Long id,MultipartFile file) {
        FileUtil.checkSize(properties.getMaxSize(), file.getSize());
//        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
//        String type = FileUtil.getFileType(suffix);
        File file_data = FileUtil.upload(file, properties.getPath().getAvatar()  +  File.separator);
        if(ObjectUtil.isNull(file_data)){
            throw new BadRequestException("上传失败");
        }
        log.info("{}",file_data);
        SysUser user = this.getById(id);
        if(user!=null){
            user.setUpdateTime(DateUtils.getDateTime());
            user.setAvatarName(file_data.getName());
            user.setAvatarPath(file_data.getName());
            this.updateById(user);
        }
    }

    @Override
    public void download(List<SysUser> users, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysUser userDTO : users) {
            List<SysRole> sysRoles = sysRoleMapper.selectRoleForUserId(userDTO.getUserId());
            String names = sysRoles.stream().map(SysRole::getName).collect(Collectors.joining(","));
            SysDept dept = deptService.getById(userDTO.getDeptId());
            List<SysUsersJobs> userJob = jobService.getUserJob(userDTO.getUserId());
            String jobName = "";
            for (SysUsersJobs sysUsersJobs : userJob) {
                SysJob job = jobService.getById(sysUsersJobs.getJobId());
                jobName += job.getName();
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("角色", names);
            map.put("部门", dept.getName());
            map.put("岗位", jobName);
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled()==1 ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("修改密码的时间", userDTO.getPwdResetTime());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void updatePassword(UserPassVo userPassVo) throws Exception {
        log.info("{}",userPassVo);
        String oldPass = RsaUtils.decryptByPrivateKey(privateKey,userPassVo.getOldPass());
        log.info("{}",oldPass);
        String newPass = RsaUtils.decryptByPrivateKey(privateKey,userPassVo.getNewPass());
        log.info("{}",newPass);
        SysUser user = this.getById(SecurityUtils.getCurrentUserId());
        if(!passwordEncoder.matches(oldPass, user.getPassword())){
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if(passwordEncoder.matches(newPass, user.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        user.setPassword(passwordEncoder.encode(newPass));
        user.setPwdResetTime(DateUtils.getDateTime());
        this.updateById(user);
    }


    public List<Long> getDepIds(){
        /**
         * 获取用户的角色
         * 获取角色对权限
         * 获取部门
         * 获取用户列表
         */
        List<SysRole> sysRoles = sysRoleMapper.selectRoleForUserId(SecurityUtils.getCurrentUserId());
        List<Long> list = new ArrayList<>();
        SysUser sysUser = sysUserMapper.selectById(SecurityUtils.getCurrentUserId());
        for (SysRole sysRole : sysRoles) {
            //获取角色权限
            list.addAll(dataScopeService.getDataScope(sysUser, sysRole));
        }
        return list;
    }
}
