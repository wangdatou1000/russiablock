package games.russiablock;

import javax.swing.JOptionPane;

public class AutoGame {

    public GameModel gm, gmnext;
    private volatile Blocks blk, blknext;
    private int fensu;
    public int sleeptime;
    public boolean next = false;
    private boolean iscomputer = true;
    private Runnable logic = () -> {
        if (go(gm.getColumnNum())) {
        } else {
            processOneEnd();
        }
        try {
            Thread.sleep(sleeptime);
        } catch (Exception e) {
        }
    };
    public MyThreadV2 xia = new MyThreadV2(logic);

    private synchronized void processOneEnd() {
        gm.change(blk, gm.ENDCOLOR, 0, false);
        getscore();
        overtest();
        createnewblocks();
        next = true;
    }
    public AutoGame(GameModel gm, boolean iscomputer, GameModel gmnext) {
        this.gm = gm;
        this.gmnext = gmnext;
        this.iscomputer = iscomputer;
    }

    public void createnewblocks() {
        if (iscomputer) {
            int blkkinds = (int) (1 + Math.random() * 7);
            int blkstate = (int) (1 + Math.random() * 4);
            blk = new Blocks(gm.getColumnNum(), blkkinds, blkstate);
            gm.change(blk, gm.MOVINGCOLOR, 0, false);
        } else {
            int nextblkkinds, blkkinds, nextblkstate, blkstate;
            if (blknext != null) {
                blk.kinds = blknext.kinds;
                blk.state = blknext.state;
                blknext.kinds = (int) (1 + Math.random() * 7);
                blknext.state = (int) (1 + Math.random() * 4);
                blk = new Blocks(gm.getColumnNum(), blk.kinds, blk.state);
                blknext = new Blocks(gmnext.getColumnNum(), blknext.kinds, blknext.state);
            } else {
                blkkinds = (int) (1 + Math.random() * 7);
                nextblkkinds = (int) (1 + Math.random() * 7);
                blkstate = (int) (1 + Math.random() * 4);
                nextblkstate = (int) (1 + Math.random() * 4);
                blk = new Blocks(gm.getColumnNum(), blkkinds, blkstate);
                blknext = new Blocks(gmnext.getColumnNum(), nextblkkinds, nextblkstate);
            }
            gm.change(blk, gm.MOVINGCOLOR, gm.BACKCOLOR, false);
            gmnext.cleanupgamepanel();
            gmnext.change(blknext, gmnext.MOVINGCOLOR, gmnext.BACKCOLOR, false);
        }
    }

    public synchronized boolean go(int p) {
        if (gm.cango(p, blk)) {
            gm.goOrCircumvolve(blk, 1);
            return true;
        } else
            return false;

    }

    public synchronized void circumvolve() {
        if (gm.cancircumvolve(blk)) {
            gm.goOrCircumvolve(blk, 2);
        }
    }
    public void getscore() {
        int defen = gm.eliminateblocks();
        if (defen > 0) {
            if (defen == 1) {
                fensu += 100;
            } else if (defen == 2) {
                fensu += 300;
            } else if (defen == 3) {
                fensu += 500;
            } else if (defen == 4) {
                fensu += 800;
            }
            sleeptime -= defen * 1.3;
        }
    }//每一次方块到底后都要判断是否可以削了.

    public void overtest() {
        if (gm.isgameover()) {
            int again = JOptionPane.showConfirmDialog(null,
                    "GAME OVER!!\n你的分数是:" +
                    fensu +
                    "\n要再来一次吗?", "提示---datou!!",
                    JOptionPane.YES_NO_OPTION);
            if (again == 0) {
                gm.cleanupgamepanel();
                fensu = 0;
                sleeptime = 700;
            } else {
                System.exit(0);
            }
        }
    }

    public int getscores() {
        return fensu;
    }

    public Blocks getblk() {
        return blk;
    }

}
