package com.abin.lee.im.cserver.handler;

import com.abin.lee.im.cserver.chat.ChannelRelationShip;
import com.abin.lee.im.cserver.enums.ChatType;
import com.abin.lee.im.custom.common.protocol.SmartCarProtocol;
import com.abin.lee.im.custom.common.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by abin on 2018/6/14.
 */

public class ImServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger LOGGER = LogManager.getLogger(ImServerHandler.class);
    // 心跳丢失计数器
    private int counter;

    // 用于获取客户端发送的信息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // 用于获取客户端发来的数据信息
        SmartCarProtocol body = (SmartCarProtocol) msg;
        String postmsg = body.byteToString();

        System.out.println("Server接受的客户端的信息 :" + postmsg);
        Map<String, String> message = JsonUtil.decodeJson(postmsg, new TypeReference<Map<String, String>>() {
        });
        if (StringUtils.equals(message.get("msgType"), ChatType.LOGIN.name())) {
            String userId = message.get("userId") ;
            ChannelRelationShip.putChannel(userId, ctx.channel());
            LOGGER.info("ChannelRelationShip.request= " + ChannelRelationShip.request);

            String str = "用户userId=" + userId + " LOGIN SUCCESS .......";
            SmartCarProtocol response = new SmartCarProtocol(str.getBytes().length,
                    str.getBytes());
            // 当服务端完成写操作后，关闭与客户端的连接
//            ctx.writeAndFlush(response);
            loginService(ctx, response);
            // .addListener(ChannelFutureListener.CLOSE);

            // 当有写操作时，不需要手动释放msg的引用
            // 当只有读操作时，才需要手动释放msg的引用
        }


//        ChannelRelationShip.putChannel(message.get("userId"), (Channel) ctx);
//        LOGGER.info("msg : ="+ JsonUtil.toJson(msg));
        Channel channel = null;
        if (StringUtils.equals(message.get("msgType"), ChatType.CHAT.name())) {
            channel = ChannelRelationShip.getChannel(message.get("toUserId"));

            // 会写数据给客户端
            String str = "userId=" + message.get("userId") + "和你说：" + message.get("chatMsg") + " ....";
            SmartCarProtocol response = new SmartCarProtocol(str.getBytes().length,
                    str.getBytes());
            // 当服务端完成写操作后，关闭与客户端的连接
//            channel.writeAndFlush(response);
            chatService(channel,response);
            // .addListener(ChannelFutureListener.CLOSE);

            // 当有写操作时，不需要手动释放msg的引用
            // 当只有读操作时，才需要手动释放msg的引用
        }


    }

    public void loginService(ChannelHandlerContext ctx ,SmartCarProtocol response){
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // 当服务端完成写操作后，关闭与客户端的连接
                ctx.writeAndFlush(response);
                // .addListener(ChannelFutureListener.CLOSE);

                // 当有写操作时，不需要手动释放msg的引用
                // 当只有读操作时，才需要手动释放msg的引用
            }
        });
    }

    public void chatService(Channel channel ,SmartCarProtocol response){
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                channel.writeAndFlush(response);
            }
        });
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // cause.printStackTrace();
        ctx.close();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            // 不管是读事件空闲还是写事件空闲都向服务器发送心跳包
        if (evt instanceof IdleStateEvent) {
            // 空闲6s之后触发 (心跳包丢失)
            if (counter >= 3) {
                // 连续丢失3个心跳包 (断开连接)
                ctx.channel().close().sync();
                System.out.println("已与Client断开连接");
            } else {
                counter++;
                System.out.println("丢失了第 " + counter + " 个心跳包");
            }
        }
    }
}