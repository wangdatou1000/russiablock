/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class Endblocks {

    public final int emptyparameter = 1000;
    public final int stateparameter = 100;
    public final int no_blocks_parameter = 10000;
    public int beginpiont;
    public int emptylength;
	public Blocks blk = new Blocks();
    public int beginlsu;
    public int endlsu;
    public int hsu;
    private int lsu;
    public int grade = no_blocks_parameter;
	public int borderparameter = 0;
	public int qi = 0;
	private int deadQi = -1;
	private Long scores = -1L;
	private List<Integer> A = new ArrayList<>();
	private int allA = -1;
	private List<Integer> B = new ArrayList<>();
	private int C = 0;
	private Map<Integer, Integer> deadQiMap = new HashMap<>();

	/** V3 算法的属性 ********/
	private int landingHeight = 0;
	private int eliminateRows = 0;
	private int boardRowTransitions = 0;
	private int boardColTransitions = 0;
	private int boardBuriedHoles = 0;
	private int boardWells = 0;

	/******* end ***************/
	public int getEvaluateBlocks() {
		int value = 0;
		/*
		 * scores:1582300----Rows:13636 scores:413700----Rows:3550
		 * scores:2034500----Rows:17483 scores:2301400----Rows:19826
		 * scores:2527100----Rows:21709 scores:107000----Rows:914
		 * scores:831100----Rows:7145 scores:6378700----Rows:54885
		 * scores:4643200----Rows:40057 scores:839700----Rows:7225
		 * scores:1936100----Rows:16735 scores:1538900----Rows:13242
		 * scores:609300----Rows:5277 scores:2916200----Rows:25140
		 */
		int value1 = (int) (-1 * landingHeight + eliminateRows - boardRowTransitions - boardColTransitions
				- 4 * boardBuriedHoles - boardWells);

		// 1 -4.500158825082766
		// 2 3.4181268101392694
		// 3 -3.2178882868487753
		// 4 -9.348695305445199
		// 5 -7.899265427351652
		// 6 -3.3855972247263626
		/*
		 * scores:466000----Rows:3810 scores:2873900----Rows:23276
		 * scores:9628000----Rows:78305 scores:6714200----Rows:54456
		 * scores:7875400----Rows:63952 scores:2341100----Rows:18999
		 * scores:3718700----Rows:30188 scores:96600----Rows:799
		 * scores:14745000----Rows:119524 scores:4047200----Rows:32852
		 * +scores:12868200----Rows:104316 create ok!!
		 * scores:6767800----Rows:54906 scores:6301400----Rows:51196
		 * scores:2053500----Rows:16687 scores:3911200----Rows:31761
		 * scores:1195400----Rows:9687 scores:12204700----Rows:99041
		 * scores:0----Rows:0 scores:2615800----Rows:21242
		 * scores:4791900----Rows:38847 create ok!! scores:5257200----Rows:42654
		 * scores:1320700----Rows:10709
		 */
		value = (int) (-4.500158825082766 * landingHeight +
		  3.4181268101392694 * eliminateRows - 3.2178882868487753 *
						boardRowTransitions
				- 9.348695305445199 * boardColTransitions -
		  7.899265427351652 * boardBuriedHoles - 3.3855972247263626 *
		  boardWells);
		 
		return value;
	}

	public Endblocks(Blocks blk) {
		this.blk = blk;
    }

	public Endblocks(int beginpiont, int lsu, int hsu) {
        this.beginpiont = beginpiont;
        this.emptylength = 1;
        this.hsu = hsu;
        this.lsu = lsu;
    }

	public Long getScores() {
		if (scores == -1L) {
			scores = (0L + C) << 60;
			Iterator<Integer> it = deadQiMap.keySet().iterator();
			int deadQi = 0;
			while (it.hasNext()) {
				deadQi += deadQiMap.get(it.next()) * 5;
			}
			scores = scores | ((0L + deadQi) << 48);

		}

		return scores;
	}

	public int getAllA() {
		if (allA != -1)
			return allA;
		allA = 0;
		for (int n = 0, m = A.size(); n < m; n++) {
			allA += A.get(n);
			// System.out.println("A," + n + ":" + A.get(n));
		}
		return allA;
	}

	public int getDeadQi() {
		if (deadQi == -1) {
			Iterator<Integer> it = deadQiMap.keySet().iterator();
			deadQi = 0;
			while (it.hasNext()) {
				int n = deadQiMap.get(it.next()) + 1;
				deadQi += ((2 + n) * n) / 2;
			}
		}
		return deadQi;
	}

	public int getC() {
		return C;
	}

	public void setC(int c) {
		C = c;
	}

	public Endblocks() {
    }

    public int getbeginlsu() {
        return beginpiont % lsu;
    }

    public int getendlsu() {
        return beginlsu + emptylength - 1;
    }

	public Map<Integer, Integer> getDeadQiMap() {
		return deadQiMap;
	}

	public void setDeadQiMap(Map<Integer, Integer> deadQiMap) {
		this.deadQiMap = deadQiMap;
	}

	public List<Integer> getA() {
		return A;
	}

	public void setA(List<Integer> a) {
		A = a;
	}

	public List<Integer> getB() {
		return B;
	}

	public int getLandingHeight() {
		return landingHeight;
	}

	public void setLandingHeight(int landingHeight) {
		this.landingHeight = landingHeight;
	}

	public int getEliminateRows() {
		return eliminateRows;
	}

	public void setEliminateRows(int eliminateRows) {
		this.eliminateRows = eliminateRows;
	}

	public int getBoardRowTransitions() {
		return boardRowTransitions;
	}

	public void setBoardRowTransitions(int boardRowTransitions) {
		this.boardRowTransitions = boardRowTransitions;
	}

	public int getBoardColTransitions() {
		return boardColTransitions;
	}

	public void setBoardColTransitions(int boardColTransitions) {
		this.boardColTransitions = boardColTransitions;
	}

	public int getBoardBuriedHoles() {
		return boardBuriedHoles;
	}

	public void setBoardBuriedHoles(int boardBuriedHoles) {
		this.boardBuriedHoles = boardBuriedHoles;
	}

	public int getBoardWells() {
		return boardWells;
	}

	public void setBoardWells(int boardWells) {
		this.boardWells = boardWells;
	}

	public void setB(List<Integer> b) {
		B = b;
	}
}

