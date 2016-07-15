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

/**
 * 该类主要负责构建游戏区的面板，并定义了游戏面板的�?些功能�??
 *
 * @author Administrator
 */
public class GameDisplay extends Container {

	private final int lsu, hsu;
	private final int blocksize, gamew, gameh;
	private Color blockbordercolor, blockbackcolor, blockmovingcolor, blockendcolor;
    private JPanel blocks[];
	public GameModel gmmodel;
	private int oldVersion;
	private Runnable logic = () -> {
		if (oldVersion != gmmodel.getVersion()) {
			for (int i = 0, j = gmmodel.gmarray.length; i < j; i++) {
				if (gmmodel.gmarray[i] == gmmodel.BACKCOLOR) {
					blocks[i].setBackground(blockbackcolor);
				} else if (gmmodel.gmarray[i] == gmmodel.ENDCOLOR) {
					blocks[i].setBackground(blockendcolor);
				} else if (gmmodel.gmarray[i] == gmmodel.MOVINGCOLOR) {
					blocks[i].setBackground(blockmovingcolor);
				}
			}
			oldVersion = gmmodel.getVersion();
			// System.out.println("the Thread-" + Thread.currentThread().getId()
			// + " has modify,version=" + oldVersion);
		} else {
			// System.out.println("the Thread-" + Thread.currentThread().getId()
			// + " not modify,version=" + oldVersion);
		}
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	public MyThreadV2 xia = new MyThreadV2(logic);

	public GameDisplay(int hsu, int lsu, int blocksize, Color blockbackcolor, Color blockbordercolor,
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
		this.gmmodel = new GameModel(hsu, lsu);
		this.oldVersion = gmmodel.getVersion();
		xia.start();
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
}

