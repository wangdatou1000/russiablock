package games.agent;
/*
 * @author uv
 * @date 2018/10/12 20:56
 * client消息处理类
 */


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import tools.net.Server.RpcRequest;
import tools.net.Server.RpcResponse;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    //处理服务端返回的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object response) throws Exception {
//        System.out.println("接受到server响应数据: " + response.toString());
        RpcResponse res = (RpcResponse) response;
        RpcRequest req=new RpcRequest();
        AIAgent.response=res;
        AIAgent.isok=true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
