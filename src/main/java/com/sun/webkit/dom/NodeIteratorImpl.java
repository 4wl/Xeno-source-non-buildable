/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeFilterImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeIteratorImpl
implements NodeIterator {
    private final long peer;

    NodeIteratorImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static NodeIterator create(long l) {
        if (l == 0L) {
            return null;
        }
        return new NodeIteratorImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof NodeIteratorImpl && this.peer == ((NodeIteratorImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(NodeIterator nodeIterator) {
        return nodeIterator == null ? 0L : ((NodeIteratorImpl)nodeIterator).getPeer();
    }

    private static native void dispose(long var0);

    static NodeIterator getImpl(long l) {
        return NodeIteratorImpl.create(l);
    }

    @Override
    public Node getRoot() {
        return NodeImpl.getImpl(NodeIteratorImpl.getRootImpl(this.getPeer()));
    }

    static native long getRootImpl(long var0);

    @Override
    public int getWhatToShow() {
        return NodeIteratorImpl.getWhatToShowImpl(this.getPeer());
    }

    static native int getWhatToShowImpl(long var0);

    @Override
    public NodeFilter getFilter() {
        return NodeFilterImpl.getImpl(NodeIteratorImpl.getFilterImpl(this.getPeer()));
    }

    static native long getFilterImpl(long var0);

    @Override
    public boolean getExpandEntityReferences() {
        return NodeIteratorImpl.getExpandEntityReferencesImpl(this.getPeer());
    }

    static native boolean getExpandEntityReferencesImpl(long var0);

    public Node getReferenceNode() {
        return NodeImpl.getImpl(NodeIteratorImpl.getReferenceNodeImpl(this.getPeer()));
    }

    static native long getReferenceNodeImpl(long var0);

    public boolean getPointerBeforeReferenceNode() {
        return NodeIteratorImpl.getPointerBeforeReferenceNodeImpl(this.getPeer());
    }

    static native boolean getPointerBeforeReferenceNodeImpl(long var0);

    @Override
    public Node nextNode() throws DOMException {
        return NodeImpl.getImpl(NodeIteratorImpl.nextNodeImpl(this.getPeer()));
    }

    static native long nextNodeImpl(long var0);

    @Override
    public Node previousNode() throws DOMException {
        return NodeImpl.getImpl(NodeIteratorImpl.previousNodeImpl(this.getPeer()));
    }

    static native long previousNodeImpl(long var0);

    @Override
    public void detach() {
        NodeIteratorImpl.detachImpl(this.getPeer());
    }

    static native void detachImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            NodeIteratorImpl.dispose(this.peer);
        }
    }
}

