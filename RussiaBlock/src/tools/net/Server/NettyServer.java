package tools.net.Server;
/*
 * @author uv
 * @date 2018/10/12 18:25
 * 服务端
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


public class NettyServer {
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

    ObjectDecoder objectDecoder;

    public void bind(int port) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(); //bossGroup就是parentGroup，是负责处理TCP/IP连接的
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //workerGroup就是childGroup,是负责处理Channel(通道)的I/O事件

        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup, workerGroup);
        sb.channel(NioServerSocketChannel.class);
        sb.option(ChannelOption.SO_BACKLOG, 1024);
        sb.childOption(ChannelOption.SO_KEEPALIVE, true);


        sb.childHandler(new ChannelInitializer<SocketChannel>() {  // 绑定客户端连接时候触发操作
            @Override
            protected void initChannel(SocketChannel sh) throws Exception {
                sh.pipeline()
                            .addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) //解码request
                        .addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) //使用ServerHandler类来处理接收到的消息
                        .addLast(handler);
            }
        });
//绑定监听端口，调用sync同步阻塞方法等待绑定操作完成，完成后返回ChannelFuture类似于JDK中Future
        ChannelFuture future = sb.bind(port).sync();

        if (future.isSuccess()) {
            System.out.println("服务端启动成功");
        } else {
            System.out.println("服务端启动失败");
            future.cause().printStackTrace();
            bossGroup.shutdownGracefully(); //关闭线程组
            workerGroup.shutdownGracefully();
        }

        //成功绑定到端口之后,给channel增加一个 管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程。
        future.channel().closeFuture().sync();

    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind(8080);
    }

}
