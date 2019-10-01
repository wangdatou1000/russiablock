package games.agent;

import games.russiablock.MyThreadV2;
import io.netty.channel.Channel;
import tools.net.Client.NettyClient;
import tools.net.Server.RpcRequest;
import tools.net.Server.RpcResponse;

public class AIAgent {
    public static volatile boolean isok = false;
    public static RpcResponse response;
    private NettyClient client = new NettyClient("127.0.0.1", 8080);
    private MyThreadV2 logic;

    private Runnable getAILogic() {
        return () -> {
            AutoAction act = new AutoAction();
            client.setHandler(new ClientHandler());
            try {
                client.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Channel channel = client.getChannel();
            //消息体
            RpcRequest request = new RpcRequest();
//        while (true){
            request.setId("StartGame");
            request.setData("client.message");
            channel.writeAndFlush(request);
            while (true) {

                if (isok && response != null) {
                    if (response.getId().equals("Router") ||
                            response.getId().equals("GetEnv") ||
                            response.getId().equals("StartGame")) {
                        RpcRequest res = new RpcRequest();
                        res.setId("Router");
                        res.setData(act.getRouter(response.getData()));
//                       request.setData("client.message");
                        channel.writeAndFlush(res);
                        isok = false;
                    }

                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    ;

    public void init() throws Exception {
        logic = new MyThreadV2(getAILogic());
        logic.start();

    }

    public static void main(String[] args) throws Exception {
        AIAgent agent = new AIAgent();
        agent.init();

    }

}
