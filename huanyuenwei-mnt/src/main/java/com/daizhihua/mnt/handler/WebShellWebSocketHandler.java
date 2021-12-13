package com.daizhihua.mnt.handler;

import com.daizhihua.mnt.service.impl.WebShellService;
import com.daizhihua.mnt.util.WebShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

/**
 * websocket的处理器
 */
@Slf4j
@Component
public class WebShellWebSocketHandler implements WebSocketHandler {

    @Autowired
    private WebShellService webShellService;

    /**
     * 用户连接上WebSocket回调
     * @param webSocketSession WebSocketSession
     * @author daizhih
     * @date 2021/11/17
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.info("用户:{},连接Web Shell", webSocketSession);
        webShellService.initConnection(webSocketSession);
    }

    /**
     *收到消息回调
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        log.info("收到消息:{}",webSocketMessage);
        if (webSocketMessage instanceof TextMessage) {
            log.info("用户:{},发送命令:{}", WebShellUtils.getUuid(webSocketSession), webSocketMessage.getPayload());
            //调用service接收消息
            webShellService.recvHandle(((TextMessage) webSocketMessage).getPayload(), webSocketSession);
        } else if (webSocketMessage instanceof BinaryMessage) {
            log.info("BinaryMessage:{}", webSocketMessage);
        } else if (webSocketMessage instanceof PongMessage) {
            log.info("PongMessage:{}", webSocketMessage);
        } else {
            log.error("Unexpected WebSocket message type: " + webSocketMessage);
        }

    }

    /**
     *错误的回调
     * @param webSocketSession
     * @param throwable
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        log.error("用户:{},数据传输错误:{}","", throwable);
        log.error("用户:{},数据传输错误:{}", WebShellUtils.getUuid(webSocketSession), throwable);
    }

    /**
     * 连接关闭
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.info("断开的url:{}",webSocketSession.getUri());
        log.info("用户:{},断开webSocket连接:{}", "", closeStatus);
        String userId = webSocketSession.getUri().getQuery();
        String substring = userId.substring(userId.indexOf("=")+1);
        WebShellUtils.remove(Long.valueOf(substring));
        webShellService.close(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
