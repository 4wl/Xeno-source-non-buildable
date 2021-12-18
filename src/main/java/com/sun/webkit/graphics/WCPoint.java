/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public final class WCPoint {
    final float x;
    final float y;

    public WCPoint(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getIntX() {
        return (int)this.x;
    }

    public int getIntY() {
        return (int)this.y;
    }
}

