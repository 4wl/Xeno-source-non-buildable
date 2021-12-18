/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathResult;

public class XPathResultImpl
implements XPathResult {
    private final long peer;
    public static final int ANY_TYPE = 0;
    public static final int NUMBER_TYPE = 1;
    public static final int STRING_TYPE = 2;
    public static final int BOOLEAN_TYPE = 3;
    public static final int UNORDERED_NODE_ITERATOR_TYPE = 4;
    public static final int ORDERED_NODE_ITERATOR_TYPE = 5;
    public static final int UNORDERED_NODE_SNAPSHOT_TYPE = 6;
    public static final int ORDERED_NODE_SNAPSHOT_TYPE = 7;
    public static final int ANY_UNORDERED_NODE_TYPE = 8;
    public static final int FIRST_ORDERED_NODE_TYPE = 9;

    XPathResultImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static XPathResult create(long l) {
        if (l == 0L) {
            return null;
        }
        return new XPathResultImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof XPathResultImpl && this.peer == ((XPathResultImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(XPathResult xPathResult) {
        return xPathResult == null ? 0L : ((XPathResultImpl)xPathResult).getPeer();
    }

    private static native void dispose(long var0);

    static XPathResult getImpl(long l) {
        return XPathResultImpl.create(l);
    }

    @Override
    public short getResultType() {
        return XPathResultImpl.getResultTypeImpl(this.getPeer());
    }

    static native short getResultTypeImpl(long var0);

    @Override
    public double getNumberValue() throws DOMException {
        return XPathResultImpl.getNumberValueImpl(this.getPeer());
    }

    static native double getNumberValueImpl(long var0);

    @Override
    public String getStringValue() throws DOMException {
        return XPathResultImpl.getStringValueImpl(this.getPeer());
    }

    static native String getStringValueImpl(long var0);

    @Override
    public boolean getBooleanValue() throws DOMException {
        return XPathResultImpl.getBooleanValueImpl(this.getPeer());
    }

    static native boolean getBooleanValueImpl(long var0);

    @Override
    public Node getSingleNodeValue() throws DOMException {
        return NodeImpl.getImpl(XPathResultImpl.getSingleNodeValueImpl(this.getPeer()));
    }

    static native long getSingleNodeValueImpl(long var0);

    @Override
    public boolean getInvalidIteratorState() {
        return XPathResultImpl.getInvalidIteratorStateImpl(this.getPeer());
    }

    static native boolean getInvalidIteratorStateImpl(long var0);

    @Override
    public int getSnapshotLength() throws DOMException {
        return XPathResultImpl.getSnapshotLengthImpl(this.getPeer());
    }

    static native int getSnapshotLengthImpl(long var0);

    @Override
    public Node iterateNext() throws DOMException {
        return NodeImpl.getImpl(XPathResultImpl.iterateNextImpl(this.getPeer()));
    }

    static native long iterateNextImpl(long var0);

    @Override
    public Node snapshotItem(int n) throws DOMException {
        return NodeImpl.getImpl(XPathResultImpl.snapshotItemImpl(this.getPeer(), n));
    }

    static native long snapshotItemImpl(long var0, int var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            XPathResultImpl.dispose(this.peer);
        }
    }
}

