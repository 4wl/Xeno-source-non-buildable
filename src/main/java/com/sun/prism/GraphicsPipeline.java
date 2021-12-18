/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.prism.ResourceFactory;
import com.sun.prism.impl.PrismSettings;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class GraphicsPipeline {
    private FontFactory fontFactory;
    protected Map deviceDetails = null;
    private static GraphicsPipeline installedPipeline;

    public abstract boolean init();

    public void dispose() {
        installedPipeline = null;
    }

    public abstract int getAdapterOrdinal(Screen var1);

    public abstract ResourceFactory getResourceFactory(Screen var1);

    public abstract ResourceFactory getDefaultResourceFactory(List<Screen> var1);

    public abstract boolean is3DSupported();

    public boolean isMSAASupported() {
        return false;
    }

    public abstract boolean isVsyncSupported();

    public abstract boolean supportsShaderType(ShaderType var1);

    public abstract boolean supportsShaderModel(ShaderModel var1);

    public boolean supportsShader(ShaderType shaderType, ShaderModel shaderModel) {
        return this.supportsShaderType(shaderType) && this.supportsShaderModel(shaderModel);
    }

    public static ResourceFactory getDefaultResourceFactory() {
        List<Screen> list = Screen.getScreens();
        return GraphicsPipeline.getPipeline().getDefaultResourceFactory(list);
    }

    public FontFactory getFontFactory() {
        if (this.fontFactory == null) {
            this.fontFactory = PrismFontFactory.getFontFactory();
        }
        return this.fontFactory;
    }

    public Map getDeviceDetails() {
        return this.deviceDetails;
    }

    protected void setDeviceDetails(Map map) {
        this.deviceDetails = map;
    }

    public static GraphicsPipeline createPipeline() {
        if (PrismSettings.tryOrder.isEmpty()) {
            if (PrismSettings.verbose) {
                System.out.println("No Prism pipelines specified");
            }
            return null;
        }
        if (installedPipeline != null) {
            throw new IllegalStateException("pipeline already created:" + installedPipeline);
        }
        for (String iterator2 : PrismSettings.tryOrder) {
            if ("j2d".equals(iterator2)) {
                System.err.println("WARNING: The prism-j2d pipeline should not be used as the software");
                System.err.println("fallback pipeline. It is no longer tested nor intended to be used for");
                System.err.println("on-screen rendering. Please use the prism-sw pipeline instead by setting");
                System.err.println("the \"prism.order\" system property to \"sw\" rather than \"j2d\".");
            }
            if (PrismSettings.verbose && ("j2d".equals(iterator2) || "sw".equals(iterator2))) {
                System.err.println("*** Fallback to Prism SW pipeline");
            }
            String string = "com.sun.prism." + iterator2 + "." + iterator2.toUpperCase() + "Pipeline";
            try {
                Method method;
                GraphicsPipeline graphicsPipeline;
                if (PrismSettings.verbose) {
                    System.out.println("Prism pipeline name = " + string);
                }
                Class<?> throwable = Class.forName(string);
                if (PrismSettings.verbose) {
                    System.out.println("(X) Got class = " + throwable);
                }
                if ((graphicsPipeline = (GraphicsPipeline)(method = throwable.getMethod("getInstance", null)).invoke(null, null)) != null && graphicsPipeline.init()) {
                    if (PrismSettings.verbose) {
                        System.out.println("Initialized prism pipeline: " + throwable.getName());
                    }
                    installedPipeline = graphicsPipeline;
                    return installedPipeline;
                }
                if (graphicsPipeline != null) {
                    graphicsPipeline.dispose();
                    graphicsPipeline = null;
                }
                if (!PrismSettings.verbose) continue;
                System.err.println("GraphicsPipeline.createPipeline: error initializing pipeline " + string);
            }
            catch (Throwable throwable) {
                if (!PrismSettings.verbose) continue;
                System.err.println("GraphicsPipeline.createPipeline failed for " + string);
                throwable.printStackTrace();
            }
        }
        StringBuffer stringBuffer = new StringBuffer("Graphics Device initialization failed for :  ");
        Iterator<String> iterator = PrismSettings.tryOrder.iterator();
        if (iterator.hasNext()) {
            stringBuffer.append(iterator.next());
            while (iterator.hasNext()) {
                stringBuffer.append(", ");
                stringBuffer.append(iterator.next());
            }
        }
        System.err.println(stringBuffer);
        return null;
    }

    public static GraphicsPipeline getPipeline() {
        return installedPipeline;
    }

    public boolean isEffectSupported() {
        return true;
    }

    public boolean isUploading() {
        return PrismSettings.forceUploadingPainter;
    }

    public static enum ShaderModel {
        SM3;

    }

    public static enum ShaderType {
        HLSL,
        GLSL;

    }
}

