/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.HTMLOptionsCollectionImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;

public class HTMLCollectionImpl
implements HTMLCollection {
    private final long peer;
    private static final int TYPE_HTMLOptionsCollection = 1;

    HTMLCollectionImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static HTMLCollection create(long l) {
        if (l == 0L) {
            return null;
        }
        switch (HTMLCollectionImpl.getCPPTypeImpl(l)) {
            case 1: {
                return new HTMLOptionsCollectionImpl(l);
            }
        }
        return new HTMLCollectionImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof HTMLCollectionImpl && this.peer == ((HTMLCollectionImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(HTMLCollection hTMLCollection) {
        return hTMLCollection == null ? 0L : ((HTMLCollectionImpl)hTMLCollection).getPeer();
    }

    private static native void dispose(long var0);

    private static native int getCPPTypeImpl(long var0);

    static HTMLCollection getImpl(long l) {
        return HTMLCollectionImpl.create(l);
    }

    @Override
    public int getLength() {
        return HTMLCollectionImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public Node item(int n) {
        return NodeImpl.getImpl(HTMLCollectionImpl.itemImpl(this.getPeer(), n));
    }

    static native long itemImpl(long var0, int var2);

    @Override
    public Node namedItem(String string) {
        return NodeImpl.getImpl(HTMLCollectionImpl.namedItemImpl(this.getPeer(), string));
    }

    static native long namedItemImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            HTMLCollectionImpl.dispose(this.peer);
        }
    }
}

