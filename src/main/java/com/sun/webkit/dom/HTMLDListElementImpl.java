/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLDListElement;

public class HTMLDListElementImpl
extends HTMLElementImpl
implements HTMLDListElement {
    HTMLDListElementImpl(long l) {
        super(l);
    }

    static HTMLDListElement getImpl(long l) {
        return (HTMLDListElement)HTMLDListElementImpl.create(l);
    }

    @Override
    public boolean getCompact() {
        return HTMLDListElementImpl.getCompactImpl(this.getPeer());
    }

    static native boolean getCompactImpl(long var0);

    @Override
    public void setCompact(boolean bl) {
        HTMLDListElementImpl.setCompactImpl(this.getPeer(), bl);
    }

    static native void setCompactImpl(long var0, boolean var2);
}

