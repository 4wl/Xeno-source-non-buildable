/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSRuleImpl;
import com.sun.webkit.dom.CSSValueImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public class CSSStyleDeclarationImpl
implements CSSStyleDeclaration {
    private final long peer;

    CSSStyleDeclarationImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static CSSStyleDeclaration create(long l) {
        if (l == 0L) {
            return null;
        }
        return new CSSStyleDeclarationImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof CSSStyleDeclarationImpl && this.peer == ((CSSStyleDeclarationImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(CSSStyleDeclaration cSSStyleDeclaration) {
        return cSSStyleDeclaration == null ? 0L : ((CSSStyleDeclarationImpl)cSSStyleDeclaration).getPeer();
    }

    private static native void dispose(long var0);

    static CSSStyleDeclaration getImpl(long l) {
        return CSSStyleDeclarationImpl.create(l);
    }

    @Override
    public String getCssText() {
        return CSSStyleDeclarationImpl.getCssTextImpl(this.getPeer());
    }

    static native String getCssTextImpl(long var0);

    @Override
    public void setCssText(String string) throws DOMException {
        CSSStyleDeclarationImpl.setCssTextImpl(this.getPeer(), string);
    }

    static native void setCssTextImpl(long var0, String var2);

    @Override
    public int getLength() {
        return CSSStyleDeclarationImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public CSSRule getParentRule() {
        return CSSRuleImpl.getImpl(CSSStyleDeclarationImpl.getParentRuleImpl(this.getPeer()));
    }

    static native long getParentRuleImpl(long var0);

    @Override
    public String getPropertyValue(String string) {
        return CSSStyleDeclarationImpl.getPropertyValueImpl(this.getPeer(), string);
    }

    static native String getPropertyValueImpl(long var0, String var2);

    @Override
    public CSSValue getPropertyCSSValue(String string) {
        return CSSValueImpl.getImpl(CSSStyleDeclarationImpl.getPropertyCSSValueImpl(this.getPeer(), string));
    }

    static native long getPropertyCSSValueImpl(long var0, String var2);

    @Override
    public String removeProperty(String string) throws DOMException {
        return CSSStyleDeclarationImpl.removePropertyImpl(this.getPeer(), string);
    }

    static native String removePropertyImpl(long var0, String var2);

    @Override
    public String getPropertyPriority(String string) {
        return CSSStyleDeclarationImpl.getPropertyPriorityImpl(this.getPeer(), string);
    }

    static native String getPropertyPriorityImpl(long var0, String var2);

    @Override
    public void setProperty(String string, String string2, String string3) throws DOMException {
        CSSStyleDeclarationImpl.setPropertyImpl(this.getPeer(), string, string2, string3);
    }

    static native void setPropertyImpl(long var0, String var2, String var3, String var4);

    @Override
    public String item(int n) {
        return CSSStyleDeclarationImpl.itemImpl(this.getPeer(), n);
    }

    static native String itemImpl(long var0, int var2);

    public String getPropertyShorthand(String string) {
        return CSSStyleDeclarationImpl.getPropertyShorthandImpl(this.getPeer(), string);
    }

    static native String getPropertyShorthandImpl(long var0, String var2);

    public boolean isPropertyImplicit(String string) {
        return CSSStyleDeclarationImpl.isPropertyImplicitImpl(this.getPeer(), string);
    }

    static native boolean isPropertyImplicitImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            CSSStyleDeclarationImpl.dispose(this.peer);
        }
    }
}

