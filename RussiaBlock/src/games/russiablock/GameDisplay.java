/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**该类主要负责构建游戏区的面板，并定义了游戏面板的�?些功能�??
 *
 * @author Administrator
 */
public class gamedisplay extends Container {

    private final int lsu,  hsu;
    private final int blocksize,  gamew,  gameh;
    private Color blockbordercolor,  blockbackcolor,  blockmovingcolor,  blockendcolor;
    private JPanel blocks[];
    public gamemodel gmmodel;

    public gamedisplay(int hsu, int lsu, int blocksize,
            Color blockbackcolor, Color blockbordercolor,
            Color blockmovingcolor, Color blockendcolor) {
        this.lsu = lsu;
        this.hsu = hsu;
        this.blocksize = blocksize;
        this.blockbackcolor = blockbackcolor;
        this.blockbordercolor = blockbordercolor;
        this.blockmovingcolor = blockmovingcolor;
        this.blockendcolor = blockendcolor;
        blocks = new JPanel[hsu * lsu];
        gamew = lsu * blocksize;
        gameh = hsu * blocksize;
        creatgamepanel();
        gmmodel = new gamemodel(hsu, lsu);
    }

    public int getlsu() {
        return this.lsu;
    }

    public int gethsu() {
        return this.hsu;
    }

    public int getblocksize() {
        return this.blocksize;
    }

    public Color getblockendcolor() {
        return this.blockendcolor;
    }

    public Color getblockbackcolor() {
        return this.blockbackcolor;
    }

    public Color getblockbordercolor() {
        return this.blockbordercolor;
    }

    public Color getblockmovingcolor() {
        return this.blockmovingcolor;
    }

    public void setblockendcolor(Color blockendcolor) {
        this.blockendcolor = blockendcolor;
    }

    public void setblockbackcolor(Color blockbackcolor) {
        this.blockbackcolor = blockbackcolor;
    }

    public void setblockbordercolor(Color blockbordercolor) {
        this.blockbordercolor = blockbordercolor;
    }

    public void setblockmovingcolor(Color blockmovingcolor) {
        this.blockmovingcolor = blockmovingcolor;
    }

    public void creatgamepanel() {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(hsu, lsu));
        this.setSize(gamew, gameh);
        for (int n = 0; n < lsu * hsu; n++) {
            blocks[n] = new JPanel();
            blocks[n].setBorder(new LineBorder(blockbordercolor));
            blocks[n].setBackground(blockbackcolor);
            this.add(blocks[n]);
        }
        this.setVisible(true);
    }

    public void displayall() {
        for (int n = 0; n < hsu * lsu; n++) {
            if (gmmodel.gmarray[n] == gmmodel.blockbackcolor) {
                blocks[n].setBackground(blockbackcolor);
            } else if (gmmodel.gmarray[n] == gmmodel.blockendcolor) {
                blocks[n].setBackground(blockendcolor);
            }
        }
    }

    public void gamedisplay(blocks blk, Color backcolor, Color movingcolor, boolean is_two) {

        this.getComponent(blk.b1).setBackground(backcolor);
        this.getComponent(blk.b2).setBackground(backcolor);
        this.getComponent(blk.b3).setBackground(backcolor);
        this.getComponent(blk.b4).setBackground(backcolor);
        if (is_two) {
            this.getComponent(blk.w1).setBackground(movingcolor);
            this.getComponent(blk.w2).setBackground(movingcolor);
            this.getComponent(blk.w3).setBackground(movingcolor);
            this.getComponent(blk.w4).setBackground(movingcolor);
        }
    }
//==============================================================================
//==============================================================================

    public void go(blocks blk) {
        gmmodel.change(blk, gmmodel.blockbackcolor, gmmodel.blockmovingcolor, true);
        gamedisplay(blk, getblockbackcolor(), getblockmovingcolor(), true);
        blk.go();
    }

    public void circumvolve(blocks blk) {
        blk.circumvolvetest();
        if (gmmodel.cancircumvolve(blk)) {
            gmmodel.change(blk, gmmodel.blockbackcolor, gmmodel.blockmovingcolor, true);
            gamedisplay(blk, getblockbackcolor(), getblockmovingcolor(), true);
            blk.circumvolve();
        }
    }

    public void cleanupgamepanel() {
        gmmodel.cleanupgamepanel();
        displayall();
    }

    public void endblocks(blocks blk) {
        gmmodel.change(blk, gmmodel.blockendcolor, 0, false);
        gamedisplay(blk, getblockendcolor(), null, false);
    }
}
