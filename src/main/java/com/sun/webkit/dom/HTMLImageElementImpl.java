/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLImageElement;

public class HTMLImageElementImpl
extends HTMLElementImpl
implements HTMLImageElement {
    HTMLImageElementImpl(long l) {
        super(l);
    }

    static HTMLImageElement getImpl(long l) {
        return (HTMLImageElement)HTMLImageElementImpl.create(l);
    }

    @Override
    public String getName() {
        return HTMLImageElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLImageElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    @Override
    public String getAlign() {
        return HTMLImageElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLImageElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getAlt() {
        return HTMLImageElementImpl.getAltImpl(this.getPeer());
    }

    static native String getAltImpl(long var0);

    @Override
    public void setAlt(String string) {
        HTMLImageElementImpl.setAltImpl(this.getPeer(), string);
    }

    static native void setAltImpl(long var0, String var2);

    @Override
    public String getBorder() {
        return HTMLImageElementImpl.getBorderImpl(this.getPeer());
    }

    static native String getBorderImpl(long var0);

    @Override
    public void setBorder(String string) {
        HTMLImageElementImpl.setBorderImpl(this.getPeer(), string);
    }

    static native void setBorderImpl(long var0, String var2);

    public String getCrossOrigin() {
        return HTMLImageElementImpl.getCrossOriginImpl(this.getPeer());
    }

    static native String getCrossOriginImpl(long var0);

    public void setCrossOrigin(String string) {
        HTMLImageElementImpl.setCrossOriginImpl(this.getPeer(), string);
    }

    static native void setCrossOriginImpl(long var0, String var2);

    @Override
    public String getHeight() {
        return HTMLImageElementImpl.getHeightImpl(this.getPeer()) + "";
    }

    static native int getHeightImpl(long var0);

    @Override
    public void setHeight(String string) {
        HTMLImageElementImpl.setHeightImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setHeightImpl(long var0, int var2);

    @Override
    public String getHspace() {
        return HTMLImageElementImpl.getHspaceImpl(this.getPeer()) + "";
    }

    static native int getHspaceImpl(long var0);

    @Override
    public void setHspace(String string) {
        HTMLImageElementImpl.setHspaceImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setHspaceImpl(long var0, int var2);

    @Override
    public boolean getIsMap() {
        return HTMLImageElementImpl.getIsMapImpl(this.getPeer());
    }

    static native boolean getIsMapImpl(long var0);

    @Override
    public void setIsMap(boolean bl) {
        HTMLImageElementImpl.setIsMapImpl(this.getPeer(), bl);
    }

    static native void setIsMapImpl(long var0, boolean var2);

    @Override
    public String getLongDesc() {
        return HTMLImageElementImpl.getLongDescImpl(this.getPeer());
    }

    static native String getLongDescImpl(long var0);

    @Override
    public void setLongDesc(String string) {
        HTMLImageElementImpl.setLongDescImpl(this.getPeer(), string);
    }

    static native void setLongDescImpl(long var0, String var2);

    @Override
    public String getSrc() {
        return HTMLImageElementImpl.getSrcImpl(this.getPeer());
    }

    static native String getSrcImpl(long var0);

    @Override
    public void setSrc(String string) {
        HTMLImageElementImpl.setSrcImpl(this.getPeer(), string);
    }

    static native void setSrcImpl(long var0, String var2);

    public String getSrcset() {
        return HTMLImageElementImpl.getSrcsetImpl(this.getPeer());
    }

    static native String getSrcsetImpl(long var0);

    public void setSrcset(String string) {
        HTMLImageElementImpl.setSrcsetImpl(this.getPeer(), string);
    }

    static native void setSrcsetImpl(long var0, String var2);

    @Override
    public String getUseMap() {
        return HTMLImageElementImpl.getUseMapImpl(this.getPeer());
    }

    static native String getUseMapImpl(long var0);

    @Override
    public void setUseMap(String string) {
        HTMLImageElementImpl.setUseMapImpl(this.getPeer(), string);
    }

    static native void setUseMapImpl(long var0, String var2);

    @Override
    public String getVspace() {
        return HTMLImageElementImpl.getVspaceImpl(this.getPeer()) + "";
    }

    static native int getVspaceImpl(long var0);

    @Override
    public void setVspace(String string) {
        HTMLImageElementImpl.setVspaceImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setVspaceImpl(long var0, int var2);

    @Override
    public String getWidth() {
        return HTMLImageElementImpl.getWidthImpl(this.getPeer()) + "";
    }

    static native int getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLImageElementImpl.setWidthImpl(this.getPeer(), Integer.parseInt(string));
    }

    static native void setWidthImpl(long var0, int var2);

    public boolean getComplete() {
        return HTMLImageElementImpl.getCompleteImpl(this.getPeer());
    }

    static native boolean getCompleteImpl(long var0);

    public String getLowsrc() {
        return HTMLImageElementImpl.getLowsrcImpl(this.getPeer());
    }

    static native String getLowsrcImpl(long var0);

    public void setLowsrc(String string) {
        HTMLImageElementImpl.setLowsrcImpl(this.getPeer(), string);
    }

    static native void setLowsrcImpl(long var0, String var2);

    public int getNaturalHeight() {
        return HTMLImageElementImpl.getNaturalHeightImpl(this.getPeer());
    }

    static native int getNaturalHeightImpl(long var0);

    public int getNaturalWidth() {
        return HTMLImageElementImpl.getNaturalWidthImpl(this.getPeer());
    }

    static native int getNaturalWidthImpl(long var0);

    public int getX() {
        return HTMLImageElementImpl.getXImpl(this.getPeer());
    }

    static native int getXImpl(long var0);

    public int getY() {
        return HTMLImageElementImpl.getYImpl(this.getPeer());
    }

    static native int getYImpl(long var0);

    @Override
    public void setLowSrc(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLowSrc() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

