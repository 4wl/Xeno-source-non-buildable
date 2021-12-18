/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLPixelFormat;

class X11GLPixelFormat
extends GLPixelFormat {
    private static native long nCreatePixelFormat(long var0, int[] var2);

    X11GLPixelFormat(long l, GLPixelFormat.Attributes attributes) {
        super(l, attributes);
        int[] arrn = new int[]{attributes.getRedSize(), attributes.getGreenSize(), attributes.getBlueSize(), attributes.getAlphaSize(), attributes.getDepthSize(), attributes.isDoubleBuffer() ? 1 : 0, attributes.isOnScreen() ? 1 : 0};
        long l2 = X11GLPixelFormat.nCreatePixelFormat(l, arrn);
        this.setNativePFInfo(l2);
    }
}

