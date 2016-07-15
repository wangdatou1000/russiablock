/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

/**
 *
 * @author Administrator
 */
public class endblocks {

    public final int emptyparameter = 1000;
    public final int stateparameter = 100;
    public final int no_blocks_parameter = 10000;
    public int beginpiont;
    public int emptylength;
    public blocks blk = new blocks();
    public int beginlsu;
    public int endlsu;
    public int hsu;
    private int lsu;
    public int grade = no_blocks_parameter;
    public int borderparameter=0;
    public int qi=0;
    public endblocks(blocks blk){
       this.blk=blk;
    }
    public endblocks(int beginpiont, int lsu, int hsu) {
        this.beginpiont = beginpiont;
        this.emptylength = 1;
        this.hsu = hsu;
        this.lsu = lsu;
    }

    public endblocks() {
    }
    

    public int getbeginlsu() {
        return beginpiont % lsu;
    }

    public int getendlsu() {
        return beginlsu + emptylength - 1;
    }
}
