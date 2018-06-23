package com.abin.lee.im.websocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import java.net.InetAddress;
/**
 * Created by abin on 2018/6/23.
 */
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {
    private String result="";
    private HttpRequest request;
    /*
     * 收到消息时，返回信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;

            String uri = request.getUri();
            System.out.println("Uri:" + uri);
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();

            String res = "I am OK";
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                    response.content().readableBytes());
            if (HttpHeaders.isKeepAlive(request)) {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.flush();
        }


//        if(! (msg instanceof FullHttpRequest)){
//            result="未知请求!";
//            send(ctx,result,HttpResponseStatus.BAD_REQUEST);
//            return;
//        }
//        DefaultFullHttpRequest httpRequest = (DefaultFullHttpRequest)msg;
//        try{
//            String path=httpRequest.getUri();          //获取路径
//            String body = getBody(httpRequest);     //获取参数
//            HttpMethod method=httpRequest.getMethod();//获取请求方法
//            //如果不是这个路径，就直接返回错误
//            if(!"/test".equalsIgnoreCase(path)){
//                result="非法请求!";
//                send(ctx,result,HttpResponseStatus.BAD_REQUEST);
//                return;
//            }
//            System.out.println("接收到:"+method+" 请求");
//            //如果是GET请求
//            if(HttpMethod.GET.equals(method)){
//                //接受到的消息，做业务逻辑处理...
//                System.out.println("body:"+body);
//                result="GET请求";
//                send(ctx,result,HttpResponseStatus.OK);
//                return;
//            }
//            //如果是POST请求
//            if(HttpMethod.POST.equals(method)){
//                //接受到的消息，做业务逻辑处理...
//                System.out.println("body:"+body);
//                result="POST请求";
//                send(ctx,result,HttpResponseStatus.OK);
//                return;
//            }
//
//            //如果是PUT请求
//            if(HttpMethod.PUT.equals(method)){
//                //接受到的消息，做业务逻辑处理...
//                System.out.println("body:"+body);
//                result="PUT请求";
//                send(ctx,result,HttpResponseStatus.OK);
//                return;
//            }
//            //如果是DELETE请求
//            if(HttpMethod.DELETE.equals(method)){
//                //接受到的消息，做业务逻辑处理...
//                System.out.println("body:"+body);
//                result="DELETE请求";
//                send(ctx,result,HttpResponseStatus.OK);
//                return;
//            }
//        }catch(Exception e){
//            System.out.println("处理请求失败!");
//            e.printStackTrace();
//        }finally{
//            //释放请求
//            httpRequest.release();
//        }
    }
    /**
     * 获取body参数
     * @param request
     * @return
     */
    private String getBody(FullHttpRequest request){
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }

    /**
     * 发送的返回值
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
     */
    private void send(ChannelHandlerContext ctx, String context,HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush("客户端"+ InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
        super.channelActive(ctx);
    }
}