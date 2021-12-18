/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLUListElement;

public class HTMLUListElementImpl
extends HTMLElementImpl
implements HTMLUListElement {
    HTMLUListElementImpl(long l) {
        super(l);
    }

    static HTMLUListElement getImpl(long l) {
        return (HTMLUListElement)HTMLUListElementImpl.create(l);
    }

    @Override
    public boolean getCompact() {
        return HTMLUListElementImpl.getCompactImpl(this.getPeer());
    }

    static native boolean getCompactImpl(long var0);

    @Override
    public void setCompact(boolean bl) {
        HTMLUListElementImpl.setCompactImpl(this.getPeer(), bl);
    }

    static native void setCompactImpl(long var0, boolean var2);

    @Override
    public String getType() {
        return HTMLUListElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLUListElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);
}

