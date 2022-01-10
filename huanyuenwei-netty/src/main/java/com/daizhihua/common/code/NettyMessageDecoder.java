package com.daizhihua.common.code;

import com.daizhihua.util.PassUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.Delimiter;

import java.security.acl.LastOwnerException;
import java.util.List;

/**
 * 自定义解码器自定义解码器
 */
@Slf4j
public class NettyMessageDecoder extends ByteToMessageDecoder {



    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("开始解码");
        int length = byteBuf.readableBytes();
        log.info("数据长度：{}",length);
        byteBuf.markReaderIndex(); // 我们标记一下当前的readIndex的位置

        // 解码后消息对象
//        GpsMessage gpsMessage = new GpsMessage();
        byte packetLen = byteBuf.readByte();
        int nPacketLen = packetLen & 0xff;
        log.info("{}",nPacketLen);
        /**
         * 协议
         */
        byte agreement = byteBuf.readByte();
        log.info("{}",agreement);
//        if (agreement == Delimiter.LOGIN_PACKET) { // 登录包
//            LoginMsg loginMsg = new LoginMsg();
//            frame = CrcUtils.decodeCodeIDFrame(ctx, in);
            String sCode = PassUtil.bytesToHexString(byteBuf);
            System.out.println("编号：" + sCode);
//            loginMsg.setCardId(sCode);
//            gpsMessage.setContent(loginMsg);
//        } else if (agreement == Delimiter.STATUS_PACKET) {// 心跳包
            System.out.println(" 心跳包：");
//            frame = CrcUtils.decodeCodeIDFrame(ctx, in);
            String sContent = PassUtil.bytesToHexString(byteBuf);
            log.info("心跳包内容：" + sContent);
            //gpsMessage.setContent(sContent);
//        }
        list.add(sContent);
        log.info("解码结束!");

    }
}
