/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.openpisces.Helpers;
import java.util.Iterator;

final class Curve {
    float ax;
    float ay;
    float bx;
    float by;
    float cx;
    float cy;
    float dx;
    float dy;
    float dax;
    float day;
    float dbx;
    float dby;

    Curve() {
    }

    void set(float[] arrf, int n) {
        switch (n) {
            case 8: {
                this.set(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5], arrf[6], arrf[7]);
                break;
            }
            case 6: {
                this.set(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                break;
            }
            default: {
                throw new InternalError("Curves can only be cubic or quadratic");
            }
        }
    }

    void set(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.ax = 3.0f * (f3 - f5) + f7 - f;
        this.ay = 3.0f * (f4 - f6) + f8 - f2;
        this.bx = 3.0f * (f - 2.0f * f3 + f5);
        this.by = 3.0f * (f2 - 2.0f * f4 + f6);
        this.cx = 3.0f * (f3 - f);
        this.cy = 3.0f * (f4 - f2);
        this.dx = f;
        this.dy = f2;
        this.dax = 3.0f * this.ax;
        this.day = 3.0f * this.ay;
        this.dbx = 2.0f * this.bx;
        this.dby = 2.0f * this.by;
    }

    void set(float f, float f2, float f3, float f4, float f5, float f6) {
        this.ay = 0.0f;
        this.ax = 0.0f;
        this.bx = f - 2.0f * f3 + f5;
        this.by = f2 - 2.0f * f4 + f6;
        this.cx = 2.0f * (f3 - f);
        this.cy = 2.0f * (f4 - f2);
        this.dx = f;
        this.dy = f2;
        this.dax = 0.0f;
        this.day = 0.0f;
        this.dbx = 2.0f * this.bx;
        this.dby = 2.0f * this.by;
    }

    float xat(float f) {
        return f * (f * (f * this.ax + this.bx) + this.cx) + this.dx;
    }

    float yat(float f) {
        return f * (f * (f * this.ay + this.by) + this.cy) + this.dy;
    }

    float dxat(float f) {
        return f * (f * this.dax + this.dbx) + this.cx;
    }

    float dyat(float f) {
        return f * (f * this.day + this.dby) + this.cy;
    }

    int dxRoots(float[] arrf, int n) {
        return Helpers.quadraticRoots(this.dax, this.dbx, this.cx, arrf, n);
    }

    int dyRoots(float[] arrf, int n) {
        return Helpers.quadraticRoots(this.day, this.dby, this.cy, arrf, n);
    }

    int infPoints(float[] arrf, int n) {
        float f = this.dax * this.dby - this.dbx * this.day;
        float f2 = 2.0f * (this.cy * this.dax - this.day * this.cx);
        float f3 = this.cy * this.dbx - this.cx * this.dby;
        return Helpers.quadraticRoots(f, f2, f3, arrf, n);
    }

    private int perpendiculardfddf(float[] arrf, int n) {
        assert (arrf.length >= n + 4);
        float f = 2.0f * (this.dax * this.dax + this.day * this.day);
        float f2 = 3.0f * (this.dax * this.dbx + this.day * this.dby);
        float f3 = 2.0f * (this.dax * this.cx + this.day * this.cy) + this.dbx * this.dbx + this.dby * this.dby;
        float f4 = this.dbx * this.cx + this.dby * this.cy;
        return Helpers.cubicRootsInAB(f, f2, f3, f4, arrf, n, 0.0f, 1.0f);
    }

    int rootsOfROCMinusW(float[] arrf, int n, float f, float f2) {
        assert (n <= 6 && arrf.length >= 10);
        int n2 = n;
        int n3 = this.perpendiculardfddf(arrf, n);
        float f3 = 0.0f;
        float f4 = this.ROCsq(f3) - f * f;
        arrf[n + n3] = 1.0f;
        for (int i = n; i < n + ++n3; ++i) {
            float f5 = arrf[i];
            float f6 = this.ROCsq(f5) - f * f;
            if (f4 == 0.0f) {
                arrf[n2++] = f3;
            } else if (f6 * f4 < 0.0f) {
                arrf[n2++] = this.falsePositionROCsqMinusX(f3, f5, f * f, f2);
            }
            f3 = f5;
            f4 = f6;
        }
        return n2 - n;
    }

    private static float eliminateInf(float f) {
        return f == Float.POSITIVE_INFINITY ? Float.MAX_VALUE : (f == Float.NEGATIVE_INFINITY ? Float.MIN_VALUE : f);
    }

    private float falsePositionROCsqMinusX(float f, float f2, float f3, float f4) {
        int n = 0;
        float f5 = f2;
        float f6 = Curve.eliminateInf(this.ROCsq(f5) - f3);
        float f7 = f;
        float f8 = Curve.eliminateInf(this.ROCsq(f7) - f3);
        float f9 = f7;
        for (int i = 0; i < 100 && Math.abs(f5 - f7) > f4 * Math.abs(f5 + f7); ++i) {
            f9 = (f8 * f5 - f6 * f7) / (f8 - f6);
            float f10 = this.ROCsq(f9) - f3;
            if (Curve.sameSign(f10, f6)) {
                f6 = f10;
                f5 = f9;
                if (n < 0) {
                    f8 /= (float)(1 << -n);
                    --n;
                    continue;
                }
                n = -1;
                continue;
            }
            if (!(f10 * f8 > 0.0f)) break;
            f8 = f10;
            f7 = f9;
            if (n > 0) {
                f6 /= (float)(1 << n);
                ++n;
                continue;
            }
            n = 1;
        }
        return f9;
    }

    private static boolean sameSign(double d, double d2) {
        return d < 0.0 && d2 < 0.0 || d > 0.0 && d2 > 0.0;
    }

    private float ROCsq(float f) {
        float f2 = f * (f * this.dax + this.dbx) + this.cx;
        float f3 = f * (f * this.day + this.dby) + this.cy;
        float f4 = 2.0f * this.dax * f + this.dbx;
        float f5 = 2.0f * this.day * f + this.dby;
        float f6 = f2 * f2 + f3 * f3;
        float f7 = f4 * f4 + f5 * f5;
        float f8 = f4 * f2 + f5 * f3;
        return f6 * (f6 * f6 / (f6 * f7 - f8 * f8));
    }

    static Iterator<Integer> breakPtsAtTs(final float[] arrf, final int n, final float[] arrf2, final int n2) {
        assert (arrf.length >= 2 * n && n2 <= arrf2.length);
        return new Iterator<Integer>(){
            final Integer i0 = 0;
            final Integer itype = n;
            int nextCurveIdx = 0;
            Integer curCurveOff = this.i0;
            float prevT = 0.0f;

            @Override
            public boolean hasNext() {
                return this.nextCurveIdx < n2 + 1;
            }

            @Override
            public Integer next() {
                Integer n3;
                if (this.nextCurveIdx < n2) {
                    float f = arrf2[this.nextCurveIdx];
                    float f2 = (f - this.prevT) / (1.0f - this.prevT);
                    Helpers.subdivideAt(f2, arrf, this.curCurveOff, arrf, 0, arrf, n, n);
                    this.prevT = f;
                    n3 = this.i0;
                    this.curCurveOff = this.itype;
                } else {
                    n3 = this.curCurveOff;
                }
                ++this.nextCurveIdx;
                return n3;
            }

            @Override
            public void remove() {
            }
        };
    }
}

