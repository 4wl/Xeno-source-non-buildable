/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.xpath.XPathNSResolver;

public class XPathNSResolverImpl
implements XPathNSResolver {
    private final long peer;

    XPathNSResolverImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static XPathNSResolver create(long l) {
        if (l == 0L) {
            return null;
        }
        return new XPathNSResolverImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof XPathNSResolverImpl && this.peer == ((XPathNSResolverImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(XPathNSResolver xPathNSResolver) {
        return xPathNSResolver == null ? 0L : ((XPathNSResolverImpl)xPathNSResolver).getPeer();
    }

    private static native void dispose(long var0);

    static XPathNSResolver getImpl(long l) {
        return XPathNSResolverImpl.create(l);
    }

    @Override
    public String lookupNamespaceURI(String string) {
        return XPathNSResolverImpl.lookupNamespaceURIImpl(this.getPeer(), string);
    }

    static native String lookupNamespaceURIImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            XPathNSResolverImpl.dispose(this.peer);
        }
    }
}

