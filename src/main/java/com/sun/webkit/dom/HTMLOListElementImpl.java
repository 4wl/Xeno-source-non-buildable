/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLOListElement;

public class HTMLOListElementImpl
extends HTMLElementImpl
implements HTMLOListElement {
    HTMLOListElementImpl(long l) {
        super(l);
    }

    static HTMLOListElement getImpl(long l) {
        return (HTMLOListElement)HTMLOListElementImpl.create(l);
    }

    @Override
    public boolean getCompact() {
        return HTMLOListElementImpl.getCompactImpl(this.getPeer());
    }

    static native boolean getCompactImpl(long var0);

    @Override
    public void setCompact(boolean bl) {
        HTMLOListElementImpl.setCompactImpl(this.getPeer(), bl);
    }

    static native void setCompactImpl(long var0, boolean var2);

    @Override
    public int getStart() {
        return HTMLOListElementImpl.getStartImpl(this.getPeer());
    }

    static native int getStartImpl(long var0);

    @Override
    public void setStart(int n) {
        HTMLOListElementImpl.setStartImpl(this.getPeer(), n);
    }

    static native void setStartImpl(long var0, int var2);

    public boolean getReversed() {
        return HTMLOListElementImpl.getReversedImpl(this.getPeer());
    }

    static native boolean getReversedImpl(long var0);

    public void setReversed(boolean bl) {
        HTMLOListElementImpl.setReversedImpl(this.getPeer(), bl);
    }

    static native void setReversedImpl(long var0, boolean var2);

    @Override
    public String getType() {
        return HTMLOListElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLOListElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);
}

