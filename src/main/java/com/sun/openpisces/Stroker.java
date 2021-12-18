/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.openpisces.Curve;
import com.sun.openpisces.Helpers;
import java.util.Arrays;

public final class Stroker
implements PathConsumer2D {
    private static final int MOVE_TO = 0;
    private static final int DRAWING_OP_TO = 1;
    private static final int CLOSE = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int JOIN_BEVEL = 2;
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    private PathConsumer2D out;
    private int capStyle;
    private int joinStyle;
    private float lineWidth2;
    private final float[][] offset = new float[3][2];
    private final float[] miter = new float[2];
    private float miterLimitSq;
    private int prev;
    private float sx0;
    private float sy0;
    private float sdx;
    private float sdy;
    private float cx0;
    private float cy0;
    private float cdx;
    private float cdy;
    private float smx;
    private float smy;
    private float cmx;
    private float cmy;
    private final PolyStack reverse = new PolyStack();
    private static final float ROUND_JOIN_THRESHOLD = 0.015258789f;
    private float[] middle = new float[88];
    private float[] lp = new float[8];
    private float[] rp = new float[8];
    private static final int MAX_N_CURVES = 11;
    private float[] subdivTs = new float[10];
    private static Curve c = new Curve();

    public Stroker(PathConsumer2D pathConsumer2D, float f, int n, int n2, float f2) {
        this(pathConsumer2D);
        this.reset(f, n, n2, f2);
    }

    public Stroker(PathConsumer2D pathConsumer2D) {
        this.setConsumer(pathConsumer2D);
    }

    public void setConsumer(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
    }

    public void reset(float f, int n, int n2, float f2) {
        this.lineWidth2 = f / 2.0f;
        this.capStyle = n;
        this.joinStyle = n2;
        float f3 = f2 * this.lineWidth2;
        this.miterLimitSq = f3 * f3;
        this.prev = 2;
    }

    private static void computeOffset(float f, float f2, float f3, float[] arrf) {
        float f4 = (float)Math.sqrt(f * f + f2 * f2);
        if (f4 == 0.0f) {
            arrf[1] = 0.0f;
            arrf[0] = 0.0f;
        } else {
            arrf[0] = f2 * f3 / f4;
            arrf[1] = -(f * f3) / f4;
        }
    }

    private static boolean isCW(float f, float f2, float f3, float f4) {
        return f * f4 <= f2 * f3;
    }

    private void drawRoundJoin(float f, float f2, float f3, float f4, float f5, float f6, boolean bl, float f7) {
        if (f3 == 0.0f && f4 == 0.0f || f5 == 0.0f && f6 == 0.0f) {
            return;
        }
        float f8 = f3 - f5;
        float f9 = f4 - f6;
        float f10 = f8 * f8 + f9 * f9;
        if (f10 < f7) {
            return;
        }
        if (bl) {
            f3 = -f3;
            f4 = -f4;
            f5 = -f5;
            f6 = -f6;
        }
        this.drawRoundJoin(f, f2, f3, f4, f5, f6, bl);
    }

    private void drawRoundJoin(float f, float f2, float f3, float f4, float f5, float f6, boolean bl) {
        double d = f3 * f5 + f4 * f6;
        int n = d >= 0.0 ? 1 : 2;
        switch (n) {
            case 1: {
                this.drawBezApproxForArc(f, f2, f3, f4, f5, f6, bl);
                break;
            }
            case 2: {
                float f7 = f6 - f4;
                float f8 = f3 - f5;
                float f9 = (float)Math.sqrt(f7 * f7 + f8 * f8);
                float f10 = this.lineWidth2 / f9;
                float f11 = f7 * f10;
                float f12 = f8 * f10;
                if (bl) {
                    f11 = -f11;
                    f12 = -f12;
                }
                this.drawBezApproxForArc(f, f2, f3, f4, f11, f12, bl);
                this.drawBezApproxForArc(f, f2, f11, f12, f5, f6, bl);
            }
        }
    }

    private void drawBezApproxForArc(float f, float f2, float f3, float f4, float f5, float f6, boolean bl) {
        float f7 = (f3 * f5 + f4 * f6) / (2.0f * this.lineWidth2 * this.lineWidth2);
        float f8 = (float)(1.3333333333333333 * Math.sqrt(0.5 - (double)f7) / (1.0 + Math.sqrt((double)f7 + 0.5)));
        if (bl) {
            f8 = -f8;
        }
        float f9 = f + f3;
        float f10 = f2 + f4;
        float f11 = f9 - f8 * f4;
        float f12 = f10 + f8 * f3;
        float f13 = f + f5;
        float f14 = f2 + f6;
        float f15 = f13 + f8 * f6;
        float f16 = f14 - f8 * f5;
        this.emitCurveTo(f9, f10, f11, f12, f15, f16, f13, f14, bl);
    }

    private void drawRoundCap(float f, float f2, float f3, float f4) {
        this.emitCurveTo(f + f3, f2 + f4, f + f3 - 0.5522848f * f4, f2 + f4 + 0.5522848f * f3, f - f4 + 0.5522848f * f3, f2 + f3 + 0.5522848f * f4, f - f4, f2 + f3, false);
        this.emitCurveTo(f - f4, f2 + f3, f - f4 - 0.5522848f * f3, f2 + f3 - 0.5522848f * f4, f - f3 - 0.5522848f * f4, f2 - f4 + 0.5522848f * f3, f - f3, f2 - f4, false);
    }

    private void computeMiter(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float[] arrf, int n) {
        float f9 = f3 - f;
        float f10 = f4 - f2;
        float f11 = f7 - f5;
        float f12 = f8 - f6;
        float f13 = f9 * f12 - f11 * f10;
        float f14 = f11 * (f2 - f6) - f12 * (f - f5);
        arrf[n++] = f + (f14 /= f13) * f9;
        arrf[n] = f2 + f14 * f10;
    }

    private void safecomputeMiter(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float[] arrf, int n) {
        float f9 = f3 - f;
        float f10 = f8 - f6;
        float f11 = f7 - f5;
        float f12 = f4 - f2;
        float f13 = f9 * f10 - f11 * f12;
        if (f13 == 0.0f) {
            arrf[n++] = (f + f5) / 2.0f;
            arrf[n] = (f2 + f6) / 2.0f;
            return;
        }
        float f14 = f11 * (f2 - f6) - f10 * (f - f5);
        arrf[n++] = f + (f14 /= f13) * f9;
        arrf[n] = f2 + f14 * f12;
    }

    private void drawMiter(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, boolean bl) {
        if (f9 == f7 && f10 == f8 || f == 0.0f && f2 == 0.0f || f5 == 0.0f && f6 == 0.0f) {
            return;
        }
        if (bl) {
            f7 = -f7;
            f8 = -f8;
            f9 = -f9;
            f10 = -f10;
        }
        this.computeMiter(f3 - f + f7, f4 - f2 + f8, f3 + f7, f4 + f8, f5 + f3 + f9, f6 + f4 + f10, f3 + f9, f4 + f10, this.miter, 0);
        float f11 = (this.miter[0] - f3) * (this.miter[0] - f3) + (this.miter[1] - f4) * (this.miter[1] - f4);
        if (f11 < this.miterLimitSq) {
            this.emitLineTo(this.miter[0], this.miter[1], bl);
        }
    }

    @Override
    public void moveTo(float f, float f2) {
        if (this.prev == 1) {
            this.finish();
        }
        this.sx0 = this.cx0 = f;
        this.sy0 = this.cy0 = f2;
        this.sdx = 1.0f;
        this.cdx = 1.0f;
        this.sdy = 0.0f;
        this.cdy = 0.0f;
        this.prev = 0;
    }

    @Override
    public void lineTo(float f, float f2) {
        float f3 = f - this.cx0;
        float f4 = f2 - this.cy0;
        if (f3 == 0.0f && f4 == 0.0f) {
            f3 = 1.0f;
        }
        Stroker.computeOffset(f3, f4, this.lineWidth2, this.offset[0]);
        float f5 = this.offset[0][0];
        float f6 = this.offset[0][1];
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f3, f4, this.cmx, this.cmy, f5, f6);
        this.emitLineTo(this.cx0 + f5, this.cy0 + f6);
        this.emitLineTo(f + f5, f2 + f6);
        this.emitLineTo(this.cx0 - f5, this.cy0 - f6, true);
        this.emitLineTo(f - f5, f2 - f6, true);
        this.cmx = f5;
        this.cmy = f6;
        this.cdx = f3;
        this.cdy = f4;
        this.cx0 = f;
        this.cy0 = f2;
        this.prev = 1;
    }

    @Override
    public void closePath() {
        if (this.prev != 1) {
            if (this.prev == 2) {
                return;
            }
            this.emitMoveTo(this.cx0, this.cy0 - this.lineWidth2);
            this.smx = 0.0f;
            this.cmx = 0.0f;
            this.cmy = this.smy = -this.lineWidth2;
            this.sdx = 1.0f;
            this.cdx = 1.0f;
            this.sdy = 0.0f;
            this.cdy = 0.0f;
            this.finish();
            return;
        }
        if (this.cx0 != this.sx0 || this.cy0 != this.sy0) {
            this.lineTo(this.sx0, this.sy0);
        }
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, this.sdx, this.sdy, this.cmx, this.cmy, this.smx, this.smy);
        this.emitLineTo(this.sx0 + this.smx, this.sy0 + this.smy);
        this.emitMoveTo(this.sx0 - this.smx, this.sy0 - this.smy);
        this.emitReverse();
        this.prev = 2;
        this.emitClose();
    }

    private void emitReverse() {
        while (!this.reverse.isEmpty()) {
            this.reverse.pop(this.out);
        }
    }

    @Override
    public void pathDone() {
        if (this.prev == 1) {
            this.finish();
        }
        this.out.pathDone();
        this.prev = 2;
    }

    private void finish() {
        if (this.capStyle == 1) {
            this.drawRoundCap(this.cx0, this.cy0, this.cmx, this.cmy);
        } else if (this.capStyle == 2) {
            this.emitLineTo(this.cx0 - this.cmy + this.cmx, this.cy0 + this.cmx + this.cmy);
            this.emitLineTo(this.cx0 - this.cmy - this.cmx, this.cy0 + this.cmx - this.cmy);
        }
        this.emitReverse();
        if (this.capStyle == 1) {
            this.drawRoundCap(this.sx0, this.sy0, -this.smx, -this.smy);
        } else if (this.capStyle == 2) {
            this.emitLineTo(this.sx0 + this.smy - this.smx, this.sy0 - this.smx - this.smy);
            this.emitLineTo(this.sx0 + this.smy + this.smx, this.sy0 - this.smx + this.smy);
        }
        this.emitClose();
    }

    private void emitMoveTo(float f, float f2) {
        this.out.moveTo(f, f2);
    }

    private void emitLineTo(float f, float f2) {
        this.out.lineTo(f, f2);
    }

    private void emitLineTo(float f, float f2, boolean bl) {
        if (bl) {
            this.reverse.pushLine(f, f2);
        } else {
            this.emitLineTo(f, f2);
        }
    }

    private void emitQuadTo(float f, float f2, float f3, float f4, float f5, float f6, boolean bl) {
        if (bl) {
            this.reverse.pushQuad(f, f2, f3, f4);
        } else {
            this.out.quadTo(f3, f4, f5, f6);
        }
    }

    private void emitCurveTo(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, boolean bl) {
        if (bl) {
            this.reverse.pushCubic(f, f2, f3, f4, f5, f6);
        } else {
            this.out.curveTo(f3, f4, f5, f6, f7, f8);
        }
    }

    private void emitClose() {
        this.out.closePath();
    }

    private void drawJoin(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        if (this.prev != 1) {
            this.emitMoveTo(f3 + f9, f4 + f10);
            this.sdx = f5;
            this.sdy = f6;
            this.smx = f9;
            this.smy = f10;
        } else {
            boolean bl = Stroker.isCW(f, f2, f5, f6);
            if (this.joinStyle == 0) {
                this.drawMiter(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, bl);
            } else if (this.joinStyle == 1) {
                this.drawRoundJoin(f3, f4, f7, f8, f9, f10, bl, 0.015258789f);
            }
            this.emitLineTo(f3, f4, !bl);
        }
        this.prev = 1;
    }

    private static boolean within(float f, float f2, float f3, float f4, float f5) {
        assert (f5 > 0.0f) : "";
        return Helpers.within(f, f3, f5) && Helpers.within(f2, f4, f5);
    }

    private void getLineOffsets(float f, float f2, float f3, float f4, float[] arrf, float[] arrf2) {
        Stroker.computeOffset(f3 - f, f4 - f2, this.lineWidth2, this.offset[0]);
        arrf[0] = f + this.offset[0][0];
        arrf[1] = f2 + this.offset[0][1];
        arrf[2] = f3 + this.offset[0][0];
        arrf[3] = f4 + this.offset[0][1];
        arrf2[0] = f - this.offset[0][0];
        arrf2[1] = f2 - this.offset[0][1];
        arrf2[2] = f3 - this.offset[0][0];
        arrf2[3] = f4 - this.offset[0][1];
    }

    private int computeOffsetCubic(float[] arrf, int n, float[] arrf2, float[] arrf3) {
        float f = arrf[n + 0];
        float f2 = arrf[n + 1];
        float f3 = arrf[n + 2];
        float f4 = arrf[n + 3];
        float f5 = arrf[n + 4];
        float f6 = arrf[n + 5];
        float f7 = arrf[n + 6];
        float f8 = arrf[n + 7];
        float f9 = f7 - f5;
        float f10 = f8 - f6;
        float f11 = f3 - f;
        float f12 = f4 - f2;
        boolean bl = Stroker.within(f, f2, f3, f4, 6.0f * Math.ulp(f4));
        boolean bl2 = Stroker.within(f5, f6, f7, f8, 6.0f * Math.ulp(f8));
        if (bl && bl2) {
            this.getLineOffsets(f, f2, f7, f8, arrf2, arrf3);
            return 4;
        }
        if (bl) {
            f11 = f5 - f;
            f12 = f6 - f2;
        } else if (bl2) {
            f9 = f7 - f3;
            f10 = f8 - f4;
        }
        float f13 = f11 * f9 + f12 * f10;
        f13 *= f13;
        float f14 = f11 * f11 + f12 * f12;
        float f15 = f9 * f9 + f10 * f10;
        if (Helpers.within(f13, f14 * f15, 4.0f * Math.ulp(f13))) {
            this.getLineOffsets(f, f2, f7, f8, arrf2, arrf3);
            return 4;
        }
        float f16 = 0.125f * (f + 3.0f * (f3 + f5) + f7);
        float f17 = 0.125f * (f2 + 3.0f * (f4 + f6) + f8);
        float f18 = f5 + f7 - f - f3;
        float f19 = f6 + f8 - f2 - f4;
        Stroker.computeOffset(f11, f12, this.lineWidth2, this.offset[0]);
        Stroker.computeOffset(f18, f19, this.lineWidth2, this.offset[1]);
        Stroker.computeOffset(f9, f10, this.lineWidth2, this.offset[2]);
        float f20 = f + this.offset[0][0];
        float f21 = f2 + this.offset[0][1];
        float f22 = f16 + this.offset[1][0];
        float f23 = f17 + this.offset[1][1];
        float f24 = f7 + this.offset[2][0];
        float f25 = f8 + this.offset[2][1];
        float f26 = 4.0f / (3.0f * (f11 * f10 - f12 * f9));
        float f27 = 2.0f * f22 - f20 - f24;
        float f28 = 2.0f * f23 - f21 - f25;
        float f29 = f26 * (f10 * f27 - f9 * f28);
        float f30 = f26 * (f11 * f28 - f12 * f27);
        float f31 = f20 + f29 * f11;
        float f32 = f21 + f29 * f12;
        float f33 = f24 + f30 * f9;
        float f34 = f25 + f30 * f10;
        arrf2[0] = f20;
        arrf2[1] = f21;
        arrf2[2] = f31;
        arrf2[3] = f32;
        arrf2[4] = f33;
        arrf2[5] = f34;
        arrf2[6] = f24;
        arrf2[7] = f25;
        f20 = f - this.offset[0][0];
        f21 = f2 - this.offset[0][1];
        f24 = f7 - this.offset[2][0];
        f25 = f8 - this.offset[2][1];
        f27 = 2.0f * (f22 -= 2.0f * this.offset[1][0]) - f20 - f24;
        f28 = 2.0f * (f23 -= 2.0f * this.offset[1][1]) - f21 - f25;
        f29 = f26 * (f10 * f27 - f9 * f28);
        f30 = f26 * (f11 * f28 - f12 * f27);
        f31 = f20 + f29 * f11;
        f32 = f21 + f29 * f12;
        f33 = f24 + f30 * f9;
        f34 = f25 + f30 * f10;
        arrf3[0] = f20;
        arrf3[1] = f21;
        arrf3[2] = f31;
        arrf3[3] = f32;
        arrf3[4] = f33;
        arrf3[5] = f34;
        arrf3[6] = f24;
        arrf3[7] = f25;
        return 8;
    }

    private int computeOffsetQuad(float[] arrf, int n, float[] arrf2, float[] arrf3) {
        float f = arrf[n + 0];
        float f2 = arrf[n + 1];
        float f3 = arrf[n + 2];
        float f4 = arrf[n + 3];
        float f5 = arrf[n + 4];
        float f6 = arrf[n + 5];
        float f7 = f5 - f3;
        float f8 = f6 - f4;
        float f9 = f3 - f;
        float f10 = f4 - f2;
        boolean bl = Stroker.within(f, f2, f3, f4, 6.0f * Math.ulp(f4));
        boolean bl2 = Stroker.within(f3, f4, f5, f6, 6.0f * Math.ulp(f6));
        if (bl || bl2) {
            this.getLineOffsets(f, f2, f5, f6, arrf2, arrf3);
            return 4;
        }
        float f11 = f9 * f7 + f10 * f8;
        float f12 = f9 * f9 + f10 * f10;
        float f13 = f7 * f7 + f8 * f8;
        if (Helpers.within(f11 *= f11, f12 * f13, 4.0f * Math.ulp(f11))) {
            this.getLineOffsets(f, f2, f5, f6, arrf2, arrf3);
            return 4;
        }
        Stroker.computeOffset(f9, f10, this.lineWidth2, this.offset[0]);
        Stroker.computeOffset(f7, f8, this.lineWidth2, this.offset[1]);
        float f14 = f + this.offset[0][0];
        float f15 = f2 + this.offset[0][1];
        float f16 = f5 + this.offset[1][0];
        float f17 = f6 + this.offset[1][1];
        this.safecomputeMiter(f14, f15, f14 + f9, f15 + f10, f16, f17, f16 - f7, f17 - f8, arrf2, 2);
        arrf2[0] = f14;
        arrf2[1] = f15;
        arrf2[4] = f16;
        arrf2[5] = f17;
        f14 = f - this.offset[0][0];
        f15 = f2 - this.offset[0][1];
        f16 = f5 - this.offset[1][0];
        f17 = f6 - this.offset[1][1];
        this.safecomputeMiter(f14, f15, f14 + f9, f15 + f10, f16, f17, f16 - f7, f17 - f8, arrf3, 2);
        arrf3[0] = f14;
        arrf3[1] = f15;
        arrf3[4] = f16;
        arrf3[5] = f17;
        return 6;
    }

    private static int findSubdivPoints(float[] arrf, float[] arrf2, int n, float f) {
        float f2 = arrf[2] - arrf[0];
        float f3 = arrf[3] - arrf[1];
        if (f3 != 0.0f && f2 != 0.0f) {
            float f4 = (float)Math.sqrt(f2 * f2 + f3 * f3);
            float f5 = f2 / f4;
            float f6 = f3 / f4;
            float f7 = f5 * arrf[0] + f6 * arrf[1];
            float f8 = f5 * arrf[1] - f6 * arrf[0];
            float f9 = f5 * arrf[2] + f6 * arrf[3];
            float f10 = f5 * arrf[3] - f6 * arrf[2];
            float f11 = f5 * arrf[4] + f6 * arrf[5];
            float f12 = f5 * arrf[5] - f6 * arrf[4];
            switch (n) {
                case 8: {
                    float f13 = f5 * arrf[6] + f6 * arrf[7];
                    float f14 = f5 * arrf[7] - f6 * arrf[6];
                    c.set(f7, f8, f9, f10, f11, f12, f13, f14);
                    break;
                }
                case 6: {
                    c.set(f7, f8, f9, f10, f11, f12);
                }
            }
        } else {
            c.set(arrf, n);
        }
        int n2 = 0;
        n2 += c.dxRoots(arrf2, n2);
        n2 += c.dyRoots(arrf2, n2);
        if (n == 8) {
            n2 += c.infPoints(arrf2, n2);
        }
        n2 += c.rootsOfROCMinusW(arrf2, n2, f, 1.0E-4f);
        n2 = Helpers.filterOutNotInAB(arrf2, 0, n2, 1.0E-4f, 0.9999f);
        Helpers.isort(arrf2, 0, n2);
        return n2;
    }

    @Override
    public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
        int n;
        float f7;
        boolean bl;
        this.middle[0] = this.cx0;
        this.middle[1] = this.cy0;
        this.middle[2] = f;
        this.middle[3] = f2;
        this.middle[4] = f3;
        this.middle[5] = f4;
        this.middle[6] = f5;
        this.middle[7] = f6;
        float f8 = this.middle[6];
        float f9 = this.middle[7];
        float f10 = this.middle[2] - this.middle[0];
        float f11 = this.middle[3] - this.middle[1];
        float f12 = this.middle[6] - this.middle[4];
        float f13 = this.middle[7] - this.middle[5];
        boolean bl2 = f10 == 0.0f && f11 == 0.0f;
        boolean bl3 = bl = f12 == 0.0f && f13 == 0.0f;
        if (bl2) {
            f10 = this.middle[4] - this.middle[0];
            f11 = this.middle[5] - this.middle[1];
            if (f10 == 0.0f && f11 == 0.0f) {
                f10 = this.middle[6] - this.middle[0];
                f11 = this.middle[7] - this.middle[1];
            }
        }
        if (bl) {
            f12 = this.middle[6] - this.middle[2];
            f13 = this.middle[7] - this.middle[3];
            if (f12 == 0.0f && f13 == 0.0f) {
                f12 = this.middle[6] - this.middle[0];
                f13 = this.middle[7] - this.middle[1];
            }
        }
        if (f10 == 0.0f && f11 == 0.0f) {
            this.lineTo(this.middle[0], this.middle[1]);
            return;
        }
        if (Math.abs(f10) < 0.1f && Math.abs(f11) < 0.1f) {
            f7 = (float)Math.sqrt(f10 * f10 + f11 * f11);
            f10 /= f7;
            f11 /= f7;
        }
        if (Math.abs(f12) < 0.1f && Math.abs(f13) < 0.1f) {
            f7 = (float)Math.sqrt(f12 * f12 + f13 * f13);
            f12 /= f7;
            f13 /= f7;
        }
        Stroker.computeOffset(f10, f11, this.lineWidth2, this.offset[0]);
        f7 = this.offset[0][0];
        float f14 = this.offset[0][1];
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f10, f11, this.cmx, this.cmy, f7, f14);
        int n2 = Stroker.findSubdivPoints(this.middle, this.subdivTs, 8, this.lineWidth2);
        float f15 = 0.0f;
        for (n = 0; n < n2; ++n) {
            float f16 = this.subdivTs[n];
            Helpers.subdivideCubicAt((f16 - f15) / (1.0f - f15), this.middle, n * 6, this.middle, n * 6, this.middle, n * 6 + 6);
            f15 = f16;
        }
        n = 0;
        for (int i = 0; i <= n2; ++i) {
            n = this.computeOffsetCubic(this.middle, i * 6, this.lp, this.rp);
            if (n == 0) continue;
            this.emitLineTo(this.lp[0], this.lp[1]);
            switch (n) {
                case 8: {
                    this.emitCurveTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], this.lp[6], this.lp[7], false);
                    this.emitCurveTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], this.rp[6], this.rp[7], true);
                    break;
                }
                case 4: {
                    this.emitLineTo(this.lp[2], this.lp[3]);
                    this.emitLineTo(this.rp[0], this.rp[1], true);
                }
            }
            this.emitLineTo(this.rp[n - 2], this.rp[n - 1], true);
        }
        this.cmx = (this.lp[n - 2] - this.rp[n - 2]) / 2.0f;
        this.cmy = (this.lp[n - 1] - this.rp[n - 1]) / 2.0f;
        this.cdx = f12;
        this.cdy = f13;
        this.cx0 = f8;
        this.cy0 = f9;
        this.prev = 1;
    }

    @Override
    public void quadTo(float f, float f2, float f3, float f4) {
        int n;
        float f5;
        this.middle[0] = this.cx0;
        this.middle[1] = this.cy0;
        this.middle[2] = f;
        this.middle[3] = f2;
        this.middle[4] = f3;
        this.middle[5] = f4;
        float f6 = this.middle[4];
        float f7 = this.middle[5];
        float f8 = this.middle[2] - this.middle[0];
        float f9 = this.middle[3] - this.middle[1];
        float f10 = this.middle[4] - this.middle[2];
        float f11 = this.middle[5] - this.middle[3];
        if (f8 == 0.0f && f9 == 0.0f || f10 == 0.0f && f11 == 0.0f) {
            f8 = f10 = this.middle[4] - this.middle[0];
            f9 = f11 = this.middle[5] - this.middle[1];
        }
        if (f8 == 0.0f && f9 == 0.0f) {
            this.lineTo(this.middle[0], this.middle[1]);
            return;
        }
        if (Math.abs(f8) < 0.1f && Math.abs(f9) < 0.1f) {
            f5 = (float)Math.sqrt(f8 * f8 + f9 * f9);
            f8 /= f5;
            f9 /= f5;
        }
        if (Math.abs(f10) < 0.1f && Math.abs(f11) < 0.1f) {
            f5 = (float)Math.sqrt(f10 * f10 + f11 * f11);
            f10 /= f5;
            f11 /= f5;
        }
        Stroker.computeOffset(f8, f9, this.lineWidth2, this.offset[0]);
        f5 = this.offset[0][0];
        float f12 = this.offset[0][1];
        this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, f8, f9, this.cmx, this.cmy, f5, f12);
        int n2 = Stroker.findSubdivPoints(this.middle, this.subdivTs, 6, this.lineWidth2);
        float f13 = 0.0f;
        for (n = 0; n < n2; ++n) {
            float f14 = this.subdivTs[n];
            Helpers.subdivideQuadAt((f14 - f13) / (1.0f - f13), this.middle, n * 4, this.middle, n * 4, this.middle, n * 4 + 4);
            f13 = f14;
        }
        n = 0;
        for (int i = 0; i <= n2; ++i) {
            n = this.computeOffsetQuad(this.middle, i * 4, this.lp, this.rp);
            if (n == 0) continue;
            this.emitLineTo(this.lp[0], this.lp[1]);
            switch (n) {
                case 6: {
                    this.emitQuadTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], false);
                    this.emitQuadTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], true);
                    break;
                }
                case 4: {
                    this.emitLineTo(this.lp[2], this.lp[3]);
                    this.emitLineTo(this.rp[0], this.rp[1], true);
                }
            }
            this.emitLineTo(this.rp[n - 2], this.rp[n - 1], true);
        }
        this.cmx = (this.lp[n - 2] - this.rp[n - 2]) / 2.0f;
        this.cmy = (this.lp[n - 1] - this.rp[n - 1]) / 2.0f;
        this.cdx = f10;
        this.cdy = f11;
        this.cx0 = f6;
        this.cy0 = f7;
        this.prev = 1;
    }

    private static final class PolyStack {
        float[] curves = new float[400];
        int end = 0;
        int[] curveTypes = new int[50];
        int numCurves = 0;
        private static final int INIT_SIZE = 50;

        PolyStack() {
        }

        public boolean isEmpty() {
            return this.numCurves == 0;
        }

        private void ensureSpace(int n) {
            int n2;
            if (this.end + n >= this.curves.length) {
                n2 = (this.end + n) * 2;
                this.curves = Arrays.copyOf(this.curves, n2);
            }
            if (this.numCurves >= this.curveTypes.length) {
                n2 = this.numCurves * 2;
                this.curveTypes = Arrays.copyOf(this.curveTypes, n2);
            }
        }

        public void pushCubic(float f, float f2, float f3, float f4, float f5, float f6) {
            this.ensureSpace(6);
            this.curveTypes[this.numCurves++] = 8;
            this.curves[this.end++] = f5;
            this.curves[this.end++] = f6;
            this.curves[this.end++] = f3;
            this.curves[this.end++] = f4;
            this.curves[this.end++] = f;
            this.curves[this.end++] = f2;
        }

        public void pushQuad(float f, float f2, float f3, float f4) {
            this.ensureSpace(4);
            this.curveTypes[this.numCurves++] = 6;
            this.curves[this.end++] = f3;
            this.curves[this.end++] = f4;
            this.curves[this.end++] = f;
            this.curves[this.end++] = f2;
        }

        public void pushLine(float f, float f2) {
            this.ensureSpace(2);
            this.curveTypes[this.numCurves++] = 4;
            this.curves[this.end++] = f;
            this.curves[this.end++] = f2;
        }

        public int pop(float[] arrf) {
            int n = this.curveTypes[this.numCurves - 1];
            --this.numCurves;
            this.end -= n - 2;
            System.arraycopy(this.curves, this.end, arrf, 0, n - 2);
            return n;
        }

        public void pop(PathConsumer2D pathConsumer2D) {
            --this.numCurves;
            int n = this.curveTypes[this.numCurves];
            this.end -= n - 2;
            switch (n) {
                case 8: {
                    pathConsumer2D.curveTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3], this.curves[this.end + 4], this.curves[this.end + 5]);
                    break;
                }
                case 6: {
                    pathConsumer2D.quadTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3]);
                    break;
                }
                case 4: {
                    pathConsumer2D.lineTo(this.curves[this.end], this.curves[this.end + 1]);
                }
            }
        }

        public String toString() {
            String string = "";
            int n = this.numCurves;
            int n2 = this.end;
            while (n > 0) {
                --n;
                int n3 = this.curveTypes[this.numCurves];
                n2 -= n3 - 2;
                switch (n3) {
                    case 8: {
                        string = string + "cubic: ";
                        break;
                    }
                    case 6: {
                        string = string + "quad: ";
                        break;
                    }
                    case 4: {
                        string = string + "line: ";
                    }
                }
                string = string + Arrays.toString(Arrays.copyOfRange(this.curves, n2, n2 + n3 - 2)) + "\n";
            }
            return string;
        }
    }
}

