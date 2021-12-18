/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class HTMLTableRowElementImpl
extends HTMLElementImpl
implements HTMLTableRowElement {
    HTMLTableRowElementImpl(long l) {
        super(l);
    }

    static HTMLTableRowElement getImpl(long l) {
        return (HTMLTableRowElement)HTMLTableRowElementImpl.create(l);
    }

    @Override
    public int getRowIndex() {
        return HTMLTableRowElementImpl.getRowIndexImpl(this.getPeer());
    }

    static native int getRowIndexImpl(long var0);

    @Override
    public int getSectionRowIndex() {
        return HTMLTableRowElementImpl.getSectionRowIndexImpl(this.getPeer());
    }

    static native int getSectionRowIndexImpl(long var0);

    @Override
    public HTMLCollection getCells() {
        return HTMLCollectionImpl.getImpl(HTMLTableRowElementImpl.getCellsImpl(this.getPeer()));
    }

    static native long getCellsImpl(long var0);

    @Override
    public String getAlign() {
        return HTMLTableRowElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLTableRowElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getBgColor() {
        return HTMLTableRowElementImpl.getBgColorImpl(this.getPeer());
    }

    static native String getBgColorImpl(long var0);

    @Override
    public void setBgColor(String string) {
        HTMLTableRowElementImpl.setBgColorImpl(this.getPeer(), string);
    }

    static native void setBgColorImpl(long var0, String var2);

    @Override
    public String getCh() {
        return HTMLTableRowElementImpl.getChImpl(this.getPeer());
    }

    static native String getChImpl(long var0);

    @Override
    public void setCh(String string) {
        HTMLTableRowElementImpl.setChImpl(this.getPeer(), string);
    }

    static native void setChImpl(long var0, String var2);

    @Override
    public String getChOff() {
        return HTMLTableRowElementImpl.getChOffImpl(this.getPeer());
    }

    static native String getChOffImpl(long var0);

    @Override
    public void setChOff(String string) {
        HTMLTableRowElementImpl.setChOffImpl(this.getPeer(), string);
    }

    static native void setChOffImpl(long var0, String var2);

    @Override
    public String getVAlign() {
        return HTMLTableRowElementImpl.getVAlignImpl(this.getPeer());
    }

    static native String getVAlignImpl(long var0);

    @Override
    public void setVAlign(String string) {
        HTMLTableRowElementImpl.setVAlignImpl(this.getPeer(), string);
    }

    static native void setVAlignImpl(long var0, String var2);

    @Override
    public HTMLElement insertCell(int n) throws DOMException {
        return HTMLElementImpl.getImpl(HTMLTableRowElementImpl.insertCellImpl(this.getPeer(), n));
    }

    static native long insertCellImpl(long var0, int var2);

    @Override
    public void deleteCell(int n) throws DOMException {
        HTMLTableRowElementImpl.deleteCellImpl(this.getPeer(), n);
    }

    static native void deleteCellImpl(long var0, int var2);
}

