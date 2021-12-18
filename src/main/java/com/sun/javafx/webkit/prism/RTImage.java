/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.webkit.prism.PrismGraphicsManager;
import com.sun.javafx.webkit.prism.PrismImage;
import com.sun.javafx.webkit.prism.PrismInvoker;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

final class RTImage
extends PrismImage
implements ResourceFactoryListener {
    private RTTexture txt;
    private final int width;
    private final int height;
    private boolean listenerAdded = false;
    private ByteBuffer pixelBuffer;
    private float pixelScale;

    RTImage(int n, int n2, float f) {
        this.width = n;
        this.height = n2;
        this.pixelScale = f;
    }

    @Override
    Image getImage() {
        return Image.fromByteBgraPreData(this.getPixelBuffer(), this.getWidth(), this.getHeight());
    }

    @Override
    Graphics getGraphics() {
        Graphics graphics = this.getTexture().createGraphics();
        graphics.transform(PrismGraphicsManager.getPixelScaleTransform());
        return graphics;
    }

    private RTTexture getTexture() {
        if (this.txt == null) {
            ResourceFactory resourceFactory = GraphicsPipeline.getDefaultResourceFactory();
            this.txt = resourceFactory.createRTTexture((int)Math.ceil((float)this.width * this.pixelScale), (int)Math.ceil((float)this.height * this.pixelScale), Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.txt.contentsUseful();
            this.txt.makePermanent();
            if (!this.listenerAdded) {
                resourceFactory.addFactoryListener(this);
                this.listenerAdded = true;
            }
        }
        return this.txt;
    }

    @Override
    void draw(Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (this.txt == null && graphics.getCompositeMode() == CompositeMode.SRC_OVER) {
            return;
        }
        if (graphics instanceof PrinterGraphics) {
            int n9 = n7 - n5;
            int n10 = n8 - n6;
            IntBuffer intBuffer = IntBuffer.allocate(n9 * n10);
            PrismInvoker.runOnRenderThread(() -> this.getTexture().readPixels(intBuffer));
            Image image = Image.fromIntArgbPreData(intBuffer, n9, n10);
            Texture texture = graphics.getResourceFactory().createTexture(image, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED);
            graphics.drawTexture(texture, n, n2, n3, n4, 0.0f, 0.0f, n9, n10);
            texture.dispose();
        } else if (this.txt == null) {
            Paint paint = graphics.getPaint();
            graphics.setPaint(Color.TRANSPARENT);
            graphics.fillQuad(n, n2, n3, n4);
            graphics.setPaint(paint);
        } else {
            graphics.drawTexture(this.txt, n, n2, n3, n4, (float)n5 * this.pixelScale, (float)n6 * this.pixelScale, (float)n7 * this.pixelScale, (float)n8 * this.pixelScale);
        }
    }

    @Override
    void dispose() {
        PrismInvoker.invokeOnRenderThread(() -> {
            if (this.txt != null) {
                this.txt.dispose();
                this.txt = null;
            }
        });
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public ByteBuffer getPixelBuffer() {
        boolean bl = false;
        if (this.pixelBuffer == null) {
            this.pixelBuffer = ByteBuffer.allocateDirect(this.width * this.height * 4);
            if (this.pixelBuffer != null) {
                this.pixelBuffer.order(ByteOrder.nativeOrder());
                bl = true;
            }
        }
        if (bl || this.isDirty()) {
            PrismInvoker.runOnRenderThread(() -> {
                this.flushRQ();
                if (this.txt != null && this.pixelBuffer != null) {
                    Object object;
                    PixelFormat pixelFormat = this.txt.getPixelFormat();
                    if (pixelFormat != PixelFormat.INT_ARGB_PRE && pixelFormat != PixelFormat.BYTE_BGRA_PRE) {
                        throw new AssertionError((Object)("Unexpected pixel format: " + (Object)((Object)pixelFormat)));
                    }
                    RTTexture rTTexture = this.txt;
                    if (this.pixelScale != 1.0f) {
                        object = GraphicsPipeline.getDefaultResourceFactory();
                        rTTexture = object.createRTTexture(this.width, this.height, Texture.WrapMode.CLAMP_NOT_NEEDED);
                        Graphics graphics = rTTexture.createGraphics();
                        graphics.drawTexture(this.txt, 0.0f, 0.0f, this.width, this.height, 0.0f, 0.0f, (float)this.width * this.pixelScale, (float)this.height * this.pixelScale);
                    }
                    this.pixelBuffer.rewind();
                    object = rTTexture.getPixels();
                    if (object != null) {
                        this.pixelBuffer.asIntBuffer().put((int[])object);
                    } else {
                        rTTexture.readPixels(this.pixelBuffer);
                    }
                    if (rTTexture != this.txt) {
                        rTTexture.dispose();
                    }
                }
            });
        }
        return this.pixelBuffer;
    }

    @Override
    protected void drawPixelBuffer() {
        PrismInvoker.invokeOnRenderThread(new Runnable(){

            @Override
            public void run() {
                Graphics graphics = RTImage.this.getGraphics();
                if (graphics != null && RTImage.this.pixelBuffer != null) {
                    RTImage.this.pixelBuffer.rewind();
                    Image image = Image.fromByteBgraPreData(RTImage.this.pixelBuffer, RTImage.this.width, RTImage.this.height);
                    Texture texture = graphics.getResourceFactory().createTexture(image, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED);
                    graphics.clear();
                    graphics.drawTexture(texture, 0.0f, 0.0f, RTImage.this.width, RTImage.this.height);
                    texture.dispose();
                }
            }
        });
    }

    @Override
    public void factoryReset() {
        if (this.txt != null) {
            this.txt.dispose();
            this.txt = null;
        }
    }

    @Override
    public void factoryReleased() {
    }

    @Override
    public float getPixelScale() {
        return this.pixelScale;
    }
}

