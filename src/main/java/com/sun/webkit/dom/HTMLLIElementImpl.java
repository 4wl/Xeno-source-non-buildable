/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLLIElement;

public class HTMLLIElementImpl
extends HTMLElementImpl
implements HTMLLIElement {
    HTMLLIElementImpl(long l) {
        super(l);
    }

    static HTMLLIElement getImpl(long l) {
        return (HTMLLIElement)HTMLLIElementImpl.create(l);
    }

    @Override
    public String getType() {
        return HTMLLIElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLLIElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    @Override
    public int getValue() {
        return HTMLLIElementImpl.getValueImpl(this.getPeer());
    }

    static native int getValueImpl(long var0);

    @Override
    public void setValue(int n) {
        HTMLLIElementImpl.setValueImpl(this.getPeer(), n);
    }

    static native void setValueImpl(long var0, int var2);
}

