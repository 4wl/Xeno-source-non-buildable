/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLPixelFormat;

class X11GLContext
extends GLContext {
    private static native long nInitialize(long var0, long var2, boolean var4);

    private static native long nGetNativeHandle(long var0);

    private static native void nMakeCurrent(long var0, long var2);

    X11GLContext(long l) {
        this.nativeCtxInfo = l;
    }

    X11GLContext(GLDrawable gLDrawable, GLPixelFormat gLPixelFormat, boolean bl) {
        int[] arrn = new int[7];
        GLPixelFormat.Attributes attributes = gLPixelFormat.getAttributes();
        arrn[0] = attributes.getRedSize();
        arrn[1] = attributes.getGreenSize();
        arrn[2] = attributes.getBlueSize();
        arrn[3] = attributes.getAlphaSize();
        arrn[4] = attributes.getDepthSize();
        arrn[5] = attributes.isDoubleBuffer() ? 1 : 0;
        arrn[6] = attributes.isOnScreen() ? 1 : 0;
        this.nativeCtxInfo = X11GLContext.nInitialize(gLDrawable.getNativeDrawableInfo(), gLPixelFormat.getNativePFInfo(), bl);
    }

    @Override
    long getNativeHandle() {
        return X11GLContext.nGetNativeHandle(this.nativeCtxInfo);
    }

    @Override
    void makeCurrent(GLDrawable gLDrawable) {
        X11GLContext.nMakeCurrent(this.nativeCtxInfo, gLDrawable.getNativeDrawableInfo());
    }
}

