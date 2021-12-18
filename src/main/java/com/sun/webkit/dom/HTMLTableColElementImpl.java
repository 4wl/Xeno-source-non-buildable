/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLTableColElement;

public class HTMLTableColElementImpl
extends HTMLElementImpl
implements HTMLTableColElement {
    HTMLTableColElementImpl(long l) {
        super(l);
    }

    static HTMLTableColElement getImpl(long l) {
        return (HTMLTableColElement)HTMLTableColElementImpl.create(l);
    }

    @Override
    public String getAlign() {
        return HTMLTableColElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLTableColElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getCh() {
        return HTMLTableColElementImpl.getChImpl(this.getPeer());
    }

    static native String getChImpl(long var0);

    @Override
    public void setCh(String string) {
        HTMLTableColElementImpl.setChImpl(this.getPeer(), string);
    }

    static native void setChImpl(long var0, String var2);

    @Override
    public String getChOff() {
        return HTMLTableColElementImpl.getChOffImpl(this.getPeer());
    }

    static native String getChOffImpl(long var0);

    @Override
    public void setChOff(String string) {
        HTMLTableColElementImpl.setChOffImpl(this.getPeer(), string);
    }

    static native void setChOffImpl(long var0, String var2);

    @Override
    public int getSpan() {
        return HTMLTableColElementImpl.getSpanImpl(this.getPeer());
    }

    static native int getSpanImpl(long var0);

    @Override
    public void setSpan(int n) {
        HTMLTableColElementImpl.setSpanImpl(this.getPeer(), n);
    }

    static native void setSpanImpl(long var0, int var2);

    @Override
    public String getVAlign() {
        return HTMLTableColElementImpl.getVAlignImpl(this.getPeer());
    }

    static native String getVAlignImpl(long var0);

    @Override
    public void setVAlign(String string) {
        HTMLTableColElementImpl.setVAlignImpl(this.getPeer(), string);
    }

    static native void setVAlignImpl(long var0, String var2);

    @Override
    public String getWidth() {
        return HTMLTableColElementImpl.getWidthImpl(this.getPeer());
    }

    static native String getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLTableColElementImpl.setWidthImpl(this.getPeer(), string);
    }

    static native void setWidthImpl(long var0, String var2);
}

