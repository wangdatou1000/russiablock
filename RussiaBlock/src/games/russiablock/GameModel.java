/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

/**
 *
 * @author Administrator
 */
public class gamemodel {

    private int lsu,  hsu;
    public int gmarray[];
    public final int blockendcolor = 3;
    public final int blockbackcolor = 0;
    public final int blockmovingcolor = 1;

    public gamemodel(int hsu, int lsu) {
        this.lsu = lsu;
        this.hsu = hsu;
        gmarray = new int[hsu * lsu];
        for (int n = 0; n < lsu * hsu; n++) {
            gmarray[n] = blockbackcolor;
        }
    }

    public int eliminateblocks() {
        int defen = 0;
        boolean xiao = true;
        for (int n = hsu - 1; n > 0; n--) {
            for (int t = n * lsu; t < (n + 1) * lsu; t++) {
                if (gmarray[t] != blockendcolor) {
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
        return defen;
    }

    public boolean cancircumvolve(blocks blk) {
        if (blk.w2 < 0 || blk.w1 < 0 || blk.w3 < 0 || blk.w4 < 0) {
            return false;
        }
        if (blk.w1 > hsu * lsu - 1 || blk.w2 > hsu * lsu - 1 ||
                blk.w3 > hsu * lsu - 1 || blk.w4 > hsu * lsu - 1) {
            return false;
        }
        if ((blk.w1 % lsu == 0 || blk.w2 % lsu == 0 || blk.w3 % lsu == 0 ||
                blk.w4 % lsu == 0) && (blk.w1 % lsu == lsu - 1 ||
                blk.w2 % lsu == lsu - 1 || blk.w3 % lsu == lsu - 1 ||
                blk.w4 % lsu == lsu - 1)) {
            return false;
        }
        if (gmarray[blk.w1] == blockendcolor ||
                gmarray[blk.w2] == blockendcolor ||
                gmarray[blk.w3] == blockendcolor ||
                gmarray[blk.w4] == blockendcolor) {
            return false;
        }
        return true;
    }//判断能不能旋�?.

    public boolean cango(int p, blocks blk) {
        blk.gotest(p);
        if (!is_overborderline(blk)) {
            if (gmarray[blk.w1] == blockendcolor ||
                    gmarray[blk.w2] == blockendcolor ||
                    gmarray[blk.w3] == blockendcolor ||
                    gmarray[blk.w4] == blockendcolor) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void cleanupgamepanel() {
        for (int n = 0; n < hsu * lsu; n++) {
            gmarray[n] = blockbackcolor;
        }
    }

    public boolean isgameover() {
        return gmarray[lsu / 2] == blockendcolor;
    }

    public void go(blocks blk) {
        change(blk, blockbackcolor, blockmovingcolor,true);
    }

    public void change(blocks blk, int backcolor, int movingcolor, boolean is_two) {

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
    }

    public boolean is_overborderline(blocks blk) {
        //判断是否到边�?.
        if ((blk.w1 % lsu == 0 || blk.w2 % lsu == 0 || blk.w3 % lsu == 0 ||
                blk.w4 % lsu == 0) && (blk.w1 % lsu == lsu - 1 ||
                blk.w2 % lsu == lsu - 1 || blk.w3 % lsu == lsu - 1 ||
                blk.w4 % lsu == lsu - 1)) {
            return true;
        }
//       判断旋转后左右两边界是否都会出现方块，如果是则已经超过边界�??
        if (blk.w1 % lsu == 0 && blk.b1 % lsu == lsu - 1) {
            return true;
        }
//       判断右移是否过界
        if (blk.w1 % lsu == lsu - 1 && blk.b1 % lsu == 0) {
            return true;
        }
//        判断左移是否过界
        if (blk.w1 > hsu * lsu - 1 || blk.w2 > hsu * lsu - 1 ||
                blk.w3 > hsu * lsu - 1 || blk.w4 > hsu * lsu - 1) {
            return true;
        }
//        判断下移是否过界
        if (blk.w2 < 0 || blk.w1 < 0 || blk.w3 < 0 || blk.w4 < 0) {
            return true;
        }
//        判断是否超出上面的边�?
        return false;
    }
}
