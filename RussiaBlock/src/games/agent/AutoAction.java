package games.agent;

import games.russiablock.ElsAIv3;
import games.russiablock.Enviroment;
import games.russiablock.GameModel;
import games.russiablock.Router;

public class AutoAction {

    public Object getRouter(Object obj) {
        Enviroment gm = (Enviroment) obj;
        ElsAIv3 aIv3 = new ElsAIv3(gm.getGmmodel(), gm.getBlk());
        aIv3.setBlocks(gm.getBlk());
        Router r = aIv3.getRouter();
        return r;
    }
}
