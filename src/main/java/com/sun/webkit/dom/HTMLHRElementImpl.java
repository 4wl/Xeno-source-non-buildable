/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLHRElement;

public class HTMLHRElementImpl
extends HTMLElementImpl
implements HTMLHRElement {
    HTMLHRElementImpl(long l) {
        super(l);
    }

    static HTMLHRElement getImpl(long l) {
        return (HTMLHRElement)HTMLHRElementImpl.create(l);
    }

    @Override
    public String getAlign() {
        return HTMLHRElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLHRElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public boolean getNoShade() {
        return HTMLHRElementImpl.getNoShadeImpl(this.getPeer());
    }

    static native boolean getNoShadeImpl(long var0);

    @Override
    public void setNoShade(boolean bl) {
        HTMLHRElementImpl.setNoShadeImpl(this.getPeer(), bl);
    }

    static native void setNoShadeImpl(long var0, boolean var2);

    @Override
    public String getSize() {
        return HTMLHRElementImpl.getSizeImpl(this.getPeer());
    }

    static native String getSizeImpl(long var0);

    @Override
    public void setSize(String string) {
        HTMLHRElementImpl.setSizeImpl(this.getPeer(), string);
    }

    static native void setSizeImpl(long var0, String var2);

    @Override
    public String getWidth() {
        return HTMLHRElementImpl.getWidthImpl(this.getPeer());
    }

    static native String getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLHRElementImpl.setWidthImpl(this.getPeer(), string);
    }

    static native void setWidthImpl(long var0, String var2);
}

