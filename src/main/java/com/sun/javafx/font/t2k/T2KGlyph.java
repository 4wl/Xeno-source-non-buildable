/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.t2k.T2KFontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

class T2KGlyph
implements Glyph {
    private T2KFontStrike strike;
    private int gc;
    private float userAdvance;
    private float deviceXAdvance;
    private float deviceYAdvance;
    byte[] pixelData;
    private int width;
    private int height;
    private int originX;
    private int originY;
    private boolean isLCDGlyph;
    private RectBounds b2d;

    public T2KGlyph(T2KFontStrike t2KFontStrike, int n, float f) {
        this.strike = t2KFontStrike;
        this.gc = n;
        this.userAdvance = f;
    }

    T2KGlyph(T2KFontStrike t2KFontStrike, int n, long l) {
        this.strike = t2KFontStrike;
        this.gc = n;
        int[] arrn = this.getGlyphInfo(l);
        this.width = arrn[0];
        this.height = arrn[1];
        this.originX = arrn[2];
        this.originY = arrn[3];
        int n2 = arrn[4];
        this.isLCDGlyph = false;
        if (n2 > this.width) {
            this.width = n2;
            this.isLCDGlyph = true;
        }
        this.deviceXAdvance = this.getGlyphPixelXAdvance(l);
        this.deviceYAdvance = this.getGlyphPixelYAdvance(l);
        this.userAdvance = t2KFontStrike.getGlyphUserAdvance(this.deviceXAdvance, this.deviceYAdvance);
        this.pixelData = this.getGlyphPixelData(l);
        this.freeGlyph(l);
    }

    @Override
    public int getGlyphCode() {
        return this.gc;
    }

    @Override
    public RectBounds getBBox() {
        if (this.b2d == null) {
            this.b2d = this.strike.getGlyphBounds(this.gc);
        }
        return this.b2d;
    }

    private native int[] getGlyphInfo(long var1);

    private native byte[] getGlyphPixelData(long var1);

    private native float getGlyphPixelXAdvance(long var1);

    private native float getGlyphPixelYAdvance(long var1);

    private native void freeGlyph(long var1);

    void setAdvance(float f, float f2, float f3) {
        this.userAdvance = f;
        this.deviceXAdvance = f2;
        this.deviceYAdvance = f3;
    }

    @Override
    public float getAdvance() {
        return this.userAdvance;
    }

    @Override
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.gc);
    }

    @Override
    public float getPixelXAdvance() {
        return this.deviceXAdvance;
    }

    @Override
    public float getPixelYAdvance() {
        return this.deviceYAdvance;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getOriginX() {
        return this.originX;
    }

    @Override
    public int getOriginY() {
        return this.originY;
    }

    @Override
    public byte[] getPixelData() {
        return this.pixelData;
    }

    @Override
    public byte[] getPixelData(int n) {
        return this.pixelData;
    }

    @Override
    public boolean isLCDGlyph() {
        return this.isLCDGlyph;
    }
}

