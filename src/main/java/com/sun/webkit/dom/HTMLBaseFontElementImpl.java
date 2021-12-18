/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLBaseFontElement;

public class HTMLBaseFontElementImpl
extends HTMLElementImpl
implements HTMLBaseFontElement {
    HTMLBaseFontElementImpl(long l) {
        super(l);
    }

    static HTMLBaseFontElement getImpl(long l) {
        return (HTMLBaseFontElement)HTMLBaseFontElementImpl.create(l);
    }

    @Override
    public String getColor() {
        return HTMLBaseFontElementImpl.getColorImpl(this.getPeer());
    }

    static native String getColorImpl(long var0);

    @Override
    public void setColor(String string) {
        HTMLBaseFontElementImpl.setColorImpl(this.getPeer(), string);
    }

    static native void setColorImpl(long var0, String var2);

    @Override
    public String getFace() {
        return HTMLBaseFontElementImpl.getFaceImpl(this.getPeer());
    }

    static native String getFaceImpl(long var0);

    @Override
    public void setFace(String string) {
        HTMLBaseFontElementImpl.setFaceImpl(this.getPeer(), string);
    }

    static native void setFaceImpl(long var0, String var2);

    @Override
    public String getSize() {
        return HTMLBaseFontElementImpl.getSizeImpl(this.getPeer()) + "";
    }

    static native int getSizeImpl(long var0);

    @Override
    public void setSize(String string) {
        HTMLBaseFontElementImpl.setSizeImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setSizeImpl(long var0, int var2);
}

