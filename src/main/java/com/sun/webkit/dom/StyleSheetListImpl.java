/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.StyleSheetImpl;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

public class StyleSheetListImpl
implements StyleSheetList {
    private final long peer;

    StyleSheetListImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static StyleSheetList create(long l) {
        if (l == 0L) {
            return null;
        }
        return new StyleSheetListImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof StyleSheetListImpl && this.peer == ((StyleSheetListImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(StyleSheetList styleSheetList) {
        return styleSheetList == null ? 0L : ((StyleSheetListImpl)styleSheetList).getPeer();
    }

    private static native void dispose(long var0);

    static StyleSheetList getImpl(long l) {
        return StyleSheetListImpl.create(l);
    }

    @Override
    public int getLength() {
        return StyleSheetListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public StyleSheet item(int n) {
        return StyleSheetImpl.getImpl(StyleSheetListImpl.itemImpl(this.getPeer(), n));
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
            StyleSheetListImpl.dispose(this.peer);
        }
    }
}

