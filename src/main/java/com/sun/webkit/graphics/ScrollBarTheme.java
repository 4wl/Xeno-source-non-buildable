/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;

public abstract class ScrollBarTheme
extends Ref {
    public static final int NO_PART = 0;
    public static final int BACK_BUTTON_START_PART = 1;
    public static final int FORWARD_BUTTON_START_PART = 2;
    public static final int BACK_TRACK_PART = 4;
    public static final int THUMB_PART = 8;
    public static final int FORWARD_TRACK_PART = 16;
    public static final int BACK_BUTTON_END_PART = 32;
    public static final int FORWARD_BUTTON_END_PART = 64;
    public static final int HORIZONTAL_SCROLLBAR = 0;
    public static final int VERTICAL_SCROLLBAR = 1;
    private static int thickness;

    public static int getThickness() {
        return thickness > 0 ? thickness : 12;
    }

    public static void setThickness(int n) {
        thickness = n;
    }

    protected abstract Ref createWidget(long var1, int var3, int var4, int var5, int var6, int var7, int var8);

    public abstract void paint(WCGraphicsContext var1, Ref var2, int var3, int var4, int var5, int var6);

    protected abstract int hitTest(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

    protected abstract int getThumbPosition(int var1, int var2, int var3, int var4, int var5, int var6);

    protected abstract int getThumbLength(int var1, int var2, int var3, int var4, int var5, int var6);

    protected abstract int getTrackPosition(int var1, int var2, int var3);

    protected abstract int getTrackLength(int var1, int var2, int var3);

    public abstract WCSize getWidgetSize(Ref var1);
}

