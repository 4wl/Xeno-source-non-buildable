/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.EventImpl;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;

public class UIEventImpl
extends EventImpl
implements UIEvent {
    UIEventImpl(long l) {
        super(l);
    }

    static UIEvent getImpl(long l) {
        return (UIEvent)UIEventImpl.create(l);
    }

    @Override
    public AbstractView getView() {
        return DOMWindowImpl.getImpl(UIEventImpl.getViewImpl(this.getPeer()));
    }

    static native long getViewImpl(long var0);

    @Override
    public int getDetail() {
        return UIEventImpl.getDetailImpl(this.getPeer());
    }

    static native int getDetailImpl(long var0);

    public int getKeyCode() {
        return UIEventImpl.getKeyCodeImpl(this.getPeer());
    }

    static native int getKeyCodeImpl(long var0);

    public int getCharCode() {
        return UIEventImpl.getCharCodeImpl(this.getPeer());
    }

    static native int getCharCodeImpl(long var0);

    public int getLayerX() {
        return UIEventImpl.getLayerXImpl(this.getPeer());
    }

    static native int getLayerXImpl(long var0);

    public int getLayerY() {
        return UIEventImpl.getLayerYImpl(this.getPeer());
    }

    static native int getLayerYImpl(long var0);

    public int getPageX() {
        return UIEventImpl.getPageXImpl(this.getPeer());
    }

    static native int getPageXImpl(long var0);

    public int getPageY() {
        return UIEventImpl.getPageYImpl(this.getPeer());
    }

    static native int getPageYImpl(long var0);

    public int getWhich() {
        return UIEventImpl.getWhichImpl(this.getPeer());
    }

    static native int getWhichImpl(long var0);

    @Override
    public void initUIEvent(String string, boolean bl, boolean bl2, AbstractView abstractView, int n) {
        UIEventImpl.initUIEventImpl(this.getPeer(), string, bl, bl2, DOMWindowImpl.getPeer(abstractView), n);
    }

    static native void initUIEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, int var7);
}

