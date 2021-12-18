/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLMetaElement;

public class HTMLMetaElementImpl
extends HTMLElementImpl
implements HTMLMetaElement {
    HTMLMetaElementImpl(long l) {
        super(l);
    }

    static HTMLMetaElement getImpl(long l) {
        return (HTMLMetaElement)HTMLMetaElementImpl.create(l);
    }

    @Override
    public String getContent() {
        return HTMLMetaElementImpl.getContentImpl(this.getPeer());
    }

    static native String getContentImpl(long var0);

    @Override
    public void setContent(String string) {
        HTMLMetaElementImpl.setContentImpl(this.getPeer(), string);
    }

    static native void setContentImpl(long var0, String var2);

    @Override
    public String getHttpEquiv() {
        return HTMLMetaElementImpl.getHttpEquivImpl(this.getPeer());
    }

    static native String getHttpEquivImpl(long var0);

    @Override
    public void setHttpEquiv(String string) {
        HTMLMetaElementImpl.setHttpEquivImpl(this.getPeer(), string);
    }

    static native void setHttpEquivImpl(long var0, String var2);

    @Override
    public String getName() {
        return HTMLMetaElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLMetaElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    @Override
    public String getScheme() {
        return HTMLMetaElementImpl.getSchemeImpl(this.getPeer());
    }

    static native String getSchemeImpl(long var0);

    @Override
    public void setScheme(String string) {
        HTMLMetaElementImpl.setSchemeImpl(this.getPeer(), string);
    }

    static native void setSchemeImpl(long var0, String var2);
}

