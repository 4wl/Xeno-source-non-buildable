/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.XPathResultImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathResult;

public class XPathExpressionImpl
implements XPathExpression {
    private final long peer;

    XPathExpressionImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static XPathExpression create(long l) {
        if (l == 0L) {
            return null;
        }
        return new XPathExpressionImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof XPathExpressionImpl && this.peer == ((XPathExpressionImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(XPathExpression xPathExpression) {
        return xPathExpression == null ? 0L : ((XPathExpressionImpl)xPathExpression).getPeer();
    }

    private static native void dispose(long var0);

    static XPathExpression getImpl(long l) {
        return XPathExpressionImpl.create(l);
    }

    @Override
    public Object evaluate(Node node, short s, Object object) throws DOMException {
        return this.evaluate(node, s, (XPathResult)object);
    }

    public XPathResult evaluate(Node node, short s, XPathResult xPathResult) throws DOMException {
        return XPathResultImpl.getImpl(XPathExpressionImpl.evaluateImpl(this.getPeer(), NodeImpl.getPeer(node), s, XPathResultImpl.getPeer(xPathResult)));
    }

    static native long evaluateImpl(long var0, long var2, short var4, long var5);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            XPathExpressionImpl.dispose(this.peer);
        }
    }
}

