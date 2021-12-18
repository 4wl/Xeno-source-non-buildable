/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLDivElement;

public class HTMLDivElementImpl
extends HTMLElementImpl
implements HTMLDivElement {
    HTMLDivElementImpl(long l) {
        super(l);
    }

    static HTMLDivElement getImpl(long l) {
        return (HTMLDivElement)HTMLDivElementImpl.create(l);
    }

    @Override
    public String getAlign() {
        return HTMLDivElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLDivElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);
}

