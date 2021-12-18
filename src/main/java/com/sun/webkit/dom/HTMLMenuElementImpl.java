/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLMenuElement;

public class HTMLMenuElementImpl
extends HTMLElementImpl
implements HTMLMenuElement {
    HTMLMenuElementImpl(long l) {
        super(l);
    }

    static HTMLMenuElement getImpl(long l) {
        return (HTMLMenuElement)HTMLMenuElementImpl.create(l);
    }

    @Override
    public boolean getCompact() {
        return HTMLMenuElementImpl.getCompactImpl(this.getPeer());
    }

    static native boolean getCompactImpl(long var0);

    @Override
    public void setCompact(boolean bl) {
        HTMLMenuElementImpl.setCompactImpl(this.getPeer(), bl);
    }

    static native void setCompactImpl(long var0, boolean var2);
}

