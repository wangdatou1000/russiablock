/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package games.russiablock;

import java.util.Arrays;

/**方块类主要定义了方块本身的一些行为和属�?��??
 * 1.初始�?----根据输入的参考点、状态�?�种类来生成方块�?
 * 2.旋转
 * 3.移动
 *
 * @author Administrator
 */
public class Blocks {

    public volatile int b1, b2, b3, b4;
    public volatile int w1, w2, w3, w4;
    public int kinds;
    public volatile int state;
    private volatile int statetest;
    public int lsu;
    private final double a = Math.PI / 2;

	public Blocks() {
    }

	public Blocks(int lsu, int kinds) {
        this.lsu = lsu;
        this.b1 = lsu / 2;
        this.state = 1;
        this.kinds = kinds;
        createblocks();
    }

	public Blocks(int lsu, int kinds, int state) {
        this.lsu = lsu;
        this.b1 = lsu / 2;
        this.kinds = kinds;
        this.state = state;
        createblocks();

    }

    public void createblocks() {
        if (state == 1) {
            state = 4;
        } else {
            state = state - 1;
        }
        circumvolvetest();
        circumvolve();
        self_correcting();
//        System.out.println(kinds);

    }

    /* self_correcting（）�?
    由于旋转后，方块的某些部分的索引可能会是负的
     * 会导致异常的错误，所以需要将索引调整为非负的�?
     */
    private void self_correcting() {
        for (int n = 1; n < 3; n++) {
            if (b2 < 0 || b3 < 0 || b4 < 0) {
                b1 += lsu;
                b2 += lsu;
                b3 += lsu;
                b4 += lsu;
            }
        }
    }

	public void circumvolve() {
        b1 = w1;
        b2 = w2;
        b3 = w3;
        b4 = w4;
        state = statetest;
    }

	public void go() {
        b1 = w1;
        b2 = w2;
        b3 = w3;
        b4 = w4;
    }

	public void gotest(int p) {
        w1 = b1 + p;
        w2 = b2 + p;
        w3 = b3 + p;
        w4 = b4 + p;
    }
//    顺时针进行旋�?

	public void circumvolvetest() {
        int x, y;
        if (state == 4) {
            statetest = 1;
        } else {
            statetest = state + 1;
        }
        w1 = b1;
        switch (kinds) {
            case 1:      // 大方�?
                w2 = b1 + 1;
                w3 = b1 + lsu + 1;
                w4 = b1 + lsu;
                statetest = 1;
                break;
            case 2:       // 长棍
                if (statetest == 3) {
                    statetest = 1;
                }
                if (statetest == 4) {
                    statetest = 2;
                }
                x = (int) Math.cos(a * (2 - statetest));
                y = (int) Math.sin(a * (2 - statetest));
                w2 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w3 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w4 = w1 - y * lsu * 2 + x * 2;
                break;
            case 3:  // 反镰�?
                x = (int) Math.cos(a * (1 - statetest));
                y = (int) Math.sin(a * (1 - statetest));
                w2 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w3 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w4 = w1 - y * lsu * 2 + x * 2;
                break;
            case 4:      //正镰�?
                x = (int) Math.cos(a * (3 - statetest));
                y = (int) Math.sin(a * (3 - statetest));
                w2 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w3 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w4 = w1 - y * lsu * 2 + x * 2;
                break;
            case 5:     //  山型方块
                x = (int) Math.cos(a * (2 - statetest));
                y = (int) Math.sin(a * (2 - statetest));
                w2 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (3 - statetest));
                y = (int) Math.sin(a * (3 - statetest));
                w3 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (1 - statetest));
                y = (int) Math.sin(a * (1 - statetest));
                w4 = w1 - y * lsu + x;
                break;
            case 6:     //  正Z�?
                if (statetest == 3) {
                    statetest = 1;
                }
                if (statetest == 4) {
                    statetest = 2;
                }
                x = (int) Math.cos(a * (3 - statetest));
                y = (int) Math.sin(a * (3 - statetest));
                w2 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w3 = w1 - y * lsu + x;
                x = (int) Math.cos(a * 2 * (1 - statetest));
                y = -1;
                w4 = w1 - y * lsu + x;
                break;
            case 7:     //  反Z�?
                if (statetest == 3) {
                    statetest = 1;
                }
                if (statetest == 4) {
                    statetest = 2;
                }
                x = (int) Math.cos(a * (1 - statetest));
                y = (int) Math.sin(a * (1 - statetest));
                w2 = w1 - y * lsu + x;
                x = (int) Math.cos(a * (4 - statetest));
                y = (int) Math.sin(a * (4 - statetest));
                w3 = w1 - y * lsu + x;
                y = -(int) Math.cos(a * 2 * (1 - statetest));
                x = -1;
                w4 = w1 - y * lsu + x;
                break;
        }
    }

    public int getblkendpoint() {
        int endpoint = b1;
        if (b2 > endpoint) {
            endpoint = b2;
        }
        if (b3 > endpoint) {
            endpoint = b3;
        }
        if (b4 > endpoint) {
            endpoint = b4;
        }
        return endpoint;
    }

    public int getblkbeginpoint() {
        int endpoint = b1;
        if (b2 < endpoint) {
            endpoint = b2;
        }
        if (b3 < endpoint) {
            endpoint = b3;
        }
        if (b4 < endpoint) {
            endpoint = b4;
        }
        return endpoint;
    }

    public int getwidth() {
        int[] width = new int[4];
        width[0] = b1 % lsu;
        width[1] = b4 % lsu;
        width[2] = b3 % lsu;
        width[3] = b2 % lsu;
        Arrays.sort(width);
        return width[3] - width[0]+1;
    }
}