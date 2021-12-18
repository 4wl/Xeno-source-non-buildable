/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLMapElement;

public class HTMLMapElementImpl
extends HTMLElementImpl
implements HTMLMapElement {
    HTMLMapElementImpl(long l) {
        super(l);
    }

    static HTMLMapElement getImpl(long l) {
        return (HTMLMapElement)HTMLMapElementImpl.create(l);
    }

    @Override
    public HTMLCollection getAreas() {
        return HTMLCollectionImpl.getImpl(HTMLMapElementImpl.getAreasImpl(this.getPeer()));
    }

    static native long getAreasImpl(long var0);

    @Override
    public String getName() {
        return HTMLMapElementImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public void setName(String string) {
        HTMLMapElementImpl.setNameImpl(this.getPeer(), string);
    }

    static native void setNameImpl(long var0, String var2);
}

