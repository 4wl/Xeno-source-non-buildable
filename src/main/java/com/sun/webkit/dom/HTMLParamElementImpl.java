/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLParamElement;

public class HTMLParamElementImpl
extends HTMLElementImpl
implements HTMLParamElement {
    HTMLParamElementImpl(long l) {
        super(l);
    }

    static HTMLParamElement getImpl(long l) {
        return (HTMLParamElement)HTMLParamElementImpl.create(l);
    }

    @Override
    public String getName() {
        return HTMLParamElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLParamElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);

    @Override
    public String getType() {
        return HTMLParamElementImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public void setType(String string) {
        HTMLParamElementImpl.setTypeImpl(this.getPeer(), string);
    }

    static native void setTypeImpl(long var0, String var2);

    @Override
    public String getValue() {
        return HTMLParamElementImpl.getValueImpl(this.getPeer());
    }

    static native String getValueImpl(long var0);

    @Override
    public void setValue(String string) {
        HTMLParamElementImpl.setValueImpl(this.getPeer(), string);
    }

    static native void setValueImpl(long var0, String var2);

    @Override
    public String getValueType() {
        return HTMLParamElementImpl.getValueTypeImpl(this.getPeer());
    }

    static native String getValueTypeImpl(long var0);

    @Override
    public void setValueType(String string) {
        HTMLParamElementImpl.setValueTypeImpl(this.getPeer(), string);
    }

    static native void setValueTypeImpl(long var0, String var2);
}

