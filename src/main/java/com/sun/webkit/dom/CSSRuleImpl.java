/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSCharsetRuleImpl;
import com.sun.webkit.dom.CSSFontFaceRuleImpl;
import com.sun.webkit.dom.CSSImportRuleImpl;
import com.sun.webkit.dom.CSSMediaRuleImpl;
import com.sun.webkit.dom.CSSPageRuleImpl;
import com.sun.webkit.dom.CSSStyleRuleImpl;
import com.sun.webkit.dom.CSSStyleSheetImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;

public class CSSRuleImpl
implements CSSRule {
    private final long peer;
    public static final int UNKNOWN_RULE = 0;
    public static final int STYLE_RULE = 1;
    public static final int CHARSET_RULE = 2;
    public static final int IMPORT_RULE = 3;
    public static final int MEDIA_RULE = 4;
    public static final int FONT_FACE_RULE = 5;
    public static final int PAGE_RULE = 6;
    public static final int WEBKIT_KEYFRAMES_RULE = 7;
    public static final int WEBKIT_KEYFRAME_RULE = 8;
    public static final int WEBKIT_REGION_RULE = 16;

    CSSRuleImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static CSSRule create(long l) {
        if (l == 0L) {
            return null;
        }
        switch (CSSRuleImpl.getTypeImpl(l)) {
            case 1: {
                return new CSSStyleRuleImpl(l);
            }
            case 2: {
                return new CSSCharsetRuleImpl(l);
            }
            case 3: {
                return new CSSImportRuleImpl(l);
            }
            case 4: {
                return new CSSMediaRuleImpl(l);
            }
            case 5: {
                return new CSSFontFaceRuleImpl(l);
            }
            case 6: {
                return new CSSPageRuleImpl(l);
            }
        }
        return new CSSRuleImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof CSSRuleImpl && this.peer == ((CSSRuleImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(CSSRule cSSRule) {
        return cSSRule == null ? 0L : ((CSSRuleImpl)cSSRule).getPeer();
    }

    private static native void dispose(long var0);

    static CSSRule getImpl(long l) {
        return CSSRuleImpl.create(l);
    }

    @Override
    public short getType() {
        return CSSRuleImpl.getTypeImpl(this.getPeer());
    }

    static native short getTypeImpl(long var0);

    @Override
    public String getCssText() {
        return CSSRuleImpl.getCssTextImpl(this.getPeer());
    }

    static native String getCssTextImpl(long var0);

    @Override
    public void setCssText(String string) throws DOMException {
        CSSRuleImpl.setCssTextImpl(this.getPeer(), string);
    }

    static native void setCssTextImpl(long var0, String var2);

    @Override
    public CSSStyleSheet getParentStyleSheet() {
        return CSSStyleSheetImpl.getImpl(CSSRuleImpl.getParentStyleSheetImpl(this.getPeer()));
    }

    static native long getParentStyleSheetImpl(long var0);

    @Override
    public CSSRule getParentRule() {
        return CSSRuleImpl.getImpl(CSSRuleImpl.getParentRuleImpl(this.getPeer()));
    }

    static native long getParentRuleImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            CSSRuleImpl.dispose(this.peer);
        }
    }
}

