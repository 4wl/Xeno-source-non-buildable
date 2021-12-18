/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

public class AttrImpl
extends NodeImpl
implements Attr {
    AttrImpl(long l) {
        super(l);
    }

    static Attr getImpl(long l) {
        return (Attr)AttrImpl.create(l);
    }

    @Override
    public String getName() {
        return AttrImpl.getNameImpl(this.getPeer());
    }

    static native String getNameImpl(long var0);

    @Override
    public boolean getSpecified() {
        return AttrImpl.getSpecifiedImpl(this.getPeer());
    }

    static native boolean getSpecifiedImpl(long var0);

    @Override
    public String getValue() {
        return AttrImpl.getValueImpl(this.getPeer());
    }

    static native String getValueImpl(long var0);

    @Override
    public void setValue(String string) throws DOMException {
        AttrImpl.setValueImpl(this.getPeer(), string);
    }

    static native void setValueImpl(long var0, String var2);

    @Override
    public Element getOwnerElement() {
        return ElementImpl.getImpl(AttrImpl.getOwnerElementImpl(this.getPeer()));
    }

    static native long getOwnerElementImpl(long var0);

    @Override
    public boolean isId() {
        return AttrImpl.isIdImpl(this.getPeer());
    }

    static native boolean isIdImpl(long var0);

    @Override
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

