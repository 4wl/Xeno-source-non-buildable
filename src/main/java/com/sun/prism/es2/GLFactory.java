/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.javafx.PlatformUtil;
import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.es2.GLGPUInfo;
import com.sun.prism.es2.GLPixelFormat;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;

abstract class GLFactory {
    private static final GLFactory platformFactory;
    long nativeCtxInfo;
    boolean gl2 = false;
    private GLContext shareCtx = null;

    private static native boolean nIsGLExtensionSupported(long var0, String var2);

    private static native String nGetGLVendor(long var0);

    private static native String nGetGLRenderer(long var0);

    private static native String nGetGLVersion(long var0);

    GLFactory() {
    }

    static GLFactory getFactory() throws RuntimeException {
        if (null != platformFactory) {
            return platformFactory;
        }
        throw new RuntimeException("No native platform GLFactory available.");
    }

    abstract GLGPUInfo[] getPreQualificationFilter();

    abstract GLGPUInfo[] getBlackList();

    private static GLGPUInfo readGPUInfo(long l) {
        String string = GLFactory.nGetGLVendor(l);
        String string2 = GLFactory.nGetGLRenderer(l);
        return new GLGPUInfo(string.toLowerCase(), string2.toLowerCase());
    }

    private static boolean matches(GLGPUInfo gLGPUInfo, GLGPUInfo[] arrgLGPUInfo) {
        if (arrgLGPUInfo != null) {
            for (int i = 0; i < arrgLGPUInfo.length; ++i) {
                if (!gLGPUInfo.matches(arrgLGPUInfo[i])) continue;
                return true;
            }
        }
        return false;
    }

    private boolean inPreQualificationFilter(GLGPUInfo gLGPUInfo) {
        GLGPUInfo[] arrgLGPUInfo = this.getPreQualificationFilter();
        if (arrgLGPUInfo == null) {
            return true;
        }
        return GLFactory.matches(gLGPUInfo, arrgLGPUInfo);
    }

    private boolean inBlackList(GLGPUInfo gLGPUInfo) {
        return GLFactory.matches(gLGPUInfo, this.getBlackList());
    }

    boolean isQualified(long l) {
        GLGPUInfo gLGPUInfo = GLFactory.readGPUInfo(l);
        if (gLGPUInfo.vendor == null || gLGPUInfo.model == null || gLGPUInfo.vendor.contains("unknown") || gLGPUInfo.model.contains("unknown")) {
            return false;
        }
        return this.inPreQualificationFilter(gLGPUInfo) && !this.inBlackList(gLGPUInfo);
    }

    abstract GLContext createGLContext(long var1);

    abstract GLContext createGLContext(GLDrawable var1, GLPixelFormat var2, GLContext var3, boolean var4);

    abstract GLDrawable createGLDrawable(long var1, GLPixelFormat var3);

    abstract GLDrawable createDummyGLDrawable(GLPixelFormat var1);

    abstract GLPixelFormat createGLPixelFormat(long var1, GLPixelFormat.Attributes var3);

    boolean isGLGPUQualify() {
        return this.isQualified(this.nativeCtxInfo);
    }

    abstract boolean initialize(Class var1, GLPixelFormat.Attributes var2);

    GLContext getShareContext() {
        if (this.shareCtx == null) {
            this.shareCtx = this.createGLContext(this.nativeCtxInfo);
        }
        return this.shareCtx;
    }

    boolean isGL2() {
        return this.gl2;
    }

    boolean isGLExtensionSupported(String string) {
        return GLFactory.nIsGLExtensionSupported(this.nativeCtxInfo, string);
    }

    boolean isNPOTSupported() {
        return this.isGLExtensionSupported("GL_ARB_texture_non_power_of_two") || this.isGLExtensionSupported("GL_OES_texture_npot");
    }

    abstract int getAdapterCount();

    abstract int getAdapterOrdinal(long var1);

    abstract void updateDeviceDetails(HashMap var1);

    void printDriverInformation(int n) {
        System.out.println("Graphics Vendor: " + GLFactory.nGetGLVendor(this.nativeCtxInfo));
        System.out.println("       Renderer: " + GLFactory.nGetGLRenderer(this.nativeCtxInfo));
        System.out.println("        Version: " + GLFactory.nGetGLVersion(this.nativeCtxInfo));
    }

    static {
        String string;
        if (PlatformUtil.isUnix()) {
            string = "eglx11".equals(PlatformUtil.getEmbeddedType()) ? "com.sun.prism.es2.EGLX11GLFactory" : ("eglfb".equals(PlatformUtil.getEmbeddedType()) ? "com.sun.prism.es2.EGLFBGLFactory" : ("monocle".equals(PlatformUtil.getEmbeddedType()) ? "com.sun.prism.es2.MonocleGLFactory" : "com.sun.prism.es2.X11GLFactory"));
        } else if (PlatformUtil.isWindows()) {
            string = "com.sun.prism.es2.WinGLFactory";
        } else if (PlatformUtil.isMac()) {
            string = "com.sun.prism.es2.MacGLFactory";
        } else if (PlatformUtil.isIOS()) {
            string = "com.sun.prism.es2.IOSGLFactory";
        } else if (PlatformUtil.isAndroid()) {
            if ("eglfb".equals(PlatformUtil.getEmbeddedType())) {
                string = "com.sun.prism.es2.EGLFBGLFactory";
            } else if ("monocle".equals(PlatformUtil.getEmbeddedType())) {
                string = "com.sun.prism.es2.MonocleGLFactory";
            } else {
                string = null;
                System.err.println("GLFactory.static - Only eglfb supported for Android!");
            }
        } else {
            string = null;
            System.err.println("GLFactory.static - No Platform Factory for: " + System.getProperty("os.name"));
        }
        if (PrismSettings.verbose) {
            System.out.println("GLFactory using " + string);
        }
        platformFactory = string == null ? null : AccessController.doPrivileged(new FactoryLoader(string));
    }

    private static class FactoryLoader
    implements PrivilegedAction<GLFactory> {
        private final String factoryClassName;

        FactoryLoader(String string) {
            this.factoryClassName = string;
        }

        @Override
        public GLFactory run() {
            GLFactory gLFactory = null;
            try {
                gLFactory = (GLFactory)Class.forName(this.factoryClassName).newInstance();
            }
            catch (Throwable throwable) {
                System.err.println("GLFactory.static - Platform: " + System.getProperty("os.name") + " - not available: " + this.factoryClassName);
                throwable.printStackTrace();
            }
            return gLFactory;
        }
    }
}

