package com.daizhihua.oauth.service.imple;

import com.daizhihua.core.util.RedisUtil;
import com.daizhihua.core.util.RsaUtils;
import com.daizhihua.excpetion.MessageNotFoundException;
import com.daizhihua.oauth.service.AuthService;
import com.wf.captcha.SpecCaptcha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 授权的逻辑操作
 *
 * @author daizhihua
 * @version 1.0
 */
@Service
@Slf4j
public class AuthServiceImple implements AuthService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    @Qualifier(value = "userService")
    private UserServiceImple userDetailsService;

    @Value("${rsa.key}")
    private String privateKey;

    @Override
    public Map<String, Object> getImage() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为30分钟
        redisUtil.set(key, verCode, 30);
        // 将key和base64返回给前端
        Map<String,Object> map  = new HashMap<>();
        map.put("uuid",key);
        map.put("image",specCaptcha.toBase64());
        return map;
    }

    @Override
    public Map<String, Object> auth(String username, String password, String code,
                                    String key, String uuid)throws MessageNotFoundException {
        Map<String, Object> map = new HashMap<>();
        log.info("秘钥key是:"+key);
        if(redisUtil.get(key)==null){
            map.put("message","验证码不存在，请重新生成");
           throw new MessageNotFoundException("验证码不存在，请重新生成");
        }
        String redisCode = redisUtil.get(key).toString();
        System.out.println(redisCode);
        // 判断验证码
        if (code ==null || !redisCode.equals(code.trim().toLowerCase())) {
            map.put("status",HttpServletResponse.SC_CREATED);
            map.put("message","验证码不正确");
            throw new MessageNotFoundException("验证码不正确");
        }
        String encodePassword = null;
        try {
//            String privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL69//kZLd8iM3eJhzmHVByoBaNhd8kV288aGqC4eeQGN32bak4Zqm78X1wbMK2FPewSPWqA932okDo+i867KSTIe7j+W+DrVFyg3+mOqqUbFQgxMGkO3XhYtYgRjRr5xDDDJYwKAQhR+B1IEeY9bqFmGTUxxFXN3yoiWqlxRgqFAgMBAAECgYEAmQLX/Z6qQq4v9TuZA5hA7EAJTdgZfHERhKCfLcDu3vHxv8xVydMi+qdvY/bX5MrXMrIrHG3am64GDQMxqiRPyrLB0s2lvymirJCWgug022KQbFRcyw5Delfex3rCPNhk+5KszV+SVtODGRDjbTS2OpZNz3Yt0IjTOKf3kcFmREECQQD0jveQ+D7muxXvnqY7S0lwdoYCB3FAXcb1+IGZ3v/OH+J2R3HvmmfprDEXMd6AcLqiIPUY0Yfl5vrHBFSxUSORAkEAx6p8I1ZV3jz9a8SX/J19Lj7R8QVRIsfuw1ZbUcNoKuNNPEuNOp+MVN4yeed+nV4f5BTkuQx7bzz/Te98Qq0VtQJAU5lnsYva3L7Jcd8WziAfW614g8sNgMZN1Bl+HB5p7YlivbIQlap/qRZutZIbkGZ4tiF0B2bhAMsjoNKvLOoisQJAU6m/LHtvrZi2w6Jz4RkIrAkMpUaKEd3e0SDtUNxlWJs38Mzjl63k+mbElcoHht8607JhiJyPWDQh8kEoOzQVhQJAD2E1ofiZdM38lSFrLJM4wmmEzuCplA1UsdDYjJbYkLGkV/fDI+UFn87GPtOgzTej4kNpV3AunbWyU8xO4trGSA==";
             encodePassword = RsaUtils.decryptByPrivateKey(privateKey, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("解密后的密码是:{}",password);
        map = userDetailsService.authLogin(username, encodePassword, code);
        map.put("status",HttpServletResponse.SC_OK);
        return map;
    }
}
