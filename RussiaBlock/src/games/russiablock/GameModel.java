/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class GameModel implements Serializable {
    private static final long serialVersionUID = 4923081703118853877L;
	private int lsu, hsu;
	public volatile int gmarray[];
	public final int ENDCOLOR = 3;
	public final int BACKCOLOR = 0;
	public final int MOVINGCOLOR = 1;
	private volatile int version = 0;

	public GameModel(int hsu, int lsu) {
        this.lsu = lsu;
        this.hsu = hsu;
        gmarray = new int[hsu * lsu];
        for (int n = 0; n < lsu * hsu; n++) {
			gmarray[n] = BACKCOLOR;
        }
		version++;
    }

	public int getVersion() {
		return version;
	}

	public int getRowNum() {
		return hsu;
	}

	public int getColumnNum() {
		return lsu;
	}
    public int eliminateblocks() {
        int defen = 0;
        boolean xiao = true;
        for (int n = hsu - 1; n > 0; n--) {
            for (int t = n * lsu; t < (n + 1) * lsu; t++) {
				if (gmarray[t] != ENDCOLOR) {
                    xiao = false;
                    break;
                }
            }
            if (xiao) {
                for (int r = n; r > 0; r--) {
                    for (int rr = r * lsu; rr < (r + 1) * lsu; rr++) {
                        gmarray[rr] = gmarray[rr - lsu];
                    }
                }
                n = n + 1;
                defen += 1;
            }
            xiao = true;
        }
		version++;
        return defen;
    }

	public boolean cancircumvolve(Blocks blk) {
		blk.circumvolvetest();
        if (blk.w2 < 0 || blk.w1 < 0 || blk.w3 < 0 || blk.w4 < 0) {
            return false;
        }
		if (blk.w1 > hsu * lsu - 1 || blk.w2 > hsu * lsu - 1 || blk.w3 > hsu * lsu - 1 || blk.w4 > hsu * lsu - 1) {
            return false;
        }
		if ((blk.w1 % lsu == 0 || blk.w2 % lsu == 0 || blk.w3 % lsu == 0 || blk.w4 % lsu == 0)
				&& (blk.w1 % lsu == lsu - 1 || blk.w2 % lsu == lsu - 1 || blk.w3 % lsu == lsu - 1
						|| blk.w4 % lsu == lsu - 1)) {
            return false;
        }
		if (gmarray[blk.w1] == ENDCOLOR || gmarray[blk.w2] == ENDCOLOR || gmarray[blk.w3] == ENDCOLOR
				|| gmarray[blk.w4] == ENDCOLOR) {
            return false;
        }
        return true;
	}// 判断能不能旋�?.

	public boolean cango(int p, Blocks blk) {
		if (blk == null)
			return false;
        blk.gotest(p);
        if (!is_overborderline(blk)) {
			if (gmarray[blk.w1] == ENDCOLOR || gmarray[blk.w2] == ENDCOLOR || gmarray[blk.w3] == ENDCOLOR
					|| gmarray[blk.w4] == ENDCOLOR) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void cleanupgamepanel() {
        for (int n = 0; n < hsu * lsu; n++) {
			gmarray[n] = BACKCOLOR;
        }
		version++;
    }

    public boolean isgameover() {
		return gmarray[lsu / 2] == ENDCOLOR;
    }

	public void goOrCircumvolve(Blocks blk, int type) {
		change(blk, BACKCOLOR, MOVINGCOLOR, true);
		if (type == 1) {
			blk.go();
		} else {
			blk.circumvolve();
		}
    }

	public void change(Blocks blk, int backcolor, int movingcolor, boolean is_two) {

        gmarray[blk.b1] = backcolor;
        gmarray[blk.b2] = backcolor;
        gmarray[blk.b3] = backcolor;
        gmarray[blk.b4] = backcolor;

        if (is_two) {
            gmarray[blk.w1] = movingcolor;
            gmarray[blk.w2] = movingcolor;
            gmarray[blk.w3] = movingcolor;
            gmarray[blk.w4] = movingcolor;
        }
		version++;
    }

	public boolean is_overborderline(Blocks blk) {
		// 判断是否到边�?.
		if ((blk.w1 % lsu == 0 || blk.w2 % lsu == 0 || blk.w3 % lsu == 0 || blk.w4 % lsu == 0)
				&& (blk.w1 % lsu == lsu - 1 || blk.w2 % lsu == lsu - 1 || blk.w3 % lsu == lsu - 1
						|| blk.w4 % lsu == lsu - 1)) {
            return true;
        }
		// 判断旋转后左右两边界是否都会出现方块，如果是则已经超过边界�??
        if (blk.w1 % lsu == 0 && blk.b1 % lsu == lsu - 1) {
            return true;
        }
		// 判断右移是否过界
        if (blk.w1 % lsu == lsu - 1 && blk.b1 % lsu == 0) {
            return true;
        }
		// 判断左移是否过界
		if (blk.w1 > hsu * lsu - 1 || blk.w2 > hsu * lsu - 1 || blk.w3 > hsu * lsu - 1 || blk.w4 > hsu * lsu - 1) {
            return true;
        }
		// 判断下移是否过界
        if (blk.w2 < 0 || blk.w1 < 0 || blk.w3 < 0 || blk.w4 < 0) {
            return true;
        }
		// 判断是否超出上面的边�?
        return false;
    }
}
