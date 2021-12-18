/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.openpisces.Helpers;

public final class Dasher
implements PathConsumer2D {
    private final PathConsumer2D out;
    private float[] dash;
    private float startPhase;
    private boolean startDashOn;
    private int startIdx;
    private boolean starting;
    private boolean needsMoveTo;
    private int idx;
    private boolean dashOn;
    private float phase;
    private float sx;
    private float sy;
    private float x0;
    private float y0;
    private float[] curCurvepts;
    static float MAX_CYCLES = 1.6E7f;
    private float[] firstSegmentsBuffer = new float[7];
    private int firstSegidx = 0;
    private LengthIterator li = null;

    public Dasher(PathConsumer2D pathConsumer2D, float[] arrf, float f) {
        this(pathConsumer2D);
        this.reset(arrf, f);
    }

    public Dasher(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
        this.curCurvepts = new float[16];
    }

    public void reset(float[] arrf, float f) {
        int n;
        int n2 = 0;
        this.dashOn = true;
        float f2 = 0.0f;
        for (float f3 : arrf) {
            f2 += f3;
        }
        float f4 = f / f2;
        if (f < 0.0f) {
            if (-f4 >= MAX_CYCLES) {
                f = 0.0f;
            } else {
                n = (int)Math.floor(-f4);
                if ((n & arrf.length & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                f += (float)n * f2;
                while (f < 0.0f) {
                    if (--n2 < 0) {
                        n2 = arrf.length - 1;
                    }
                    f += arrf[n2];
                    this.dashOn = !this.dashOn;
                }
            }
        } else if (f > 0.0f) {
            if (f4 >= MAX_CYCLES) {
                f = 0.0f;
            } else {
                n = (int)Math.floor(f4);
                if ((n & arrf.length & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                f -= (float)n * f2;
                while (true) {
                    float f5;
                    float f6 = arrf[n2];
                    if (!(f >= f5)) break;
                    f -= f6;
                    n2 = (n2 + 1) % arrf.length;
                    this.dashOn = !this.dashOn;
                }
            }
        }
        this.dash = arrf;
        this.startPhase = this.phase = f;
        this.startDashOn = this.dashOn;
        this.startIdx = n2;
        this.starting = true;
    }

    @Override
    public void moveTo(float f, float f2) {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            this.emitFirstSegments();
        }
        this.needsMoveTo = true;
        this.idx = this.startIdx;
        this.dashOn = this.startDashOn;
        this.phase = this.startPhase;
        this.sx = this.x0 = f;
        this.sy = this.y0 = f2;
        this.starting = true;
    }

    private void emitSeg(float[] arrf, int n, int n2) {
        switch (n2) {
            case 8: {
                this.out.curveTo(arrf[n + 0], arrf[n + 1], arrf[n + 2], arrf[n + 3], arrf[n + 4], arrf[n + 5]);
                break;
            }
            case 6: {
                this.out.quadTo(arrf[n + 0], arrf[n + 1], arrf[n + 2], arrf[n + 3]);
                break;
            }
            case 4: {
                this.out.lineTo(arrf[n], arrf[n + 1]);
            }
        }
    }

    private void emitFirstSegments() {
        for (int i = 0; i < this.firstSegidx; i += (int)this.firstSegmentsBuffer[i] - 1) {
            this.emitSeg(this.firstSegmentsBuffer, i + 1, (int)this.firstSegmentsBuffer[i]);
        }
        this.firstSegidx = 0;
    }

    private void goTo(float[] arrf, int n, int n2) {
        float f = arrf[n + n2 - 4];
        float f2 = arrf[n + n2 - 3];
        if (this.dashOn) {
            if (this.starting) {
                this.firstSegmentsBuffer = Helpers.widenArray(this.firstSegmentsBuffer, this.firstSegidx, n2 - 1);
                this.firstSegmentsBuffer[this.firstSegidx++] = n2;
                System.arraycopy(arrf, n, this.firstSegmentsBuffer, this.firstSegidx, n2 - 2);
                this.firstSegidx += n2 - 2;
            } else {
                if (this.needsMoveTo) {
                    this.out.moveTo(this.x0, this.y0);
                    this.needsMoveTo = false;
                }
                this.emitSeg(arrf, n, n2);
            }
        } else {
            this.starting = false;
            this.needsMoveTo = true;
        }
        this.x0 = f;
        this.y0 = f2;
    }

    @Override
    public void lineTo(float f, float f2) {
        float f3 = f - this.x0;
        float f4 = f2 - this.y0;
        float f5 = (float)Math.sqrt(f3 * f3 + f4 * f4);
        if (f5 == 0.0f) {
            return;
        }
        float f6 = f3 / f5;
        float f7 = f4 / f5;
        while (true) {
            float f8;
            if (f5 <= (f8 = this.dash[this.idx] - this.phase)) {
                this.curCurvepts[0] = f;
                this.curCurvepts[1] = f2;
                this.goTo(this.curCurvepts, 0, 4);
                this.phase += f5;
                if (f5 == f8) {
                    this.phase = 0.0f;
                    this.idx = (this.idx + 1) % this.dash.length;
                    this.dashOn = !this.dashOn;
                }
                return;
            }
            float f9 = this.dash[this.idx] * f6;
            float f10 = this.dash[this.idx] * f7;
            if (this.phase == 0.0f) {
                this.curCurvepts[0] = this.x0 + f9;
                this.curCurvepts[1] = this.y0 + f10;
            } else {
                float f11 = f8 / this.dash[this.idx];
                this.curCurvepts[0] = this.x0 + f11 * f9;
                this.curCurvepts[1] = this.y0 + f11 * f10;
            }
            this.goTo(this.curCurvepts, 0, 4);
            f5 -= f8;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
        }
    }

    private void somethingTo(int n) {
        if (Dasher.pointCurve(this.curCurvepts, n)) {
            return;
        }
        if (this.li == null) {
            this.li = new LengthIterator(4, 0.01f);
        }
        this.li.initializeIterationOnCurve(this.curCurvepts, n);
        int n2 = 0;
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = this.dash[this.idx] - this.phase;
        while (true) {
            float f4;
            f2 = this.li.next(f3);
            if (!(f4 < 1.0f)) break;
            if (f2 != 0.0f) {
                Helpers.subdivideAt((f2 - f) / (1.0f - f), this.curCurvepts, n2, this.curCurvepts, 0, this.curCurvepts, n, n);
                f = f2;
                this.goTo(this.curCurvepts, 2, n);
                n2 = n;
            }
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
            f3 = this.dash[this.idx];
        }
        this.goTo(this.curCurvepts, n2 + 2, n);
        this.phase += this.li.lastSegLen();
        if (this.phase >= this.dash[this.idx]) {
            this.phase = 0.0f;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
        }
    }

    private static boolean pointCurve(float[] arrf, int n) {
        for (int i = 2; i < n; ++i) {
            if (arrf[i] == arrf[i - 2]) continue;
            return false;
        }
        return true;
    }

    @Override
    public void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = f;
        this.curCurvepts[3] = f2;
        this.curCurvepts[4] = f3;
        this.curCurvepts[5] = f4;
        this.curCurvepts[6] = f5;
        this.curCurvepts[7] = f6;
        this.somethingTo(8);
    }

    @Override
    public void quadTo(float f, float f2, float f3, float f4) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = f;
        this.curCurvepts[3] = f2;
        this.curCurvepts[4] = f3;
        this.curCurvepts[5] = f4;
        this.somethingTo(6);
    }

    @Override
    public void closePath() {
        this.lineTo(this.sx, this.sy);
        if (this.firstSegidx > 0) {
            if (!this.dashOn || this.needsMoveTo) {
                this.out.moveTo(this.sx, this.sy);
            }
            this.emitFirstSegments();
        }
        this.moveTo(this.sx, this.sy);
    }

    @Override
    public void pathDone() {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            this.emitFirstSegments();
        }
        this.out.pathDone();
    }

    private static class LengthIterator {
        private float[][] recCurveStack;
        private Side[] sides;
        private int curveType;
        private final int limit;
        private final float ERR;
        private final float minTincrement;
        private float nextT;
        private float lenAtNextT;
        private float lastT;
        private float lenAtLastT;
        private float lenAtLastSplit;
        private float lastSegLen;
        private int recLevel;
        private boolean done;
        private float[] curLeafCtrlPolyLengths = new float[3];
        private int cachedHaveLowAcceleration = -1;
        private float[] nextRoots = new float[4];
        private float[] flatLeafCoefCache = new float[]{0.0f, 0.0f, -1.0f, 0.0f};

        public LengthIterator(int n, float f) {
            this.limit = n;
            this.minTincrement = 1.0f / (float)(1 << this.limit);
            this.ERR = f;
            this.recCurveStack = new float[n + 1][8];
            this.sides = new Side[n];
            this.nextT = Float.MAX_VALUE;
            this.lenAtNextT = Float.MAX_VALUE;
            this.lenAtLastSplit = Float.MIN_VALUE;
            this.recLevel = Integer.MIN_VALUE;
            this.lastSegLen = Float.MAX_VALUE;
            this.done = true;
        }

        public void initializeIterationOnCurve(float[] arrf, int n) {
            System.arraycopy(arrf, 0, this.recCurveStack[0], 0, n);
            this.curveType = n;
            this.recLevel = 0;
            this.lastT = 0.0f;
            this.lenAtLastT = 0.0f;
            this.nextT = 0.0f;
            this.lenAtNextT = 0.0f;
            this.goLeft();
            this.lenAtLastSplit = 0.0f;
            if (this.recLevel > 0) {
                this.sides[0] = Side.LEFT;
                this.done = false;
            } else {
                this.sides[0] = Side.RIGHT;
                this.done = true;
            }
            this.lastSegLen = 0.0f;
        }

        private boolean haveLowAcceleration(float f) {
            if (this.cachedHaveLowAcceleration == -1) {
                float f2;
                float f3 = this.curLeafCtrlPolyLengths[0];
                float f4 = this.curLeafCtrlPolyLengths[1];
                if (!Helpers.within(f3, f4, f * f4)) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
                if (!(this.curveType != 8 || Helpers.within(f4, f2 = this.curLeafCtrlPolyLengths[2], f * f2) && Helpers.within(f3, f2, f * f2))) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
                this.cachedHaveLowAcceleration = 1;
                return true;
            }
            return this.cachedHaveLowAcceleration == 1;
        }

        public float next(float f) {
            float f2 = this.lenAtLastSplit + f;
            while (this.lenAtNextT < f2) {
                if (this.done) {
                    this.lastSegLen = this.lenAtNextT - this.lenAtLastSplit;
                    return 1.0f;
                }
                this.goToNextLeaf();
            }
            this.lenAtLastSplit = f2;
            float f3 = this.lenAtNextT - this.lenAtLastT;
            float f4 = (f2 - this.lenAtLastT) / f3;
            if (!this.haveLowAcceleration(0.05f)) {
                float f5;
                int n;
                float f6;
                float f7;
                float f8;
                if (this.flatLeafCoefCache[2] < 0.0f) {
                    f8 = 0.0f + this.curLeafCtrlPolyLengths[0];
                    f7 = f8 + this.curLeafCtrlPolyLengths[1];
                    if (this.curveType == 8) {
                        f6 = f7 + this.curLeafCtrlPolyLengths[2];
                        this.flatLeafCoefCache[0] = 3.0f * (f8 - f7) + f6;
                        this.flatLeafCoefCache[1] = 3.0f * (f7 - 2.0f * f8);
                        this.flatLeafCoefCache[2] = 3.0f * f8;
                        this.flatLeafCoefCache[3] = -f6;
                    } else if (this.curveType == 6) {
                        this.flatLeafCoefCache[0] = 0.0f;
                        this.flatLeafCoefCache[1] = f7 - 2.0f * f8;
                        this.flatLeafCoefCache[2] = 2.0f * f8;
                        this.flatLeafCoefCache[3] = -f7;
                    }
                }
                if ((n = Helpers.cubicRootsInAB(f8 = this.flatLeafCoefCache[0], f7 = this.flatLeafCoefCache[1], f6 = this.flatLeafCoefCache[2], f5 = f4 * this.flatLeafCoefCache[3], this.nextRoots, 0, 0.0f, 1.0f)) == 1 && !Float.isNaN(this.nextRoots[0])) {
                    f4 = this.nextRoots[0];
                }
            }
            if ((f4 = f4 * (this.nextT - this.lastT) + this.lastT) >= 1.0f) {
                f4 = 1.0f;
                this.done = true;
            }
            this.lastSegLen = f;
            return f4;
        }

        public float lastSegLen() {
            return this.lastSegLen;
        }

        private void goToNextLeaf() {
            --this.recLevel;
            while (this.sides[this.recLevel] == Side.RIGHT) {
                if (this.recLevel == 0) {
                    this.done = true;
                    return;
                }
                --this.recLevel;
            }
            this.sides[this.recLevel] = Side.RIGHT;
            System.arraycopy(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.curveType);
            ++this.recLevel;
            this.goLeft();
        }

        private void goLeft() {
            float f = this.onLeaf();
            if (f >= 0.0f) {
                this.lastT = this.nextT;
                this.lenAtLastT = this.lenAtNextT;
                this.nextT += (float)(1 << this.limit - this.recLevel) * this.minTincrement;
                this.lenAtNextT += f;
                this.flatLeafCoefCache[2] = -1.0f;
                this.cachedHaveLowAcceleration = -1;
            } else {
                Helpers.subdivide(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.recCurveStack[this.recLevel], 0, this.curveType);
                this.sides[this.recLevel] = Side.LEFT;
                ++this.recLevel;
                this.goLeft();
            }
        }

        private float onLeaf() {
            float[] arrf = this.recCurveStack[this.recLevel];
            float f = 0.0f;
            float f2 = arrf[0];
            float f3 = arrf[1];
            for (int i = 2; i < this.curveType; i += 2) {
                float f4 = arrf[i];
                float f5 = arrf[i + 1];
                float f6 = Helpers.linelen(f2, f3, f4, f5);
                f += f6;
                this.curLeafCtrlPolyLengths[i / 2 - 1] = f6;
                f2 = f4;
                f3 = f5;
            }
            float f7 = Helpers.linelen(arrf[0], arrf[1], arrf[this.curveType - 2], arrf[this.curveType - 1]);
            if (f - f7 < this.ERR || this.recLevel == this.limit) {
                return (f + f7) / 2.0f;
            }
            return -1.0f;
        }

        private static enum Side {
            LEFT,
            RIGHT;

        }
    }
}

