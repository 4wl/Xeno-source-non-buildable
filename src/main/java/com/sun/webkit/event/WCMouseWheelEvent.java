/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.event;

public final class WCMouseWheelEvent {
    private final long when;
    private final int x;
    private final int y;
    private final int screenX;
    private final int screenY;
    private final float deltaX;
    private final float deltaY;
    private final boolean shift;
    private final boolean control;
    private final boolean alt;
    private final boolean meta;

    public WCMouseWheelEvent(int n, int n2, int n3, int n4, long l, boolean bl, boolean bl2, boolean bl3, boolean bl4, float f, float f2) {
        this.x = n;
        this.y = n2;
        this.screenX = n3;
        this.screenY = n4;
        this.when = l;
        this.shift = bl;
        this.control = bl2;
        this.alt = bl3;
        this.meta = bl4;
        this.deltaX = f;
        this.deltaY = f2;
    }

    public long getWhen() {
        return this.when;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getScreenX() {
        return this.screenX;
    }

    public int getScreenY() {
        return this.screenY;
    }

    public boolean isShiftDown() {
        return this.shift;
    }

    public boolean isControlDown() {
        return this.control;
    }

    public boolean isAltDown() {
        return this.alt;
    }

    public boolean isMetaDown() {
        return this.meta;
    }

    public float getDeltaX() {
        return this.deltaX;
    }

    public float getDeltaY() {
        return this.deltaY;
    }
}

