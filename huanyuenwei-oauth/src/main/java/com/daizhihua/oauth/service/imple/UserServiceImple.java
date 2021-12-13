package com.daizhihua.oauth.service.imple;

import com.daizhihua.core.entity.SysDept;
import com.daizhihua.core.entity.SysMenu;
import com.daizhihua.core.entity.SysUser;
import com.daizhihua.core.mapper.SysDeptMapper;
import com.daizhihua.core.mapper.SysMenuMapper;
import com.daizhihua.core.mapper.SysUserMapper;
import com.daizhihua.core.util.DateUtils;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.oauth.entity.OnlineUserDto;
import com.daizhihua.oauth.entity.UserDto;
import com.daizhihua.oauth.util.CacheUtil;
import com.daizhihua.oauth.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * springSeacrity的数据库实现类
 */
@Service(value = "userService")
@Slf4j
public class UserServiceImple implements UserDetailsService {


    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SysDeptMapper sysDeptMapper;


    private SysUser sysUser;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser byUsername = sysUserMapper.findByUsername(username);
        if(byUsername!=null){
            this.sysUser = byUsername;
            UserDto userDto = new UserDto(byUsername);
            List<SysMenu> sysMenu = sysMenuMapper.listMenu(byUsername.getUserId());
            this.sysUser.setList(sysMenu);
            this.sysUser = getSysUser();
            userDto.setList(sysMenu);
            return userDto;
        }
        throw new UsernameNotFoundException("当前用户不存在");
    }

    public SysUser getSysUserForUserName(String username){
        SysUser sysUser = sysUserMapper.findByUsername(username);
        if(sysUser!=null){
            List<SysMenu> sysMenu = sysMenuMapper.listMenu(sysUser.getUserId());
            sysUser.setList(sysMenu);
            sysUser = getSysUser();
        }
        return sysUser;
    }


    public SysUser getSysUser() {
        if(sysUser!=null){
            if(sysUser.getIsAdmin()){
                List<String> list = new ArrayList<>();
                list.add("admin");
                sysUser.setPermissions(list);
            }else{
                sysUser.setPermissions(sysMenuMapper.listPermission(sysUser.getUserId()));
            }
        }
        return sysUser;
    }

    public Map<String,Object> authLogin(String username, String password, String code){
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken( username, password );
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = this.loadUserByUsername( username );
        final String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,Object> map = new HashMap<>();
        map.put("user",this.sysUser);
        map.put("token",token);
        /**
         * 保存在线用户
         * 1.存在list 方便查询
         * 2.存在map中方便处理
         */
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        save(sysUser,token,request);

        return map;
    }


    /**
     * 保存在线用户信息
     * @param sysUser /
     * @param token /
     * @param request /
     */
    private void save(SysUser sysUser, String token, HttpServletRequest request){

        String dept="";
        SysDept sysDept = sysDeptMapper.selectById(sysUser.getDeptId());
        if(sysDept!=null){
            dept = sysDept.getName();
        }
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        OnlineUserDto onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDto(sysUser.getUserId(),sysUser.getUsername(), sysUser.getNickName(), dept, browser ,
                    ip, address, token, DateUtils.getDateTime());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

        CacheUtil.sertUser(onlineUserDto);
        CacheUtil.setOnlineUserDto(sysUser.getUserId(),onlineUserDto);
//        redisUtils.set(properties.getOnlineKey() + token, onlineUserDto, properties.getTokenValidityInSeconds()/1000);
    }


}
