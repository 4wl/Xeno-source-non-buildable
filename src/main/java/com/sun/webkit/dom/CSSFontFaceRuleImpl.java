/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSStyleDeclarationImpl;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSFontFaceRuleImpl
extends CSSRuleImpl
implements CSSFontFaceRule {
    CSSFontFaceRuleImpl(long l) {
        super(l);
    }

    static CSSFontFaceRule getImpl(long l) {
        return (CSSFontFaceRule)CSSFontFaceRuleImpl.create(l);
    }

    @Override
    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(CSSFontFaceRuleImpl.getStyleImpl(this.getPeer()));
    }

    static native long getStyleImpl(long var0);
}

