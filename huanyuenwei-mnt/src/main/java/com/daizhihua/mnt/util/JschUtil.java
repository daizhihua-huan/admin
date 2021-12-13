package com.daizhihua.mnt.util;

import cn.hutool.core.io.IoUtil;
import com.daizhihua.core.config.Constant;
import com.daizhihua.core.util.FileUtil;
import com.daizhihua.mnt.entity.vo.ShellConnectInfo;
import com.daizhihua.mnt.entity.vo.WebShellData;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

@Slf4j
public class JschUtil {

    private  Session session;

    private  Vector<String> vector;

    public JschUtil(WebShellData remote) {
        try {
            JSch jsch = new JSch();
            Properties config = new Properties();
            // SSH 连接远程主机时，会检查主机的公钥。如果是第一次该主机，会显示该主机的公钥摘要，提示用户是否信任该主机
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(remote.getUsername(), remote.getHost(), remote.getPort());
            session.setPassword(remote.getPassword());
            session.setConfig(config);
            session.connect(3000);
        }catch (JSchException e){
            e.printStackTrace();
        }
    }

    public  Boolean isContent() {
        if (session.isConnected()) {
            return true;
        }
        return false;
    }



    public void execute(final String command,Long userId) {
        vector = new Vector<>();
        ChannelShell channel = null;
        PrintWriter printWriter = null;
        BufferedReader input = null;
        try {
            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
            input = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            printWriter = new PrintWriter(channel.getOutputStream());
            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();
            log.info("The remote command is: ");
            String line;
            JSONObject jsonObject = new JSONObject();
            while ((line = input.readLine()) != null) {
//                System.out.println(line);
                vector.add(line+"\r" +
                        "\n");
                if(userId!=null){
                    jsonObject.put("type","message");
                    jsonObject.put("info",line+"\r\n");
                    WebShellUtils.sendMessage(WebShellUtils.getMap(userId),jsonObject.toString());
                }
            }
            System.out.println(vector.toString());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }finally {
            IoUtil.close(printWriter);
            IoUtil.close(input);
            if (channel != null) {
                channel.disconnect();
                session.disconnect();
            }
        }
    }

    public String executeForResult(String command) {
        execute(command,null);
        StringBuilder sb = new StringBuilder();
        for (String str : vector) {
            sb.append(str);
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        WebShellData webShellData = new WebShellData();
//        webShellData.setHost("139.198.36.222");
//        webShellData.setPort(22);
//        webShellData.setUsername("root");
//        webShellData.setPassword("DAIzhihua1996");
////        webShellData.setCommand("ls");
//        JschUtil jschUtil = new JschUtil(webShellData);
//        jschUtil.execute("ls",'');
//
//    }


}
