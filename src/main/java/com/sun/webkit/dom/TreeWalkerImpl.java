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
import org.w3c.dom.traversal.TreeWalker;

public class TreeWalkerImpl
implements TreeWalker {
    private final long peer;

    TreeWalkerImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static TreeWalker create(long l) {
        if (l == 0L) {
            return null;
        }
        return new TreeWalkerImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof TreeWalkerImpl && this.peer == ((TreeWalkerImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(TreeWalker treeWalker) {
        return treeWalker == null ? 0L : ((TreeWalkerImpl)treeWalker).getPeer();
    }

    private static native void dispose(long var0);

    static TreeWalker getImpl(long l) {
        return TreeWalkerImpl.create(l);
    }

    @Override
    public Node getRoot() {
        return NodeImpl.getImpl(TreeWalkerImpl.getRootImpl(this.getPeer()));
    }

    static native long getRootImpl(long var0);

    @Override
    public int getWhatToShow() {
        return TreeWalkerImpl.getWhatToShowImpl(this.getPeer());
    }

    static native int getWhatToShowImpl(long var0);

    @Override
    public NodeFilter getFilter() {
        return NodeFilterImpl.getImpl(TreeWalkerImpl.getFilterImpl(this.getPeer()));
    }

    static native long getFilterImpl(long var0);

    @Override
    public boolean getExpandEntityReferences() {
        return TreeWalkerImpl.getExpandEntityReferencesImpl(this.getPeer());
    }

    static native boolean getExpandEntityReferencesImpl(long var0);

    @Override
    public Node getCurrentNode() {
        return NodeImpl.getImpl(TreeWalkerImpl.getCurrentNodeImpl(this.getPeer()));
    }

    static native long getCurrentNodeImpl(long var0);

    @Override
    public void setCurrentNode(Node node) throws DOMException {
        TreeWalkerImpl.setCurrentNodeImpl(this.getPeer(), NodeImpl.getPeer(node));
    }

    static native void setCurrentNodeImpl(long var0, long var2);

    @Override
    public Node parentNode() {
        return NodeImpl.getImpl(TreeWalkerImpl.parentNodeImpl(this.getPeer()));
    }

    static native long parentNodeImpl(long var0);

    @Override
    public Node firstChild() {
        return NodeImpl.getImpl(TreeWalkerImpl.firstChildImpl(this.getPeer()));
    }

    static native long firstChildImpl(long var0);

    @Override
    public Node lastChild() {
        return NodeImpl.getImpl(TreeWalkerImpl.lastChildImpl(this.getPeer()));
    }

    static native long lastChildImpl(long var0);

    @Override
    public Node previousSibling() {
        return NodeImpl.getImpl(TreeWalkerImpl.previousSiblingImpl(this.getPeer()));
    }

    static native long previousSiblingImpl(long var0);

    @Override
    public Node nextSibling() {
        return NodeImpl.getImpl(TreeWalkerImpl.nextSiblingImpl(this.getPeer()));
    }

    static native long nextSiblingImpl(long var0);

    @Override
    public Node previousNode() {
        return NodeImpl.getImpl(TreeWalkerImpl.previousNodeImpl(this.getPeer()));
    }

    static native long previousNodeImpl(long var0);

    @Override
    public Node nextNode() {
        return NodeImpl.getImpl(TreeWalkerImpl.nextNodeImpl(this.getPeer()));
    }

    static native long nextNodeImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            TreeWalkerImpl.dispose(this.peer);
        }
    }
}

