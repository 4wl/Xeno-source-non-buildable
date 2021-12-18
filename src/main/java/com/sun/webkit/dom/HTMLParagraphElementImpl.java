/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLParagraphElement;

public class HTMLParagraphElementImpl
extends HTMLElementImpl
implements HTMLParagraphElement {
    HTMLParagraphElementImpl(long l) {
        super(l);
    }

    static HTMLParagraphElement getImpl(long l) {
        return (HTMLParagraphElement)HTMLParagraphElementImpl.create(l);
    }

    @Override
    public String getAlign() {
        return HTMLParagraphElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLParagraphElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);
}

