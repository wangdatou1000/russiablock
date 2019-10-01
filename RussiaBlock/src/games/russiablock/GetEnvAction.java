package games.russiablock;

import javax.swing.*;

public class GetEnvAction implements Command{
    private AutoGame computergame;
    public GetEnvAction(AutoGame computergame){
        this.computergame=computergame;
    }
    @Override
    public Object action(Object obj) {
        return new Enviroment(computergame.gm,computergame.getblk());
    }
}
