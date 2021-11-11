package com.daizhihua.oauth.service.imple;

import com.daizhihua.core.entity.SysMenu;
import com.daizhihua.core.entity.SysUser;
import com.daizhihua.core.mapper.SysMenuMapper;
import com.daizhihua.core.mapper.SysUserMapper;
import com.daizhihua.oauth.entity.UserDto;
import com.daizhihua.oauth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * springSeacrity的数据库实现类
 */
@Service(value = "userService")
public class UserServiceImple implements UserDetailsService {


    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
        throw new UsernameNotFoundException("");
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
        return map;
    }



}
