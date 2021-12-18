/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class HTMLOptionsCollectionImpl
extends HTMLCollectionImpl {
    HTMLOptionsCollectionImpl(long l) {
        super(l);
    }

    static HTMLOptionsCollectionImpl getImpl(long l) {
        return (HTMLOptionsCollectionImpl)HTMLOptionsCollectionImpl.create(l);
    }

    public int getSelectedIndex() {
        return HTMLOptionsCollectionImpl.getSelectedIndexImpl(this.getPeer());
    }

    static native int getSelectedIndexImpl(long var0);

    public void setSelectedIndex(int n) {
        HTMLOptionsCollectionImpl.setSelectedIndexImpl(this.getPeer(), n);
    }

    static native void setSelectedIndexImpl(long var0, int var2);

    @Override
    public int getLength() {
        return HTMLOptionsCollectionImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    public void setLength(int n) throws DOMException {
        HTMLOptionsCollectionImpl.setLengthImpl(this.getPeer(), n);
    }

    static native void setLengthImpl(long var0, int var2);

    @Override
    public Node namedItem(String string) {
        return NodeImpl.getImpl(HTMLOptionsCollectionImpl.namedItemImpl(this.getPeer(), string));
    }

    static native long namedItemImpl(long var0, String var2);
}

