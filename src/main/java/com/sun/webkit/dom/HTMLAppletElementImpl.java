/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLAppletElement;

public class HTMLAppletElementImpl
extends HTMLElementImpl
implements HTMLAppletElement {
    HTMLAppletElementImpl(long l) {
        super(l);
    }

    static HTMLAppletElement getImpl(long l) {
        return (HTMLAppletElement)HTMLAppletElementImpl.create(l);
    }

    @Override
    public String getAlign() {
        return HTMLAppletElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLAppletElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getAlt() {
        return HTMLAppletElementImpl.getAltImpl(this.getPeer());
    }

    static native String getAltImpl(long var0);

    @Override
    public void setAlt(String string) {
        HTMLAppletElementImpl.setAltImpl(this.getPeer(), string);
    }

    static native void setAltImpl(long var0, String var2);

    @Override
    public String getArchive() {
        return HTMLAppletElementImpl.getArchiveImpl(this.getPeer());
    }

    static native String getArchiveImpl(long var0);

    @Override
    public void setArchive(String string) {
        HTMLAppletElementImpl.setArchiveImpl(this.getPeer(), string);
    }

    static native void setArchiveImpl(long var0, String var2);

    @Override
    public String getCode() {
        return HTMLAppletElementImpl.getCodeImpl(this.getPeer());
    }

    static native String getCodeImpl(long var0);

    @Override
    public void setCode(String string) {
        HTMLAppletElementImpl.setCodeImpl(this.getPeer(), string);
    }

    static native void setCodeImpl(long var0, String var2);

    @Override
    public String getCodeBase() {
        return HTMLAppletElementImpl.getCodeBaseImpl(this.getPeer());
    }

    static native String getCodeBaseImpl(long var0);

    @Override
    public void setCodeBase(String string) {
        HTMLAppletElementImpl.setCodeBaseImpl(this.getPeer(), string);
    }

    static native void setCodeBaseImpl(long var0, String var2);

    @Override
    public String getHeight() {
        return HTMLAppletElementImpl.getHeightImpl(this.getPeer());
    }

    static native String getHeightImpl(long var0);

    @Override
    public void setHeight(String string) {
        HTMLAppletElementImpl.setHeightImpl(this.getPeer(), string);
    }

    static native void setHeightImpl(long var0, String var2);

    @Override
    public String getHspace() {
        return HTMLAppletElementImpl.getHspaceImpl(this.getPeer()) + "";
    }

    static native int getHspaceImpl(long var0);

    @Override
    public void setHspace(String string) {
        HTMLAppletElementImpl.setHspaceImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setHspaceImpl(long var0, int var2);

    @Override
    public String getName() {
        return HTMLAppletElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLAppletElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    @Override
    public String getObject() {
        return HTMLAppletElementImpl.getObjectImpl(this.getPeer());
    }

    static native String getObjectImpl(long var0);

    @Override
    public void setObject(String string) {
        HTMLAppletElementImpl.setObjectImpl(this.getPeer(), string);
    }

    static native void setObjectImpl(long var0, String var2);

    @Override
    public String getVspace() {
        return HTMLAppletElementImpl.getVspaceImpl(this.getPeer()) + "";
    }

    static native int getVspaceImpl(long var0);

    @Override
    public void setVspace(String string) {
        HTMLAppletElementImpl.setVspaceImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setVspaceImpl(long var0, int var2);

    @Override
    public String getWidth() {
        return HTMLAppletElementImpl.getWidthImpl(this.getPeer());
    }

    static native String getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLAppletElementImpl.setWidthImpl(this.getPeer(), string);
    }

    static native void setWidthImpl(long var0, String var2);
}

