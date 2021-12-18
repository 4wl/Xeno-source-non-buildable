/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFormElementImpl;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLLabelElement;

public class HTMLLabelElementImpl
extends HTMLElementImpl
implements HTMLLabelElement {
    HTMLLabelElementImpl(long l) {
        super(l);
    }

    static HTMLLabelElement getImpl(long l) {
        return (HTMLLabelElement)HTMLLabelElementImpl.create(l);
    }

    @Override
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(HTMLLabelElementImpl.getFormImpl(this.getPeer()));
    }

    static native long getFormImpl(long var0);

    @Override
    public String getHtmlFor() {
        return HTMLLabelElementImpl.getHtmlForImpl(this.getPeer());
    }

    static native String getHtmlForImpl(long var0);

    @Override
    public void setHtmlFor(String string) {
        HTMLLabelElementImpl.setHtmlForImpl(this.getPeer(), string);
    }

    static native void setHtmlForImpl(long var0, String var2);

    public HTMLElement getControl() {
        return HTMLElementImpl.getImpl(HTMLLabelElementImpl.getControlImpl(this.getPeer()));
    }

    static native long getControlImpl(long var0);
}

