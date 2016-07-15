/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class elsaiv2 {

    private autogame autogame;
    private gamedisplay gm, gmnext;
    private int lsu, hsu;
    private blocks blk, blk2, orblk;
    private int endpoint;

    public elsaiv2(autogame autogame) {
        this.autogame = autogame;
        this.gm = autogame.gm;
        orblk = clone(autogame.getblk());
        // orblk.createblocks();
        lsu = gm.getlsu();
        hsu = gm.gethsu();
        endpoint = getendpoint();
    }

    public int getendpoint() {
        boolean isend = true;
        int endpoint = lsu * hsu;
        for (int n = hsu; n > 0; n--) {
            for (int m = lsu * n - 1; m >= lsu * (n - 1); m--) {
                if (gm.gmmodel.gmarray[m] == gm.gmmodel.blockendcolor) {
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

    public void setblk(blocks blk) {
        int endpoint = blk.getblkendpoint();
        int end = lsu * hsu - 1;
        int p = end - endpoint;
        blk.gotest(p);
        blk.go();
    }

    public boolean isfilled(blocks blk) {
    	if(blk.b1<0||blk.b2<0||blk.b3<0||blk.b4<0)return true;
        if (gm.gmmodel.gmarray[blk.b1] == gm.gmmodel.blockendcolor) {
            return true;
        }
        if (gm.gmmodel.gmarray[blk.b2] == gm.gmmodel.blockendcolor) {
            return true;
        }
        if (gm.gmmodel.gmarray[blk.b3] == gm.gmmodel.blockendcolor) {
            return true;
        }
        if (gm.gmmodel.gmarray[blk.b4] == gm.gmmodel.blockendcolor) {
            return true;
        }
        return false;
    }

    public boolean isenable(blocks blk) {
        blocks checkblk = clone(blk);
        int end = blk.getblkendpoint();
        boolean isenable = true;
        while (end > endpoint - 4) {
            if (gm.gmmodel.cango(-lsu, checkblk)) {
                checkblk.go();
                end -= lsu;
            } else {
                return false;
            }
        }
        return isenable;
    }

    public boolean isgood(int t) {
        for (int n = t; n >= 0; n -= lsu) {
            if (gm.gmmodel.gmarray[n] == gm.gmmodel.blockendcolor) {
                return false;
            }
        }
        return true;
    }

    public endblocks getoneokblock(blocks blk) {
        blocks b = clone(blk);
        endblocks okblk = new endblocks(b);
        ArrayList<Integer> a = new ArrayList<Integer>();
        int qi = 0;
        qi += getoneqi(blk.b1, b, a);
        qi += getoneqi(blk.b2, b, a);
        qi += getoneqi(blk.b3, b, a);
        qi += getoneqi(blk.b4, b, a);
        okblk.qi = qi;
        a = null;
        return okblk;
    }

    public int getunableqi(blocks blk, int one, ArrayList<Integer> a) {
        int qi = 0;
        if (one - 1 == blk.b2 || one - 1 == blk.b3 || one - 1 == blk.b4 || one -
                1 == blk.b1) {
            qi--;
        }
        if (one + 1 == blk.b2 || one + 1 == blk.b3 || one + 1 == blk.b4 || one +
                1 == blk.b1) {
            qi--;
        }
        if (one + lsu == blk.b2 || one + lsu == blk.b3 || one + lsu ==
                blk.b4 || one + lsu == blk.b1) {
            qi--;
        }
        if (one - lsu == blk.b3 || one - lsu == blk.b4 || one - lsu ==
                blk.b2 || one - lsu == blk.b1) {
            qi--;
        }

        return qi;
    }

    public int getoneqi(int one, blocks blk, ArrayList<Integer> a) {

        int qi = 0;
        if (one % lsu != 0 && gm.gmmodel.gmarray[one - 1] !=
                gm.gmmodel.blockendcolor &&
                !a.contains(one - 1)) {
            if (one - 1 != blk.b2 && one - 1 != blk.b3 && one - 1 != blk.b4 && one -
                    1 != blk.b1) {
                a.add(one - 1);
                qi++;
                if (!isgood(one - 1)) {
                    qi++;
                }
            }

        } else if (one % lsu != 0 && gm.gmmodel.gmarray[one - 1] ==
                gm.gmmodel.blockendcolor &&
                !a.contains(one - 1)) {
            a.add(one - 1);
            qi--;
        }
        if (one % lsu != lsu - 1 && gm.gmmodel.gmarray[one + 1] !=
                gm.gmmodel.blockendcolor &&
                !a.contains(one + 1)) {
            if (one + 1 != blk.b2 && one + 1 != blk.b3 && one + 1 != blk.b4 && one +
                    1 != blk.b1) {
                a.add(one + 1);
                qi++;
                if (!isgood(one + 1)) {
                    qi++;
                }
            }

        } else if (one % lsu != lsu - 1 && one + 1 < lsu * hsu && gm.gmmodel.gmarray[one + 1] ==
                gm.gmmodel.blockendcolor &&
                !a.contains(one + 1)) {
            a.add(one + 1);
            qi--;
        }
        if (one - lsu > 0 && gm.gmmodel.gmarray[one - lsu] !=
                gm.gmmodel.blockendcolor &&
                !a.contains(one - lsu)) {
            if (one - lsu != blk.b3 && one - lsu != blk.b4 && one - lsu !=
                    blk.b2 && one - lsu != blk.b1) {
                a.add(one - lsu);
                qi++;
                if (!isgood(one - lsu)) {
                    qi++;
                }
            }

        } else if (one - lsu > 0 && gm.gmmodel.gmarray[one - lsu] ==
                gm.gmmodel.blockendcolor &&
                !a.contains(one - lsu)) {
            a.add(one - lsu);
            qi--;
        }
        if (one + lsu < lsu * hsu - 1 && gm.gmmodel.gmarray[one + lsu] !=
                gm.gmmodel.blockendcolor &&
                !a.contains(one + lsu)) {
            if (one + lsu != blk.b2 && one + lsu != blk.b3 && one + lsu !=
                    blk.b4 && one + lsu != blk.b1) {
                a.add(one + lsu);
                qi++;
                if (!isgood(one + lsu)) {
                    qi++;
                }
            }
        } else {
            a.add(one + lsu);
            qi--;
        }
        return qi;
    }

    public endblocks compareblocks(endblocks e1, endblocks e2) {
        if (e1 == null && e2 == null) {
            return null;
        }
        if (e1 == null && e2 != null) {
            return e2;
        }
        if (e2 == null && e1 != null) {
            return e1;
        }
        if (e1.qi < e2.qi) {
            return e1;
        } else if (e1.qi > e2.qi) {
            System.out.println(e2.blk.state + "--" + e2.blk.getblkendpoint() /
                    lsu);
            return e2;
        }
        if (e1.blk.state == e2.blk.state) {
            if (e1.blk.getblkbeginpoint() > e2.blk.getblkbeginpoint()) {
                return e1;
            } else {
                return e2;
            }
        } else {
            int a = e1.blk.getblkbeginpoint() / lsu;
            int b = e2.blk.getblkbeginpoint() / lsu;

            if (a < b) {
                System.out.println(a + "--" + b);
                return e2;
            } else {
                return e1;
            }
        }
    }

    public blocks clone(blocks blk) {
        blocks b = new blocks();
        b.b1 = blk.b1;
        b.b2 = blk.b2;
        b.b3 = blk.b3;
        b.b4 = blk.b4;
        b.state = blk.state;
        b.kinds = blk.kinds;
        b.lsu = blk.lsu;
        return b;
    }

    public endblocks getbestblock() {
        endblocks endblock = null;
        endblocks tempendblock = null;
        blocks blk = clone(orblk);
        blocks blk_state = clone(blk);
        int state = blk.state;
        int x = 0;
        boolean isend = true;
        while (isend) {
            setblk(blk);
            int end = lsu * hsu;
            while (end > endpoint - lsu - 1) {

                if (!gm.gmmodel.cango(lsu, blk)) {

                    if (!isfilled(blk) && isenable(blk)) {

                        if (endblock == null) {
                            endblock = getoneokblock(blk);
                            System.out.println(blk.state + "\ttemp:" + "\tok:" +
                                    endblock.qi +
                                    "---" + blk.getblkendpoint() % lsu +
                                    "\tendpoint:" + endpoint);
                        } else {
                            tempendblock = getoneokblock(blk);
                            endblock = compareblocks(endblock, tempendblock);
                            System.out.println(blk.state + "\ttemp:" +
                                    tempendblock.qi + "\tok:" + endblock.qi +
                                    "---" + blk.getblkendpoint() % lsu +
                                    "\tendpoint:" + endpoint + "===" + blk.b1 %
                                    lsu + "-" + blk.b2 % lsu + "--" + blk.b3 %
                                    lsu + "--" + blk.b4 % lsu);
                        }
                    }

                }
                if (gm.gmmodel.cango(-1, blk)) {
                    blk.go();
                    end -= 1;
                } else if (gm.gmmodel.is_overborderline(blk)) {
                    blk.gotest(-blk.getwidth());
                    blk.go();
                    end -= blk.getwidth();

                } else {
                    blk.gotest(-1);
                    blk.go();
                    end -= 1;
                }

            }
            blk_state.circumvolvetest();
            blk_state.circumvolve();
            blk = clone(blk_state);
            System.out.print(blk.b1 % lsu + "-" + blk.b2 % lsu + "--" + blk.b3 %
                    lsu + "--" + blk.b4 % lsu + "state:" + blk.state);
            tempendblock = null;
            if (blk.state == state) {
                isend = false;
            }
        }
        return endblock;
    }

    public router getrouter() {
        router r = new router();
        endblocks okblk = getbestblock();
        if (okblk != null) {
            r.state = okblk.blk.state;
            r.x = okblk.blk.b1 % lsu - orblk.b1 % lsu;
//
//            System.out.println(r.state + "\t" + r.x + "=====" + okblk.qi +
//                    "=====" + okblk.blk.b1 + "=====" + orblk.b1 + "----" +
//                    okblk.blk.b1 % lsu + "======" + orblk.b1 % lsu);
        }

        return r;
    }
}
