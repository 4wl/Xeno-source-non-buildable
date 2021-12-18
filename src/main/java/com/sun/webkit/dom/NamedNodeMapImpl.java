/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapImpl
implements NamedNodeMap {
    private final long peer;

    NamedNodeMapImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static NamedNodeMap create(long l) {
        if (l == 0L) {
            return null;
        }
        return new NamedNodeMapImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof NamedNodeMapImpl && this.peer == ((NamedNodeMapImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(NamedNodeMap namedNodeMap) {
        return namedNodeMap == null ? 0L : ((NamedNodeMapImpl)namedNodeMap).getPeer();
    }

    private static native void dispose(long var0);

    static NamedNodeMap getImpl(long l) {
        return NamedNodeMapImpl.create(l);
    }

    @Override
    public int getLength() {
        return NamedNodeMapImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public Node getNamedItem(String string) {
        return NodeImpl.getImpl(NamedNodeMapImpl.getNamedItemImpl(this.getPeer(), string));
    }

    static native long getNamedItemImpl(long var0, String var2);

    @Override
    public Node setNamedItem(Node node) throws DOMException {
        return NodeImpl.getImpl(NamedNodeMapImpl.setNamedItemImpl(this.getPeer(), NodeImpl.getPeer(node)));
    }

    static native long setNamedItemImpl(long var0, long var2);

    @Override
    public Node removeNamedItem(String string) throws DOMException {
        return NodeImpl.getImpl(NamedNodeMapImpl.removeNamedItemImpl(this.getPeer(), string));
    }

    static native long removeNamedItemImpl(long var0, String var2);

    @Override
    public Node item(int n) {
        return NodeImpl.getImpl(NamedNodeMapImpl.itemImpl(this.getPeer(), n));
    }

    static native long itemImpl(long var0, int var2);

    @Override
    public Node getNamedItemNS(String string, String string2) {
        return NodeImpl.getImpl(NamedNodeMapImpl.getNamedItemNSImpl(this.getPeer(), string, string2));
    }

    static native long getNamedItemNSImpl(long var0, String var2, String var3);

    @Override
    public Node setNamedItemNS(Node node) throws DOMException {
        return NodeImpl.getImpl(NamedNodeMapImpl.setNamedItemNSImpl(this.getPeer(), NodeImpl.getPeer(node)));
    }

    static native long setNamedItemNSImpl(long var0, long var2);

    @Override
    public Node removeNamedItemNS(String string, String string2) throws DOMException {
        return NodeImpl.getImpl(NamedNodeMapImpl.removeNamedItemNSImpl(this.getPeer(), string, string2));
    }

    static native long removeNamedItemNSImpl(long var0, String var2, String var3);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            NamedNodeMapImpl.dispose(this.peer);
        }
    }
}

