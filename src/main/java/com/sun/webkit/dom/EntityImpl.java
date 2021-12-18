/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Entity;

public class EntityImpl
extends NodeImpl
implements Entity {
    EntityImpl(long l) {
        super(l);
    }

    static Entity getImpl(long l) {
        return (Entity)EntityImpl.create(l);
    }

    @Override
    public String getPublicId() {
        return EntityImpl.getPublicIdImpl(this.getPeer());
    }

    static native String getPublicIdImpl(long var0);

    @Override
    public String getSystemId() {
        return EntityImpl.getSystemIdImpl(this.getPeer());
    }

    static native String getSystemIdImpl(long var0);

    @Override
    public String getNotationName() {
        return EntityImpl.getNotationNameImpl(this.getPeer());
    }

    static native String getNotationNameImpl(long var0);

    @Override
    public String getInputEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getXmlVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getXmlEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

