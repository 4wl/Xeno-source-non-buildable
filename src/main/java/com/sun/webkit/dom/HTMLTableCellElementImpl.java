/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLTableCellElement;

public class HTMLTableCellElementImpl
extends HTMLElementImpl
implements HTMLTableCellElement {
    HTMLTableCellElementImpl(long l) {
        super(l);
    }

    static HTMLTableCellElement getImpl(long l) {
        return (HTMLTableCellElement)HTMLTableCellElementImpl.create(l);
    }

    @Override
    public int getCellIndex() {
        return HTMLTableCellElementImpl.getCellIndexImpl(this.getPeer());
    }

    static native int getCellIndexImpl(long var0);

    @Override
    public String getAbbr() {
        return HTMLTableCellElementImpl.getAbbrImpl(this.getPeer());
    }

    static native String getAbbrImpl(long var0);

    @Override
    public void setAbbr(String string) {
        HTMLTableCellElementImpl.setAbbrImpl(this.getPeer(), string);
    }

    static native void setAbbrImpl(long var0, String var2);

    @Override
    public String getAlign() {
        return HTMLTableCellElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLTableCellElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getAxis() {
        return HTMLTableCellElementImpl.getAxisImpl(this.getPeer());
    }

    static native String getAxisImpl(long var0);

    @Override
    public void setAxis(String string) {
        HTMLTableCellElementImpl.setAxisImpl(this.getPeer(), string);
    }

    static native void setAxisImpl(long var0, String var2);

    @Override
    public String getBgColor() {
        return HTMLTableCellElementImpl.getBgColorImpl(this.getPeer());
    }

    static native String getBgColorImpl(long var0);

    @Override
    public void setBgColor(String string) {
        HTMLTableCellElementImpl.setBgColorImpl(this.getPeer(), string);
    }

    static native void setBgColorImpl(long var0, String var2);

    @Override
    public String getCh() {
        return HTMLTableCellElementImpl.getChImpl(this.getPeer());
    }

    static native String getChImpl(long var0);

    @Override
    public void setCh(String string) {
        HTMLTableCellElementImpl.setChImpl(this.getPeer(), string);
    }

    static native void setChImpl(long var0, String var2);

    @Override
    public String getChOff() {
        return HTMLTableCellElementImpl.getChOffImpl(this.getPeer());
    }

    static native String getChOffImpl(long var0);

    @Override
    public void setChOff(String string) {
        HTMLTableCellElementImpl.setChOffImpl(this.getPeer(), string);
    }

    static native void setChOffImpl(long var0, String var2);

    @Override
    public int getColSpan() {
        return HTMLTableCellElementImpl.getColSpanImpl(this.getPeer());
    }

    static native int getColSpanImpl(long var0);

    @Override
    public void setColSpan(int n) {
        HTMLTableCellElementImpl.setColSpanImpl(this.getPeer(), n);
    }

    static native void setColSpanImpl(long var0, int var2);

    @Override
    public String getHeaders() {
        return HTMLTableCellElementImpl.getHeadersImpl(this.getPeer());
    }

    static native String getHeadersImpl(long var0);

    @Override
    public void setHeaders(String string) {
        HTMLTableCellElementImpl.setHeadersImpl(this.getPeer(), string);
    }

    static native void setHeadersImpl(long var0, String var2);

    @Override
    public String getHeight() {
        return HTMLTableCellElementImpl.getHeightImpl(this.getPeer());
    }

    static native String getHeightImpl(long var0);

    @Override
    public void setHeight(String string) {
        HTMLTableCellElementImpl.setHeightImpl(this.getPeer(), string);
    }

    static native void setHeightImpl(long var0, String var2);

    @Override
    public boolean getNoWrap() {
        return HTMLTableCellElementImpl.getNoWrapImpl(this.getPeer());
    }

    static native boolean getNoWrapImpl(long var0);

    @Override
    public void setNoWrap(boolean bl) {
        HTMLTableCellElementImpl.setNoWrapImpl(this.getPeer(), bl);
    }

    static native void setNoWrapImpl(long var0, boolean var2);

    @Override
    public int getRowSpan() {
        return HTMLTableCellElementImpl.getRowSpanImpl(this.getPeer());
    }

    static native int getRowSpanImpl(long var0);

    @Override
    public void setRowSpan(int n) {
        HTMLTableCellElementImpl.setRowSpanImpl(this.getPeer(), n);
    }

    static native void setRowSpanImpl(long var0, int var2);

    @Override
    public String getScope() {
        return HTMLTableCellElementImpl.getScopeImpl(this.getPeer());
    }

    static native String getScopeImpl(long var0);

    @Override
    public void setScope(String string) {
        HTMLTableCellElementImpl.setScopeImpl(this.getPeer(), string);
    }

    static native void setScopeImpl(long var0, String var2);

    @Override
    public String getVAlign() {
        return HTMLTableCellElementImpl.getVAlignImpl(this.getPeer());
    }

    static native String getVAlignImpl(long var0);

    @Override
    public void setVAlign(String string) {
        HTMLTableCellElementImpl.setVAlignImpl(this.getPeer(), string);
    }

    static native void setVAlignImpl(long var0, String var2);

    @Override
    public String getWidth() {
        return HTMLTableCellElementImpl.getWidthImpl(this.getPeer());
    }

    static native String getWidthImpl(long var0);

    @Override
    public void setWidth(String string) {
        HTMLTableCellElementImpl.setWidthImpl(this.getPeer(), string);
    }

    static native void setWidthImpl(long var0, String var2);
}

