package com.daizhihua.oauth.security;

import com.daizhihua.oauth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.daizhihua.core.config.Constant.HEADER_STRING;
import static com.daizhihua.core.config.Constant.TOKEN_PREFIX;

/**
 * @www.codesheep.cn
 * 20190312
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier(value = "userService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException{

        String authHeader = request.getHeader( HEADER_STRING );
        if (authHeader != null && authHeader.startsWith( TOKEN_PREFIX )) {
            final String authToken = authHeader.substring( TOKEN_PREFIX.length() );
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            System.out.println(SecurityContextHolder.getContext().getAuthentication());
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                	if (jwtTokenUtil.validateToken(authToken,userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
            }
        }
        chain.doFilter(request, response);
    }
}

