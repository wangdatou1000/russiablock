/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 *
 * @author Administrator
 */
public class ElsAIv2 {

	private GameModel gm, gmnext;
	private final int COLUMNS, ROWS, ALLBLOCKS;
	private Blocks blk, blk2, orblk;
	private int endpoint;
	private Router r = new Router();

	public ElsAIv2(GameModel gmModel, Blocks blk) {
		this.gm = gmModel;
		System.out.println(gm.gmarray.length);
		// orblk.createblocks();
		COLUMNS = gm.getColumnNum();
		ROWS = gm.getRowNum();
		ALLBLOCKS = COLUMNS * ROWS;
		endpoint = getEndPoint();
	}

	public int getEndPoint() {
		boolean isend = true;
		int endpoint = ALLBLOCKS;
		for (int n = ROWS; n > 0; n--) {
			for (int m = COLUMNS * n - 1; m >= COLUMNS * (n - 1); m--) {
				if (gm.gmarray[m] == gm.ENDCOLOR) {
					endpoint = m;
					isend = false;
				}
			}
			if (isend) {
				break;
			} else {
				isend = true;
			}

		}
		return endpoint;
	}

	public void setBlk(Blocks blk, int end) {
		int endpoint = blk.getblkendpoint();
		int p = end - endpoint;
		blk.gotest(p);
		blk.go();
	}

	public boolean isFilled(Blocks blk) {
		if (blk.b1 < 0 || blk.b2 < 0 || blk.b3 < 0 || blk.b4 < 0)
			return true;
		if (gm.gmarray[blk.b1] == gm.ENDCOLOR) {
			return true;
		}
		if (gm.gmarray[blk.b2] == gm.ENDCOLOR) {
			return true;
		}
		if (gm.gmarray[blk.b3] == gm.ENDCOLOR) {
			return true;
		}
		if (gm.gmarray[blk.b4] == gm.ENDCOLOR) {
			return true;
		}
		return false;
	}

	public boolean isEnable(Blocks blk) {
		Blocks checkblk = clone(blk);
		int end = blk.getblkbeginpoint();
		if (end <= 0)
			return false;
		boolean isenable = true;
		while (end > COLUMNS) {
			if (gm.cango(-COLUMNS, checkblk)) {
				checkblk.go();
				end -= COLUMNS;
			} else {
				return false;
			}
		}
		return isenable;
	}

	public boolean isDead(int t, Map<Integer, Integer> deadQiMap, Set<Integer> deadQiSet, Blocks bk) {
		if (deadQiSet.contains(t))
			return false;
		List<Integer> tempList = new ArrayList<>();
		tempList.add(t);
		for (int n = t - COLUMNS; n >= 0; n -= COLUMNS) {
			if (gm.gmarray[n] == gm.ENDCOLOR || n == bk.b1 || n == bk.b2 || n == bk.b3 || n == bk.b4) {
				deadQiMap.put(t, tempList.size());
				deadQiSet.addAll(tempList);
				return false;
			} else {
				tempList.add(n);
			}
		}
		return true;
	}

	public Endblocks getOneOkBlock(Blocks blk) {
		Blocks bk = clone(blk);
		Endblocks okblk = new Endblocks(bk);
		int n = ALLBLOCKS - 1, a = 0, b = 0, c = 0;
		int isEndBlock = -1;
		Set<Integer> deadQiSet = new HashSet<>();

		while (n >= 0) {
			if (gm.gmarray[n] == gm.ENDCOLOR || n == bk.b1 || n == bk.b2 || n == bk.b3 || n == bk.b4) {
				if (isEndBlock == -1 || isEndBlock != 1) {
					a++;
					isEndBlock = 1;
				}
				b++;
			} else if (gm.gmarray[n] == gm.BACKCOLOR) {
				if (isEndBlock == -1 || isEndBlock != 0) {
					a++;
					isEndBlock = 0;
				}
				isDead(n, okblk.getDeadQiMap(), deadQiSet, bk);
				c++;
			}
			if (n % COLUMNS == 0) {

				if (c == COLUMNS) {
					break;
				}
				if (b == COLUMNS) {
					okblk.setC(okblk.getC() + 1);
				}
				okblk.getA().add(a);
				okblk.getB().add(b);
				a = 0;
				b = 0;
				c = 0;
				isEndBlock = -1;
			}
			n--;
		}
		return okblk;
	}

	public void setBlocks(Blocks blk) {
		// this.blk = blk;
		orblk = clone(blk);
		endpoint = getEndPoint();
	}

	public Endblocks compareBlocks(Endblocks e1, Endblocks e2) {
		if (e1 == null && e2 == null) {
			return null;
		}
		if (e1 == null && e2 != null) {
			return e2;
		}
		if (e2 == null && e1 != null) {
			return e1;
		}
		int c1 = e1.getC();
		int c2 = e2.getC();
		int qi1 = e1.getDeadQi(), qi2 = e2.getDeadQi();
		Supplier<Endblocks> cf = () -> {
			if (c1 > c2) {
				return e1;
			} else if (c1 < c2) {
				return e2;
			}
			return null;
		};
		int aSize1 = e1.getA().size(), aSize2 = e2.getA().size();
		Supplier<Endblocks> qif = () -> {
			if (qi1 > qi2) {
				return e2;
			} else if (qi1 < qi2) {
				return e1;
			}
			return null;
		};
		Supplier<Endblocks> aSizef = () -> {
			if (aSize1 > aSize2) {
				return e2;
			} else if (aSize1 < aSize2) {
				return e1;
			}
			return null;
		};
		if (endpoint / COLUMNS > 13) {
			Endblocks tempE = qif.get();
			tempE = tempE == null ? cf.get() : tempE;
			tempE = tempE == null ? aSizef.get() : tempE;
			if (tempE != null)
				return tempE;
		} else {
			Endblocks tempE = cf.get();
			tempE = tempE == null ? aSizef.get() : tempE;
			tempE = tempE == null ? qif.get() : tempE;
			if (tempE != null)
				return tempE;
		}

		if (e1.getAllA() > e2.getAllA()) {
			return e2;
		} else if (e1.getAllA() < e2.getAllA()) {
			return e1;
		}
		for (int n = 0, m = e2.getB().size(); n < m; n++) {
			if (e1.getB().get(n) > e2.getB().get(n))
				return e1;
			else if (e1.getB().get(n) < e2.getB().get(n))
				return e2;
		}

		if (e1.blk.state == e2.blk.state) {
			if (e1.blk.getblkbeginpoint() > e2.blk.getblkbeginpoint()) {
				return e1;
			} else {
				return e2;
			}
		} else {
			int a = e1.blk.getblkbeginpoint() / COLUMNS;
			int b = e2.blk.getblkbeginpoint() / COLUMNS;

			if (a < b) {
				System.out.println(a + "--" + b);
				return e2;
			} else {
				return e1;
			}
		}
	}

	public Blocks clone(Blocks blk) {
		Blocks b = new Blocks();
		b.b1 = blk.b1;
		b.b2 = blk.b2;
		b.b3 = blk.b3;
		b.b4 = blk.b4;
		b.state = blk.state;
		b.kinds = blk.kinds;
		b.lsu = blk.lsu;
		return b;
	}

	private int getBeginScanPoint() {
		int beginScanPoint = ALLBLOCKS - 1;
		boolean isGood = true;
		for (int n = beginScanPoint; n > 0; n--) {
			if (gm.gmarray[n] == gm.BACKCOLOR) {
				for (int i = n - COLUMNS; i > 0; i -= COLUMNS) {
					if (gm.gmarray[i] == gm.ENDCOLOR) {
						isGood = false;
						break;
					}
				}
				if (isGood) {
					return n;
				}
				isGood = true;
			}
		}
		return beginScanPoint;
	}

	public Endblocks getBestBlocks() {
		Endblocks endblock = null;
		Endblocks tempendblock = null;
		Blocks blk = clone(orblk);
		Blocks blk_state = clone(blk);
		int state = blk.state;
		int x = 0;
		boolean isend = true;
		while (isend) {
			int end = getBeginScanPoint();
			System.out.println("end:" + end + "\tendpoint:" + endpoint);
			setBlk(blk, end);// 将blk移动到可以开始搜索点
			System.out.println("kinds:" + blk.kinds + "\t" + blk.b1 + "=" + blk.b1 % COLUMNS + "-" + blk.b2 % COLUMNS
					+ "--" + blk.b3 % COLUMNS + "--" + blk.b4 % COLUMNS + "state:" + blk.state);
			while (end > endpoint - COLUMNS - 1 && end > 0) {
				/* 如果悬空则不计算，跳过 */
				if (!gm.cango(COLUMNS, blk)) {

					if (!isFilled(blk) && isEnable(blk)) {

						if (endblock == null) {
							endblock = getOneOkBlock(blk);
						} else {
							tempendblock = getOneOkBlock(blk);
							endblock = compareBlocks(endblock, tempendblock);
							System.out.println("state:" + blk.state + "\ttemp:" + tempendblock.getC() + "\t"
									+ tempendblock.getDeadQi() + "\t" + tempendblock.getA().size() + "\t"
									+ tempendblock.getAllA() + "\tok--state:" + endblock.blk.state + "\t"
									+ endblock.getC() + "\t" + endblock.getDeadQi() + "\t" + endblock.getA().size()
									+ "\t" + endblock.getAllA() + "\tendpoint:" + endpoint + "-" + end + "--"
									+ endblock.blk.b1 + "===" + blk.b1 % COLUMNS + "-" + blk.b2 % COLUMNS + "--"
									+ blk.b3 % COLUMNS + "--" + blk.b4 % COLUMNS);
						}
					}
				}

				blk.gotest(-1);
				blk.go();
				end -= 1;

			}
			blk_state.circumvolvetest();
			blk_state.circumvolve();
			blk = clone(blk_state);
			tempendblock = null;
			if (blk.state == state) {
				isend = false;
			}
		}
		return endblock;
	}

	public Router getRouter() {
		Endblocks okblk = getBestBlocks();
		if (okblk != null) {
			r.state = okblk.blk.state;
			r.x = okblk.blk.b1 % COLUMNS - orblk.b1 % COLUMNS;

			System.out.println("r.kinds:" + okblk.blk.kinds + "  r.state=" + r.state + "\t" + r.x + "\t" + okblk.blk.b1
					+ "=====" + orblk.b1 + "\t" + okblk.blk.b1 % COLUMNS + "======" + orblk.b1 % COLUMNS);
		} else {
			System.out.println("okblk is null,blk.kinds=" + orblk.kinds + "," + orblk.b1 + "-" + orblk.b2 + "-"
					+ orblk.b3 + "-" + orblk.b4);
		}
		System.out.println("\n\n\n");
		return r;
	}
}
