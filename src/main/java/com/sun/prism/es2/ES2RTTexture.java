/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Graphics;
import com.sun.prism.es2.ES2RTTextureData;
import com.sun.prism.es2.ES2RenderTarget;
import com.sun.prism.es2.ES2Texture;
import com.sun.prism.es2.ES2TextureResource;
import com.sun.prism.es2.ES2VramPool;
import com.sun.prism.es2.GLContext;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.PrismTrace;
import java.nio.Buffer;

class ES2RTTexture
extends ES2Texture<ES2RTTextureData>
implements ES2RenderTarget,
RTTexture,
ReadbackRenderTarget {
    private boolean opaque;

    private ES2RTTexture(ES2Context eS2Context, ES2TextureResource<ES2RTTextureData> eS2TextureResource, Texture.WrapMode wrapMode, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        super(eS2Context, eS2TextureResource, PixelFormat.BYTE_BGRA_PRE, wrapMode, n, n2, n3, n4, n5, n6, n7, n8, false);
        PrismTrace.rttCreated((long)((ES2RTTextureData)eS2TextureResource.getResource()).getFboID(), n, n2, PixelFormat.BYTE_BGRA_PRE.getBytesPerPixelUnit());
        this.opaque = false;
    }

    void attachDepthBuffer(ES2Context eS2Context) {
        ES2RTTextureData eS2RTTextureData = (ES2RTTextureData)((ES2TextureResource)this.resource).getResource();
        int n = eS2RTTextureData.getDepthBufferID();
        if (n != 0) {
            return;
        }
        int n2 = this.isMSAA() ? eS2Context.getGLContext().getSampleSize() : 0;
        n = eS2Context.getGLContext().createDepthBuffer(this.getPhysicalWidth(), this.getPhysicalHeight(), n2);
        eS2RTTextureData.setDepthBufferID(n);
    }

    private void createAndAttachMSAABuffer(ES2Context eS2Context) {
        ES2RTTextureData eS2RTTextureData = (ES2RTTextureData)((ES2TextureResource)this.resource).getResource();
        int n = eS2RTTextureData.getMSAARenderBufferID();
        if (n != 0) {
            return;
        }
        GLContext gLContext = eS2Context.getGLContext();
        n = gLContext.createRenderBuffer(this.getPhysicalWidth(), this.getPhysicalHeight(), gLContext.getSampleSize());
        eS2RTTextureData.setMSAARenderBufferID(n);
    }

    static int getCompatibleDimension(ES2Context eS2Context, int n, Texture.WrapMode wrapMode) {
        boolean bl;
        GLContext gLContext = eS2Context.getGLContext();
        switch (wrapMode) {
            case CLAMP_NOT_NEEDED: {
                bl = false;
                break;
            }
            case CLAMP_TO_ZERO: {
                bl = !gLContext.canClampToZero();
                break;
            }
            default: {
                throw new IllegalArgumentException("wrap mode not supported for RT textures: " + (Object)((Object)wrapMode));
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_ZERO_SIMULATED: 
            case REPEAT_SIMULATED: {
                throw new IllegalArgumentException("Cannot request simulated wrap mode: " + (Object)((Object)wrapMode));
            }
        }
        int n2 = bl ? n + 2 : n;
        int n3 = gLContext.getMaxTextureSize();
        int n4 = gLContext.canCreateNonPowTwoTextures() ? (n2 <= n3 ? n2 : 0) : ES2RTTexture.nextPowerOfTwo(n2, n3);
        if (n4 == 0) {
            throw new RuntimeException("Requested texture dimension (" + n + ") " + "requires dimension (" + n4 + ") " + "that exceeds maximum texture size (" + n3 + ")");
        }
        n4 = Math.max(n4, PrismSettings.minRTTSize);
        return bl ? n4 - 2 : n4;
    }

    static ES2RTTexture create(ES2Context eS2Context, int n, int n2, Texture.WrapMode wrapMode, boolean bl) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        boolean bl2;
        GLContext gLContext = eS2Context.getGLContext();
        switch (wrapMode) {
            case CLAMP_NOT_NEEDED: {
                bl2 = false;
                break;
            }
            case CLAMP_TO_ZERO: {
                bl2 = !gLContext.canClampToZero();
                break;
            }
            default: {
                throw new IllegalArgumentException("wrap mode not supported for RT textures: " + (Object)((Object)wrapMode));
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_ZERO_SIMULATED: 
            case REPEAT_SIMULATED: {
                throw new IllegalArgumentException("Cannot request simulated wrap mode: " + (Object)((Object)wrapMode));
            }
        }
        if (bl2) {
            n12 = 1;
            n11 = 1;
            n10 = n + 2;
            n9 = n2 + 2;
            wrapMode = wrapMode.simulatedVersion();
        } else {
            n12 = 0;
            n11 = 0;
            n10 = n;
            n9 = n2;
        }
        int n13 = gLContext.getMaxTextureSize();
        if (gLContext.canCreateNonPowTwoTextures()) {
            n8 = n10 <= n13 ? n10 : 0;
            n7 = n9 <= n13 ? n9 : 0;
        } else {
            n8 = ES2RTTexture.nextPowerOfTwo(n10, n13);
            n7 = ES2RTTexture.nextPowerOfTwo(n9, n13);
        }
        if (n8 == 0 || n7 == 0) {
            throw new RuntimeException("Requested texture dimensions (" + n + "x" + n2 + ") " + "require dimensions (" + n8 + "x" + n7 + ") " + "that exceed maximum texture size (" + n13 + ")");
        }
        ES2VramPool eS2VramPool = ES2VramPool.instance;
        int n14 = PrismSettings.minRTTSize;
        long l = eS2VramPool.estimateRTTextureSize(n8 = Math.max(n8, n14), n7 = Math.max(n7, n14), false);
        if (!eS2VramPool.prepareForAllocation(l)) {
            return null;
        }
        if (bl2) {
            n6 = n8 - 2;
            n5 = n7 - 2;
            n4 = n;
            n3 = n2;
        } else {
            n6 = n8;
            n5 = n7;
            n4 = n;
            n3 = n2;
        }
        gLContext.setActiveTextureUnit(0);
        int n15 = gLContext.getBoundFBO();
        int n16 = gLContext.getBoundTexture();
        int n17 = 0;
        if (!bl) {
            n17 = gLContext.createTexture(n8, n7);
        }
        int n18 = 0;
        if ((n17 != 0 || bl) && (n18 = gLContext.createFBO(n17)) == 0) {
            gLContext.deleteTexture(n17);
            n17 = 0;
        }
        ES2RTTextureData eS2RTTextureData = new ES2RTTextureData(eS2Context, n17, n18, n8, n7, l);
        ES2TextureResource<ES2RTTextureData> eS2TextureResource = new ES2TextureResource<ES2RTTextureData>(eS2RTTextureData);
        ES2RTTexture eS2RTTexture = new ES2RTTexture(eS2Context, eS2TextureResource, wrapMode, n8, n7, n12, n11, n4, n3, n6, n5);
        if (bl) {
            eS2RTTexture.createAndAttachMSAABuffer(eS2Context);
        }
        gLContext.bindFBO(n15);
        gLContext.setBoundTexture(n16);
        return eS2RTTexture;
    }

    @Override
    public Texture getBackBuffer() {
        return this;
    }

    @Override
    public Graphics createGraphics() {
        return ES2Graphics.create(this.context, this);
    }

    @Override
    public int[] getPixels() {
        return null;
    }

    @Override
    public boolean readPixels(Buffer buffer, int n, int n2, int n3, int n4) {
        boolean bl;
        this.context.flushVertexBuffer();
        GLContext gLContext = this.context.getGLContext();
        int n5 = gLContext.getBoundFBO();
        int n6 = this.getFboID();
        boolean bl2 = bl = n5 != n6;
        if (bl) {
            gLContext.bindFBO(n6);
        }
        boolean bl3 = gLContext.readPixels(buffer, n, n2, n3, n4);
        if (bl) {
            gLContext.bindFBO(n5);
        }
        return bl3;
    }

    @Override
    public boolean readPixels(Buffer buffer) {
        return this.readPixels(buffer, this.getContentX(), this.getContentY(), this.getContentWidth(), this.getContentHeight());
    }

    @Override
    public int getFboID() {
        return ((ES2RTTextureData)((ES2TextureResource)this.resource).getResource()).getFboID();
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.context.getAssociatedScreen();
    }

    @Override
    public void update(Image image) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n, int n2) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4, boolean bl) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public boolean isOpaque() {
        return this.opaque;
    }

    @Override
    public void setOpaque(boolean bl) {
        this.opaque = bl;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }

    @Override
    public boolean isMSAA() {
        return ((ES2RTTextureData)((ES2TextureResource)this.resource).getResource()).getMSAARenderBufferID() != 0;
    }
}

