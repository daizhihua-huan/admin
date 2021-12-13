package com.daizhihua.mnt.minotor;

import com.jcraft.jsch.SftpProgressMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 获取进度接口回调
 */
@Slf4j
@Component
public class MyProgressMonitor  implements SftpProgressMonitor {
    private long transfered;
    @Override
    public void init(int i, String s, String s1, long l) {
        log.info("开始");
    }

    @Override
    public boolean count(long l) {
        transfered = transfered + l;
        log.info("Currently transferred total size: " + transfered + " bytes");
        return true;
    }

    @Override
    public void end() {
        log.info("结束");
    }
}
