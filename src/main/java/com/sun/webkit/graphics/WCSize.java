/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public final class WCSize {
    private final float width;
    private final float height;

    public WCSize(float f, float f2) {
        this.width = f;
        this.height = f2;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public int getIntWidth() {
        return (int)this.width;
    }

    public int getIntHeight() {
        return (int)this.height;
    }
}

