/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import java.util.Arrays;

public abstract class WCStroke<P, S> {
    public static final int NO_STROKE = 0;
    public static final int SOLID_STROKE = 1;
    public static final int DOTTED_STROKE = 2;
    public static final int DASHED_STROKE = 3;
    public static final int BUTT_CAP = 0;
    public static final int ROUND_CAP = 1;
    public static final int SQUARE_CAP = 2;
    public static final int MITER_JOIN = 0;
    public static final int ROUND_JOIN = 1;
    public static final int BEVEL_JOIN = 2;
    private int style = 1;
    private int lineCap = 0;
    private int lineJoin = 0;
    private float miterLimit = 10.0f;
    private float thickness = 1.0f;
    private float offset;
    private float[] sizes;
    private P paint;

    protected abstract void invalidate();

    public abstract S getPlatformStroke();

    public void copyFrom(WCStroke<P, S> wCStroke) {
        this.style = wCStroke.style;
        this.lineCap = wCStroke.lineCap;
        this.lineJoin = wCStroke.lineJoin;
        this.miterLimit = wCStroke.miterLimit;
        this.thickness = wCStroke.thickness;
        this.offset = wCStroke.offset;
        this.sizes = wCStroke.sizes;
        this.paint = wCStroke.paint;
    }

    public void setStyle(int n) {
        if (n != 1 && n != 2 && n != 3) {
            n = 0;
        }
        if (this.style != n) {
            this.style = n;
            this.invalidate();
        }
    }

    public void setLineCap(int n) {
        if (n != 1 && n != 2) {
            n = 0;
        }
        if (this.lineCap != n) {
            this.lineCap = n;
            this.invalidate();
        }
    }

    public void setLineJoin(int n) {
        if (n != 1 && n != 2) {
            n = 0;
        }
        if (this.lineJoin != n) {
            this.lineJoin = n;
            this.invalidate();
        }
    }

    public void setMiterLimit(float f) {
        if (f < 1.0f) {
            f = 1.0f;
        }
        if (this.miterLimit != f) {
            this.miterLimit = f;
            this.invalidate();
        }
    }

    public void setThickness(float f) {
        if (f < 0.0f) {
            f = 1.0f;
        }
        if (this.thickness != f) {
            this.thickness = f;
            this.invalidate();
        }
    }

    public void setDashOffset(float f) {
        if (this.offset != f) {
            this.offset = f;
            this.invalidate();
        }
    }

    public void setDashSizes(float ... arrf) {
        if (arrf == null || arrf.length == 0) {
            if (this.sizes != null) {
                this.sizes = null;
                this.invalidate();
            }
        } else if (!Arrays.equals(this.sizes, arrf)) {
            this.sizes = (float[])arrf.clone();
            this.invalidate();
        }
    }

    public void setPaint(P p) {
        this.paint = p;
    }

    public int getStyle() {
        return this.style;
    }

    public int getLineCap() {
        return this.lineCap;
    }

    public int getLineJoin() {
        return this.lineJoin;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public float getThickness() {
        return this.thickness;
    }

    public float getDashOffset() {
        return this.offset;
    }

    public float[] getDashSizes() {
        return this.sizes != null ? (float[])this.sizes.clone() : null;
    }

    public P getPaint() {
        return this.paint;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.getClass().getSimpleName());
        stringBuilder.append("[style=").append(this.style);
        stringBuilder.append(", lineCap=").append(this.lineCap);
        stringBuilder.append(", lineJoin=").append(this.lineJoin);
        stringBuilder.append(", miterLimit=").append(this.miterLimit);
        stringBuilder.append(", thickness=").append(this.thickness);
        stringBuilder.append(", offset=").append(this.offset);
        stringBuilder.append(", sizes=").append(Arrays.toString(this.sizes));
        stringBuilder.append(", paint=").append(this.paint);
        return stringBuilder.append("]").toString();
    }
}

