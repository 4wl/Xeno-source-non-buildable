/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.PlatformUtil;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import com.sun.prism.es2.ES2ResourceFactory;
import com.sun.prism.es2.GLFactory;
import com.sun.prism.es2.GLPixelFormat;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessController;
import java.util.HashMap;
import java.util.List;

public class ES2Pipeline
extends GraphicsPipeline {
    public static final GLFactory glFactory;
    public static final GLPixelFormat.Attributes pixelFormatAttributes;
    static final boolean msaa;
    static final boolean npotSupported;
    private static boolean es2Enabled;
    private static boolean isEglfb;
    private static Thread creator;
    private static final ES2Pipeline theInstance;
    private static ES2ResourceFactory[] factories;
    ES2ResourceFactory _default;

    public static ES2Pipeline getInstance() {
        return theInstance;
    }

    @Override
    public boolean init() {
        if (es2Enabled) {
            HashMap hashMap = new HashMap();
            glFactory.updateDeviceDetails(hashMap);
            this.setDeviceDetails(hashMap);
            if (!PrismSettings.forceGPU) {
                es2Enabled = glFactory.isGLGPUQualify();
                if (PrismSettings.verbose && !es2Enabled) {
                    System.err.println("Failed Graphics Hardware Qualifier check.\nSystem GPU doesn't meet the es2 pipe requirement");
                }
            }
        } else if (PrismSettings.verbose) {
            System.err.println("Failed to initialize ES2 backend: ");
        }
        return es2Enabled;
    }

    private static ES2ResourceFactory getES2ResourceFactory(int n, Screen screen) {
        ES2ResourceFactory eS2ResourceFactory = factories[n];
        if (eS2ResourceFactory == null && screen != null) {
            ES2Pipeline.factories[n] = eS2ResourceFactory = new ES2ResourceFactory(screen);
        }
        return eS2ResourceFactory;
    }

    private static Screen getScreenForAdapter(List<Screen> list, int n) {
        for (Screen screen : list) {
            if (screen.getAdapterOrdinal() != n) continue;
            return screen;
        }
        return Screen.getMainScreen();
    }

    @Override
    public int getAdapterOrdinal(Screen screen) {
        return glFactory.getAdapterOrdinal(screen.getNativeScreen());
    }

    private static ES2ResourceFactory findDefaultResourceFactory(List<Screen> list) {
        int n = glFactory.getAdapterCount();
        for (int i = 0; i != n; ++i) {
            ES2ResourceFactory eS2ResourceFactory = ES2Pipeline.getES2ResourceFactory(i, ES2Pipeline.getScreenForAdapter(list, i));
            if (eS2ResourceFactory != null) {
                if (PrismSettings.verbose) {
                    glFactory.printDriverInformation(i);
                }
                return eS2ResourceFactory;
            }
            if (PrismSettings.disableBadDriverWarning) continue;
            System.err.println("disableBadDriverWarning is unsupported on prism-es2");
        }
        return null;
    }

    @Override
    public ResourceFactory getDefaultResourceFactory(List<Screen> list) {
        if (this._default == null) {
            this._default = ES2Pipeline.findDefaultResourceFactory(list);
        }
        return this._default;
    }

    @Override
    public ResourceFactory getResourceFactory(Screen screen) {
        return ES2Pipeline.getES2ResourceFactory(screen.getAdapterOrdinal(), screen);
    }

    @Override
    public void dispose() {
        if (creator != Thread.currentThread()) {
            throw new IllegalStateException("This operation is not permitted on the current thread [" + Thread.currentThread().getName() + "]");
        }
        if (isEglfb) {
            this._default.dispose();
        }
        super.dispose();
    }

    @Override
    public boolean is3DSupported() {
        return npotSupported;
    }

    @Override
    public final boolean isMSAASupported() {
        return msaa;
    }

    @Override
    public boolean isVsyncSupported() {
        return true;
    }

    @Override
    public boolean supportsShaderType(GraphicsPipeline.ShaderType shaderType) {
        switch (shaderType) {
            case GLSL: {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supportsShaderModel(GraphicsPipeline.ShaderModel shaderModel) {
        switch (shaderModel) {
            case SM3: {
                return true;
            }
        }
        return false;
    }

    static {
        pixelFormatAttributes = new GLPixelFormat.Attributes();
        isEglfb = false;
        AccessController.doPrivileged(() -> {
            String string = "prism_es2";
            String string2 = PlatformUtil.getEmbeddedType();
            if ("eglfb".equals(string2)) {
                isEglfb = true;
                string = "prism_es2_eglfb";
            } else if ("monocle".equals(string2)) {
                isEglfb = true;
                string = "prism_es2_monocle";
            } else if ("eglx11".equals(string2)) {
                string = "prism_es2_eglx11";
            }
            if (PrismSettings.verbose) {
                System.out.println("Loading ES2 native library ... " + string);
            }
            NativeLibLoader.loadLibrary(string);
            if (PrismSettings.verbose) {
                System.out.println("\tsucceeded.");
            }
            return null;
        });
        glFactory = GLFactory.getFactory();
        creator = Thread.currentThread();
        es2Enabled = glFactory != null ? glFactory.initialize(PrismSettings.class, pixelFormatAttributes) : false;
        if (es2Enabled) {
            theInstance = new ES2Pipeline();
            factories = new ES2ResourceFactory[glFactory.getAdapterCount()];
            msaa = glFactory.isGLExtensionSupported("GL_ARB_multisample");
            npotSupported = glFactory.isNPOTSupported();
        } else {
            theInstance = null;
            msaa = false;
            npotSupported = false;
        }
    }
}

