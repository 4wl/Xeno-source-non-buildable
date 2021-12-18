/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLScriptElement;

public class HTMLScriptElementImpl
extends HTMLElementImpl
implements HTMLScriptElement {
    HTMLScriptElementImpl(long l) {
        super(l);
    }

    static HTMLScriptElement getImpl(long l) {
        return (HTMLScriptElement)HTMLScriptElementImpl.create(l);
    }

    @Override
    public String getText() {
        return HTMLScriptElementImpl.getTextImpl(this.getPeer());
    }

    static native String getTextImpl(long var0);

    @Override
    public void setText(String string) {
        HTMLScriptElementImpl.setTextImpl(this.getPeer(), string);
    }

    static native void setTextImpl(long var0, String var2);

    @Override
    public String getHtmlFor() {
        return HTMLScriptElementImpl.getHtmlForImpl(this.getPeer());
    }

    static native String getHtmlForImpl(long var0);

    @Override
    public void setHtmlFor(String string) {
        HTMLScriptElementImpl.setHtmlForImpl(this.getPeer(), string);
    }

    static native void setHtmlForImpl(long var0, String var2);

    @Override
    public String getEvent() {
        return HTMLScriptElementImpl.getEventImpl(this.getPeer());
    }

    static native String getEventImpl(long var0);

    @Override
    public void setEvent(String string) {
        HTMLScriptElementImpl.setEventImpl(this.getPeer(), string);
    }

    static native void setEventImpl(long var0, String var2);

    @Override
    public String getCharset() {
        return HTMLScriptElementImpl.getCharsetImpl(this.getPeer());
    }

    static native String getCharsetImpl(long var0);

    @Override
    public void setCharset(String string) {
        HTMLScriptElementImpl.setCharsetImpl(this.getPeer(), string);
    }

    static native void setCharsetImpl(long var0, String var2);

    public boolean getAsync() {
        return HTMLScriptElementImpl.getAsyncImpl(this.getPeer());
    }

    static native boolean getAsyncImpl(long var0);

    public void setAsync(boolean bl) {
        HTMLScriptElementImpl.setAsyncImpl(this.getPeer(), bl);
    }

    static native void setAsyncImpl(long var0, boolean var2);

    @Override
    public boolean getDefer() {
        return HTMLScriptElementImpl.getDeferImpl(this.getPeer());
    }

    static native boolean getDeferImpl(long var0);

    @Override
    public void setDefer(boolean bl) {
        HTMLScriptElementImpl.setDeferImpl(this.getPeer(), bl);
    }

    static native void setDeferImpl(long var0, boolean var2);

    @Override
    public String getSrc() {
        return HTMLScriptElementImpl.getSrcImpl(this.getPeer());
    }

    static native String getSrcImpl(long var0);

    @Override
    public void setSrc(String string) {
        HTMLScriptElementImpl.setSrcImpl(this.getPeer(), string);
    }

    static native void setSrcImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLScriptElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLScriptElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    public String getCrossOrigin() {
        return HTMLScriptElementImpl.getCrossOriginImpl(this.getPeer());
    }

    static native String getCrossOriginImpl(long var0);

    public void setCrossOrigin(String string) {
        HTMLScriptElementImpl.setCrossOriginImpl(this.getPeer(), string);
    }

    static native void setCrossOriginImpl(long var0, String var2);
}

