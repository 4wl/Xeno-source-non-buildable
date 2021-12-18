/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.UIEventImpl;
import org.w3c.dom.views.AbstractView;

public class KeyboardEventImpl
extends UIEventImpl {
    public static final int KEY_LOCATION_STANDARD = 0;
    public static final int KEY_LOCATION_LEFT = 1;
    public static final int KEY_LOCATION_RIGHT = 2;
    public static final int KEY_LOCATION_NUMPAD = 3;

    KeyboardEventImpl(long l) {
        super(l);
    }

    static KeyboardEventImpl getImpl(long l) {
        return (KeyboardEventImpl)KeyboardEventImpl.create(l);
    }

    public String getKeyIdentifier() {
        return KeyboardEventImpl.getKeyIdentifierImpl(this.getPeer());
    }

    static native String getKeyIdentifierImpl(long var0);

    public int getLocation() {
        return KeyboardEventImpl.getLocationImpl(this.getPeer());
    }

    static native int getLocationImpl(long var0);

    public int getKeyLocation() {
        return KeyboardEventImpl.getKeyLocationImpl(this.getPeer());
    }

    static native int getKeyLocationImpl(long var0);

    public boolean getCtrlKey() {
        return KeyboardEventImpl.getCtrlKeyImpl(this.getPeer());
    }

    static native boolean getCtrlKeyImpl(long var0);

    public boolean getShiftKey() {
        return KeyboardEventImpl.getShiftKeyImpl(this.getPeer());
    }

    static native boolean getShiftKeyImpl(long var0);

    public boolean getAltKey() {
        return KeyboardEventImpl.getAltKeyImpl(this.getPeer());
    }

    static native boolean getAltKeyImpl(long var0);

    public boolean getMetaKey() {
        return KeyboardEventImpl.getMetaKeyImpl(this.getPeer());
    }

    static native boolean getMetaKeyImpl(long var0);

    public boolean getAltGraphKey() {
        return KeyboardEventImpl.getAltGraphKeyImpl(this.getPeer());
    }

    static native boolean getAltGraphKeyImpl(long var0);

    @Override
    public int getKeyCode() {
        return KeyboardEventImpl.getKeyCodeImpl(this.getPeer());
    }

    static native int getKeyCodeImpl(long var0);

    @Override
    public int getCharCode() {
        return KeyboardEventImpl.getCharCodeImpl(this.getPeer());
    }

    static native int getCharCodeImpl(long var0);

    public boolean getModifierState(String string) {
        return KeyboardEventImpl.getModifierStateImpl(this.getPeer(), string);
    }

    static native boolean getModifierStateImpl(long var0, String var2);

    public void initKeyboardEvent(String string, boolean bl, boolean bl2, AbstractView abstractView, String string2, int n, boolean bl3, boolean bl4, boolean bl5, boolean bl6, boolean bl7) {
        KeyboardEventImpl.initKeyboardEventImpl(this.getPeer(), string, bl, bl2, DOMWindowImpl.getPeer(abstractView), string2, n, bl3, bl4, bl5, bl6, bl7);
    }

    static native void initKeyboardEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, String var7, int var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13);

    public void initKeyboardEventEx(String string, boolean bl, boolean bl2, AbstractView abstractView, String string2, int n, boolean bl3, boolean bl4, boolean bl5, boolean bl6) {
        KeyboardEventImpl.initKeyboardEventExImpl(this.getPeer(), string, bl, bl2, DOMWindowImpl.getPeer(abstractView), string2, n, bl3, bl4, bl5, bl6);
    }

    static native void initKeyboardEventExImpl(long var0, String var2, boolean var3, boolean var4, long var5, String var7, int var8, boolean var9, boolean var10, boolean var11, boolean var12);
}

