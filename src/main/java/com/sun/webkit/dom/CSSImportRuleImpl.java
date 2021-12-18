/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSStyleSheetImpl;
import com.sun.webkit.dom.MediaListImpl;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;

public class CSSImportRuleImpl
extends CSSRuleImpl
implements CSSImportRule {
    CSSImportRuleImpl(long l) {
        super(l);
    }

    static CSSImportRule getImpl(long l) {
        return (CSSImportRule)CSSImportRuleImpl.create(l);
    }

    @Override
    public String getHref() {
        return CSSImportRuleImpl.getHrefImpl(this.getPeer());
    }

    static native String getHrefImpl(long var0);

    @Override
    public MediaList getMedia() {
        return MediaListImpl.getImpl(CSSImportRuleImpl.getMediaImpl(this.getPeer()));
    }

    static native long getMediaImpl(long var0);

    @Override
    public CSSStyleSheet getStyleSheet() {
        return CSSStyleSheetImpl.getImpl(CSSImportRuleImpl.getStyleSheetImpl(this.getPeer()));
    }

    static native long getStyleSheetImpl(long var0);
}

