/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLAreaElement;

public class HTMLAreaElementImpl
extends HTMLElementImpl
implements HTMLAreaElement {
    HTMLAreaElementImpl(long l) {
        super(l);
    }

    static HTMLAreaElement getImpl(long l) {
        return (HTMLAreaElement)HTMLAreaElementImpl.create(l);
    }

    @Override
    public String getAlt() {
        return HTMLAreaElementImpl.getAltImpl(this.getPeer());
    }

    static native String getAltImpl(long var0);

    @Override
    public void setAlt(String string) {
        HTMLAreaElementImpl.setAltImpl(this.getPeer(), string);
    }

    static native void setAltImpl(long var0, String var2);

    @Override
    public String getCoords() {
        return HTMLAreaElementImpl.getCoordsImpl(this.getPeer());
    }

    static native String getCoordsImpl(long var0);

    @Override
    public void setCoords(String string) {
        HTMLAreaElementImpl.setCoordsImpl(this.getPeer(), string);
    }

    static native void setCoordsImpl(long var0, String var2);

    @Override
    public String getHref() {
        return HTMLAreaElementImpl.getHrefImpl(this.getPeer());
    }

    static native String getHrefImpl(long var0);

    @Override
    public void setHref(String string) {
        HTMLAreaElementImpl.setHrefImpl(this.getPeer(), string);
    }

    static native void setHrefImpl(long var0, String var2);

    @Override
    public boolean getNoHref() {
        return HTMLAreaElementImpl.getNoHrefImpl(this.getPeer());
    }

    static native boolean getNoHrefImpl(long var0);

    @Override
    public void setNoHref(boolean bl) {
        HTMLAreaElementImpl.setNoHrefImpl(this.getPeer(), bl);
    }

    static native void setNoHrefImpl(long var0, boolean var2);

    public String getPing() {
        return HTMLAreaElementImpl.getPingImpl(this.getPeer());
    }

    static native String getPingImpl(long var0);

    public void setPing(String string) {
        HTMLAreaElementImpl.setPingImpl(this.getPeer(), string);
    }

    static native void setPingImpl(long var0, String var2);

    @Override
    public String getShape() {
        return HTMLAreaElementImpl.getShapeImpl(this.getPeer());
    }

    static native String getShapeImpl(long var0);

    @Override
    public void setShape(String string) {
        HTMLAreaElementImpl.setShapeImpl(this.getPeer(), string);
    }

    static native void setShapeImpl(long var0, String var2);

    @Override
    public String getTarget() {
        return HTMLAreaElementImpl.getTargetImpl(this.getPeer());
    }

    static native String getTargetImpl(long var0);

    @Override
    public void setTarget(String string) {
        HTMLAreaElementImpl.setTargetImpl(this.getPeer(), string);
    }

    static native void setTargetImpl(long var0, String var2);

    public String getHash() {
        return HTMLAreaElementImpl.getHashImpl(this.getPeer());
    }

    static native String getHashImpl(long var0);

    public String getHost() {
        return HTMLAreaElementImpl.getHostImpl(this.getPeer());
    }

    static native String getHostImpl(long var0);

    public String getHostname() {
        return HTMLAreaElementImpl.getHostnameImpl(this.getPeer());
    }

    static native String getHostnameImpl(long var0);

    public String getPathname() {
        return HTMLAreaElementImpl.getPathnameImpl(this.getPeer());
    }

    static native String getPathnameImpl(long var0);

    public String getPort() {
        return HTMLAreaElementImpl.getPortImpl(this.getPeer());
    }

    static native String getPortImpl(long var0);

    public String getProtocol() {
        return HTMLAreaElementImpl.getProtocolImpl(this.getPeer());
    }

    static native String getProtocolImpl(long var0);

    public String getSearch() {
        return HTMLAreaElementImpl.getSearchImpl(this.getPeer());
    }

    static native String getSearchImpl(long var0);
}

