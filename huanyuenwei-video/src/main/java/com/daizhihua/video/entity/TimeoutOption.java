package com.daizhihua.video.entity;

public enum TimeoutOption {

    /**
     * 取决于协议（FTP，HTTP，RTMP，SMB，SSH，TCP，UDP或UNIX）。
     *
     * http://ffmpeg.org/ffmpeg-all.html
     */
    TIMEOUT,
    /**
     * 协议
     *
     * 等待（网络）读/写操作完成的最长时间， 以微秒为单位。
     *
     * http://ffmpeg.org/ffmpeg-all.html#Protocols
     */
    RW_TIMEOUT,
    /**
     * Protocols -> RTSP
     *
     * 设置套接字TCP I / O超时（以微秒为单位）。
     *
     * http://ffmpeg.org/ffmpeg-all.html#rtsp
     */
    STIMEOUT;

    public String getKey() {
        return toString().toLowerCase();
    }
}
