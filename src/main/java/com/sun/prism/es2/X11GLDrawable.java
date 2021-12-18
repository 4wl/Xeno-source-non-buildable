/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLPixelFormat;

class X11GLDrawable
extends GLDrawable {
    private static native long nCreateDrawable(long var0, long var2);

    private static native long nGetDummyDrawable(long var0);

    private static native boolean nSwapBuffers(long var0);

    X11GLDrawable(GLPixelFormat gLPixelFormat) {
        super(0L, gLPixelFormat);
        long l = X11GLDrawable.nGetDummyDrawable(gLPixelFormat.getNativePFInfo());
        this.setNativeDrawableInfo(l);
    }

    X11GLDrawable(long l, GLPixelFormat gLPixelFormat) {
        super(l, gLPixelFormat);
        long l2 = X11GLDrawable.nCreateDrawable(l, gLPixelFormat.getNativePFInfo());
        this.setNativeDrawableInfo(l2);
    }

    @Override
    boolean swapBuffers(GLContext gLContext) {
        return X11GLDrawable.nSwapBuffers(this.getNativeDrawableInfo());
    }
}

