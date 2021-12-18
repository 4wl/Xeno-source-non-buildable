/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSRuleImpl;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;

public class CSSRuleListImpl
implements CSSRuleList {
    private final long peer;

    CSSRuleListImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static CSSRuleList create(long l) {
        if (l == 0L) {
            return null;
        }
        return new CSSRuleListImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof CSSRuleListImpl && this.peer == ((CSSRuleListImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(CSSRuleList cSSRuleList) {
        return cSSRuleList == null ? 0L : ((CSSRuleListImpl)cSSRuleList).getPeer();
    }

    private static native void dispose(long var0);

    static CSSRuleList getImpl(long l) {
        return CSSRuleListImpl.create(l);
    }

    @Override
    public int getLength() {
        return CSSRuleListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public CSSRule item(int n) {
        return CSSRuleImpl.getImpl(CSSRuleListImpl.itemImpl(this.getPeer(), n));
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
            CSSRuleListImpl.dispose(this.peer);
        }
    }
}

