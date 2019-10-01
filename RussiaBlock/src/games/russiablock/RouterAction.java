package games.russiablock;

import javax.swing.*;

public class RouterAction implements Command{
    private AutoGame computergame;
    public RouterAction(AutoGame computergame){
        this.computergame=computergame;
    }
    @Override
    public Object action(Object obj) {
        Router r=(Router)obj;
        while (r != null && r.state != computergame.getblk().state) {
            computergame.circumvolve();
        }
        computergame.go(r.x);
        computergame.next = false;
        while (!computergame.next){
            computergame.go(11);
             try {
             // Thread.sleep(100 + computergame.getscores() / 300);
             Thread.sleep(10);
             } catch (Exception e) {
             }
        }

        return new Enviroment(computergame.gm,computergame.getblk());
    }
}
