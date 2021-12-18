/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.DocumentFragmentImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;

public class RangeImpl
implements Range {
    private final long peer;
    public static final int START_TO_START = 0;
    public static final int START_TO_END = 1;
    public static final int END_TO_END = 2;
    public static final int END_TO_START = 3;
    public static final int NODE_BEFORE = 0;
    public static final int NODE_AFTER = 1;
    public static final int NODE_BEFORE_AND_AFTER = 2;
    public static final int NODE_INSIDE = 3;

    RangeImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static Range create(long l) {
        if (l == 0L) {
            return null;
        }
        return new RangeImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof RangeImpl && this.peer == ((RangeImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(Range range) {
        return range == null ? 0L : ((RangeImpl)range).getPeer();
    }

    private static native void dispose(long var0);

    static Range getImpl(long l) {
        return RangeImpl.create(l);
    }

    @Override
    public Node getStartContainer() throws DOMException {
        return NodeImpl.getImpl(RangeImpl.getStartContainerImpl(this.getPeer()));
    }

    static native long getStartContainerImpl(long var0);

    @Override
    public int getStartOffset() throws DOMException {
        return RangeImpl.getStartOffsetImpl(this.getPeer());
    }

    static native int getStartOffsetImpl(long var0);

    @Override
    public Node getEndContainer() throws DOMException {
        return NodeImpl.getImpl(RangeImpl.getEndContainerImpl(this.getPeer()));
    }

    static native long getEndContainerImpl(long var0);

    @Override
    public int getEndOffset() throws DOMException {
        return RangeImpl.getEndOffsetImpl(this.getPeer());
    }

    static native int getEndOffsetImpl(long var0);

    @Override
    public boolean getCollapsed() throws DOMException {
        return RangeImpl.getCollapsedImpl(this.getPeer());
    }

    static native boolean getCollapsedImpl(long var0);

    @Override
    public Node getCommonAncestorContainer() throws DOMException {
        return NodeImpl.getImpl(RangeImpl.getCommonAncestorContainerImpl(this.getPeer()));
    }

    static native long getCommonAncestorContainerImpl(long var0);

    public String getText() {
        return RangeImpl.getTextImpl(this.getPeer());
    }

    static native String getTextImpl(long var0);

    @Override
    public void setStart(Node node, int n) throws DOMException {
        RangeImpl.setStartImpl(this.getPeer(), NodeImpl.getPeer(node), n);
    }

    static native void setStartImpl(long var0, long var2, int var4);

    @Override
    public void setEnd(Node node, int n) throws DOMException {
        RangeImpl.setEndImpl(this.getPeer(), NodeImpl.getPeer(node), n);
    }

    static native void setEndImpl(long var0, long var2, int var4);

    @Override
    public void setStartBefore(Node node) throws DOMException {
        RangeImpl.setStartBeforeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void setStartBeforeImpl(long var0, long var2);

    @Override
    public void setStartAfter(Node node) throws DOMException {
        RangeImpl.setStartAfterImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void setStartAfterImpl(long var0, long var2);

    @Override
    public void setEndBefore(Node node) throws DOMException {
        RangeImpl.setEndBeforeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void setEndBeforeImpl(long var0, long var2);

    @Override
    public void setEndAfter(Node node) throws DOMException {
        RangeImpl.setEndAfterImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void setEndAfterImpl(long var0, long var2);

    @Override
    public void collapse(boolean bl) throws DOMException {
        RangeImpl.collapseImpl(this.getPeer(), bl);
    }

    static native void collapseImpl(long var0, boolean var2);

    @Override
    public void selectNode(Node node) throws DOMException {
        RangeImpl.selectNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void selectNodeImpl(long var0, long var2);

    @Override
    public void selectNodeContents(Node node) throws DOMException {
        RangeImpl.selectNodeContentsImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void selectNodeContentsImpl(long var0, long var2);

    @Override
    public short compareBoundaryPoints(short s, Range range) throws DOMException {
        return RangeImpl.compareBoundaryPointsImpl(this.getPeer(), s, RangeImpl.getPeer(range));
    }

    static native short compareBoundaryPointsImpl(long var0, short var2, long var3);

    @Override
    public void deleteContents() throws DOMException {
        RangeImpl.deleteContentsImpl(this.getPeer());
    }

    static native void deleteContentsImpl(long var0);

    @Override
    public DocumentFragment extractContents() throws DOMException {
        return DocumentFragmentImpl.getImpl(RangeImpl.extractContentsImpl(this.getPeer()));
    }

    static native long extractContentsImpl(long var0);

    @Override
    public DocumentFragment cloneContents() throws DOMException {
        return DocumentFragmentImpl.getImpl(RangeImpl.cloneContentsImpl(this.getPeer()));
    }

    static native long cloneContentsImpl(long var0);

    @Override
    public void insertNode(Node node) throws DOMException {
        RangeImpl.insertNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void insertNodeImpl(long var0, long var2);

    @Override
    public void surroundContents(Node node) throws DOMException {
        RangeImpl.surroundContentsImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void surroundContentsImpl(long var0, long var2);

    @Override
    public Range cloneRange() throws DOMException {
        return RangeImpl.getImpl(RangeImpl.cloneRangeImpl(this.getPeer()));
    }

    static native long cloneRangeImpl(long var0);

    @Override
    public String toString() throws DOMException {
        return RangeImpl.toStringImpl(this.getPeer());
    }

    static native String toStringImpl(long var0);

    @Override
    public void detach() throws DOMException {
        RangeImpl.detachImpl(this.getPeer());
    }

    static native void detachImpl(long var0);

    public DocumentFragment createContextualFragment(String string) throws DOMException {
        return DocumentFragmentImpl.getImpl(RangeImpl.createContextualFragmentImpl(this.getPeer(), string));
    }

    static native long createContextualFragmentImpl(long var0, String var2);

    public boolean intersectsNode(Node node) throws DOMException {
        return RangeImpl.intersectsNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native boolean intersectsNodeImpl(long var0, long var2);

    public short compareNode(Node node) throws DOMException {
        return RangeImpl.compareNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native short compareNodeImpl(long var0, long var2);

    public short comparePoint(Node node, int n) throws DOMException {
        return RangeImpl.comparePointImpl(this.getPeer(), NodeImpl.getPeer(node), n);
    }

    static native short comparePointImpl(long var0, long var2, int var4);

    public boolean isPointInRange(Node node, int n) throws DOMException {
        return RangeImpl.isPointInRangeImpl(this.getPeer(), NodeImpl.getPeer(node), n);
    }

    static native boolean isPointInRangeImpl(long var0, long var2, int var4);

    public void expand(String string) throws DOMException {
        RangeImpl.expandImpl(this.getPeer(), string);
    }

    static native void expandImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            RangeImpl.dispose(this.peer);
        }
    }
}

