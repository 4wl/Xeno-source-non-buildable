/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RenderTarget;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.impl.ps.BaseShaderGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public class ES2Graphics
extends BaseShaderGraphics {
    private final ES2Context context;

    private ES2Graphics(ES2Context eS2Context, RenderTarget renderTarget) {
        super(eS2Context, renderTarget);
        this.context = eS2Context;
    }

    static ES2Graphics create(ES2Context eS2Context, RenderTarget renderTarget) {
        if (renderTarget == null) {
            return null;
        }
        return new ES2Graphics(eS2Context, renderTarget);
    }

    static void clearBuffers(ES2Context eS2Context, Color color, boolean bl, boolean bl2, boolean bl3) {
        eS2Context.getGLContext().clearBuffers(color, bl, bl2, bl3);
    }

    @Override
    public void clearQuad(float f, float f2, float f3, float f4) {
        this.context.setRenderTarget(this);
        this.context.flushVertexBuffer();
        CompositeMode compositeMode = this.getCompositeMode();
        this.context.updateCompositeMode(CompositeMode.CLEAR);
        Paint paint = this.getPaint();
        this.setPaint(Color.BLACK);
        this.fillQuad(f, f2, f3, f4);
        this.context.flushVertexBuffer();
        this.setPaint(paint);
        this.context.updateCompositeMode(compositeMode);
    }

    @Override
    public void clear(Color color) {
        this.context.validateClearOp(this);
        this.getRenderTarget().setOpaque(color.isOpaque());
        ES2Graphics.clearBuffers(this.context, color, true, this.isDepthBuffer(), false);
    }

    @Override
    public void sync() {
        this.context.flushVertexBuffer();
        this.context.getGLContext().finish();
    }

    void forceRenderTarget() {
        this.context.forceRenderTarget(this);
    }

    @Override
    public void transform(BaseTransform baseTransform) {
        if (!GraphicsPipeline.getPipeline().is3DSupported() && !baseTransform.is2D()) {
            return;
        }
        super.transform(baseTransform);
    }

    @Override
    public void translate(float f, float f2, float f3) {
        if (!GraphicsPipeline.getPipeline().is3DSupported() && f3 != 0.0f) {
            return;
        }
        super.translate(f, f2, f3);
    }

    @Override
    public void scale(float f, float f2, float f3) {
        if (!GraphicsPipeline.getPipeline().is3DSupported() && f3 != 1.0f) {
            return;
        }
        super.scale(f, f2, f3);
    }

    @Override
    public void setCamera(NGCamera nGCamera) {
        if (GraphicsPipeline.getPipeline().is3DSupported()) {
            super.setCamera(nGCamera);
        }
    }
}

