/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLHeadElement;

public class HTMLHeadElementImpl
extends HTMLElementImpl
implements HTMLHeadElement {
    HTMLHeadElementImpl(long l) {
        super(l);
    }

    static HTMLHeadElement getImpl(long l) {
        return (HTMLHeadElement)HTMLHeadElementImpl.create(l);
    }

    @Override
    public String getProfile() {
        return HTMLHeadElementImpl.getProfileImpl(this.getPeer());
    }

    static native String getProfileImpl(long var0);

    @Override
    public void setProfile(String string) {
        HTMLHeadElementImpl.setProfileImpl(this.getPeer(), string);
    }

    static native void setProfileImpl(long var0, String var2);
}

