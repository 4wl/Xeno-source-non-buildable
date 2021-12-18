/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.CompositeMode;
import com.sun.prism.GraphicsResource;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Graphics;
import com.sun.prism.es2.ES2Pipeline;
import com.sun.prism.es2.ES2RenderTarget;
import com.sun.prism.es2.GLContext;
import com.sun.prism.es2.GLDrawable;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.PrismSettings;

class ES2SwapChain
implements ES2RenderTarget,
Presentable,
GraphicsResource {
    private final ES2Context context;
    private final PresentableState pState;
    private GLDrawable drawable;
    private boolean needsResize;
    private boolean opaque = false;
    private int w;
    private int h;
    private float pixelScaleFactor;
    int nativeDestHandle = 0;
    private final boolean msaa;
    private RTTexture stableBackbuffer;
    private boolean copyFullBuffer;

    @Override
    public boolean isOpaque() {
        if (this.stableBackbuffer != null) {
            return this.stableBackbuffer.isOpaque();
        }
        return this.opaque;
    }

    @Override
    public void setOpaque(boolean bl) {
        if (this.stableBackbuffer != null) {
            this.stableBackbuffer.setOpaque(bl);
        } else {
            this.opaque = bl;
        }
    }

    ES2SwapChain(ES2Context eS2Context, PresentableState presentableState) {
        this.context = eS2Context;
        this.pState = presentableState;
        this.pixelScaleFactor = presentableState.getRenderScale();
        this.msaa = presentableState.isMSAA();
        long l = presentableState.getNativeWindow();
        this.drawable = ES2Pipeline.glFactory.createGLDrawable(l, eS2Context.getPixelFormat());
    }

    @Override
    public boolean lockResources(PresentableState presentableState) {
        if (this.pState != presentableState || this.pixelScaleFactor != presentableState.getRenderScale()) {
            return true;
        }
        boolean bl = this.needsResize = this.w != presentableState.getRenderWidth() || this.h != presentableState.getRenderHeight();
        if (this.stableBackbuffer != null && !this.needsResize) {
            this.stableBackbuffer.lock();
            if (this.stableBackbuffer.isSurfaceLost()) {
                this.stableBackbuffer = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean prepare(Rectangle rectangle) {
        try {
            ES2Graphics eS2Graphics = ES2Graphics.create(this.context, this);
            if (this.stableBackbuffer != null) {
                if (this.needsResize) {
                    eS2Graphics.forceRenderTarget();
                    this.needsResize = false;
                }
                this.w = this.pState.getRenderWidth();
                this.h = this.pState.getRenderHeight();
                int n = this.w;
                int n2 = this.h;
                int n3 = this.pState.getOutputWidth();
                int n4 = this.pState.getOutputHeight();
                this.copyFullBuffer = false;
                if (this.isMSAA()) {
                    this.context.flushVertexBuffer();
                    eS2Graphics.blit(this.stableBackbuffer, null, 0, 0, n, n2, 0, n4, n3, 0);
                } else {
                    this.drawTexture(eS2Graphics, this.stableBackbuffer, 0.0f, 0.0f, n3, n4, 0.0f, 0.0f, n, n2);
                }
                this.stableBackbuffer.unlock();
            }
            return this.drawable != null;
        }
        catch (Throwable throwable) {
            if (PrismSettings.verbose) {
                throwable.printStackTrace();
            }
            return false;
        }
    }

    private void drawTexture(ES2Graphics eS2Graphics, RTTexture rTTexture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        CompositeMode compositeMode = eS2Graphics.getCompositeMode();
        if (!this.pState.hasWindowManager()) {
            eS2Graphics.setExtraAlpha(this.pState.getAlpha());
            eS2Graphics.setCompositeMode(CompositeMode.SRC_OVER);
        } else {
            eS2Graphics.setCompositeMode(CompositeMode.SRC);
        }
        eS2Graphics.drawTexture(rTTexture, f, f2, f3, f4, f5, f6, f7, f8);
        this.context.flushVertexBuffer();
        eS2Graphics.setCompositeMode(compositeMode);
    }

    @Override
    public boolean present() {
        boolean bl = this.drawable.swapBuffers(this.context.getGLContext());
        this.context.makeCurrent(null);
        return bl;
    }

    @Override
    public ES2Graphics createGraphics() {
        Object object;
        if (this.drawable.getNativeWindow() != this.pState.getNativeWindow()) {
            this.drawable = ES2Pipeline.glFactory.createGLDrawable(this.pState.getNativeWindow(), this.context.getPixelFormat());
        }
        this.context.makeCurrent(this.drawable);
        this.nativeDestHandle = this.pState.getNativeFrameBuffer();
        if (this.nativeDestHandle == 0) {
            object = this.context.getGLContext();
            this.nativeDestHandle = ((GLContext)object).getBoundFBO();
        }
        boolean bl = this.needsResize = this.w != this.pState.getRenderWidth() || this.h != this.pState.getRenderHeight();
        if (this.stableBackbuffer == null || this.needsResize) {
            if (this.stableBackbuffer != null) {
                this.stableBackbuffer.dispose();
                this.stableBackbuffer = null;
            } else {
                ES2Graphics.create(this.context, this);
            }
            this.w = this.pState.getRenderWidth();
            this.h = this.pState.getRenderHeight();
            object = this.context.getResourceFactory();
            this.stableBackbuffer = object.createRTTexture(this.w, this.h, Texture.WrapMode.CLAMP_NOT_NEEDED, this.msaa);
            if (PrismSettings.dirtyOptsEnabled) {
                this.stableBackbuffer.contentsUseful();
            }
            this.copyFullBuffer = true;
        }
        object = ES2Graphics.create(this.context, this.stableBackbuffer);
        ((BaseGraphics)object).scale(this.pixelScaleFactor, this.pixelScaleFactor);
        return object;
    }

    @Override
    public int getFboID() {
        return this.nativeDestHandle;
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.context.getAssociatedScreen();
    }

    @Override
    public int getPhysicalWidth() {
        return this.pState.getOutputWidth();
    }

    @Override
    public int getPhysicalHeight() {
        return this.pState.getOutputHeight();
    }

    @Override
    public int getContentX() {
        if (PlatformUtil.useEGL()) {
            return this.pState.getWindowX();
        }
        return 0;
    }

    @Override
    public int getContentY() {
        if (PlatformUtil.useEGL()) {
            return this.pState.getScreenHeight() - this.pState.getOutputHeight() - this.pState.getWindowY();
        }
        return 0;
    }

    @Override
    public int getContentWidth() {
        return this.pState.getOutputWidth();
    }

    @Override
    public int getContentHeight() {
        return this.pState.getOutputHeight();
    }

    @Override
    public float getPixelScaleFactor() {
        return this.pixelScaleFactor;
    }

    @Override
    public void dispose() {
        if (this.stableBackbuffer != null) {
            this.stableBackbuffer.dispose();
            this.stableBackbuffer = null;
        }
    }

    @Override
    public boolean isMSAA() {
        return this.stableBackbuffer != null ? this.stableBackbuffer.isMSAA() : this.msaa;
    }
}

