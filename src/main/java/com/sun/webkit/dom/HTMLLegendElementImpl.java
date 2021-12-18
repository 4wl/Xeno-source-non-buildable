/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLLegendElement;

public class HTMLLegendElementImpl
extends HTMLElementImpl
implements HTMLLegendElement {
    HTMLLegendElementImpl(long l) {
        super(l);
    }

    static HTMLLegendElement getImpl(long l) {
        return (HTMLLegendElement)HTMLLegendElementImpl.create(l);
    }

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLLegendElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    @Override
    public String getAlign() {
        return HTMLLegendElementImpl.getAlignImpl(this.getPeer());
    }

    static native String getAlignImpl(long var0);

    @Override
    public void setAlign(String string) {
        HTMLLegendElementImpl.setAlignImpl(this.getPeer(), string);
    }

    static native void setAlignImpl(long var0, String var2);
}

