/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.Ref;

public abstract class WCFont
extends Ref {
    public abstract Object getPlatformFont();

    public abstract WCFont deriveFont(float var1);

    public abstract int getOffsetForPosition(String var1, float var2);

    public abstract int[] getGlyphCodes(char[] var1);

    public abstract float getXHeight();

    public abstract double getGlyphWidth(int var1);

    public abstract double[] getStringBounds(String var1, int var2, int var3, boolean var4);

    public abstract double getStringWidth(String var1);

    public int hashCode() {
        Object object = this.getPlatformFont();
        return object != null ? object.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (object instanceof WCFont) {
            Object object2 = this.getPlatformFont();
            Object object3 = ((WCFont)object).getPlatformFont();
            return object2 == null ? object3 == null : object2.equals(object3);
        }
        return false;
    }

    public abstract float getAscent();

    public abstract float getDescent();

    public abstract float getLineSpacing();

    public abstract float getLineGap();

    public abstract boolean hasUniformLineMetrics();
}

