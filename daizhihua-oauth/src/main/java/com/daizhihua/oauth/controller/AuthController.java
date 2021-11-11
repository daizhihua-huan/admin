package com.daizhihua.oauth.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.daizhihua.core.config.ResponseData;
import com.daizhihua.core.res.Resut;
import com.daizhihua.core.util.RedisUtil;
import com.daizhihua.oauth.entity.dto.AuthDto;
import com.daizhihua.oauth.service.AuthService;
import com.daizhihua.oauth.service.imple.UserServiceImple;
import com.daizhihua.oauth.util.SecurityUtils;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 *
 */

/**
 * 用户登录验证码生成 jwt授权功能
 *
 * @author  代志华
 * @version 1.0
 */

@Api(tags = "用户验证")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;



    /**
     * 验证码生成
     * @return
     */
    @ApiOperation(value = "验证码的生成",notes = "登录时候通过此方法获取验证码")
    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public Resut captcha(){

        return Resut.ok( authService.getImage());
    }


    /***
     *  系统登录
     * @return
     */
    @ApiOperation(value = "系统登录",notes = "系统登录功能")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true),
            @ApiImplicitParam(name = "code",value = "验证码",required = true),
            @ApiImplicitParam(name = "key",value = "秘钥",required = true)
    })

    public Resut login( @RequestBody AuthDto authDto){
        Map<String, Object> auth = authService.auth(authDto.getUsername(),
                authDto.getPassword(), authDto.getCode(), authDto.getUuid(),authDto.getKey());
        return Resut.ok(auth);
    }

    @ApiOperation(value = "获取用户基本信息",notes = "获取用户信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Resut getInfo(){
//        UserDetails currentUser =
        return Resut.ok(SecurityUtils.getCurrentUser());
    }



}
