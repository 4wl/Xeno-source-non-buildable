/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public final class WCRectangle {
    float x;
    float y;
    float w;
    float h;

    public WCRectangle(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.w = f3;
        this.h = f4;
    }

    public WCRectangle(WCRectangle wCRectangle) {
        this.x = wCRectangle.x;
        this.y = wCRectangle.y;
        this.w = wCRectangle.w;
        this.h = wCRectangle.h;
    }

    public WCRectangle() {
    }

    public float getX() {
        return this.x;
    }

    public int getIntX() {
        return (int)this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getIntY() {
        return (int)this.y;
    }

    public float getWidth() {
        return this.w;
    }

    public int getIntWidth() {
        return (int)this.w;
    }

    public float getHeight() {
        return this.h;
    }

    public int getIntHeight() {
        return (int)this.h;
    }

    public boolean contains(WCRectangle wCRectangle) {
        return this.x <= wCRectangle.x && this.x + this.w >= wCRectangle.x + wCRectangle.w && this.y <= wCRectangle.y && this.y + this.h >= wCRectangle.y + wCRectangle.h;
    }

    public WCRectangle intersection(WCRectangle wCRectangle) {
        float f = this.x;
        float f2 = this.y;
        float f3 = wCRectangle.x;
        float f4 = wCRectangle.y;
        float f5 = f;
        f5 += this.w;
        float f6 = f2;
        f6 += this.h;
        float f7 = f3;
        f7 += wCRectangle.w;
        float f8 = f4;
        f8 += wCRectangle.h;
        if (f < f3) {
            f = f3;
        }
        if (f2 < f4) {
            f2 = f4;
        }
        if (f5 > f7) {
            f5 = f7;
        }
        if (f6 > f8) {
            f6 = f8;
        }
        f5 -= f;
        f6 -= f2;
        if (f5 < Float.MIN_VALUE) {
            f5 = Float.MIN_VALUE;
        }
        if (f6 < Float.MIN_VALUE) {
            f6 = Float.MIN_VALUE;
        }
        return new WCRectangle(f, f2, f5, f6);
    }

    public void translate(float f, float f2) {
        float f3 = this.x;
        float f4 = f3 + f;
        if (f < 0.0f) {
            if (f4 > f3) {
                if (this.w >= 0.0f) {
                    this.w += f4 - Float.MIN_VALUE;
                }
                f4 = Float.MIN_VALUE;
            }
        } else if (f4 < f3) {
            if (this.w >= 0.0f) {
                this.w += f4 - Float.MAX_VALUE;
                if (this.w < 0.0f) {
                    this.w = Float.MAX_VALUE;
                }
            }
            f4 = Float.MAX_VALUE;
        }
        this.x = f4;
        f3 = this.y;
        f4 = f3 + f2;
        if (f2 < 0.0f) {
            if (f4 > f3) {
                if (this.h >= 0.0f) {
                    this.h += f4 - Float.MIN_VALUE;
                }
                f4 = Float.MIN_VALUE;
            }
        } else if (f4 < f3) {
            if (this.h >= 0.0f) {
                this.h += f4 - Float.MAX_VALUE;
                if (this.h < 0.0f) {
                    this.h = Float.MAX_VALUE;
                }
            }
            f4 = Float.MAX_VALUE;
        }
        this.y = f4;
    }

    public WCRectangle createUnion(WCRectangle wCRectangle) {
        WCRectangle wCRectangle2 = new WCRectangle();
        WCRectangle.union(this, wCRectangle, wCRectangle2);
        return wCRectangle2;
    }

    public static void union(WCRectangle wCRectangle, WCRectangle wCRectangle2, WCRectangle wCRectangle3) {
        float f = Math.min(wCRectangle.getMinX(), wCRectangle2.getMinX());
        float f2 = Math.min(wCRectangle.getMinY(), wCRectangle2.getMinY());
        float f3 = Math.max(wCRectangle.getMaxX(), wCRectangle2.getMaxX());
        float f4 = Math.max(wCRectangle.getMaxY(), wCRectangle2.getMaxY());
        wCRectangle3.setFrameFromDiagonal(f, f2, f3, f4);
    }

    public void setFrameFromDiagonal(float f, float f2, float f3, float f4) {
        float f5;
        if (f3 < f) {
            f5 = f;
            f = f3;
            f3 = f5;
        }
        if (f4 < f2) {
            f5 = f2;
            f2 = f4;
            f4 = f5;
        }
        this.setFrame(f, f2, f3 - f, f4 - f2);
    }

    public void setFrame(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.w = f3;
        this.h = f4;
    }

    public float getMinX() {
        return this.getX();
    }

    public float getMaxX() {
        return this.getX() + this.getWidth();
    }

    public float getMinY() {
        return this.getY();
    }

    public float getMaxY() {
        return this.getY() + this.getHeight();
    }

    public boolean isEmpty() {
        return this.w <= 0.0f || this.h <= 0.0f;
    }

    public boolean equals(Object object) {
        if (object instanceof WCRectangle) {
            WCRectangle wCRectangle = (WCRectangle)object;
            return this.x == wCRectangle.x && this.y == wCRectangle.y && this.w == wCRectangle.w && this.h == wCRectangle.h;
        }
        return super.equals(object);
    }

    public String toString() {
        return "WCRectangle{x:" + this.x + " y:" + this.y + " w:" + this.w + " h:" + this.h + "}";
    }
}

