/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLHtmlElement;

public class HTMLHtmlElementImpl
extends HTMLElementImpl
implements HTMLHtmlElement {
    HTMLHtmlElementImpl(long l) {
        super(l);
    }

    static HTMLHtmlElement getImpl(long l) {
        return (HTMLHtmlElement)HTMLHtmlElementImpl.create(l);
    }

    @Override
    public String getVersion() {
        return HTMLHtmlElementImpl.getVersionImpl(this.getPeer());
    }

    static native String getVersionImpl(long var0);

    @Override
    public void setVersion(String string) {
        HTMLHtmlElementImpl.setVersionImpl(this.getPeer(), string);
    }

    static native void setVersionImpl(long var0, String var2);

    public String getManifest() {
        return HTMLHtmlElementImpl.getManifestImpl(this.getPeer());
    }

    static native String getManifestImpl(long var0);

    public void setManifest(String string) {
        HTMLHtmlElementImpl.setManifestImpl(this.getPeer(), string);
    }

    static native void setManifestImpl(long var0, String var2);
}

