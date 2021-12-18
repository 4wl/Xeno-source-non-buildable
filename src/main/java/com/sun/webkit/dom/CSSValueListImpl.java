/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CSSValueImpl;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

public class CSSValueListImpl
extends CSSValueImpl
implements CSSValueList {
    CSSValueListImpl(long l) {
        super(l);
    }

    static CSSValueList getImpl(long l) {
        return (CSSValueList)CSSValueListImpl.create(l);
    }

    @Override
    public int getLength() {
        return CSSValueListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public CSSValue item(int n) {
        return CSSValueImpl.getImpl(CSSValueListImpl.itemImpl(this.getPeer(), n));
    }

    static native long itemImpl(long var0, int var2);
}

