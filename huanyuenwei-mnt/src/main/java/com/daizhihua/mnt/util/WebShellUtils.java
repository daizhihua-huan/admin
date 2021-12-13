/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.daizhihua.mnt.util;

import com.daizhihua.core.config.Constant;
import com.daizhihua.core.util.SpringContextUtils;
import com.daizhihua.log.annotation.Log;
import com.daizhihua.mnt.entity.vo.ShellConnectInfo;
import com.daizhihua.mnt.entity.vo.WebShellData;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 常用工具类
 * @title WebShellUtils
 * @author zmzhou
 * @version 1.0
 * @date 2021/2/23 20:45
 */
@Slf4j
public final class WebShellUtils {

	private static Map<Long ,WebSocketSession> map = new HashMap<>();

	private static Map<Long, ShellConnectInfo> shellMap = new HashMap<>();

	public static void setShellMap(Long userId,ShellConnectInfo shellConnectInfo){
		shellMap.put(userId,shellConnectInfo);
	}

	public static ShellConnectInfo getSellMap(Long userId){
		return shellMap.get(userId);
	}

	public static void setMap(Long userId,WebSocketSession webSocketSession){
		map.put(userId,webSocketSession);
	}

	public static WebSocketSession getMap(Long userId){
		return map.get(userId);
	}

	public static void remove(Long userId){
		map.remove(userId);
	}

	public static void sendMessage(WebSocketSession session, String message) {
		try {
			session.sendMessage(new TextMessage(message));

		} catch (IOException e) {
			log.error("数据写回前端异常：", e);
		}
	}
    private WebShellUtils() {
    }

    /**
	 * 从WebSocketSession获取用户名
	 * @param webSocketSession WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 20:47
	 */
	public static String getUuid(WebSocketSession webSocketSession){
		return String.valueOf(webSocketSession.getAttributes().get(Constant.USER_UUID_KEY));
	}


	/**
	 * 使用jsch连接终端
	 * @param shellConnectInfo ShellConnectInfo
	 * @param sshData WebShellData
	 * @author zmzhou
	 * @date 2021/2/23 21:15
	 */
	public static void  connectToSsh(ShellConnectInfo shellConnectInfo,
							  WebShellData sshData, WebSocketSession webSocketSession)
			throws JSchException {
		Properties config = new Properties();
		log.info("连接中断");
		// SSH 连接远程主机时，会检查主机的公钥。如果是第一次该主机，会显示该主机的公钥摘要，提示用户是否信任该主机
		config.put("StrictHostKeyChecking", "no");
		//获取jsch的会话
		Session session = shellConnectInfo.getJsch().getSession(sshData.getUsername(), sshData.getHost(), sshData.getPort());
		session.setConfig(config);
		//设置密码
		log.info("密码是:{}", sshData.getPassword());
		session.setPassword(sshData.getPassword());
		//连接超时时间30s
		session.connect(30000);

		//开启shell通道
		Channel channel = session.openChannel("shell");
		//通道连接超时时间3s
		channel.connect(3000);
		//设置channel
		shellConnectInfo.setChannel(channel);
		//读取终端返回的信息流
		try (InputStream inputStream = channel.getInputStream()) {
			//循环读取
			byte[] buffer = new byte[Constant.BUFFER_SIZE];
			int i;
			//如果没有数据来，线程会一直阻塞在这个地方等待数据。
			while ((i = inputStream.read(buffer)) != -1) {
				sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
			}
		} catch (IOException e) {
			log.error("读取终端返回的信息流异常：", e);
		} finally {
			//断开连接后关闭会话
			session.disconnect();
			channel.disconnect();
		}
	}

	/**
	 * 数据写回前端
	 * @param session WebSocketSession
	 * @author zmzhou
	 * @date 2021/2/23 21:18
	 */
	public static void sendMessage(WebSocketSession session, byte[] buffer) {
		try {
			TextMessage textMessage = new TextMessage(buffer);
			session.sendMessage(textMessage);

		} catch (IOException e) {
			log.error("数据写回前端异常：", e);
		}
	}

	/**
	 * 将消息转发到终端
	 * @param channel Channel
	 * @author zmzhou
	 * @date 2021/2/23 21:13
	 */
	public static void sendToTerminal(Channel channel, String command) {
		if (channel != null) {
			try {
				OutputStream outputStream = channel.getOutputStream();
				outputStream.write(command.getBytes());
				outputStream.flush();
			} catch (IOException e) {
				log.error("web shell将消息转发到终端异常:{}", e.getMessage());
			}
		}
	}

	/**
	 * Gets session id.
	 *
	 * @return the session id
	 * @author zmzhou
	 * @date 2021/3/1 14:36
	 */
	public static String getSessionId() {
		return SpringContextUtils.getSession().getId();
	}

	/**
	 * 文件大小转换单位
	 * @param size 文件大小
	 * @author zmzhou
	 * @date 2021/3/1 17:53
	 */
	public static String convertFileSize(long size) {
		long kb = Constant.KB;
		long mb = kb * Constant.KB;
		long gb = mb * Constant.KB;
		String fileSize;
		if (size >= gb) {
			fileSize = String.format("%.1fGB", (float)size / (float)gb);
		} else {
			float f;
			if (size >= mb) {
				f = (float)size / (float)mb;
				fileSize = String.format(f > 100.0F ? "%.0fMB" : "%.1fMB", f);
			} else if (size >= kb) {
				f = (float)size / (float)kb;
				fileSize = String.format(f > 100.0F ? "%.0fKB" : "%.1fKB", f);
			} else {
				fileSize = String.format("%dB", size);
			}
		}
		return fileSize;
	}
}
