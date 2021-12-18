/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSRuleListImpl;
import com.sun.webkit.dom.MediaListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.stylesheets.MediaList;

public class CSSMediaRuleImpl
extends CSSRuleImpl
implements CSSMediaRule {
    CSSMediaRuleImpl(long l) {
        super(l);
    }

    static CSSMediaRule getImpl(long l) {
        return (CSSMediaRule)CSSMediaRuleImpl.create(l);
    }

    @Override
    public MediaList getMedia() {
        return MediaListImpl.getImpl(CSSMediaRuleImpl.getMediaImpl(this.getPeer()));
    }

    static native long getMediaImpl(long var0);

    @Override
    public CSSRuleList getCssRules() {
        return CSSRuleListImpl.getImpl(CSSMediaRuleImpl.getCssRulesImpl(this.getPeer()));
    }

    static native long getCssRulesImpl(long var0);

    @Override
    public int insertRule(String string, int n) throws DOMException {
        return CSSMediaRuleImpl.insertRuleImpl(this.getPeer(), string, n);
    }

    static native int insertRuleImpl(long var0, String var2, int var3);

    @Override
    public void deleteRule(int n) throws DOMException {
        CSSMediaRuleImpl.deleteRuleImpl(this.getPeer(), n);
    }

    static native void deleteRuleImpl(long var0, int var2);
}

