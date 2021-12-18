/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.UIEventImpl;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.views.AbstractView;

public class MouseEventImpl
extends UIEventImpl
implements MouseEvent {
    MouseEventImpl(long l) {
        super(l);
    }

    static MouseEvent getImpl(long l) {
        return (MouseEvent)MouseEventImpl.create(l);
    }

    @Override
    public int getScreenX() {
        return MouseEventImpl.getScreenXImpl(this.getPeer());
    }

    static native int getScreenXImpl(long var0);

    @Override
    public int getScreenY() {
        return MouseEventImpl.getScreenYImpl(this.getPeer());
    }

    static native int getScreenYImpl(long var0);

    @Override
    public int getClientX() {
        return MouseEventImpl.getClientXImpl(this.getPeer());
    }

    static native int getClientXImpl(long var0);

    @Override
    public int getClientY() {
        return MouseEventImpl.getClientYImpl(this.getPeer());
    }

    static native int getClientYImpl(long var0);

    @Override
    public boolean getCtrlKey() {
        return MouseEventImpl.getCtrlKeyImpl(this.getPeer());
    }

    static native boolean getCtrlKeyImpl(long var0);

    @Override
    public boolean getShiftKey() {
        return MouseEventImpl.getShiftKeyImpl(this.getPeer());
    }

    static native boolean getShiftKeyImpl(long var0);

    @Override
    public boolean getAltKey() {
        return MouseEventImpl.getAltKeyImpl(this.getPeer());
    }

    static native boolean getAltKeyImpl(long var0);

    @Override
    public boolean getMetaKey() {
        return MouseEventImpl.getMetaKeyImpl(this.getPeer());
    }

    static native boolean getMetaKeyImpl(long var0);

    @Override
    public short getButton() {
        return MouseEventImpl.getButtonImpl(this.getPeer());
    }

    static native short getButtonImpl(long var0);

    @Override
    public EventTarget getRelatedTarget() {
        return (EventTarget)((Object)NodeImpl.getImpl(MouseEventImpl.getRelatedTargetImpl(this.getPeer())));
    }

    static native long getRelatedTargetImpl(long var0);

    public int getOffsetX() {
        return MouseEventImpl.getOffsetXImpl(this.getPeer());
    }

    static native int getOffsetXImpl(long var0);

    public int getOffsetY() {
        return MouseEventImpl.getOffsetYImpl(this.getPeer());
    }

    static native int getOffsetYImpl(long var0);

    public int getX() {
        return MouseEventImpl.getXImpl(this.getPeer());
    }

    static native int getXImpl(long var0);

    public int getY() {
        return MouseEventImpl.getYImpl(this.getPeer());
    }

    static native int getYImpl(long var0);

    public Node getFromElement() {
        return NodeImpl.getImpl(MouseEventImpl.getFromElementImpl(this.getPeer()));
    }

    static native long getFromElementImpl(long var0);

    public Node getToElement() {
        return NodeImpl.getImpl(MouseEventImpl.getToElementImpl(this.getPeer()));
    }

    static native long getToElementImpl(long var0);

    @Override
    public void initMouseEvent(String string, boolean bl, boolean bl2, AbstractView abstractView, int n, int n2, int n3, int n4, int n5, boolean bl3, boolean bl4, boolean bl5, boolean bl6, short s, EventTarget eventTarget) {
        MouseEventImpl.initMouseEventImpl(this.getPeer(), string, bl, bl2, DOMWindowImpl.getPeer(abstractView), n, n2, n3, n4, n5, bl3, bl4, bl5, bl6, s, NodeImpl.getPeer((NodeImpl)eventTarget));
    }

    static native void initMouseEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, int var7, int var8, int var9, int var10, int var11, boolean var12, boolean var13, boolean var14, boolean var15, short var16, long var17);
}

