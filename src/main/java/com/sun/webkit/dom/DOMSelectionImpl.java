/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.RangeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;

public class DOMSelectionImpl {
    private final long peer;

    DOMSelectionImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static DOMSelectionImpl create(long l) {
        if (l == 0L) {
            return null;
        }
        return new DOMSelectionImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof DOMSelectionImpl && this.peer == ((DOMSelectionImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(DOMSelectionImpl dOMSelectionImpl) {
        return dOMSelectionImpl == null ? 0L : dOMSelectionImpl.getPeer();
    }

    private static native void dispose(long var0);

    static DOMSelectionImpl getImpl(long l) {
        return DOMSelectionImpl.create(l);
    }

    public Node getAnchorNode() {
        return NodeImpl.getImpl(DOMSelectionImpl.getAnchorNodeImpl(this.getPeer()));
    }

    static native long getAnchorNodeImpl(long var0);

    public int getAnchorOffset() {
        return DOMSelectionImpl.getAnchorOffsetImpl(this.getPeer());
    }

    static native int getAnchorOffsetImpl(long var0);

    public Node getFocusNode() {
        return NodeImpl.getImpl(DOMSelectionImpl.getFocusNodeImpl(this.getPeer()));
    }

    static native long getFocusNodeImpl(long var0);

    public int getFocusOffset() {
        return DOMSelectionImpl.getFocusOffsetImpl(this.getPeer());
    }

    static native int getFocusOffsetImpl(long var0);

    public boolean getIsCollapsed() {
        return DOMSelectionImpl.getIsCollapsedImpl(this.getPeer());
    }

    static native boolean getIsCollapsedImpl(long var0);

    public int getRangeCount() {
        return DOMSelectionImpl.getRangeCountImpl(this.getPeer());
    }

    static native int getRangeCountImpl(long var0);

    public Node getBaseNode() {
        return NodeImpl.getImpl(DOMSelectionImpl.getBaseNodeImpl(this.getPeer()));
    }

    static native long getBaseNodeImpl(long var0);

    public int getBaseOffset() {
        return DOMSelectionImpl.getBaseOffsetImpl(this.getPeer());
    }

    static native int getBaseOffsetImpl(long var0);

    public Node getExtentNode() {
        return NodeImpl.getImpl(DOMSelectionImpl.getExtentNodeImpl(this.getPeer()));
    }

    static native long getExtentNodeImpl(long var0);

    public int getExtentOffset() {
        return DOMSelectionImpl.getExtentOffsetImpl(this.getPeer());
    }

    static native int getExtentOffsetImpl(long var0);

    public String getType() {
        return DOMSelectionImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    public void collapse(Node node, int n) throws DOMException {
        DOMSelectionImpl.collapseImpl(this.getPeer(), NodeImpl.getPeer(node), n);
    }

    static native void collapseImpl(long var0, long var2, int var4);

    public void collapseToEnd() throws DOMException {
        DOMSelectionImpl.collapseToEndImpl(this.getPeer());
    }

    static native void collapseToEndImpl(long var0);

    public void collapseToStart() throws DOMException {
        DOMSelectionImpl.collapseToStartImpl(this.getPeer());
    }

    static native void collapseToStartImpl(long var0);

    public void deleteFromDocument() {
        DOMSelectionImpl.deleteFromDocumentImpl(this.getPeer());
    }

    static native void deleteFromDocumentImpl(long var0);

    public boolean containsNode(Node node, boolean bl) {
        return DOMSelectionImpl.containsNodeImpl(this.getPeer(), NodeImpl.getPeer(node), bl);
    }

    static native boolean containsNodeImpl(long var0, long var2, boolean var4);

    public void selectAllChildren(Node node) throws DOMException {
        DOMSelectionImpl.selectAllChildrenImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void selectAllChildrenImpl(long var0, long var2);

    public void extend(Node node, int n) throws DOMException {
        DOMSelectionImpl.extendImpl(this.getPeer(), NodeImpl.getPeer(node), n);
    }

    static native void extendImpl(long var0, long var2, int var4);

    public Range getRangeAt(int n) throws DOMException {
        return RangeImpl.getImpl(DOMSelectionImpl.getRangeAtImpl(this.getPeer(), n));
    }

    static native long getRangeAtImpl(long var0, int var2);

    public void removeAllRanges() {
        DOMSelectionImpl.removeAllRangesImpl(this.getPeer());
    }

    static native void removeAllRangesImpl(long var0);

    public void addRange(Range range) {
        DOMSelectionImpl.addRangeImpl(this.getPeer(), RangeImpl.getPeer(range));
    }

    static native void addRangeImpl(long var0, long var2);

    public void modify(String string, String string2, String string3) {
        DOMSelectionImpl.modifyImpl(this.getPeer(), string, string2, string3);
    }

    static native void modifyImpl(long var0, String var2, String var3, String var4);

    public void setBaseAndExtent(Node node, int n, Node node2, int n2) throws DOMException {
        DOMSelectionImpl.setBaseAndExtentImpl(this.getPeer(), NodeImpl.getPeer(node), n, NodeImpl.getPeer(node2), n2);
    }

    static native void setBaseAndExtentImpl(long var0, long var2, int var4, long var5, int var7);

    public void setPosition(Node node, int n) throws DOMException {
        DOMSelectionImpl.setPositionImpl(this.getPeer(), NodeImpl.getPeer(node), n);
    }

    static native void setPositionImpl(long var0, long var2, int var4);

    public void empty() {
        DOMSelectionImpl.emptyImpl(this.getPeer());
    }

    static native void emptyImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            DOMSelectionImpl.dispose(this.peer);
        }
    }
}

