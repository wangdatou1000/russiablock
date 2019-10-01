package tools.net.Client;
/*
 * @author uv
 * @date 2018/10/12 20:54
 *
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import tools.net.Server.*;

import java.util.UUID;

public class NettyClient {

    private final String host;
    private final int port;
    private Channel channel;

    public Class<?> getRequest() {
        return request;
    }

    public void setRequest(Class<?> request) {
        this.request = request;
    }

    private Class<?> request;

    public Class<?> getResponse() {
        return response;
    }

    public void setResponse(Class<?> response) {
        this.response = response;
    }

    private Class<?> response;

    public ChannelInboundHandlerAdapter getHandler() {
        return handler;
    }

    public void setHandler(ChannelInboundHandlerAdapter handler) {
        this.handler = handler;
    }

    private ChannelInboundHandlerAdapter handler;


    //连接服务端的端口号地址和端口号
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)  // 使用NioSocketChannel来作为连接用的channel类
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("正在连接中...----");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) //解码request
                                .addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) //使用Ser
                                .addLast(handler); //客户端处理类

                    }
                });
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接服务器成功");

                } else {
                    System.out.println("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });

        this.channel = future.channel();
    }

    public Channel getChannel() {
        return channel;
    }

    public static void main(String[] args) throws Exception {

        NettyClient client = new NettyClient("127.0.0.1", 8080);
//        启动client服务
        client.start();

        Channel channel = client.getChannel();
        int id = 0;

        //消息体
        RpcRequest request = new RpcRequest();
//        while (true){

        request.setId("GetEnv");
        request.setData("client.message");
        request.setStatus(id++);
        channel.writeAndFlush(request);
        Thread.sleep(1000);
            System.out.println("send ok--"+id);
//        }
    }
}
