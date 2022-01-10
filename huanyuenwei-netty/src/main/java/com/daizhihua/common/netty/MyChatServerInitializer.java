package com.daizhihua.common.netty;

import com.daizhihua.common.code.NettyMessageDecoder;
import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * netty重写类 通道初始化器ChannelInitializer
 */
@Slf4j
@Component
@Data
public class MyChatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${alltime}")
    private Integer allIdleTime;



    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        log.info("===================网络连接======================");
        log.info("有客户端连接了");
        log.info("ip是:{}",channel.localAddress().getHostName());
        log.info("端口是:{}",channel.localAddress().getPort());
        ChannelPipeline pipeline = channel.pipeline();
        //设置服务端的字符串解码
        //pipeline.addLast(new ByteToStringDecoder());
        pipeline.addLast(new NettyMessageDecoder());

        /**
         * readerIdleTime读空闲超时时间设定，如果channelRead()方法超过readerIdleTime时间未被调用则会触发超时事件调用userEventTrigger()方法；
         *
         * writerIdleTime写空闲超时时间设定，如果write()方法超过writerIdleTime时间未被调用则会触发超时事件调用userEventTrigger()方法；
         *
         * allIdleTime所有类型的空闲超时时间设定，包括读空闲和写空闲；
         */
        pipeline.addLast(new IdleStateHandler(0,0,allIdleTime, TimeUnit.SECONDS));

        pipeline.addLast();

    }
}
