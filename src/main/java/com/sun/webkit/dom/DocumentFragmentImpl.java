/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.NodeListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DocumentFragmentImpl
extends NodeImpl
implements DocumentFragment {
    DocumentFragmentImpl(long l) {
        super(l);
    }

    static DocumentFragment getImpl(long l) {
        return (DocumentFragment)DocumentFragmentImpl.create(l);
    }

    public Element querySelector(String string) throws DOMException {
        return ElementImpl.getImpl(DocumentFragmentImpl.querySelectorImpl(this.getPeer(), string));
    }

    static native long querySelectorImpl(long var0, String var2);

    public NodeList querySelectorAll(String string) throws DOMException {
        return NodeListImpl.getImpl(DocumentFragmentImpl.querySelectorAllImpl(this.getPeer(), string));
    }

    static native long querySelectorAllImpl(long var0, String var2);
}

