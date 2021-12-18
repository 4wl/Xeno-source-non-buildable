/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Invoker;
import java.security.AccessControlContext;
import java.security.AccessController;
import netscape.javascript.JSException;

class JSObject
extends netscape.javascript.JSObject {
    private static final String UNDEFINED = new String("undefined");
    static final int JS_CONTEXT_OBJECT = 0;
    static final int JS_DOM_NODE_OBJECT = 1;
    static final int JS_DOM_WINDOW_OBJECT = 2;
    private final long peer;
    private final int peer_type;

    JSObject(long l, int n) {
        this.peer = l;
        this.peer_type = n;
    }

    long getPeer() {
        return this.peer;
    }

    @Override
    public Object eval(String string) throws JSException {
        Invoker.getInvoker().checkEventThread();
        return JSObject.evalImpl(this.peer, this.peer_type, string);
    }

    private static native Object evalImpl(long var0, int var2, String var3);

    @Override
    public Object getMember(String string) {
        Invoker.getInvoker().checkEventThread();
        return JSObject.getMemberImpl(this.peer, this.peer_type, string);
    }

    private static native Object getMemberImpl(long var0, int var2, String var3);

    @Override
    public void setMember(String string, Object object) throws JSException {
        Invoker.getInvoker().checkEventThread();
        JSObject.setMemberImpl(this.peer, this.peer_type, string, object, AccessController.getContext());
    }

    private static native void setMemberImpl(long var0, int var2, String var3, Object var4, AccessControlContext var5);

    @Override
    public void removeMember(String string) throws JSException {
        Invoker.getInvoker().checkEventThread();
        JSObject.removeMemberImpl(this.peer, this.peer_type, string);
    }

    private static native void removeMemberImpl(long var0, int var2, String var3);

    @Override
    public Object getSlot(int n) throws JSException {
        Invoker.getInvoker().checkEventThread();
        return JSObject.getSlotImpl(this.peer, this.peer_type, n);
    }

    private static native Object getSlotImpl(long var0, int var2, int var3);

    @Override
    public void setSlot(int n, Object object) throws JSException {
        Invoker.getInvoker().checkEventThread();
        JSObject.setSlotImpl(this.peer, this.peer_type, n, object, AccessController.getContext());
    }

    private static native void setSlotImpl(long var0, int var2, int var3, Object var4, AccessControlContext var5);

    @Override
    public Object call(String string, Object ... arrobject) throws JSException {
        Invoker.getInvoker().checkEventThread();
        return JSObject.callImpl(this.peer, this.peer_type, string, arrobject, AccessController.getContext());
    }

    private static native Object callImpl(long var0, int var2, String var3, Object[] var4, AccessControlContext var5);

    public String toString() {
        Invoker.getInvoker().checkEventThread();
        return JSObject.toStringImpl(this.peer, this.peer_type);
    }

    private static native String toStringImpl(long var0, int var2);

    public boolean equals(Object object) {
        return object == this || object != null && object.getClass() == JSObject.class && this.peer == ((JSObject)object).peer;
    }

    public int hashCode() {
        return (int)(this.peer ^ this.peer >> 17);
    }

    private static JSException fwkMakeException(Object object) {
        String string = object == null ? null : object.toString();
        JSException jSException = new JSException(object == null ? null : object.toString());
        if (object instanceof Throwable) {
            jSException.initCause((Throwable)object);
        }
        return jSException;
    }
}

