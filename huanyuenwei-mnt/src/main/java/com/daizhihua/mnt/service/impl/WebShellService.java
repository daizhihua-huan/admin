package com.daizhihua.mnt.service.impl;

import com.daizhihua.core.config.Constant;
import com.daizhihua.core.config.thread.ThreadPoolExecutorUtil;
import com.daizhihua.mnt.entity.vo.ShellConnectInfo;
import com.daizhihua.mnt.entity.vo.WebShellData;
import com.daizhihua.mnt.util.SftpUtils;
import com.daizhihua.mnt.util.WebShellUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.daizhihua.mnt.util.WebShellUtils.*;

@Slf4j
@Service
public class WebShellService {

    /** 存放ssh连接信息的map */
    private static final Map<String, Object> SSH_MAP = new ConcurrentHashMap<>();


    /**
     * 初始化连接
     * @param session WebSocketSession
     * @author daizhihua
     * @date 2021/11/23 21:22
     */
    public void initConnection(WebSocketSession session) {
        JSch jSch = new JSch();
        ShellConnectInfo shellConnectInfo = new ShellConnectInfo();
        shellConnectInfo.setJsch(jSch);
        shellConnectInfo.setWebSocketSession(session);
        String uuid = WebShellUtils.getUuid(session);
        //将这个ssh连接信息放入缓存中
        SSH_MAP.put(uuid, shellConnectInfo);
        String userId = session.getUri().getQuery();
        String substring = userId.substring(userId.indexOf("=")+1);
        WebShellUtils.setMap(Long.valueOf(substring),session);
        WebShellUtils.setShellMap(Long.valueOf(substring),shellConnectInfo);
        log.info("获取用户id:{}",substring);
    }


    /**
     * 处理客户端发送的数据
     * @author daizhihua
     * @date 2021/11/23 21:21
     */
    public void recvHandle(String buffer, WebSocketSession session) {
        log.info("消息是:{}",buffer);
        ObjectMapper objectMapper = new ObjectMapper();
        WebShellData shellData;
        try {
            shellData = objectMapper.readValue(buffer, WebShellData.class);
        } catch (IOException e) {
            log.error("Json转换异常:{}", e.getMessage());
            return;
        }
        String uuid = WebShellUtils.getUuid(session);
        //找到刚才存储的ssh连接对象
        ShellConnectInfo shellConnectInfo = (ShellConnectInfo) SSH_MAP.get(uuid);
        if (shellConnectInfo != null) {
            if (Constant.OPERATE_CONNECT.equals(shellData.getOperate())) {
                //启动线程异步处理
                ThreadPoolExecutorUtil.execute(() -> {
                    try {
                        connectToSsh(shellConnectInfo, shellData, session);
                    } catch (JSchException e) {
                        log.error("web shell连接异常:{}", e.getMessage());
                        sendMessage(session, e.getMessage().getBytes());
                        close(session);
                    }
                });
            } else if (Constant.OPERATE_COMMAND.equals(shellData.getOperate())) {
                String command = shellData.getCommand();
                sendToTerminal(shellConnectInfo.getChannel(), command);
            } else {
                log.error("不支持的操作");
                close(session);
            }
        }
    }

    /**
     * 关闭连接
     * @param session WebSocketSession
     * @author daizhihua
     * @date 2021/11/23 21:16
     */
    public void close(WebSocketSession session) {
        String userId = WebShellUtils.getUuid(session);
        ShellConnectInfo shellConnectInfo = (ShellConnectInfo) SSH_MAP.get(userId);
        if (shellConnectInfo != null) {
            //断开连接
            if (shellConnectInfo.getChannel() != null) {
                shellConnectInfo.getChannel().disconnect();
            }
            //map中移除
            SSH_MAP.remove(userId);
        }
    }

}
