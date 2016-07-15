package games.russiablock;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class elsmain extends JFrame {

	private final int hsu = 20, lsu = 11, fkdx = 28, tisihsu = 4, tisilsu = 5;
	private final int gameh = hsu * fkdx, gamew = lsu * fkdx;
	// 设定方块区的横数和列�?,以及�?方块的宽(正方�?,也就是方块大�?)
	private final Color clfkbk = Color.GRAY, clfk = Color.ORANGE, clfkdong = Color.BLUE,
			fkdi = Color.DARK_GRAY;
	private final Color enmitycolor = Color.RED;
	// 定义�?些颜�?
	private int sleeptime = 500;
	private int fensu = 0;
	private String s1 = "YOUR SCORES:", s2 = "COMPUTER SCORES:", next = "Next";
	private JLabel l1 = new JLabel(s1 + fensu);
	private JLabel n = new JLabel(next);
	private JLabel computerscores = new JLabel("COMPUTER SCORES:" + 0);
	private JButton kzi = new JButton();
	private computer cm = new computer();
	private autogame computergame, mygame;
	private gamedisplay gm, gmnext, enmitygm;

	public elsmain() {
		super("俄罗斯方块");
		Container a = getContentPane();
		a.setLayout(null);
		a.setSize(gamew * 3 + fkdx * 2, gameh + fkdx * 2);

		gm = new gamedisplay(hsu, lsu, fkdx, clfk, clfkbk, clfkdong, fkdi);
		gm.setblockendcolor(fkdi);
		gm.setLocation(a.getSize().width - gamew * 2 + fkdx * 3, a.getSize().height - gameh);

		gmnext = new gamedisplay(tisihsu, tisilsu, fkdx, clfk, clfkbk, clfkdong, null);
		gmnext.setLocation(gm.getLocation().x + gm.getWidth() + fkdx * 2, gameh / 3);

		enmitygm = new gamedisplay(hsu, lsu, fkdx, clfk, clfkbk, clfkdong, fkdi);
		enmitygm.setblockendcolor(fkdi);
		enmitygm.setLocation(fkdx * 2, gm.getLocation().y);

		n.setForeground(Color.blue);
		n.setFont(new Font(next, Font.BOLD, 24));
		n.setLocation(gmnext.getLocation().x + fkdx * 3 / 2, gmnext.getLocation().y - fkdx * 3);
		n.setSize(100, 100);

		l1.setForeground(Color.RED);
		l1.setFont(new Font(s1, Font.BOLD, 17));
		l1.setLocation(gm.getLocation().x + fkdx * 3, gm.getLocation().y - fkdx * 5 / 2);
		l1.setSize(300, 100);
		l1.setVisible(true);

		computerscores.setForeground(Color.RED);
		computerscores.setFont(new Font(s2, Font.BOLD, 17));
		computerscores.setLocation(enmitygm.getLocation().x + fkdx * 2, l1.getLocation().y);
		computerscores.setSize(300, 100);

		kzi = createbutton();
		kzi.setSize(gmnext.getWidth() / 2, kzi.getHeight());
		kzi.setLocation(n.getLocation().x, gmnext.getLocation().y + gmnext.getHeight() + fkdx * 2);

		a.add(gm);
		a.add(gmnext);
		a.add(n);
		a.add(l1);
		a.add(kzi);
		a.add(enmitygm);
		a.add(computerscores);

		setLocation(100, 80);
		setSize(a.getSize().width + fkdx, a.getSize().height + fkdx);
		setResizable(false);
		setVisible(true);

		computergame = new autogame(enmitygm, true, null);
		mygame = new autogame(gm, false, gmnext);
		computergame.sleeptime = sleeptime;
		mygame.sleeptime = sleeptime;

		mygame.xia.start();
		mygame.xia.setSuspend(true);
		computergame.xia.start();
		computergame.xia.setSuspend(true);
		cm.start();
		cm.setSuspend(true);
	}

	public static void main(String[] args) {
		elsmain xiaowei = new elsmain();
		xiaowei.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void kzi_e(ActionEvent e) {
		if (e.getActionCommand().equals("开始")) {
			mygame.createnewblocks();
			computergame.createnewblocks();
			computergame.next = true;
			mygame.xia.setSuspend(false);
			computergame.xia.setSuspend(false);
			cm.setSuspend(false);
			kzi.setText("暂停");
		} else if (e.getActionCommand() == "暂停") {
			kzi.setText("继续");
			try {
				mygame.xia.setSuspend(true);
				computergame.xia.setSuspend(true);
				cm.setSuspend(true);
			} catch (Exception exception) {
				System.out.println("ddddddddddd");
			}
		} else if (e.getActionCommand() == "继续") {
			kzi.setText("暂停");
			mygame.xia.setSuspend(false);
			computergame.xia.setSuspend(false);
			cm.setSuspend(false);
		}
	}

	public void p5_e(KeyEvent e) {
		l1.setText(s1 + mygame.getscores());
		if (!kzi.getText().equals("开始")) {
			if (e.getKeyCode() == 37) {
				mygame.go(-1);
				// 先判断能向左移动�?,如果能向左移动一�?.
			} else if (e.getKeyCode() == 38) {
				mygame.circumvolve();
				// 使方块旋�?,每一次顺时针旋转90�?.
			} else if (e.getKeyCode() == 39) {
				mygame.go(1);
			} else if (e.getKeyCode() == 40) {
				mygame.go(lsu);
				// 使方块下�?�?,也就是q1,q2,q3,q4的索引�?�加列数.
			}
		}
		if (e.getKeyCode() == 27) {
			System.exit(0); // 按ESC键�??出程�?.
		}
	}

	public JButton createbutton() {
		JButton bt = new JButton();
		bt.setSize(70, 30);
		bt.setText("开始");
		bt.setForeground(Color.blue);
		bt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				kzi_e(e);
			}
		});
		bt.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				p5_e(e);
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		return bt;
	}

	class computer extends MyThreadV2 {

		/**
		 * 该类为电脑玩俄罗斯方块的控制线程，它通过类elsai（相等于人的大脑�? 来获得方块的旋转和移动的相关参数�?
		 *
		 */
		@Override
		protected void runPersonelLogic() {
			elsai ai = null;
			router r = null;
			if (computergame.next) {
				computerscores.setText(s2 + computergame.getscores());
				ai = new elsai(computergame);
				r = ai.getrouter();
				try {
					sleep(700);
				} catch (Exception e) {
				}
				while (r != null && r.state != computergame.getblk().state) {
					computergame.circumvolve();
					try {
						sleep(200);
					} catch (Exception e) {
					}
				}
				computergame.go(r.x);
				computergame.next = false;
				try {
					sleep(100);
				} catch (Exception e) {
				}
			} else {
				computergame.go(lsu);
				try {
					sleep(300 + computergame.getscores() / 300);
				} catch (Exception e) {
				}
			}

		}
	}
}
