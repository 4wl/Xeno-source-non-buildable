/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLFactory;
import com.sun.prism.es2.GLGPUInfo;
import com.sun.prism.es2.GLPixelFormat;
import com.sun.prism.es2.X11GLContext;
import com.sun.prism.es2.X11GLDrawable;
import com.sun.prism.es2.X11GLPixelFormat;
import java.util.HashMap;

class X11GLFactory
extends GLFactory {
    private GLGPUInfo[] preQualificationFilter = new GLGPUInfo[]{new GLGPUInfo("advanced micro devices", null), new GLGPUInfo("ati", null), new GLGPUInfo("intel open source technology center", null), new GLGPUInfo("nvidia", null), new GLGPUInfo("nouveau", null), new GLGPUInfo("x.org", null)};
    private GLGPUInfo[] blackList = new GLGPUInfo[]{new GLGPUInfo("ati", "radeon x1300"), new GLGPUInfo("ati", "radeon x1350"), new GLGPUInfo("ati", "radeon x1400"), new GLGPUInfo("ati", "radeon x1450"), new GLGPUInfo("ati", "radeon x1500"), new GLGPUInfo("ati", "radeon x1550"), new GLGPUInfo("ati", "radeon x1600"), new GLGPUInfo("ati", "radeon x1650"), new GLGPUInfo("ati", "radeon x1700"), new GLGPUInfo("ati", "radeon x1800"), new GLGPUInfo("ati", "radeon x1900"), new GLGPUInfo("ati", "radeon x1950"), new GLGPUInfo("x.org", "amd rv505"), new GLGPUInfo("x.org", "amd rv515"), new GLGPUInfo("x.org", "amd rv516"), new GLGPUInfo("x.org", "amd r520"), new GLGPUInfo("x.org", "amd rv530"), new GLGPUInfo("x.org", "amd rv535"), new GLGPUInfo("x.org", "amd rv560"), new GLGPUInfo("x.org", "amd rv570"), new GLGPUInfo("x.org", "amd r580"), new GLGPUInfo("nvidia", "geforce 6100"), new GLGPUInfo("nvidia", "geforce 6150"), new GLGPUInfo("nvidia", "geforce 6200"), new GLGPUInfo("nvidia", "geforce 6500"), new GLGPUInfo("nvidia", "geforce 6600"), new GLGPUInfo("nvidia", "geforce 6700"), new GLGPUInfo("nvidia", "geforce 6800"), new GLGPUInfo("nvidia", "geforce 7025"), new GLGPUInfo("nvidia", "geforce 7100"), new GLGPUInfo("nvidia", "geforce 7150"), new GLGPUInfo("nvidia", "geforce 7200"), new GLGPUInfo("nvidia", "geforce 7300"), new GLGPUInfo("nvidia", "geforce 7350"), new GLGPUInfo("nvidia", "geforce 7500"), new GLGPUInfo("nvidia", "geforce 7600"), new GLGPUInfo("nvidia", "geforce 7650"), new GLGPUInfo("nvidia", "geforce 7800"), new GLGPUInfo("nvidia", "geforce 7900"), new GLGPUInfo("nvidia", "geforce 7950")};

    X11GLFactory() {
    }

    private static native long nInitialize(int[] var0);

    private static native int nGetAdapterOrdinal(long var0);

    private static native int nGetAdapterCount();

    private static native int nGetDefaultScreen(long var0);

    private static native long nGetDisplay(long var0);

    private static native long nGetVisualID(long var0);

    @Override
    GLGPUInfo[] getPreQualificationFilter() {
        return this.preQualificationFilter;
    }

    @Override
    GLGPUInfo[] getBlackList() {
        return this.blackList;
    }

    @Override
    GLContext createGLContext(long l) {
        return new X11GLContext(l);
    }

    @Override
    GLContext createGLContext(GLDrawable gLDrawable, GLPixelFormat gLPixelFormat, GLContext gLContext, boolean bl) {
        return new X11GLContext(gLDrawable, gLPixelFormat, bl);
    }

    @Override
    GLDrawable createDummyGLDrawable(GLPixelFormat gLPixelFormat) {
        return new X11GLDrawable(gLPixelFormat);
    }

    @Override
    GLDrawable createGLDrawable(long l, GLPixelFormat gLPixelFormat) {
        return new X11GLDrawable(l, gLPixelFormat);
    }

    @Override
    GLPixelFormat createGLPixelFormat(long l, GLPixelFormat.Attributes attributes) {
        return new X11GLPixelFormat(l, attributes);
    }

    @Override
    boolean initialize(Class class_, GLPixelFormat.Attributes attributes) {
        int[] arrn = new int[]{attributes.getRedSize(), attributes.getGreenSize(), attributes.getBlueSize(), attributes.getAlphaSize(), attributes.getDepthSize(), attributes.isDoubleBuffer() ? 1 : 0, attributes.isOnScreen() ? 1 : 0};
        this.nativeCtxInfo = X11GLFactory.nInitialize(arrn);
        if (this.nativeCtxInfo == 0L) {
            return false;
        }
        this.gl2 = true;
        return true;
    }

    @Override
    int getAdapterCount() {
        return X11GLFactory.nGetAdapterCount();
    }

    @Override
    int getAdapterOrdinal(long l) {
        return X11GLFactory.nGetAdapterOrdinal(l);
    }

    @Override
    void updateDeviceDetails(HashMap hashMap) {
        hashMap.put("XVisualID", new Long(X11GLFactory.nGetVisualID(this.nativeCtxInfo)));
        hashMap.put("XDisplay", new Long(X11GLFactory.nGetDisplay(this.nativeCtxInfo)));
        hashMap.put("XScreenID", new Integer(X11GLFactory.nGetDefaultScreen(this.nativeCtxInfo)));
    }
}

