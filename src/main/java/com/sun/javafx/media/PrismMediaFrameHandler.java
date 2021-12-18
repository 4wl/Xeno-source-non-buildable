/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.media;

import com.sun.glass.ui.Screen;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.control.VideoFormat;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.WeakHashMap;

public class PrismMediaFrameHandler
implements ResourceFactoryListener {
    private final Map<Screen, TextureMapEntry> textures = new WeakHashMap<Screen, TextureMapEntry>(1);
    private static Map<Object, PrismMediaFrameHandler> handlers;
    private boolean registeredWithFactory = false;
    private final RenderJob releaseRenderJob = new RenderJob(() -> this.releaseData());

    public static synchronized PrismMediaFrameHandler getHandler(Object object) {
        PrismMediaFrameHandler prismMediaFrameHandler;
        if (object == null) {
            throw new IllegalArgumentException("provider must be non-null");
        }
        if (handlers == null) {
            handlers = new WeakHashMap<Object, PrismMediaFrameHandler>(1);
        }
        if ((prismMediaFrameHandler = handlers.get(object)) == null) {
            prismMediaFrameHandler = new PrismMediaFrameHandler(object);
            handlers.put(object, prismMediaFrameHandler);
        }
        return prismMediaFrameHandler;
    }

    private PrismMediaFrameHandler(Object object) {
    }

    public Texture getTexture(Graphics graphics, VideoDataBuffer videoDataBuffer) {
        Screen screen = graphics.getAssociatedScreen();
        TextureMapEntry textureMapEntry = this.textures.get(screen);
        if (null == videoDataBuffer) {
            if (this.textures.containsKey(screen)) {
                this.textures.remove(screen);
            }
            return null;
        }
        if (null == textureMapEntry) {
            textureMapEntry = new TextureMapEntry();
            this.textures.put(screen, textureMapEntry);
        }
        if (textureMapEntry.texture != null) {
            textureMapEntry.texture.lock();
            if (textureMapEntry.texture.isSurfaceLost()) {
                textureMapEntry.texture = null;
            }
        }
        if (null == textureMapEntry.texture || textureMapEntry.lastFrameTime != videoDataBuffer.getTimestamp()) {
            this.updateTexture(graphics, videoDataBuffer, textureMapEntry);
        }
        return textureMapEntry.texture;
    }

    private void updateTexture(Graphics graphics, VideoDataBuffer videoDataBuffer, TextureMapEntry textureMapEntry) {
        Screen screen = graphics.getAssociatedScreen();
        if (textureMapEntry.texture != null && (textureMapEntry.encodedWidth != videoDataBuffer.getEncodedWidth() || textureMapEntry.encodedHeight != videoDataBuffer.getEncodedHeight())) {
            textureMapEntry.texture.dispose();
            textureMapEntry.texture = null;
        }
        PrismFrameBuffer prismFrameBuffer = new PrismFrameBuffer(videoDataBuffer);
        if (textureMapEntry.texture == null) {
            if (!this.registeredWithFactory) {
                GraphicsPipeline.getDefaultResourceFactory().addFactoryListener(this);
                this.registeredWithFactory = true;
            }
            textureMapEntry.texture = GraphicsPipeline.getPipeline().getResourceFactory(screen).createTexture(prismFrameBuffer);
            textureMapEntry.encodedWidth = videoDataBuffer.getEncodedWidth();
            textureMapEntry.encodedHeight = videoDataBuffer.getEncodedHeight();
        }
        if (textureMapEntry.texture != null) {
            textureMapEntry.texture.update(prismFrameBuffer, false);
        }
        textureMapEntry.lastFrameTime = videoDataBuffer.getTimestamp();
    }

    private void releaseData() {
        for (TextureMapEntry textureMapEntry : this.textures.values()) {
            if (textureMapEntry == null || textureMapEntry.texture == null) continue;
            textureMapEntry.texture.dispose();
        }
        this.textures.clear();
    }

    public void releaseTextures() {
        Toolkit toolkit = Toolkit.getToolkit();
        toolkit.addRenderJob(this.releaseRenderJob);
    }

    @Override
    public void factoryReset() {
        this.releaseData();
    }

    @Override
    public void factoryReleased() {
        this.releaseData();
    }

    private static class TextureMapEntry {
        public double lastFrameTime = -1.0;
        public Texture texture;
        public int encodedWidth;
        public int encodedHeight;

        private TextureMapEntry() {
        }
    }

    private class PrismFrameBuffer
    implements MediaFrame {
        private final PixelFormat videoFormat;
        private final VideoDataBuffer master;

        public PrismFrameBuffer(VideoDataBuffer videoDataBuffer) {
            if (null == videoDataBuffer) {
                throw new NullPointerException();
            }
            this.master = videoDataBuffer;
            switch (this.master.getFormat()) {
                case BGRA_PRE: {
                    this.videoFormat = PixelFormat.INT_ARGB_PRE;
                    break;
                }
                case YCbCr_420p: {
                    this.videoFormat = PixelFormat.MULTI_YCbCr_420;
                    break;
                }
                case YCbCr_422: {
                    this.videoFormat = PixelFormat.BYTE_APPLE_422;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported video format " + (Object)((Object)this.master.getFormat()));
                }
            }
        }

        @Override
        public ByteBuffer getBufferForPlane(int n) {
            return this.master.getBufferForPlane(n);
        }

        @Override
        public void holdFrame() {
            this.master.holdFrame();
        }

        @Override
        public void releaseFrame() {
            this.master.releaseFrame();
        }

        @Override
        public PixelFormat getPixelFormat() {
            return this.videoFormat;
        }

        @Override
        public int getWidth() {
            return this.master.getWidth();
        }

        @Override
        public int getHeight() {
            return this.master.getHeight();
        }

        @Override
        public int getEncodedWidth() {
            return this.master.getEncodedWidth();
        }

        @Override
        public int getEncodedHeight() {
            return this.master.getEncodedHeight();
        }

        @Override
        public int planeCount() {
            return this.master.getPlaneCount();
        }

        @Override
        public int[] planeStrides() {
            return this.master.getPlaneStrides();
        }

        @Override
        public int strideForPlane(int n) {
            return this.master.getStrideForPlane(n);
        }

        @Override
        public MediaFrame convertToFormat(PixelFormat pixelFormat) {
            if (pixelFormat == this.getPixelFormat()) {
                return this;
            }
            if (pixelFormat != PixelFormat.INT_ARGB_PRE) {
                return null;
            }
            VideoDataBuffer videoDataBuffer = this.master.convertToFormat(VideoFormat.BGRA_PRE);
            if (null == videoDataBuffer) {
                return null;
            }
            return new PrismFrameBuffer(videoDataBuffer);
        }
    }
}

