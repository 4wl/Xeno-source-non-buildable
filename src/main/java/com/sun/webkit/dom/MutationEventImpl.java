/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.EventImpl;
import com.sun.webkit.dom.NodeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class MutationEventImpl
extends EventImpl
implements MutationEvent {
    public static final int MODIFICATION = 1;
    public static final int ADDITION = 2;
    public static final int REMOVAL = 3;

    MutationEventImpl(long l) {
        super(l);
    }

    static MutationEvent getImpl(long l) {
        return (MutationEvent)MutationEventImpl.create(l);
    }

    @Override
    public Node getRelatedNode() {
        return NodeImpl.getImpl(MutationEventImpl.getRelatedNodeImpl(this.getPeer()));
    }

    static native long getRelatedNodeImpl(long var0);

    @Override
    public String getPrevValue() {
        return MutationEventImpl.getPrevValueImpl(this.getPeer());
    }

    static native String getPrevValueImpl(long var0);

    @Override
    public String getNewValue() {
        return MutationEventImpl.getNewValueImpl(this.getPeer());
    }

    static native String getNewValueImpl(long var0);

    @Override
    public String getAttrName() {
        return MutationEventImpl.getAttrNameImpl(this.getPeer());
    }

    static native String getAttrNameImpl(long var0);

    @Override
    public short getAttrChange() {
        return MutationEventImpl.getAttrChangeImpl(this.getPeer());
    }

    static native short getAttrChangeImpl(long var0);

    @Override
    public void initMutationEvent(String string, boolean bl, boolean bl2, Node node, String string2, String string3, String string4, short s) {
        MutationEventImpl.initMutationEventImpl(this.getPeer(), string, bl, bl2, NodeImpl.getPeer(node), string2, string3, string4, s);
    }

    static native void initMutationEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, String var7, String var8, String var9, short var10);
}

