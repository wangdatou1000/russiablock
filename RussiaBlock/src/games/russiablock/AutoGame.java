package games.russiablock;

import java.util.ArrayList;
import java.util.List;

public class AutoGame {

	public GameModel gm, gmnext;
	private volatile Blocks blk, blknext;
	private int fensu;
	private int eliminateRows = 0;
	private int blocksIndex = 0;
	public int sleeptime;
	public boolean next = false;
	private boolean iscomputer = true;
	private List<Blocks> allBlocks = new ArrayList<Blocks>();
	private List<Integer> eliminateRowList = new ArrayList<>();
	private Runnable logic = () -> {
		if (allBlocks.size() != 0) {
			if (go(gm.getColumnNum())) {
				try {
					Thread.sleep(50);
				} catch (Exception e) {
				}
			} else {
				processOneEnd();
				try {
					Thread.sleep(5);
				} catch (Exception e) {
				}
			}
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
		creatAllNewBlocks();
	}

	private void creatAllNewBlocks() {
		int n = 0;
		allBlocks.clear();
		while (n++ < 900000) {
			int blkkinds = (int) (1 + Math.random() * 7);
			int blkstate = (int) (1 + Math.random() * 4);
			Blocks tempBlk = new Blocks(gm.getColumnNum(), blkkinds, blkstate);
			allBlocks.add(tempBlk);
		}
		System.out.println("create ok!!");
	}

	public void createnewblocks() {
		// System.out.println("index=" + blocksIndex);
		if (blocksIndex >= allBlocks.size()) {
			creatAllNewBlocks();
			blocksIndex = 0;
		}
		if (iscomputer) {
			blk = allBlocks.get(blocksIndex++);
			gm.change(blk, gm.MOVINGCOLOR, 0, false);
		} else {
			blk = allBlocks.get(blocksIndex);
			blknext = allBlocks.get(blocksIndex + 1);
			gm.change(blk, gm.MOVINGCOLOR, gm.BACKCOLOR, false);
			gmnext.cleanupgamepanel();
			blknext = new Blocks(gmnext.getColumnNum(), blknext.kinds, blknext.state);
			gmnext.change(blknext, gmnext.MOVINGCOLOR, gmnext.BACKCOLOR, false);
			blocksIndex++;
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
			eliminateRows += defen;
			// sleeptime -= defen * 1.3;
		}
	}// 每一次方块到底后都要判断是否可以削了.

	public void overtest() {
		if (gm.isgameover()) {
			int again = 0;
			// JOptionPane.showConfirmDialog(null,
			// "GAME OVER!!\n你的分数是:" +
			// fensu +
			// "\n要再来一次吗?", "提示---datou!!",
			// JOptionPane.YES_NO_OPTION);
			eliminateRowList.add(eliminateRows);
			if (iscomputer)
				System.out.println("scores:" + fensu + "----Rows:" + eliminateRows);
			fensu = 0;
			eliminateRows = 0;
			if (again == 0) {
				gm.cleanupgamepanel();
				fensu = 0;
				// sleeptime = 100;
			} else {
				System.exit(0);
			}
		}
	}

	public int getscores() {
		return fensu;
	}

	public int getEliminateRows() {
		return eliminateRows;
	}

	public void setEliminateRows(int eliminateRows) {
		this.eliminateRows = eliminateRows;
	}

	public Blocks getblk() {
		return blk;
	}

}
