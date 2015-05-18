/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.util.ArrayList;

/**该类主要负责为电脑提供最佳决策�??
 *具体思路为：从最后一行开始扫描，每次扫描�?行�?�假设该行为m行，找到m行所有空的格子，
 *每个空格子是�?个endblocks类， 如果有连续的几个空格，则视其为一个endblocks类�??
 * 第一步：扫描�?行，在所有空格中找出找出�?优的格子x�?
 * 第二步：如果x是虚的，则向上扫描一行并接着找出�?优的y，直到找到实的格子为止�??
 * 第三步：如果没有找到实的格子，则�?优的虚格子就是结果�??
 * �?优原则为：优先级分层，最坏为没有格子�?1000分），有格子但是是虚的（每个虚格�?1000分）
 * 格子的不同状态（100分），格式的在边界还是不在边界（10分）。不同层有不同的权重�?
 * 分数就代表了权重�?
 * @author Administrator
 */
public class elsai {

    private autogame autogame;
    private gamedisplay gm,  gmnext;
    private int lsu,  hsu;
    private ArrayList<endblocks> edblkarray;
    private blocks blk;

    public elsai(autogame autogame) {
        this.autogame = autogame;
        this.gm = autogame.gm;
        this.blk = autogame.getblk();
        lsu = gm.getlsu();
        hsu = gm.gethsu();
    }

    public ArrayList<endblocks> scan_get_emptyblocks(int hsu) {
        ArrayList<endblocks> edblkarray = new ArrayList<endblocks>();
        int count = 0;
        for (int n = hsu; n > 0; n--) {
            boolean begin = false;
            endblocks ed = null;
            for (int m = n * lsu - 1; m >= (n - 1) * lsu; m--) {
                if (gm.gmmodel.gmarray[m] == gm.gmmodel.blockbackcolor) {
                    if (!begin) {
                        ed = new endblocks(m, lsu, n);
                        begin = true;
                    } else {
                        ed.emptylength += 1;
                    }
                } else if (begin) {
                    begin = false;
                    edblkarray.add(ed);
                    count += 1;
                    ed = null;
                }
                if ((m == (n - 1) * lsu) && (begin)) {
                    begin = false;
                    edblkarray.add(ed);
                    count += 1;
                    ed = null;
                }

            }
            if (count != 0) {
                break;
            }
        }
        return edblkarray;
    }

    public endblocks get_bestblocks() {
        endblocks edblk, okblk = new endblocks();
        ArrayList<endblocks> ed;
        int scannumber = 0;
        for (int h = hsu; h >= 2; h--) {
            ed = scan_get_emptyblocks(h);
            for (int n = 0; n < ed.size(); n++) {
                edblk = get_okblocks(ed.get(n));
                if (edblk.grade < okblk.grade) {
                    okblk = edblk;
                }
            }
            if (okblk.grade < okblk.emptyparameter) {
                break;
            } else if (okblk.grade > okblk.emptylength &&
                    okblk.grade < okblk.no_blocks_parameter) {
                scannumber += 1;
            }
            if (scannumber > h / 9) {
                break;
            }
        }
        return okblk;
    }

    public router getrouter() {
        router r = new router();
        endblocks okblk = get_bestblocks();
        if (okblk != null) {
            r.state = okblk.blk.state;
            r.x = (okblk.blk.b1 - blk.b1) % lsu;
        }
        return r;
    }

//    public int getmovecout(endblocks edblk) {
//        return blk.movecout;
//    }
    public boolean usable(int b, int l) {
        for (int n = b; n >= lsu; n -= lsu) {
            if (l == 1) {
                if (gm.gmmodel.gmarray[n] == gm.gmmodel.blockendcolor) {
                    return false;
                }
            } else if (l == 2) {
                if (gm.gmmodel.gmarray[n] == gm.gmmodel.blockendcolor ||
                        gm.gmmodel.gmarray[n - 1] == gm.gmmodel.blockendcolor) {
                    return false;
                }
            } else if (l == 3) {
                if (gm.gmmodel.gmarray[n] == gm.gmmodel.blockendcolor ||
                        gm.gmmodel.gmarray[n - 1] == gm.gmmodel.blockendcolor ||
                        gm.gmmodel.gmarray[n - 2] == gm.gmmodel.blockendcolor) {
                    return false;
                }
            } else if (l == 4) {
                if (gm.gmmodel.gmarray[n] == gm.gmmodel.blockendcolor ||
                        gm.gmmodel.gmarray[n - 1] == gm.gmmodel.blockendcolor ||
                        gm.gmmodel.gmarray[n - 2] == gm.gmmodel.blockendcolor ||
                        gm.gmmodel.gmarray[n - 3] == gm.gmmodel.blockendcolor) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getemptyblocks(int point, int l) {
        int endp = (hsu - 1) * lsu + point % lsu;
        int number = 0;
        for (int n = 0; n < l; n++) {
            for (int p = point + lsu - n; p <= endp - n; p += lsu) {
                if (gm.gmmodel.gmarray[p] == gm.gmmodel.blockbackcolor) {
                    number += 1;
                } else {
                    break;
                }
            }
        }
        return number;
    }

    public endblocks get_okblocks(endblocks edblk) {
        switch (blk.kinds) {
            case 1:
                return getblocks_kinds_one(edblk);
            case 2:
                return getblocks_kinds_two(edblk);
            case 3:
                return getblocks_kinds_three(edblk);
            case 4:
                return getblocks_kinds_four(edblk);
            case 5:
                return getblocks_kinds_five(edblk);
            case 6:
                return getblocks_kinds_six(edblk);
            case 7:
                return getblocks_kinds_seven(edblk);
        }
        return edblk;
    }

    public endblocks getblocks_kinds_one(endblocks edblk) {
        int p = edblk.beginpiont;
        int t = edblk.emptylength - 2;
        int grade = 0;
        for (int n = 0; n <= t; n++) {
//            System.out.println("n==" + n);
            if (n > 0 && n < t) {
                edblk.borderparameter = 10;
            } else {
                edblk.borderparameter = 0;
            }
            if (usable(p - lsu - n, 2)) {
                grade = getemptyblocks(p - n, 2) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter;
//                System.out.println("grad--" + (p - n) + "===" + grade);
                if (edblk.grade > grade) {
                    edblk.grade = grade;

                    edblk.blk.b4 = p - n;
                    edblk.blk.b3 = edblk.blk.b4 - 1;
                    edblk.blk.b1 = edblk.blk.b3 - lsu;
                    edblk.blk.b2 = edblk.blk.b4 - lsu;
                    edblk.blk.state = 1;
                }
            }
        }
//        System.out.println("edblk.blk.q4=" + edblk.blk.q4 + "grad==" + edblk.grade);
        return edblk;
    }

    public endblocks getblocks_kinds_two(endblocks edblk) {
        int p = edblk.beginpiont;
        int t = edblk.emptylength - 1;
        int t2 = edblk.emptylength - 4;
        for (int n = 0; n <= t2; n++) {
            if (usable(p - lsu - n, 4)) {
                edblk.blk.b2 = p - n;
                edblk.blk.b1 = edblk.blk.b2 - 1;
                edblk.blk.b3 = edblk.blk.b1 - 1;
                edblk.blk.b4 = edblk.blk.b3 - 1;
                edblk.blk.state = 2;
                edblk.grade = 1;
                return edblk;
            }
        }
        for (int n = 0; n <= t; n++) {
            if (usable(p - lsu - n, 1)) {
                edblk.blk.b4 = p - n;
                edblk.blk.b3 = edblk.blk.b4 - lsu;
                edblk.blk.b1 = edblk.blk.b3 - lsu;
                edblk.blk.b2 = edblk.blk.b1 - lsu;
                edblk.blk.state = 1;
                edblk.grade = edblk.emptylength + 2;
                return edblk;
            }
        }
        return edblk;
    }

    public endblocks getblocks_kinds_three(endblocks edblk) {
        int p = edblk.beginpiont;
        int t = edblk.emptylength - 3;
        int p2 = edblk.beginpiont - edblk.emptylength + 1;
        int t4 = edblk.emptylength - 2;
        int grade = 0;
        for (int n = 0; n <= t; n++) {
            if (n > 0 && n < t) {
                edblk.borderparameter = 10;
            } else {
                edblk.borderparameter = 0;
            }
            if (usable(p - lsu - n, 3)) {
                grade = getemptyblocks(p - n, 3) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter;
                if (edblk.grade > grade) {
                    edblk.grade = grade;
                    edblk.blk.b4 = p - n;
                    edblk.blk.b3 = edblk.blk.b4 - 1;
                    edblk.blk.b1 = edblk.blk.b3 - 1;
                    edblk.blk.b2 = edblk.blk.b1 - lsu;
                    edblk.blk.state = 4;
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= t4; n++) {
            if (n > 0 && n < t4) {
                edblk.borderparameter = 10;
            } else {
                edblk.borderparameter = 0;
            }
            if (usable(p - lsu - n, 2)) {
                grade = getemptyblocks(p - n, 2) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter * 2;
                if (edblk.grade > grade) {
                    edblk.grade = grade;
                    edblk.blk.b1 = p - n;
                    edblk.blk.b3 = edblk.blk.b1 - lsu;
                    edblk.blk.b4 = edblk.blk.b3 - lsu;
                    edblk.blk.b2 = edblk.blk.b1 - 1;
                    edblk.blk.state = 3;
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if ((p2 % lsu > 1) && (gm.gmmodel.gmarray[p2 - 2] == gm.gmmodel.blockendcolor)) {
                if (usable(p2 - lsu, 3)) {
                    grade = getemptyblocks(p2 - n, 1) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter * 3;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b2 = p2;
                        edblk.blk.b1 = edblk.blk.b2 - lsu;
                        edblk.blk.b3 = edblk.blk.b1 - 1;
                        edblk.blk.b4 = edblk.blk.b3 - 1;
                        edblk.blk.state = 2;
                    }
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if ((p % lsu < lsu - 1) &&
                    (gm.gmmodel.gmarray[p - lsu + 1] == gm.gmmodel.blockendcolor) &&
                    (!(gm.gmmodel.gmarray[p - lsu] == gm.gmmodel.blockendcolor))) {
                if (usable(p - lsu * 2 + 1, 2)) {
                    grade = getemptyblocks(p - n, 1) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter * 4;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b4 = p;
                        edblk.blk.b3 = edblk.blk.b4 - lsu;
                        edblk.blk.b1 = edblk.blk.b3 - lsu;
                        edblk.blk.b2 = edblk.blk.b1 + 1;
                        edblk.blk.state = 1;
                    }
                }
            }
        }


        return edblk;
    }

    public endblocks getblocks_kinds_four(endblocks edblk) {
        int p = edblk.beginpiont;
        int t = edblk.emptylength - 3;
        int p2 = edblk.beginpiont - edblk.emptylength + 1;
        int t4 = edblk.emptylength - 2;
        int grade = 0;
        for (int n = 0; n <= t; n++) {
            if (n > 0 && n < t) {
                edblk.borderparameter = 10;
            } else {
                edblk.borderparameter = 0;
            }
            if (usable(p - lsu - n, 3)) {
                grade = getemptyblocks(p - n, 3) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter;
                if (edblk.grade > grade) {
                    edblk.grade = grade;
                    edblk.blk.b1 = p - n;
                    edblk.blk.b3 = edblk.blk.b1 - 1;
                    edblk.blk.b4 = edblk.blk.b3 - 1;
                    edblk.blk.b2 = edblk.blk.b1 - lsu;
                    edblk.blk.state = 2;
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= t4; n++) {
            if (n > 0 && n < t4) {
                edblk.borderparameter = 10;
            } else {
                edblk.borderparameter = 0;
            }
            if (usable(p - lsu - n, 2)) {
                grade = getemptyblocks(p - n, 2) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter * 2;
                if (edblk.grade > grade) {
                    edblk.grade = grade;
                    edblk.blk.b2 = p - n;
                    edblk.blk.b1 = edblk.blk.b2 - 1;
                    edblk.blk.b3 = edblk.blk.b1 - lsu;
                    edblk.blk.b4 = edblk.blk.b3 - lsu;
                    edblk.blk.state = 3;

                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if (p % lsu < lsu - 2) {
                if ((usable(p - lsu + 2, 3)) &&
                        (gm.gmmodel.gmarray[p + 2] == gm.gmmodel.blockendcolor)) {
                    grade = getemptyblocks(p - n, 1) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter * 3;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b1 = p;
                        edblk.blk.b2 = edblk.blk.b1 - lsu;
                        edblk.blk.b3 = edblk.blk.b1 + 1;
                        edblk.blk.b4 = edblk.blk.b3 + 1;
                        edblk.blk.state = 4;
                    }
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if ((p2 % lsu > 0) && gm.gmmodel.gmarray[p2 - lsu - 1] ==
                    gm.gmmodel.blockendcolor &&
                    !(gm.gmmodel.gmarray[p2 - lsu] ==
                    gm.gmmodel.blockendcolor)) {
                if (usable(p2 - lsu * 2, 2)) {
                    grade = getemptyblocks(p2 - n, 1) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter * 4;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b4 = p2;
                        edblk.blk.b3 = edblk.blk.b4 - lsu;
                        edblk.blk.b1 = edblk.blk.b3 - lsu;
                        edblk.blk.b2 = edblk.blk.b1 - 1;
                        edblk.blk.state = 1;
                    }
                }
            }
        }

        return edblk;
    }

    public endblocks getblocks_kinds_five(endblocks edblk) {
        int p = edblk.beginpiont;
        int t = edblk.emptylength - 3;
        int p2 = edblk.beginpiont - edblk.emptylength + 1;
        int t1 = 1 - edblk.emptylength;
        int grade = 0;
        for (int n = 0; n <= t1; n++) {
            if (p % lsu != 0 && p % lsu != lsu - 1) {
                if (usable(p - lsu + 1, 3)) {
                    grade = getemptyblocks(p - n, 1) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b2 = p;
                        edblk.blk.b1 = edblk.blk.b2 - lsu;
                        edblk.blk.b4 = edblk.blk.b1 - 1;
                        edblk.blk.b3 = edblk.blk.b1 + 1;
                        edblk.blk.state = 3;
                    }
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= t; n++) {
            if (n > 0 && n < t) {
                edblk.borderparameter = 10;
            } else {
                edblk.borderparameter = 0;
            }
            if (usable(p - lsu - n, 3)) {
                grade = getemptyblocks(p - n, 3) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter * 2;
                if (edblk.grade > grade) {
                    edblk.grade = grade;
                    edblk.blk.b4 = p - n;
                    edblk.blk.b1 = edblk.blk.b4 - 1;
                    edblk.blk.b3 = edblk.blk.b1 - 1;
                    edblk.blk.b2 = edblk.blk.b1 - lsu;
                    edblk.blk.state = 1;
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if ((p2 % lsu > 0) && (usable(p2 - lsu, 2))) {
                grade = getemptyblocks(p2 - n, 1) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter * 3;
                if (edblk.grade > grade) {
                    edblk.grade = grade;
                    edblk.blk.b3 = p2;
                    edblk.blk.b1 = edblk.blk.b3 - lsu;
                    edblk.blk.b2 = edblk.blk.b1 - 1;
                    edblk.blk.b4 = edblk.blk.b1 - lsu;
                    edblk.blk.state = 4;
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if ((p % lsu < lsu - 1) && (usable(p - lsu + 1, 2))) {
                grade = getemptyblocks(p - n, 1) * edblk.emptyparameter +
                        edblk.emptylength + edblk.borderparameter +
                        edblk.stateparameter * 3;
                if (edblk.grade > grade) {
                    edblk.grade = grade;
                    edblk.blk.b4 = p;
//                    System.out.println("q4=====" + p);
                    edblk.blk.b1 = edblk.blk.b4 - lsu;
                    edblk.blk.b2 = edblk.blk.b1 + 1;
                    edblk.blk.b3 = edblk.blk.b1 - lsu;
                    edblk.blk.state = 2;
                }
            }
        }
        return edblk;
    }

    public endblocks getblocks_kinds_six(endblocks edblk) {
        int p = edblk.beginpiont;
        int t = edblk.emptylength - 2;
        int p2 = edblk.beginpiont - edblk.emptylength + 1;
        int t1 = 1 - edblk.emptylength;
        int grade = 0;
        for (int n = 0; n <= t; n++) {
            if (p2 % lsu > 0) {
                if (usable(p2 - lsu + 1, 3)) {
                    grade = getemptyblocks(p2 + 1, 2) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b3 = p2;
                        edblk.blk.b1 = edblk.blk.b3 - lsu;
                        edblk.blk.b2 = edblk.blk.b1 - 1;
                        edblk.blk.b4 = edblk.blk.b3 + 1;
                        edblk.blk.state = 1;
                    }
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if (p % lsu < lsu - 1) {
                if (usable(p - lsu + 1, 2)) {
                    grade = getemptyblocks(p, 1) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter * 2;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b4 = p;
                        edblk.blk.b3 = edblk.blk.b4 - lsu;
                        edblk.blk.b1 = edblk.blk.b4 + 1;
                        edblk.blk.b2 = edblk.blk.b1 - lsu;
                        edblk.blk.state = 2;
                    }
                }
            }
        }
        return edblk;
    }

    public endblocks getblocks_kinds_seven(endblocks edblk) {
        int p = edblk.beginpiont;
        int t = edblk.emptylength - 2;
        int p2 = edblk.beginpiont - edblk.emptylength + 1;
        int t1 = 1 - edblk.emptylength;
        int grade = 0;
        for (int n = 0; n <= t; n++) {
            if (p % lsu < lsu - 1) {
                if (usable(p - lsu + 1, 3)) {
                    grade = getemptyblocks(p, 2) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b3 = p;
                        edblk.blk.b1 = edblk.blk.b3 - lsu;
                        edblk.blk.b2 = edblk.blk.b1 + 1;
                        edblk.blk.b4 = edblk.blk.b3 - 1;
                        edblk.blk.state = 1;
                    }
                }
            }
        }
        if (edblk.grade < edblk.emptyparameter) {
            return edblk;
        }
        for (int n = 0; n <= 0; n++) {
            if (p2 % lsu > 0) {
                if (usable(p2 - lsu, 2)) {
                    grade = getemptyblocks(p2 - n, 1) * edblk.emptyparameter +
                            edblk.emptylength + edblk.borderparameter +
                            edblk.stateparameter * 2;
                    if (edblk.grade > grade) {
                        edblk.grade = grade;
                        edblk.blk.b2 = p2;
                        edblk.blk.b1 = edblk.blk.b2 - lsu;
                        edblk.blk.b3 = edblk.blk.b1 - 1;
                        edblk.blk.b4 = edblk.blk.b3 - 1;
                        edblk.blk.state = 2;
                    }
                }
            }
        }
        return edblk;
    }
}
