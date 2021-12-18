/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class HTMLTableSectionElementImpl
extends HTMLElementImpl
implements HTMLTableSectionElement {
    HTMLTableSectionElementImpl(long l) {
        super(l);
    }

    static HTMLTableSectionElement getImpl(long l) {
        return (HTMLTableSectionElement)HTMLTableSectionElementImpl.create(l);
    }

    @Override
    public String getAlign() {
        return HTMLTableSectionElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLTableSectionElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);

    @Override
    public String getCh() {
        return HTMLTableSectionElementImpl.getChImpl(this.getPeer());
    }

    static native String getChImpl(long var0);

    @Override
    public void setCh(String string) {
        HTMLTableSectionElementImpl.setChImpl(this.getPeer(), string);
    }

    static native void setChImpl(long var0, String var2);

    @Override
    public String getChOff() {
        return HTMLTableSectionElementImpl.getChOffImpl(this.getPeer());
    }

    static native String getChOffImpl(long var0);

    @Override
    public void setChOff(String string) {
        HTMLTableSectionElementImpl.setChOffImpl(this.getPeer(), string);
    }

    static native void setChOffImpl(long var0, String var2);

    @Override
    public String getVAlign() {
        return HTMLTableSectionElementImpl.getVAlignImpl(this.getPeer());
    }

    static native String getVAlignImpl(long var0);

    @Override
    public void setVAlign(String string) {
        HTMLTableSectionElementImpl.setVAlignImpl(this.getPeer(), string);
    }

    static native void setVAlignImpl(long var0, String var2);

    @Override
    public HTMLCollection getRows() {
        return HTMLCollectionImpl.getImpl(HTMLTableSectionElementImpl.getRowsImpl(this.getPeer()));
    }

    static native long getRowsImpl(long var0);

    @Override
    public HTMLElement insertRow(int n) throws DOMException {
        return HTMLElementImpl.getImpl(HTMLTableSectionElementImpl.insertRowImpl(this.getPeer(), n));
    }

    static native long insertRowImpl(long var0, int var2);

    @Override
    public void deleteRow(int n) throws DOMException {
        HTMLTableSectionElementImpl.deleteRowImpl(this.getPeer(), n);
    }

    static native void deleteRowImpl(long var0, int var2);
}

