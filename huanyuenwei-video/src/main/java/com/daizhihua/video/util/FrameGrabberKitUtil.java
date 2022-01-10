package com.daizhihua.video.util;

import com.daizhihua.video.entity.TimeoutOption;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FrameGrabberKitUtil {

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        FFmpegFrameGrabber.tryLoad();
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber("rtmp://xyrtmp.ys7.com:1935/v3/openlive/33011017992507926389:33010389991327334558_1_1?expire=1639711459&id=391551112304087040&t=b6e332b32cff8c8be346075b6b66223f934ea9f43feb60ed959975537c77ba66&ev=100&devProto=gb28181");
//        FFmpegFrameGrabber ff = new FFmpegFrameGrabber("rtsp://admin:1q2w3e4r@192.168.3.64");
        for(int i=0;i<10;i++){
            // 微秒 大概为设置时间的两倍
            ff.setOption(TimeoutOption.RW_TIMEOUT.getKey(), "1000000");
            // rtsp 默认udp 丢包 改为tcp
//        ff.setOption("rtsp_transport", "tcp");
            ff.start();
            String base = "";
            Frame f = null;
            while (true) {
                f = ff.grabImage();
                if (null != f) {
                    // 若显示帧不为关键帧，则寻找相邻的下一帧
                    Frame frame = ff.grabImage();
                    if (null == frame) {
                        continue;
                    }
                    base = doExecuteBase(f,i);
                    break;
                }
            }

            ff.release();
            ff.close();
        }

//        long endTime = System.currentTimeMillis();
//        float seconds = (endTime - startTime) / 1000F;

//        System.out.println(Float.toString(seconds) + " seconds.");
    }

    public static String screenshot(String url) throws IOException {

        //方法或者外部类代码在后边
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(url);
        // 微秒 大概为设置时间的两倍
        ff.setOption(TimeoutOption.RW_TIMEOUT.getKey(), "1000000");
        // rtsp 默认udp 丢包 改为tcp
        ff.setOption("rtsp_transport", "tcp");
        ff.start();
        String base = "";
        Frame f = null;
        while (true) {
            f = ff.grabFrame();
            Frame frame = ff.grabImage();
            if (null == frame) {
                continue;
            }
            if (null != f) {
                // 若显示帧不为关键帧，则寻找相邻的下一帧
                if (f.image != null) {
                    base = doExecuteBase(f,0);
                    break;
                }
            }
        }

        ff.release();
        ff.close();
        return base;
    }

    /**
     * 输出成base64，保存到数据结构
     */
    public static String doExecuteBase(Frame f,int i) throws IOException {
        if (null == f || null == f.image) {
            return null;
        }
        long startTime = System.currentTimeMillis();
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        String base64 = getImageBinary(bi, "jpg");
        System.out.println(base64);
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println(Float.toString(seconds) + " seconds.");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", os);
        byte[] sdf = os.toByteArray();
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        File imageFile = new File("D:/"+i+".png");
        //创建输出流
        FileOutputStream fileOutStream = new FileOutputStream(imageFile);
        //写入数据
        fileOutStream .write(sdf);
        return base64;

    }

    /**
     * 图片流转化成base64
     */
    public static String getImageBinary(BufferedImage bi, String format) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();// io流
            ImageIO.write(bi, format, baos);// 写入流中
            byte[] bytes = baos.toByteArray();// 转换成字节
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encodeBuffer(bytes).trim();// 转换成base64串
            base64 = base64.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
            return base64;
        } catch (IOException e) {
//            log.info("base64转化失败");
            e.printStackTrace();
        }
        return null;
    }


}
