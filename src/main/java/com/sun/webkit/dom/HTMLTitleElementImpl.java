/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLTitleElement;

public class HTMLTitleElementImpl
extends HTMLElementImpl
implements HTMLTitleElement {
    HTMLTitleElementImpl(long l) {
        super(l);
    }

    static HTMLTitleElement getImpl(long l) {
        return (HTMLTitleElement)HTMLTitleElementImpl.create(l);
    }

    @Override
    public String getText() {
        return HTMLTitleElementImpl.getTextImpl(this.getPeer());
    }

    static native String getTextImpl(long var0);

    @Override
    public void setText(String string) {
        HTMLTitleElementImpl.setTextImpl(this.getPeer(), string);
    }

    static native void setTextImpl(long var0, String var2);
}

