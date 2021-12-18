/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.dom.CSSPrimitiveValueImpl;
import com.sun.webkit.dom.CSSValueListImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

public class CSSValueImpl
implements CSSValue {
    private final long peer;
    public static final int CSS_INHERIT = 0;
    public static final int CSS_PRIMITIVE_VALUE = 1;
    public static final int CSS_VALUE_LIST = 2;
    public static final int CSS_CUSTOM = 3;

    CSSValueImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static CSSValue create(long l) {
        if (l == 0L) {
            return null;
        }
        switch (CSSValueImpl.getCssValueTypeImpl(l)) {
            case 1: {
                return new CSSPrimitiveValueImpl(l);
            }
            case 2: {
                return new CSSValueListImpl(l);
            }
        }
        return new CSSValueImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof CSSValueImpl && this.peer == ((CSSValueImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(CSSValue cSSValue) {
        return cSSValue == null ? 0L : ((CSSValueImpl)cSSValue).getPeer();
    }

    private static native void dispose(long var0);

    static CSSValue getImpl(long l) {
        return CSSValueImpl.create(l);
    }

    @Override
    public String getCssText() {
        return CSSValueImpl.getCssTextImpl(this.getPeer());
    }

    static native String getCssTextImpl(long var0);

    @Override
    public void setCssText(String string) throws DOMException {
        CSSValueImpl.setCssTextImpl(this.getPeer(), string);
    }

    static native void setCssTextImpl(long var0, String var2);

    @Override
    public short getCssValueType() {
        return CSSValueImpl.getCssValueTypeImpl(this.getPeer());
    }

    static native short getCssValueTypeImpl(long var0);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            CSSValueImpl.dispose(this.peer);
        }
    }
}

