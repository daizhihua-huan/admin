package com.daizhihua.oauth.security;

import com.daizhihua.core.util.ResponseUtil;
import com.daizhihua.core.util.StringUtils;
import com.daizhihua.oauth.entity.OnlineUserDto;
import com.daizhihua.oauth.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        JSONObject o = new JSONObject();
        String id = request.getParameter("id");
        o.put("status",HttpServletResponse.SC_OK);
        o.put("messgae","退出成功");
        log.info("{}",authentication);
        try {
            ResponseUtil.write(response,o);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(!StringUtils.isEmpty(id)){
                OnlineUserDto forUserId = CacheUtil.getForUserId(Long.valueOf(id));
                CacheUtil.removeUser(forUserId);
                CacheUtil.removeForUserId(Long.valueOf(id));
            }

        }
    }
}
