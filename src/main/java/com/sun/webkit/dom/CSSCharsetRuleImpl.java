/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSCharsetRule;

public class CSSCharsetRuleImpl
extends CSSRuleImpl
implements CSSCharsetRule {
    CSSCharsetRuleImpl(long l) {
        super(l);
    }

    static CSSCharsetRule getImpl(long l) {
        return (CSSCharsetRule)CSSCharsetRuleImpl.create(l);
    }

    @Override
    public String getEncoding() {
        return CSSCharsetRuleImpl.getEncodingImpl(this.getPeer());
    }

    static native String getEncodingImpl(long var0);

    @Override
    public void setEncoding(String string) throws DOMException {
        CSSCharsetRuleImpl.setEncodingImpl(this.getPeer(), string);
    }

    static native void setEncodingImpl(long var0, String var2);
}

