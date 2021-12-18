/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public abstract class PrRenderer
extends Renderer {
    private static final Set<String> intrinsicPeerNames = new HashSet<String>(4);

    protected PrRenderer() {
    }

    public abstract PrDrawable createDrawable(RTTexture var1);

    public static Renderer createRenderer(FilterContext filterContext) {
        boolean bl;
        Object object = filterContext.getReferent();
        if (!(object instanceof Screen)) {
            return null;
        }
        if (((PrFilterContext)filterContext).isForceSoftware()) {
            bl = false;
        } else {
            GraphicsPipeline graphicsPipeline = GraphicsPipeline.getPipeline();
            if (graphicsPipeline == null) {
                return null;
            }
            bl = graphicsPipeline.supportsShaderModel(GraphicsPipeline.ShaderModel.SM3);
        }
        return PrRenderer.createRenderer(filterContext, bl);
    }

    private static PrRenderer createRenderer(FilterContext filterContext, boolean bl) {
        String string = bl ? "com.sun.scenario.effect.impl.prism.ps.PPSRenderer" : "com.sun.scenario.effect.impl.prism.sw.PSWRenderer";
        try {
            Class<?> class_ = Class.forName(string);
            Method method = class_.getMethod("createRenderer", FilterContext.class);
            return (PrRenderer)method.invoke(null, filterContext);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    public static boolean isIntrinsicPeer(String string) {
        return intrinsicPeerNames.contains(string);
    }

    static {
        intrinsicPeerNames.add("Crop");
        intrinsicPeerNames.add("Flood");
        intrinsicPeerNames.add("Merge");
        intrinsicPeerNames.add("Reflection");
    }
}

