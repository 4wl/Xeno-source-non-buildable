/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.EntityReference;

public class EntityReferenceImpl
extends NodeImpl
implements EntityReference {
    EntityReferenceImpl(long l) {
        super(l);
    }

    static EntityReference getImpl(long l) {
        return (EntityReference)EntityReferenceImpl.create(l);
    }
}

