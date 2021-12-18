/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.StyleSheetImpl;
import org.w3c.dom.html.HTMLStyleElement;
import org.w3c.dom.stylesheets.StyleSheet;

public class HTMLStyleElementImpl
extends HTMLElementImpl
implements HTMLStyleElement {
    HTMLStyleElementImpl(long l) {
        super(l);
    }

    static HTMLStyleElement getImpl(long l) {
        return (HTMLStyleElement)HTMLStyleElementImpl.create(l);
    }

    @Override
    public boolean getDisabled() {
        return HTMLStyleElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLStyleElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public String getMedia() {
        return HTMLStyleElementImpl.getMediaImpl(this.getPeer());
    }

    static native String getMediaImpl(long var0);

    @Override
    public void setMedia(String string) {
        HTMLStyleElementImpl.setMediaImpl(this.getPeer(), string);
    }

    static native void setMediaImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLStyleElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLStyleElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    public StyleSheet getSheet() {
        return StyleSheetImpl.getImpl(HTMLStyleElementImpl.getSheetImpl(this.getPeer()));
    }

    static native long getSheetImpl(long var0);
}

