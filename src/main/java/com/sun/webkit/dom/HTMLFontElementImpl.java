/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLFontElement;

public class HTMLFontElementImpl
extends HTMLElementImpl
implements HTMLFontElement {
    HTMLFontElementImpl(long l) {
        super(l);
    }

    static HTMLFontElement getImpl(long l) {
        return (HTMLFontElement)HTMLFontElementImpl.create(l);
    }

    @Override
    public String getColor() {
        return HTMLFontElementImpl.getColorImpl(this.getPeer());
    }

    static native String getColorImpl(long var0);

    @Override
    public void setColor(String string) {
        HTMLFontElementImpl.setColorImpl(this.getPeer(), string);
    }

    static native void setColorImpl(long var0, String var2);

    @Override
    public String getFace() {
        return HTMLFontElementImpl.getFaceImpl(this.getPeer());
    }

    static native String getFaceImpl(long var0);

    @Override
    public void setFace(String string) {
        HTMLFontElementImpl.setFaceImpl(this.getPeer(), string);
    }

    static native void setFaceImpl(long var0, String var2);

    @Override
    public String getSize() {
        return HTMLFontElementImpl.getSizeImpl(this.getPeer());
    }

    static native String getSizeImpl(long var0);

    @Override
    public void setSize(String string) {
        HTMLFontElementImpl.setSizeImpl(this.getPeer(), string);
    }

    static native void setSizeImpl(long var0, String var2);
}

