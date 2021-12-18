/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSRuleListImpl;
import com.sun.webkit.dom.StyleSheetImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

public class CSSStyleSheetImpl
extends StyleSheetImpl
implements CSSStyleSheet {
    CSSStyleSheetImpl(long l) {
        super(l);
    }

    static CSSStyleSheet getImpl(long l) {
        return (CSSStyleSheet)CSSStyleSheetImpl.create(l);
    }

    @Override
    public CSSRule getOwnerRule() {
        return CSSRuleImpl.getImpl(CSSStyleSheetImpl.getOwnerRuleImpl(this.getPeer()));
    }

    static native long getOwnerRuleImpl(long var0);

    @Override
    public CSSRuleList getCssRules() {
        return CSSRuleListImpl.getImpl(CSSStyleSheetImpl.getCssRulesImpl(this.getPeer()));
    }

    static native long getCssRulesImpl(long var0);

    public CSSRuleList getRules() {
        return CSSRuleListImpl.getImpl(CSSStyleSheetImpl.getRulesImpl(this.getPeer()));
    }

    static native long getRulesImpl(long var0);

    @Override
    public int insertRule(String string, int n) throws DOMException {
        return CSSStyleSheetImpl.insertRuleImpl(this.getPeer(), string, n);
    }

    static native int insertRuleImpl(long var0, String var2, int var3);

    @Override
    public void deleteRule(int n) throws DOMException {
        CSSStyleSheetImpl.deleteRuleImpl(this.getPeer(), n);
    }

    static native void deleteRuleImpl(long var0, int var2);

    public int addRule(String string, String string2, int n) throws DOMException {
        return CSSStyleSheetImpl.addRuleImpl(this.getPeer(), string, string2, n);
    }

    static native int addRuleImpl(long var0, String var2, String var3, int var4);

    public void removeRule(int n) throws DOMException {
        CSSStyleSheetImpl.removeRuleImpl(this.getPeer(), n);
    }

    static native void removeRuleImpl(long var0, int var2);
}

