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
		value = -1 * landingHeight + eliminateRows - boardRowTransitions - boardColTransitions - 3 * boardBuriedHoles
				- boardWells;
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

