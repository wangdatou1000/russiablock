package games.russiablock;

import javax.swing.JOptionPane;

public class autogame {

    public gamedisplay gm,  gmnext;
    private blocks blk,  blknext;
    private int fensu;
    public int sleeptime;
    public boolean next = false;
    private boolean iscomputer = true;
    public xiatimer xia = new xiatimer();

    public autogame(gamedisplay gm, boolean iscomputer, gamedisplay gmnext) {
        this.gm = gm;
        this.gmnext = gmnext;
        this.iscomputer = iscomputer;
    }

    public void createnewblocks() {
        if (iscomputer) {
            int blkkinds = (int) (1 + Math.random() * 7);
            int blkstate = (int) (1 + Math.random() * 4);
            blk = new blocks(gm.getlsu(), blkkinds, blkstate);
            gm.gmmodel.change(blk, gm.gmmodel.blockmovingcolor, 0, false);
            gm.gamedisplay(blk, gm.getblockmovingcolor(),null,false);
        } else {
            int nextblkkinds, blkkinds, nextblkstate, blkstate;
            if (blknext != null) {
                blk.kinds = blknext.kinds;
                blk.state = blknext.state;
                gmnext.gamedisplay(blknext, gmnext.getblockbackcolor(),null,false);
                blknext.kinds = (int) (1 + Math.random() * 7);
                blknext.state = (int) (1 + Math.random() * 4);
                blk = new blocks(gm.getlsu(), blk.kinds, blk.state);
                blknext = new blocks(gmnext.getlsu(), blknext.kinds, blknext.state);
            } else {
                blkkinds = (int) (1 + Math.random() * 7);
                nextblkkinds = (int) (1 + Math.random() * 7);
                blkstate = (int) (1 + Math.random() * 4);
                nextblkstate = (int) (1 + Math.random() * 4);
                blk = new blocks(gm.getlsu(), blkkinds, blkstate);
                blknext = new blocks(gmnext.getlsu(), nextblkkinds, nextblkstate);
            }
            gm.gmmodel.change(blk, gm.gmmodel.blockmovingcolor, 0, false);
            gm.gamedisplay(blk, gm.getblockmovingcolor(),null,false);
            gmnext.gamedisplay(blknext, gmnext.getblockmovingcolor(),null,false);

        }
    }

    public void go(int p) {
        if (gm.gmmodel.cango(p, blk)) {
            gm.go(blk);
        }
    }
    public void circumvolve() {
        gm.circumvolve(blk);
    }
    public void getscore() {
        int defen = gm.gmmodel.eliminateblocks();
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
            sleeptime -= defen * 2;
        }
    }//每一次方块到底后都要判断是否可以削了.

    public void overtest() {
        if (gm.gmmodel.isgameover()) {
            int again = JOptionPane.showConfirmDialog(null,
                    "GAME OVER!!\n你的分数�?:" +
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

    public blocks getblk() {
        return blk;
    }

    class xiatimer extends MyThreadV2 {

        public xiatimer() {
        }

		@Override
		protected void runPersonelLogic() {
			if (gm.gmmodel.cango(gm.getlsu(), blk)) {
                gm.go(blk);
            } else {
                gm.endblocks(blk);
                getscore();
                gm.displayall();
                overtest();
                createnewblocks();
                next = true;
            }
            try {
                sleep(sleeptime);
            } catch (Exception e) {
            }
			
		}
    }
}
