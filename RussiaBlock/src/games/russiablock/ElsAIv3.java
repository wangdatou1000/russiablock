/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class ElsAIv3 {

	private GameModel gm, gmnext;
	private final int COLUMNS, ROWS, ALLBLOCKS;
	private Blocks blk, blk2, orblk;
	private int endpoint;
	private Router r = new Router();

	public ElsAIv3(GameModel gmModel, Blocks blk) {
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

	private int getLandingHeight(Blocks blk){
		return ROWS - 1 - blk.getblkendpoint() / COLUMNS;
    }

	private int getEliminateRows(Blocks blk) {
		int a = 0, eliminateRow = 0, eliminateBlock = 0, backColor = 0;
		for (int n = ALLBLOCKS - 1; n > 0; n--) {
			if (gm.gmarray[n] == gm.ENDCOLOR || n == blk.b1 || n == blk.b2 || n == blk.b3 || n == blk.b4) {
				a++;
			} else {
				backColor++;
			}
			if (n % COLUMNS == 0 && a == COLUMNS) {
				eliminateRow++;
				int row = n / COLUMNS;
				if (row == blk.b1 / COLUMNS) {
					eliminateBlock++;
				}
				if (row == blk.b2 / COLUMNS) {
					eliminateBlock++;
				}
				if (row == blk.b3 / COLUMNS) {
					eliminateBlock++;
				}
				if (row == blk.b3 / COLUMNS) {
					eliminateBlock++;
				}
			}
			if (n % COLUMNS == 0 && backColor == COLUMNS) {
				break;
			}
		}
		return eliminateRow * (eliminateBlock + 1);
	}

	private int getBoardRowTransitions(Blocks blk) {
		int n = ALLBLOCKS - 1, a = 0, rowTransitions = 0, c = 0;
		int isEndBlock = -1;
		while (n >= 0) {
			if (gm.gmarray[n] == gm.ENDCOLOR || n == blk.b1 || n == blk.b2 || n == blk.b3 || n == blk.b4) {
				if (isEndBlock == -1 || isEndBlock != 1) {
					a++;
					isEndBlock = 1;
				}
			} else if (gm.gmarray[n] == gm.BACKCOLOR) {
				if (isEndBlock == -1 || isEndBlock != 0) {
					a++;
					isEndBlock = 0;
				}
				c++;
			}
			if (n % COLUMNS == 0) {

				if (c == COLUMNS) {
					break;
				}
				rowTransitions += a;
				a = 0;
				c = 0;
				isEndBlock = -1;
			}
			n--;
		}

		return rowTransitions;
	}

	private int getBoardColTransitions(Blocks blk) {
		int a = 0, colTransitions = 0;
		int isEndBlock = -1;
		for (int n = ALLBLOCKS - 1; n >= ALLBLOCKS - COLUMNS; n--) {
			for (int j = n; j >= 0; j -= COLUMNS) {
				if (gm.gmarray[j] == gm.ENDCOLOR || j == blk.b1 || j == blk.b2 || j == blk.b3 || j == blk.b4) {
					if (isEndBlock == -1 || isEndBlock != 1) {
						a++;
						isEndBlock = 1;
					}
				} else if (gm.gmarray[j] == gm.BACKCOLOR) {
					if (isEndBlock == -1 || isEndBlock != 0) {
						a++;
						isEndBlock = 0;
					}
				}
			}
			colTransitions += a;
			isEndBlock = -1;
			a = 0;
		}

		return colTransitions;
	}

	private int getBoardBuriedHoles(Blocks blk) {
		int boardBuriedHoles = 0;
		int isEndBlock = -1;
		for (int n = 10; n >= 0; n--) {
			for (int j = n; j < ALLBLOCKS; j += COLUMNS) {
				if (gm.gmarray[j] == gm.BACKCOLOR && j != blk.b1 && j != blk.b2 && j != blk.b3 && j != blk.b4
						&& isEndBlock == -1) {
					continue;
				}
				if (gm.gmarray[j] == gm.ENDCOLOR || j == blk.b1 || j == blk.b2 || j == blk.b3 || j == blk.b4) {
					isEndBlock = 0;
					continue;
				}
				if (gm.gmarray[j] == gm.BACKCOLOR && isEndBlock == 0) {
					boardBuriedHoles++;
				}
			}
			isEndBlock = -1;
		}

		return boardBuriedHoles;
	}

	private int getBoardWells(Blocks blk) {
		int boardWells = 0, allWells = 0;
		for (int n = 10; n >= 0; n--) {
			for (int j = n; j < ALLBLOCKS; j += COLUMNS) {
				if (gm.gmarray[j] == gm.BACKCOLOR && j != blk.b1 && j != blk.b2 && j != blk.b3 && j != blk.b4) {
					if (j % COLUMNS != 0 && j % COLUMNS != 10 && (gm.gmarray[j - 1] == gm.ENDCOLOR || j - 1 == blk.b1
							|| j - 1 == blk.b2 || j - 1 == blk.b3 || j - 1 == blk.b4)
							&& (gm.gmarray[j + 1] == gm.ENDCOLOR || j + 1 == blk.b1 || j + 1 == blk.b2
									|| j + 1 == blk.b3 || j + 1 == blk.b4)) {

						boardWells++;
					}
					if (j % COLUMNS == 0 && (gm.gmarray[j + 1] == gm.ENDCOLOR || j + 1 == blk.b1 || j + 1 == blk.b2
							|| j + 1 == blk.b3 || j + 1 == blk.b4)) {
						boardWells++;
					}
					if (j % COLUMNS == 10 && (gm.gmarray[j - 1] == gm.ENDCOLOR || j - 1 == blk.b1 || j - 1 == blk.b2
							|| j - 1 == blk.b3 || j - 1 == blk.b4)) {
						boardWells++;
					}
				} else if(boardWells!=0) {
					allWells += ((1 + boardWells) * boardWells) / 2;
				}

			}
			boardWells = 0;
		}

		return allWells;

	}
	public Endblocks getOneOkBlock(Blocks blk) {
		Blocks bk = clone(blk);
		Endblocks okblk = new Endblocks(bk);
		okblk.setBoardBuriedHoles(getBoardBuriedHoles(bk));
		okblk.setLandingHeight(getLandingHeight(bk));
		okblk.setBoardColTransitions(getBoardColTransitions(bk));
		okblk.setBoardRowTransitions(getBoardRowTransitions(bk));
		okblk.setBoardWells(getBoardWells(bk));
		okblk.setEliminateRows(getEliminateRows(bk));
		return okblk;
	}

	public int getPriority(Endblocks blocks) {
		Blocks blk_state = clone(blocks.blk);
		int circumvolveTimes = 0;
		while (blk_state.state != orblk.state) {
			blk_state.circumvolvetest();
			blk_state.circumvolve();
			circumvolveTimes++;
		}
		int movieTimes = blocks.blk.b1 % COLUMNS - orblk.b1 % COLUMNS;
		if (blk_state.b1 % COLUMNS < COLUMNS / 2) {
			return 100 * movieTimes + 10 + circumvolveTimes;
		} else {
			return 100 * movieTimes + circumvolveTimes;
		}

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
		if (e1.getEvaluateBlocks() > e2.getEvaluateBlocks()) {
			return e1;
		} else if (e1.getEvaluateBlocks() < e2.getEvaluateBlocks()) {
			return e2;
		} else {
			if (getPriority(e1) > getPriority(e2))
				return e1;
			else
				return e2;
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
									+ tempendblock.getLandingHeight() + "\t" + tempendblock.getEliminateRows() + "\t"
									+ tempendblock.getBoardColTransitions() + "\t"
									+ tempendblock.getBoardRowTransitions() + "\t" + tempendblock.getBoardWells() + "\t"
									+ tempendblock.getBoardBuriedHoles() + "\tok--state:" + endblock.blk.state + "\t"
									+ endblock.getLandingHeight() + "\t" + endblock.getEliminateRows() + "\t"
									+ endblock.getBoardColTransitions() + "\t" + endblock.getBoardRowTransitions()
									+ "\t" + endblock.getBoardWells() + "\t" + endblock.getBoardBuriedHoles()
									+ "\tendpoint:" + endpoint + "-" + end + "--"
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
