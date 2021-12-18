/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLBRElement;

public class HTMLBRElementImpl
extends HTMLElementImpl
implements HTMLBRElement {
    HTMLBRElementImpl(long l) {
        super(l);
    }

    static HTMLBRElement getImpl(long l) {
        return (HTMLBRElement)HTMLBRElementImpl.create(l);
    }

    @Override
    public String getClear() {
        return HTMLBRElementImpl.getClearImpl(this.getPeer());
    }

    static native String getClearImpl(long var0);

    @Override
    public void setClear(String string) {
        HTMLBRElementImpl.setClearImpl(this.getPeer(), string);
    }

    static native void setClearImpl(long var0, String var2);
}

