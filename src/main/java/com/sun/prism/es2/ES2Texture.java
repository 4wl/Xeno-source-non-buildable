/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.javafx.PlatformUtil;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.MultiTexture;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Pipeline;
import com.sun.prism.es2.ES2TextureData;
import com.sun.prism.es2.ES2TextureResource;
import com.sun.prism.es2.ES2VramPool;
import com.sun.prism.es2.GLContext;
import com.sun.prism.impl.BaseTexture;
import com.sun.prism.impl.BufferUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class ES2Texture<T extends ES2TextureData>
extends BaseTexture<ES2TextureResource<T>> {
    final ES2Context context;

    ES2Texture(ES2Context eS2Context, ES2TextureResource<T> eS2TextureResource, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        super(eS2TextureResource, pixelFormat, wrapMode, n, n2, n3, n4, n5, n6, bl);
        this.context = eS2Context;
    }

    ES2Texture(ES2Context eS2Context, ES2TextureResource<T> eS2TextureResource, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        super(eS2TextureResource, pixelFormat, wrapMode, n, n2, n3, n4, n5, n6, n7, n8, bl);
        this.context = eS2Context;
    }

    private ES2Texture(ES2Texture eS2Texture, Texture.WrapMode wrapMode) {
        super(eS2Texture, wrapMode, false);
        this.context = eS2Texture.context;
    }

    @Override
    protected Texture createSharedTexture(Texture.WrapMode wrapMode) {
        return new ES2Texture<T>(this, wrapMode);
    }

    static int nextPowerOfTwo(int n, int n2) {
        int n3;
        if (n > n2) {
            return 0;
        }
        for (n3 = 1; n3 < n; n3 *= 2) {
        }
        return n3;
    }

    static ES2Texture create(ES2Context eS2Context, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n, int n2, boolean bl) {
        long l;
        ES2VramPool eS2VramPool;
        int n3;
        int n4;
        int n5;
        int n6;
        if (!eS2Context.getResourceFactory().isFormatSupported(pixelFormat)) {
            throw new UnsupportedOperationException("Pixel format " + (Object)((Object)pixelFormat) + " not supported on this device");
        }
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format requires multitexturing: " + (Object)((Object)pixelFormat));
        }
        GLContext gLContext = eS2Context.getGLContext();
        switch (wrapMode) {
            case CLAMP_TO_ZERO: {
                if (gLContext.canClampToZero()) break;
                wrapMode = wrapMode.simulatedVersion();
                break;
            }
            case CLAMP_TO_EDGE: 
            case REPEAT: {
                if (gLContext.canCreateNonPowTwoTextures() || (n & n - 1) == 0 && (n2 & n2 - 1) == 0) break;
                wrapMode = wrapMode.simulatedVersion();
                break;
            }
            case CLAMP_NOT_NEEDED: {
                break;
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_ZERO_SIMULATED: 
            case REPEAT_SIMULATED: {
                throw new IllegalArgumentException("Cannot request simulated wrap mode: " + (Object)((Object)wrapMode));
            }
        }
        int n7 = gLContext.getMaxTextureSize();
        int n8 = n;
        int n9 = n2;
        switch (wrapMode) {
            case CLAMP_TO_ZERO_SIMULATED: {
                n6 = 1;
                n5 = 1;
                n4 = n8 + 2;
                n3 = n9 + 2;
                break;
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case REPEAT_SIMULATED: {
                n6 = 0;
                n5 = 0;
                n4 = n8;
                n3 = n9;
                if ((n & n - 1) != 0) {
                    ++n4;
                }
                if ((n2 & n2 - 1) == 0) break;
                ++n3;
                break;
            }
            default: {
                n6 = 0;
                n5 = 0;
                n4 = n8;
                n3 = n9;
            }
        }
        if (n4 > n7 || n3 > n7) {
            throw new RuntimeException("Requested texture dimensions (" + n + "x" + n2 + ") " + "require dimensions (" + n4 + "x" + n3 + ") " + "that exceed maximum texture size (" + n7 + ")");
        }
        if (!gLContext.canCreateNonPowTwoTextures()) {
            n4 = ES2Texture.nextPowerOfTwo(n4, n7);
            n3 = ES2Texture.nextPowerOfTwo(n3, n7);
        }
        if (!(eS2VramPool = ES2VramPool.instance).prepareForAllocation(l = eS2VramPool.estimateTextureSize(n4, n3, pixelFormat))) {
            return null;
        }
        int n10 = gLContext.getBoundTexture();
        ES2TextureData eS2TextureData = new ES2TextureData(eS2Context, gLContext.genAndBindTexture(), n4, n3, l);
        ES2TextureResource<ES2TextureData> eS2TextureResource = new ES2TextureResource<ES2TextureData>(eS2TextureData);
        boolean bl2 = ES2Texture.uploadPixels(gLContext, 50, null, pixelFormat, n4, n3, n6, n5, 0, 0, n8, n9, 0, true, bl);
        gLContext.texParamsMinMax(53, bl);
        gLContext.setBoundTexture(n10);
        if (!bl2) {
            return null;
        }
        return new ES2Texture<ES2TextureData>(eS2Context, eS2TextureResource, pixelFormat, wrapMode, n4, n3, n6, n5, n8, n9, bl);
    }

    public static Texture create(ES2Context eS2Context, MediaFrame mediaFrame) {
        long l;
        ES2VramPool eS2VramPool;
        mediaFrame.holdFrame();
        PixelFormat pixelFormat = mediaFrame.getPixelFormat();
        if (mediaFrame.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            int n = mediaFrame.getEncodedWidth();
            int n2 = mediaFrame.getEncodedHeight();
            int n3 = mediaFrame.planeCount();
            MultiTexture multiTexture = new MultiTexture(pixelFormat, Texture.WrapMode.CLAMP_TO_EDGE, mediaFrame.getWidth(), mediaFrame.getHeight());
            for (int i = 0; i < n3; ++i) {
                ES2Texture eS2Texture;
                int n4 = n;
                int n5 = n2;
                if (i == 2 || i == 1) {
                    n4 /= 2;
                    n5 /= 2;
                }
                if ((eS2Texture = ES2Texture.create(eS2Context, PixelFormat.BYTE_ALPHA, Texture.WrapMode.CLAMP_TO_EDGE, n4, n5, false)) == null) continue;
                multiTexture.setTexture(eS2Texture, i);
            }
            mediaFrame.releaseFrame();
            return multiTexture;
        }
        GLContext gLContext = eS2Context.getGLContext();
        int n = gLContext.getMaxTextureSize();
        int n6 = mediaFrame.getEncodedHeight();
        int n7 = mediaFrame.getEncodedWidth();
        int n8 = n6;
        pixelFormat = mediaFrame.getPixelFormat();
        if (!gLContext.canCreateNonPowTwoTextures()) {
            n7 = ES2Texture.nextPowerOfTwo(n7, n);
            n8 = ES2Texture.nextPowerOfTwo(n8, n);
        }
        if (!(eS2VramPool = ES2VramPool.instance).prepareForAllocation(l = eS2VramPool.estimateTextureSize(n7, n8, pixelFormat))) {
            return null;
        }
        int n9 = gLContext.getBoundTexture();
        ES2TextureData eS2TextureData = new ES2TextureData(eS2Context, gLContext.genAndBindTexture(), n7, n8, l);
        ES2TextureResource<ES2TextureData> eS2TextureResource = new ES2TextureResource<ES2TextureData>(eS2TextureData);
        boolean bl = ES2Texture.uploadPixels(eS2Context.getGLContext(), 50, mediaFrame, n7, n8, true);
        gLContext.texParamsMinMax(53, false);
        gLContext.setBoundTexture(n9);
        ES2Texture<ES2TextureData> eS2Texture = null;
        if (bl) {
            eS2Texture = new ES2Texture<ES2TextureData>(eS2Context, eS2TextureResource, pixelFormat, Texture.WrapMode.CLAMP_TO_EDGE, n7, n8, 0, 0, mediaFrame.getWidth(), mediaFrame.getHeight(), false);
        }
        mediaFrame.releaseFrame();
        return eS2Texture;
    }

    private static boolean uploadPixels(GLContext gLContext, int n, Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, boolean bl2) {
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int n17 = 1;
        boolean bl3 = ES2Pipeline.glFactory.isGL2();
        switch (pixelFormat) {
            case BYTE_BGRA_PRE: 
            case INT_ARGB_PRE: {
                n17 = 4;
                n16 = 40;
                n15 = 41;
                if (!bl3) {
                    if (!PlatformUtil.isIOS()) {
                        if (ES2Pipeline.glFactory.isGLExtensionSupported("GL_EXT_texture_format_BGRA8888")) {
                            n15 = 41;
                            n16 = 41;
                        } else {
                            n15 = 40;
                        }
                    }
                    n14 = 21;
                    break;
                }
                n14 = 22;
                break;
            }
            case BYTE_RGB: {
                n16 = bl3 ? 40 : 42;
                n15 = 42;
                n14 = 21;
                break;
            }
            case BYTE_GRAY: {
                n16 = 43;
                n15 = 43;
                n14 = 21;
                break;
            }
            case BYTE_ALPHA: {
                n16 = 44;
                n15 = 44;
                n14 = 21;
                break;
            }
            case FLOAT_XYZW: {
                n17 = 4;
                n16 = bl3 ? 45 : 40;
                n15 = 40;
                n14 = 20;
                break;
            }
            case BYTE_APPLE_422: {
                n17 = 2;
                n16 = 42;
                n15 = 46;
                n14 = 24;
                break;
            }
            default: {
                throw new InternalError("Image format not supported: " + (Object)((Object)pixelFormat));
            }
        }
        if (!bl3 && n16 != n15 && !PlatformUtil.isIOS()) {
            throw new InternalError("On ES 2.0 device, internalFormat must match pixelFormat");
        }
        boolean bl4 = true;
        if (bl) {
            gLContext.pixelStorei(60, 1);
            if (pixelFormat == PixelFormat.FLOAT_XYZW && n16 == 40) {
                bl4 = gLContext.texImage2D(n, 0, 40, n2, n3, 0, n15, n14, null, bl2);
            } else {
                if (bl3) {
                    n13 = 44;
                    n12 = 21;
                    n11 = 1;
                } else {
                    n13 = n15;
                    n12 = n14;
                    n11 = pixelFormat.getBytesPerPixelUnit();
                }
                ByteBuffer byteBuffer = null;
                if (n8 != n2 || n9 != n3) {
                    int n18 = n2 * n3 * n11;
                    byteBuffer = BufferUtil.newByteBuffer(n18);
                }
                if (bl3) {
                    gLContext.pixelStorei(61, 0);
                    gLContext.pixelStorei(62, 0);
                    gLContext.pixelStorei(63, 0);
                    gLContext.pixelStorei(60, n17);
                }
                bl4 = gLContext.texImage2D(n, 0, n16, n2, n3, 0, n13, n12, byteBuffer, bl2);
            }
        }
        if (buffer != null) {
            n13 = n10 / pixelFormat.getBytesPerPixelUnit();
            if (!(bl3 || n6 == 0 && n7 == 0 && n8 == n13)) {
                buffer = Image.createPackedBuffer(buffer, pixelFormat, n6, n7, n8, n9, n10);
                n7 = 0;
                n6 = 0;
                n10 = n8;
                n13 = n10 / pixelFormat.getBytesPerPixelUnit();
            }
            gLContext.pixelStorei(60, n17);
            if (bl3) {
                if (n8 == n13) {
                    gLContext.pixelStorei(61, 0);
                } else {
                    gLContext.pixelStorei(61, n13);
                }
            }
            n12 = buffer.position();
            n11 = ES2Texture.getBufferElementSizeLog(buffer);
            int n19 = pixelFormat.getBytesPerPixelUnit() >> n11;
            buffer.position(n6 * n19 + n7 * (n10 >> n11));
            gLContext.texSubImage2D(n, 0, n4, n5, n8, n9, n15, n14, buffer);
            buffer.position(n12);
        }
        return bl4;
    }

    private static boolean uploadPixels(GLContext gLContext, int n, MediaFrame mediaFrame, int n2, int n3, boolean bl) {
        int n4;
        int n5;
        int n6;
        int n7;
        mediaFrame.holdFrame();
        int n8 = 1;
        int n9 = mediaFrame.getEncodedWidth();
        int n10 = n7 = mediaFrame.getEncodedHeight();
        ByteBuffer byteBuffer = mediaFrame.getBufferForPlane(0);
        switch (mediaFrame.getPixelFormat()) {
            case INT_ARGB_PRE: {
                n8 = 4;
                n6 = 40;
                n5 = 41;
                if (byteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
                    n4 = 22;
                    break;
                }
                n4 = 23;
                break;
            }
            case BYTE_APPLE_422: {
                n8 = 2;
                n6 = 42;
                n5 = 46;
                n4 = 24;
                break;
            }
            default: {
                mediaFrame.releaseFrame();
                throw new InternalError("Invalid video image format " + (Object)((Object)mediaFrame.getPixelFormat()));
            }
        }
        boolean bl2 = true;
        if (bl) {
            gLContext.pixelStorei(60, 1);
            ByteBuffer byteBuffer2 = null;
            if (n9 != n2 || n10 != n3) {
                int n11 = n2 * n3;
                byteBuffer2 = BufferUtil.newByteBuffer(n11);
            }
            bl2 = gLContext.texImage2D(n, 0, n6, n2, n3, 0, 44, 21, byteBuffer2, false);
        }
        if (byteBuffer != null) {
            gLContext.pixelStorei(60, n8);
            gLContext.pixelStorei(61, mediaFrame.strideForPlane(0) / n8);
            gLContext.texSubImage2D(n, 0, 0, 0, n9, mediaFrame.getHeight(), n5, n4, byteBuffer);
        }
        mediaFrame.releaseFrame();
        return bl2;
    }

    public static int getBufferElementSizeLog(Buffer buffer) {
        if (buffer instanceof ByteBuffer) {
            return 0;
        }
        if (buffer instanceof IntBuffer || buffer instanceof FloatBuffer) {
            return 2;
        }
        throw new InternalError("Unsupported Buffer type: " + buffer.getClass());
    }

    void updateWrapState() {
        Texture.WrapMode wrapMode = this.getWrapMode();
        ES2TextureData eS2TextureData = (ES2TextureData)((ES2TextureResource)this.resource).getResource();
        if (eS2TextureData.getWrapMode() != wrapMode) {
            int n;
            GLContext gLContext = this.context.getGLContext();
            int n2 = gLContext.getBoundTexture();
            if (n2 != (n = eS2TextureData.getTexID())) {
                gLContext.setBoundTexture(n);
            }
            gLContext.updateWrapState(n, wrapMode);
            if (n2 != n) {
                gLContext.setBoundTexture(n2);
            }
            eS2TextureData.setWrapMode(wrapMode);
        }
    }

    void updateFilterState() {
        boolean bl = this.getLinearFiltering();
        ES2TextureData eS2TextureData = (ES2TextureData)((ES2TextureResource)this.resource).getResource();
        if (eS2TextureData.isFiltered() != bl) {
            int n;
            GLContext gLContext = this.context.getGLContext();
            int n2 = gLContext.getBoundTexture();
            if (n2 != (n = eS2TextureData.getTexID())) {
                gLContext.setBoundTexture(n);
            }
            gLContext.updateFilterState(n, bl);
            if (n2 != n) {
                gLContext.setBoundTexture(n2);
            }
            eS2TextureData.setFiltered(bl);
        }
    }

    public int getNativeSourceHandle() {
        return ((ES2TextureData)((ES2TextureResource)this.resource).getResource()).getTexID();
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        int n8;
        this.checkUpdateParams(buffer, pixelFormat, n, n2, n3, n4, n5, n6, n7);
        if (!bl) {
            this.context.flushVertexBuffer();
        }
        if ((n8 = this.getNativeSourceHandle()) != 0) {
            int n9;
            GLContext gLContext = this.context.getGLContext();
            int n10 = gLContext.getActiveTextureUnit();
            int n11 = gLContext.getBoundTexture();
            boolean bl2 = false;
            for (n9 = 0; n9 < 2; ++n9) {
                if (gLContext.getBoundTexture(n9) != n8) continue;
                bl2 = true;
                if (n10 == n9) break;
                gLContext.setActiveTextureUnit(n9);
                break;
            }
            if (!bl2) {
                gLContext.setBoundTexture(n8);
            }
            n9 = this.getContentX();
            int n12 = this.getContentY();
            int n13 = this.getContentWidth();
            int n14 = this.getContentHeight();
            int n15 = this.getPhysicalWidth();
            int n16 = this.getPhysicalHeight();
            boolean bl3 = this.getUseMipmap();
            ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n15, n16, n9 + n, n12 + n2, n3, n4, n5, n6, n7, false, bl3);
            switch (this.getWrapMode()) {
                case CLAMP_TO_EDGE: {
                    break;
                }
                case CLAMP_TO_EDGE_SIMULATED: {
                    boolean bl4;
                    boolean bl5 = n13 < n15 && n + n5 == n13;
                    boolean bl6 = bl4 = n14 < n16 && n2 + n6 == n14;
                    if (bl5) {
                        ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n15, n16, n9 + n13, n12 + n2, n3 + n5 - 1, n4, 1, n6, n7, false, bl3);
                    }
                    if (!bl4) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n15, n16, n9 + n, n12 + n14, n3, n4 + n6 - 1, n5, 1, n7, false, bl3);
                    if (!bl5) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n15, n16, n9 + n13, n12 + n14, n3 + n5 - 1, n4 + n6 - 1, 1, 1, n7, false, bl3);
                    break;
                }
                case REPEAT: {
                    break;
                }
                case REPEAT_SIMULATED: {
                    boolean bl7;
                    boolean bl8 = n13 < n15 && n == 0;
                    boolean bl9 = bl7 = n14 < n16 && n2 == 0;
                    if (bl8) {
                        ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n15, n16, n9 + n13, n12 + n2, n3, n4, 1, n6, n7, false, bl3);
                    }
                    if (!bl7) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n15, n16, n9 + n, n12 + n14, n3, n4, n5, 1, n7, false, bl3);
                    if (!bl8) break;
                    ES2Texture.uploadPixels(gLContext, 50, buffer, pixelFormat, n15, n16, n9 + n13, n12 + n14, n3, n4, 1, 1, n7, false, bl3);
                    break;
                }
            }
            if (n10 != gLContext.getActiveTextureUnit()) {
                gLContext.setActiveTextureUnit(n10);
            }
            if (n11 != gLContext.getBoundTexture()) {
                gLContext.setBoundTexture(n11);
            }
        }
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        int n;
        if (!bl) {
            this.context.flushVertexBuffer();
        }
        if ((n = this.getNativeSourceHandle()) != 0) {
            GLContext gLContext = this.context.getGLContext();
            int n2 = gLContext.getActiveTextureUnit();
            int n3 = gLContext.getBoundTexture();
            boolean bl2 = false;
            for (int i = 0; i < 2; ++i) {
                if (gLContext.getBoundTexture(i) != n) continue;
                bl2 = true;
                if (n2 == i) break;
                gLContext.setActiveTextureUnit(i);
                break;
            }
            if (!bl2) {
                gLContext.setBoundTexture(n);
            }
            ES2Texture.uploadPixels(gLContext, 50, mediaFrame, this.getPhysicalWidth(), this.getPhysicalHeight(), false);
            if (n2 != gLContext.getActiveTextureUnit()) {
                gLContext.setActiveTextureUnit(n2);
            }
            if (n3 != gLContext.getBoundTexture()) {
                gLContext.setBoundTexture(n3);
            }
        }
    }
}

