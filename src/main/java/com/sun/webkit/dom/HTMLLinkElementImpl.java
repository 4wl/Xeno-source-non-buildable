/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.StyleSheetImpl;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.stylesheets.StyleSheet;

public class HTMLLinkElementImpl
extends HTMLElementImpl
implements HTMLLinkElement {
    HTMLLinkElementImpl(long l) {
        super(l);
    }

    static HTMLLinkElement getImpl(long l) {
        return (HTMLLinkElement)HTMLLinkElementImpl.create(l);
    }

    @Override
    public boolean getDisabled() {
        return HTMLLinkElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLLinkElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public String getCharset() {
        return HTMLLinkElementImpl.getCharsetImpl(this.getPeer());
    }

    static native String getCharsetImpl(long var0);

    @Override
    public void setCharset(String string) {
        HTMLLinkElementImpl.setCharsetImpl(this.getPeer(), string);
    }

    static native void setCharsetImpl(long var0, String var2);

    @Override
    public String getHref() {
        return HTMLLinkElementImpl.getHrefImpl(this.getPeer());
    }

    static native String getHrefImpl(long var0);

    @Override
    public void setHref(String string) {
        HTMLLinkElementImpl.setHrefImpl(this.getPeer(), string);
    }

    static native void setHrefImpl(long var0, String var2);

    @Override
    public String getHreflang() {
        return HTMLLinkElementImpl.getHreflangImpl(this.getPeer());
    }

    static native String getHreflangImpl(long var0);

    @Override
    public void setHreflang(String string) {
        HTMLLinkElementImpl.setHreflangImpl(this.getPeer(), string);
    }

    static native void setHreflangImpl(long var0, String var2);

    @Override
    public String getMedia() {
        return HTMLLinkElementImpl.getMediaImpl(this.getPeer());
    }

    static native String getMediaImpl(long var0);

    @Override
    public void setMedia(String string) {
        HTMLLinkElementImpl.setMediaImpl(this.getPeer(), string);
    }

    static native void setMediaImpl(long var0, String var2);

    @Override
    public String getRel() {
        return HTMLLinkElementImpl.getRelImpl(this.getPeer());
    }

    static native String getRelImpl(long var0);

    @Override
    public void setRel(String string) {
        HTMLLinkElementImpl.setRelImpl(this.getPeer(), string);
    }

    static native void setRelImpl(long var0, String var2);

    @Override
    public String getRev() {
        return HTMLLinkElementImpl.getRevImpl(this.getPeer());
    }

    static native String getRevImpl(long var0);

    @Override
    public void setRev(String string) {
        HTMLLinkElementImpl.setRevImpl(this.getPeer(), string);
    }

    static native void setRevImpl(long var0, String var2);

    @Override
    public String getTarget() {
        return HTMLLinkElementImpl.getTargetImpl(this.getPeer());
    }

    static native String getTargetImpl(long var0);

    @Override
    public void setTarget(String string) {
        HTMLLinkElementImpl.setTargetImpl(this.getPeer(), string);
    }

    static native void setTargetImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLLinkElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLLinkElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    public StyleSheet getSheet() {
        return StyleSheetImpl.getImpl(HTMLLinkElementImpl.getSheetImpl(this.getPeer()));
    }

    static native long getSheetImpl(long var0);
}

