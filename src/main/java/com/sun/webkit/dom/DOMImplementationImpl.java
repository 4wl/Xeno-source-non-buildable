/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSStyleSheetImpl;
import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.DocumentTypeImpl;
import com.sun.webkit.dom.HTMLDocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html.HTMLDocument;

public class DOMImplementationImpl
implements DOMImplementation {
    private final long peer;

    DOMImplementationImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static DOMImplementation create(long l) {
        if (l == 0L) {
            return null;
        }
        return new DOMImplementationImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof DOMImplementationImpl && this.peer == ((DOMImplementationImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(DOMImplementation dOMImplementation) {
        return dOMImplementation == null ? 0L : ((DOMImplementationImpl)dOMImplementation).getPeer();
    }

    private static native void dispose(long var0);

    static DOMImplementation getImpl(long l) {
        return DOMImplementationImpl.create(l);
    }

    @Override
    public boolean hasFeature(String string, String string2) {
        return DOMImplementationImpl.hasFeatureImpl(this.getPeer(), string, string2);
    }

    static native boolean hasFeatureImpl(long var0, String var2, String var3);

    @Override
    public DocumentType createDocumentType(String string, String string2, String string3) throws DOMException {
        return DocumentTypeImpl.getImpl(DOMImplementationImpl.createDocumentTypeImpl(this.getPeer(), string, string2, string3));
    }

    static native long createDocumentTypeImpl(long var0, String var2, String var3, String var4);

    @Override
    public Document createDocument(String string, String string2, DocumentType documentType) throws DOMException {
        return DocumentImpl.getImpl(DOMImplementationImpl.createDocumentImpl(this.getPeer(), string, string2, DocumentTypeImpl.getPeer(documentType)));
    }

    static native long createDocumentImpl(long var0, String var2, String var3, long var4);

    public CSSStyleSheet createCSSStyleSheet(String string, String string2) throws DOMException {
        return CSSStyleSheetImpl.getImpl(DOMImplementationImpl.createCSSStyleSheetImpl(this.getPeer(), string, string2));
    }

    static native long createCSSStyleSheetImpl(long var0, String var2, String var3);

    public HTMLDocument createHTMLDocument(String string) {
        return HTMLDocumentImpl.getImpl(DOMImplementationImpl.createHTMLDocumentImpl(this.getPeer(), string));
    }

    static native long createHTMLDocumentImpl(long var0, String var2);

    @Override
    public Object getFeature(String string, String string2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            DOMImplementationImpl.dispose(this.peer);
        }
    }
}

