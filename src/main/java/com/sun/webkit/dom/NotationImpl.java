/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Notation;

public class NotationImpl
extends NodeImpl
implements Notation {
    NotationImpl(long l) {
        super(l);
    }

    static Notation getImpl(long l) {
        return (Notation)NotationImpl.create(l);
    }

    @Override
    public String getPublicId() {
        return NotationImpl.getPublicIdImpl(this.getPeer());
    }

    static native String getPublicIdImpl(long var0);

    @Override
    public String getSystemId() {
        return NotationImpl.getSystemIdImpl(this.getPeer());
    }

    static native String getSystemIdImpl(long var0);
}

