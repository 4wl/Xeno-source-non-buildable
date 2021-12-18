/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;

public class NodeFilterImpl
implements NodeFilter {
    private final long peer;
    public static final int FILTER_ACCEPT = 1;
    public static final int FILTER_REJECT = 2;
    public static final int FILTER_SKIP = 3;
    public static final int SHOW_ALL = -1;
    public static final int SHOW_ELEMENT = 1;
    public static final int SHOW_ATTRIBUTE = 2;
    public static final int SHOW_TEXT = 4;
    public static final int SHOW_CDATA_SECTION = 8;
    public static final int SHOW_ENTITY_REFERENCE = 16;
    public static final int SHOW_ENTITY = 32;
    public static final int SHOW_PROCESSING_INSTRUCTION = 64;
    public static final int SHOW_COMMENT = 128;
    public static final int SHOW_DOCUMENT = 256;
    public static final int SHOW_DOCUMENT_TYPE = 512;
    public static final int SHOW_DOCUMENT_FRAGMENT = 1024;
    public static final int SHOW_NOTATION = 2048;

    NodeFilterImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static NodeFilter create(long l) {
        if (l == 0L) {
            return null;
        }
        return new NodeFilterImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof NodeFilterImpl && this.peer == ((NodeFilterImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(NodeFilter nodeFilter) {
        return nodeFilter == null ? 0L : ((NodeFilterImpl)nodeFilter).getPeer();
    }

    private static native void dispose(long var0);

    static NodeFilter getImpl(long l) {
        return NodeFilterImpl.create(l);
    }

    @Override
    public short acceptNode(Node node) {
        return NodeFilterImpl.acceptNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native short acceptNodeImpl(long var0, long var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            NodeFilterImpl.dispose(this.peer);
        }
    }
}

