/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLOptGroupElement;

public class HTMLOptGroupElementImpl
extends HTMLElementImpl
implements HTMLOptGroupElement {
    HTMLOptGroupElementImpl(long l) {
        super(l);
    }

    static HTMLOptGroupElement getImpl(long l) {
        return (HTMLOptGroupElement)HTMLOptGroupElementImpl.create(l);
    }

    @Override
    public boolean getDisabled() {
        return HTMLOptGroupElementImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        HTMLOptGroupElementImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public String getLabel() {
        return HTMLOptGroupElementImpl.getLabelImpl(this.getPeer());
    }

    static native String getLabelImpl(long var0);

    @Override
    public void setLabel(String string) {
        HTMLOptGroupElementImpl.setLabelImpl(this.getPeer(), string);
    }

    static native void setLabelImpl(long var0, String var2);
}

