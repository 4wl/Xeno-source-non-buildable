/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.PixelFormat
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.webkit.prism.PrismImage;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.CompoundTexture;
import com.sun.prism.image.Coords;
import com.sun.prism.image.ViewPort;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.PixelFormat;

final class WCImageImpl
extends PrismImage {
    private static final Logger log = Logger.getLogger(WCImageImpl.class.getName());
    private final Image img;
    private Texture texture;
    private CompoundTexture compoundTexture;

    WCImageImpl(int n, int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating empty image({0},{1})", new Object[]{n, n2});
        }
        this.img = Image.fromIntArgbPreData(new int[n * n2], n, n2);
    }

    WCImageImpl(int[] arrn, int n, int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating image({0},{1}) from buffer", new Object[]{n, n2});
        }
        this.img = Image.fromIntArgbPreData(arrn, n, n2);
    }

    WCImageImpl(ImageFrame imageFrame) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating image {0}x{1} of type {2} from buffer", new Object[]{imageFrame.getWidth(), imageFrame.getHeight(), imageFrame.getImageType()});
        }
        this.img = Image.convertImageFrame(imageFrame);
    }

    @Override
    Image getImage() {
        return this.img;
    }

    @Override
    Graphics getGraphics() {
        return null;
    }

    @Override
    void draw(Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        Object object;
        if (graphics instanceof PrinterGraphics) {
            Texture texture = graphics.getResourceFactory().createTexture(this.img, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED);
            graphics.drawTexture(texture, n, n2, n3, n4, n5, n6, n7, n8);
            texture.dispose();
            return;
        }
        if (this.texture != null) {
            this.texture.lock();
            if (this.texture.isSurfaceLost()) {
                this.texture = null;
            }
        }
        if (this.texture == null && this.compoundTexture == null) {
            object = graphics.getResourceFactory();
            int n9 = object.getMaximumTextureSize();
            if (this.img.getWidth() <= n9 && this.img.getHeight() <= n9) {
                this.texture = object.createTexture(this.img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
                assert (this.texture != null);
            } else {
                this.compoundTexture = new CompoundTexture(this.img, n9);
            }
        }
        if (this.texture != null) {
            assert (this.compoundTexture == null);
            graphics.drawTexture(this.texture, n, n2, n3, n4, n5, n6, n7, n8);
            this.texture.unlock();
        } else {
            assert (this.compoundTexture != null);
            object = new ViewPort(n5, n6, n7 - n5, n8 - n6);
            Coords coords = new Coords(n3 - n, n4 - n2, (ViewPort)object);
            CompoundCoords compoundCoords = new CompoundCoords(this.compoundTexture, coords);
            compoundCoords.draw(graphics, this.compoundTexture, n, n2);
        }
    }

    @Override
    void dispose() {
        if (this.texture != null) {
            this.texture.dispose();
            this.texture = null;
        }
        if (this.compoundTexture != null) {
            this.compoundTexture.dispose();
            this.compoundTexture = null;
        }
    }

    @Override
    public int getWidth() {
        return this.img.getWidth();
    }

    @Override
    public int getHeight() {
        return this.img.getHeight();
    }

    @Override
    public ByteBuffer getPixelBuffer() {
        int n = this.img.getWidth();
        int n2 = this.img.getHeight();
        int n3 = n * 4;
        ByteBuffer byteBuffer = ByteBuffer.allocate(n3 * n2);
        this.img.getPixels(0, 0, n, n2, PixelFormat.getByteBgraInstance(), byteBuffer, n3);
        return byteBuffer;
    }

    @Override
    public float getPixelScale() {
        return this.img.getPixelScale();
    }
}

