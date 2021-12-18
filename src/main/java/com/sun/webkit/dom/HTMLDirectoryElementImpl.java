/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLDirectoryElement;

public class HTMLDirectoryElementImpl
extends HTMLElementImpl
implements HTMLDirectoryElement {
    HTMLDirectoryElementImpl(long l) {
        super(l);
    }

    static HTMLDirectoryElement getImpl(long l) {
        return (HTMLDirectoryElement)HTMLDirectoryElementImpl.create(l);
    }

    @Override
    public boolean getCompact() {
        return HTMLDirectoryElementImpl.getCompactImpl(this.getPeer());
    }

    static native boolean getCompactImpl(long var0);

    @Override
    public void setCompact(boolean bl) {
        HTMLDirectoryElementImpl.setCompactImpl(this.getPeer(), bl);
    }

    static native void setCompactImpl(long var0, boolean var2);
}

