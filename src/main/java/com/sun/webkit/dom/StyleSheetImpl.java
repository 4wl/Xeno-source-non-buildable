/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSStyleSheetImpl;
import com.sun.webkit.dom.MediaListImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.stylesheets.MediaList;
import org.w3c.dom.stylesheets.StyleSheet;

public class StyleSheetImpl
implements StyleSheet {
    private final long peer;
    private static final int TYPE_CSSStyleSheet = 1;

    StyleSheetImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static StyleSheet create(long l) {
        if (l == 0L) {
            return null;
        }
        switch (StyleSheetImpl.getCPPTypeImpl(l)) {
            case 1: {
                return new CSSStyleSheetImpl(l);
            }
        }
        return new StyleSheetImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof StyleSheetImpl && this.peer == ((StyleSheetImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(StyleSheet styleSheet) {
        return styleSheet == null ? 0L : ((StyleSheetImpl)styleSheet).getPeer();
    }

    private static native void dispose(long var0);

    private static native int getCPPTypeImpl(long var0);

    static StyleSheet getImpl(long l) {
        return StyleSheetImpl.create(l);
    }

    @Override
    public String getType() {
        return StyleSheetImpl.getTypeImpl(this.getPeer());
    }

    static native String getTypeImpl(long var0);

    @Override
    public boolean getDisabled() {
        return StyleSheetImpl.getDisabledImpl(this.getPeer());
    }

    static native boolean getDisabledImpl(long var0);

    @Override
    public void setDisabled(boolean bl) {
        StyleSheetImpl.setDisabledImpl(this.getPeer(), bl);
    }

    static native void setDisabledImpl(long var0, boolean var2);

    @Override
    public Node getOwnerNode() {
        return NodeImpl.getImpl(StyleSheetImpl.getOwnerNodeImpl(this.getPeer()));
    }

    static native long getOwnerNodeImpl(long var0);

    @Override
    public StyleSheet getParentStyleSheet() {
        return StyleSheetImpl.getImpl(StyleSheetImpl.getParentStyleSheetImpl(this.getPeer()));
    }

    static native long getParentStyleSheetImpl(long var0);

    @Override
    public String getHref() {
        return StyleSheetImpl.getHrefImpl(this.getPeer());
    }

    static native String getHrefImpl(long var0);

    @Override
    public String getTitle() {
        return StyleSheetImpl.getTitleImpl(this.getPeer());
    }

    static native String getTitleImpl(long var0);

    @Override
    public MediaList getMedia() {
        return MediaListImpl.getImpl(StyleSheetImpl.getMediaImpl(this.getPeer()));
    }

    static native long getMediaImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            StyleSheetImpl.dispose(this.peer);
        }
    }
}

