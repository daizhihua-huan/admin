package com.daizhihua.mnt.util;

import ch.ethz.ssh2.Connection;

import java.io.IOException;
import java.nio.charset.Charset;

public class RemoteShellExecutor {

    private Connection conn;
    /** 远程机器IP */
    private String ip;
    /** 用户名 */
    private String osUsername;
    /** 密码 */
    private String password;
    private String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5 * 60;

    public RemoteShellExecutor(String ip, String usr, String pasword) {
        this.ip = ip;
        this.osUsername = usr;
        this.password = pasword;
    }

    /**
     * 登录
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(osUsername, password);
    }

    public static void main(String args[]) throws Exception {
        RemoteShellExecutor executor = new RemoteShellExecutor("139.198.36.222", "root", "DAIzhihua1996");
        // 执行myTest.sh 参数为java Know dummy
        System.out.println(executor.login());
    }
}
