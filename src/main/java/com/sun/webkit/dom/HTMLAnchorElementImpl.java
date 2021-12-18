/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLAnchorElement;

public class HTMLAnchorElementImpl
extends HTMLElementImpl
implements HTMLAnchorElement {
    HTMLAnchorElementImpl(long l) {
        super(l);
    }

    static HTMLAnchorElement getImpl(long l) {
        return (HTMLAnchorElement)HTMLAnchorElementImpl.create(l);
    }

    @Override
    public String getCharset() {
        return HTMLAnchorElementImpl.getCharsetImpl(this.getPeer());
    }

    static native String getCharsetImpl(long var0);

    @Override
    public void setCharset(String string) {
        HTMLAnchorElementImpl.setCharsetImpl(this.getPeer(), string);
    }

    static native void setCharsetImpl(long var0, String var2);

    @Override
    public String getCoords() {
        return HTMLAnchorElementImpl.getCoordsImpl(this.getPeer());
    }

    static native String getCoordsImpl(long var0);

    @Override
    public void setCoords(String string) {
        HTMLAnchorElementImpl.setCoordsImpl(this.getPeer(), string);
    }

    static native void setCoordsImpl(long var0, String var2);

    @Override
    public String getHref() {
        return HTMLAnchorElementImpl.getHrefImpl(this.getPeer());
    }

    static native String getHrefImpl(long var0);

    @Override
    public void setHref(String string) {
        HTMLAnchorElementImpl.setHrefImpl(this.getPeer(), string);
    }

    static native void setHrefImpl(long var0, String var2);

    @Override
    public String getHreflang() {
        return HTMLAnchorElementImpl.getHreflangImpl(this.getPeer());
    }

    static native String getHreflangImpl(long var0);

    @Override
    public void setHreflang(String string) {
        HTMLAnchorElementImpl.setHreflangImpl(this.getPeer(), string);
    }

    static native void setHreflangImpl(long var0, String var2);

    @Override
    public String getName() {
        return HTMLAnchorElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLAnchorElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    public String getPing() {
        return HTMLAnchorElementImpl.getPingImpl(this.getPeer());
    }

    static native String getPingImpl(long var0);

    public void setPing(String string) {
        HTMLAnchorElementImpl.setPingImpl(this.getPeer(), string);
    }

    static native void setPingImpl(long var0, String var2);

    @Override
    public String getRel() {
        return HTMLAnchorElementImpl.getRelImpl(this.getPeer());
    }

    static native String getRelImpl(long var0);

    @Override
    public void setRel(String string) {
        HTMLAnchorElementImpl.setRelImpl(this.getPeer(), string);
    }

    static native void setRelImpl(long var0, String var2);

    @Override
    public String getRev() {
        return HTMLAnchorElementImpl.getRevImpl(this.getPeer());
    }

    static native String getRevImpl(long var0);

    @Override
    public void setRev(String string) {
        HTMLAnchorElementImpl.setRevImpl(this.getPeer(), string);
    }

    static native void setRevImpl(long var0, String var2);

    @Override
    public String getShape() {
        return HTMLAnchorElementImpl.getShapeImpl(this.getPeer());
    }

    static native String getShapeImpl(long var0);

    @Override
    public void setShape(String string) {
        HTMLAnchorElementImpl.setShapeImpl(this.getPeer(), string);
    }

    static native void setShapeImpl(long var0, String var2);

    @Override
    public String getTarget() {
        return HTMLAnchorElementImpl.getTargetImpl(this.getPeer());
    }

    static native String getTargetImpl(long var0);

    @Override
    public void setTarget(String string) {
        HTMLAnchorElementImpl.setTargetImpl(this.getPeer(), string);
    }

    static native void setTargetImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLAnchorElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLAnchorElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    public String getHash() {
        return HTMLAnchorElementImpl.getHashImpl(this.getPeer());
    }

    static native String getHashImpl(long var0);

    public void setHash(String string) {
        HTMLAnchorElementImpl.setHashImpl(this.getPeer(), string);
    }

    static native void setHashImpl(long var0, String var2);

    public String getHost() {
        return HTMLAnchorElementImpl.getHostImpl(this.getPeer());
    }

    static native String getHostImpl(long var0);

    public void setHost(String string) {
        HTMLAnchorElementImpl.setHostImpl(this.getPeer(), string);
    }

    static native void setHostImpl(long var0, String var2);

    public String getHostname() {
        return HTMLAnchorElementImpl.getHostnameImpl(this.getPeer());
    }

    static native String getHostnameImpl(long var0);

    public void setHostname(String string) {
        HTMLAnchorElementImpl.setHostnameImpl(this.getPeer(), string);
    }

    static native void setHostnameImpl(long var0, String var2);

    public String getPathname() {
        return HTMLAnchorElementImpl.getPathnameImpl(this.getPeer());
    }

    static native String getPathnameImpl(long var0);

    public void setPathname(String string) {
        HTMLAnchorElementImpl.setPathnameImpl(this.getPeer(), string);
    }

    static native void setPathnameImpl(long var0, String var2);

    public String getPort() {
        return HTMLAnchorElementImpl.getPortImpl(this.getPeer());
    }

    static native String getPortImpl(long var0);

    public void setPort(String string) {
        HTMLAnchorElementImpl.setPortImpl(this.getPeer(), string);
    }

    static native void setPortImpl(long var0, String var2);

    public String getProtocol() {
        return HTMLAnchorElementImpl.getProtocolImpl(this.getPeer());
    }

    static native String getProtocolImpl(long var0);

    public void setProtocol(String string) {
        HTMLAnchorElementImpl.setProtocolImpl(this.getPeer(), string);
    }

    static native void setProtocolImpl(long var0, String var2);

    public String getSearch() {
        return HTMLAnchorElementImpl.getSearchImpl(this.getPeer());
    }

    static native String getSearchImpl(long var0);

    public void setSearch(String string) {
        HTMLAnchorElementImpl.setSearchImpl(this.getPeer(), string);
    }

    static native void setSearchImpl(long var0, String var2);

    public String getOrigin() {
        return HTMLAnchorElementImpl.getOriginImpl(this.getPeer());
    }

    static native String getOriginImpl(long var0);

    public String getText() {
        return HTMLAnchorElementImpl.getTextImpl(this.getPeer());
    }

    static native String getTextImpl(long var0);
}

