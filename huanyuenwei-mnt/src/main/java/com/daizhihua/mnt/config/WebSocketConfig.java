package com.daizhihua.mnt.config;

import com.daizhihua.mnt.handler.WebShellWebSocketHandler;
import com.daizhihua.mnt.interceptor.WebSocketInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@EnableWebSocket
@Configuration
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private WebShellWebSocketHandler webSocketHandler;

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        log.info("指定通道，{}",webSocketHandler.supportsPartialMessages());
        //socket通道
        //指定处理器和路径
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/websocket")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*")
        ;
    }
}
