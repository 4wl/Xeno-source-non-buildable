/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLQuoteElement;

public class HTMLQuoteElementImpl
extends HTMLElementImpl
implements HTMLQuoteElement {
    HTMLQuoteElementImpl(long l) {
        super(l);
    }

    static HTMLQuoteElement getImpl(long l) {
        return (HTMLQuoteElement)HTMLQuoteElementImpl.create(l);
    }

    @Override
    public String getCite() {
        return HTMLQuoteElementImpl.getCiteImpl(this.getPeer());
    }

    static native String getCiteImpl(long var0);

    @Override
    public void setCite(String string) {
        HTMLQuoteElementImpl.setCiteImpl(this.getPeer(), string);
    }

    static native void setCiteImpl(long var0, String var2);
}

