/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.event;

public final class WCMouseEvent {
    public static final int MOUSE_PRESSED = 0;
    public static final int MOUSE_RELEASED = 1;
    public static final int MOUSE_MOVED = 2;
    public static final int MOUSE_DRAGGED = 3;
    public static final int MOUSE_WHEEL = 4;
    public static final int NOBUTTON = 0;
    public static final int BUTTON1 = 1;
    public static final int BUTTON2 = 2;
    public static final int BUTTON3 = 4;
    private final int id;
    private final long when;
    private final int button;
    private final int clickCount;
    private final int x;
    private final int y;
    private final int screenX;
    private final int screenY;
    private final boolean shift;
    private final boolean control;
    private final boolean alt;
    private final boolean meta;
    private final boolean popupTrigger;

    public WCMouseEvent(int n, int n2, int n3, int n4, int n5, int n6, int n7, long l, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        this.id = n;
        this.button = n2;
        this.clickCount = n3;
        this.x = n4;
        this.y = n5;
        this.screenX = n6;
        this.screenY = n7;
        this.when = l;
        this.shift = bl;
        this.control = bl2;
        this.alt = bl3;
        this.meta = bl4;
        this.popupTrigger = bl5;
    }

    public int getID() {
        return this.id;
    }

    public long getWhen() {
        return this.when;
    }

    public int getButton() {
        return this.button;
    }

    public int getClickCount() {
        return this.clickCount;
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

    public boolean isPopupTrigger() {
        return this.popupTrigger;
    }
}

