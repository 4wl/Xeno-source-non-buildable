/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListImpl
implements NodeList {
    private final long peer;

    NodeListImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static NodeList create(long l) {
        if (l == 0L) {
            return null;
        }
        return new NodeListImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof NodeListImpl && this.peer == ((NodeListImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(NodeList nodeList) {
        return nodeList == null ? 0L : ((NodeListImpl)nodeList).getPeer();
    }

    private static native void dispose(long var0);

    static NodeList getImpl(long l) {
        return NodeListImpl.create(l);
    }

    @Override
    public int getLength() {
        return NodeListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public Node item(int n) {
        return NodeImpl.getImpl(NodeListImpl.itemImpl(this.getPeer(), n));
    }

    static native long itemImpl(long var0, int var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            NodeListImpl.dispose(this.peer);
        }
    }
}

