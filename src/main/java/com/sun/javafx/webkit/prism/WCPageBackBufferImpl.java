/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCPageBackBuffer;

final class WCPageBackBufferImpl
extends WCPageBackBuffer
implements ResourceFactoryListener {
    private RTTexture texture;
    private boolean listenerAdded = false;
    private float pixelScale;

    WCPageBackBufferImpl(float f) {
        this.pixelScale = f;
    }

    private static RTTexture createTexture(int n, int n2) {
        return GraphicsPipeline.getDefaultResourceFactory().createRTTexture(n, n2, Texture.WrapMode.CLAMP_NOT_NEEDED);
    }

    @Override
    public WCGraphicsContext createGraphics() {
        Graphics graphics = this.texture.createGraphics();
        graphics.scale(this.pixelScale, this.pixelScale);
        return WCGraphicsManager.getGraphicsManager().createGraphicsContext(graphics);
    }

    @Override
    public void disposeGraphics(WCGraphicsContext wCGraphicsContext) {
        wCGraphicsContext.dispose();
    }

    @Override
    public void flush(WCGraphicsContext wCGraphicsContext, int n, int n2, int n3, int n4) {
        int n5 = n + n3;
        int n6 = n2 + n4;
        ((Graphics)wCGraphicsContext.getPlatformGraphics()).drawTexture(this.texture, n, n2, n5, n6, (float)n * this.pixelScale, (float)n2 * this.pixelScale, (float)n5 * this.pixelScale, (float)n6 * this.pixelScale);
        this.texture.unlock();
    }

    @Override
    protected void copyArea(int n, int n2, int n3, int n4, int n5, int n6) {
        n = (int)((float)n * this.pixelScale);
        n2 = (int)((float)n2 * this.pixelScale);
        n3 = (int)Math.ceil((float)n3 * this.pixelScale);
        n4 = (int)Math.ceil((float)n4 * this.pixelScale);
        n5 = (int)((float)n5 * this.pixelScale);
        n6 = (int)((float)n6 * this.pixelScale);
        RTTexture rTTexture = WCPageBackBufferImpl.createTexture(n3, n4);
        rTTexture.createGraphics().drawTexture(this.texture, 0.0f, 0.0f, n3, n4, n, n2, n + n3, n2 + n4);
        this.texture.createGraphics().drawTexture(rTTexture, n + n5, n2 + n6, n + n3 + n5, n2 + n4 + n6, 0.0f, 0.0f, n3, n4);
        rTTexture.dispose();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean validate(int n, int n2) {
        n = (int)Math.ceil((float)n * this.pixelScale);
        n2 = (int)Math.ceil((float)n2 * this.pixelScale);
        if (this.texture != null) {
            this.texture.lock();
            if (this.texture.isSurfaceLost()) {
                this.texture.dispose();
                this.texture = null;
            }
        }
        if (this.texture == null) {
            this.texture = WCPageBackBufferImpl.createTexture(n, n2);
            this.texture.contentsUseful();
            if (!this.listenerAdded) {
                GraphicsPipeline.getDefaultResourceFactory().addFactoryListener(this);
                this.listenerAdded = true;
                return true;
            }
            this.texture.unlock();
            return false;
        }
        int n3 = this.texture.getContentWidth();
        int n4 = this.texture.getContentHeight();
        if (n3 == n) {
            if (n4 == n2) return true;
        }
        RTTexture rTTexture = WCPageBackBufferImpl.createTexture(n, n2);
        rTTexture.contentsUseful();
        rTTexture.createGraphics().drawTexture(this.texture, 0.0f, 0.0f, Math.min(n, n3), Math.min(n2, n4));
        this.texture.dispose();
        this.texture = rTTexture;
        return true;
    }

    @Override
    public void factoryReset() {
        if (this.texture != null) {
            this.texture.dispose();
            this.texture = null;
        }
    }

    @Override
    public void factoryReleased() {
    }
}

