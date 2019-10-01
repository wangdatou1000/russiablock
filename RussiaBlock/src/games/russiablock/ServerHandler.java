package games.russiablock;
/*
 * @author uv
 * @date 2018/10/12 18:33
 * 处理服务端接收的数据
 */

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import tools.net.Server.RpcRequest;
import tools.net.Server.RpcResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter{
    public ServerHandler(Map<String,Command> Commands){
        this.Commands=Commands;
    }
    private Map<String,Command> Commands;
    //接受client发送的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest) msg;
//        System.out.println("接收到客户端信息:" + request.toString());
        //返回的数据结构
        String cmd=request.getId();
        Object obj=((Command)Commands.get(cmd)).action(request.getData());
        RpcResponse response = new RpcResponse();
        response.setId(request.getId());
        response.setData(obj);
        response.setStatus(request.getStatus());
        ctx.writeAndFlush(response);
    }

    //通知处理器最后的channelRead()是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("服务端接收数据完毕..");
        ctx.flush();
    }

    //读操作时捕获到异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    //客户端去和服务端连接成功时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
//        ctx.writeAndFlush("hello client");
    }
}
