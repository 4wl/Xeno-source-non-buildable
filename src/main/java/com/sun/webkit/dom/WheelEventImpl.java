/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.MouseEventImpl;
import org.w3c.dom.views.AbstractView;

public class WheelEventImpl
extends MouseEventImpl {
    public static final int DOM_DELTA_PIXEL = 0;
    public static final int DOM_DELTA_LINE = 1;
    public static final int DOM_DELTA_PAGE = 2;

    WheelEventImpl(long l) {
        super(l);
    }

    static WheelEventImpl getImpl(long l) {
        return (WheelEventImpl)WheelEventImpl.create(l);
    }

    public double getDeltaX() {
        return WheelEventImpl.getDeltaXImpl(this.getPeer());
    }

    static native double getDeltaXImpl(long var0);

    public double getDeltaY() {
        return WheelEventImpl.getDeltaYImpl(this.getPeer());
    }

    static native double getDeltaYImpl(long var0);

    public double getDeltaZ() {
        return WheelEventImpl.getDeltaZImpl(this.getPeer());
    }

    static native double getDeltaZImpl(long var0);

    public int getDeltaMode() {
        return WheelEventImpl.getDeltaModeImpl(this.getPeer());
    }

    static native int getDeltaModeImpl(long var0);

    public int getWheelDeltaX() {
        return WheelEventImpl.getWheelDeltaXImpl(this.getPeer());
    }

    static native int getWheelDeltaXImpl(long var0);

    public int getWheelDeltaY() {
        return WheelEventImpl.getWheelDeltaYImpl(this.getPeer());
    }

    static native int getWheelDeltaYImpl(long var0);

    public int getWheelDelta() {
        return WheelEventImpl.getWheelDeltaImpl(this.getPeer());
    }

    static native int getWheelDeltaImpl(long var0);

    public boolean getWebkitDirectionInvertedFromDevice() {
        return WheelEventImpl.getWebkitDirectionInvertedFromDeviceImpl(this.getPeer());
    }

    static native boolean getWebkitDirectionInvertedFromDeviceImpl(long var0);

    public void initWheelEvent(int n, int n2, AbstractView abstractView, int n3, int n4, int n5, int n6, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        WheelEventImpl.initWheelEventImpl(this.getPeer(), n, n2, DOMWindowImpl.getPeer(abstractView), n3, n4, n5, n6, bl, bl2, bl3, bl4);
    }

    static native void initWheelEventImpl(long var0, int var2, int var3, long var4, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13);
}

