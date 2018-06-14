package com.abin.lee.im.cserver.handler;

import com.abin.lee.im.cserver.chat.ChannelRelationShip;
import com.abin.lee.im.cserver.enums.ChatType;
import com.abin.lee.im.custom.common.protocol.SmartCarProtocol;
import com.abin.lee.im.custom.common.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by abin on 2018/6/14.
 */

public class ImServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger LOGGER = LogManager.getLogger(ImServerHandler.class);

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
            ctx.writeAndFlush(response);
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
            channel.writeAndFlush(response);
            // .addListener(ChannelFutureListener.CLOSE);

            // 当有写操作时，不需要手动释放msg的引用
            // 当只有读操作时，才需要手动释放msg的引用
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // cause.printStackTrace();
        ctx.close();
    }
}