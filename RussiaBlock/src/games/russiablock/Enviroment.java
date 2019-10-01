package games.russiablock;

import java.io.Serializable;

public class Enviroment implements Serializable {
    private static final long serialVersionUID = 4923081103118853877L;
    public Enviroment(GameModel gmmodel, Blocks blk) {
        this.gmmodel = gmmodel;
        this.blk = blk;
    }

    public GameModel getGmmodel() {
        return gmmodel;
    }

    public void setGmmodel(GameModel gmmodel) {
        this.gmmodel = gmmodel;
    }

    public Blocks getBlk() {
        return blk;
    }

    public void setBlk(Blocks blk) {
        this.blk = blk;
    }

    private GameModel gmmodel;
    private Blocks blk;

}
