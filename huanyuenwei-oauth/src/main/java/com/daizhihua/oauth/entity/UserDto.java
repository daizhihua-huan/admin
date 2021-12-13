package com.daizhihua.oauth.entity;

import com.daizhihua.core.entity.SysMenu;
import com.daizhihua.core.entity.SysUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Slf4j
public class UserDto implements UserDetails {

    private long userId;

    private String username;

    private String password;

    
    private List<SysMenu> list;

    //账户有没有过期
    private  boolean accountNonExpired;

    //账户有没被锁定 （是否冻结）
    private  boolean accountNonLocked;
    /**
     *密码有没有过期
     */
    private  boolean credentialsNonExpired;

    /**
     * 账户是否可用（是否被删除）
     */
    private  boolean enabled;

    public UserDto(SysUser sysUser) {
        this.userId = sysUser.getUserId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.accountNonExpired = sysUser.getAccountNonExpired()==1?true:false;
        this.accountNonLocked = sysUser.getAccountNonLocked()==1?true:false;
        this.credentialsNonExpired = sysUser.getCredentialsNonExpired()==1?true:true;
        this.enabled = sysUser.getEnabled()==1?true:true;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysMenu sysMenu : list) {
//            System.out.println(sysMenu);
            if(sysMenu!=null){
//                log.info(sysMenu.getPermission());
                if(StringUtils.hasText(sysMenu.getPermission())){
                    authorities.add(new SimpleGrantedAuthority(sysMenu.getPermission()));
                }
            }else{
//                log.info("列表是空:{}", sysMenu);
            }

        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
