/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.NamedNodeMapImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

public class DocumentTypeImpl
extends NodeImpl
implements DocumentType {
    DocumentTypeImpl(long l) {
        super(l);
    }

    static DocumentType getImpl(long l) {
        return (DocumentType)DocumentTypeImpl.create(l);
    }

    @Override
    public String getName() {
        return DocumentTypeImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public NamedNodeMap getEntities() {
        return NamedNodeMapImpl.getImpl(DocumentTypeImpl.getEntitiesImpl(this.getPeer()));
    }

    static native long getEntitiesImpl(long var0);

    @Override
    public NamedNodeMap getNotations() {
        return NamedNodeMapImpl.getImpl(DocumentTypeImpl.getNotationsImpl(this.getPeer()));
    }

    static native long getNotationsImpl(long var0);

    @Override
    public String getPublicId() {
        return DocumentTypeImpl.getPublicIdImpl(this.getPeer());
    }

    static native String getPublicIdImpl(long var0);

    @Override
    public String getSystemId() {
        return DocumentTypeImpl.getSystemIdImpl(this.getPeer());
    }

    static native String getSystemIdImpl(long var0);

    @Override
    public String getInternalSubset() {
        return DocumentTypeImpl.getInternalSubsetImpl(this.getPeer());
    }

    static native String getInternalSubsetImpl(long var0);

    public void remove() throws DOMException {
        DocumentTypeImpl.removeImpl(this.getPeer());
    }

    static native void removeImpl(long var0);
}

