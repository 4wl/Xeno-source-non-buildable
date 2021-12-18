/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.Image
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.webkit.UIClientImpl;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.webkit.graphics.WCImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

abstract class PrismImage
extends WCImage {
    PrismImage() {
    }

    abstract Image getImage();

    abstract Graphics getGraphics();

    abstract void draw(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9);

    abstract void dispose();

    @Override
    public Object getPlatformImage() {
        return this.getImage();
    }

    @Override
    public void deref() {
        super.deref();
        if (!this.hasRefs()) {
            this.dispose();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected final String toDataURL(String string) {
        BufferedImage bufferedImage = UIClientImpl.toBufferedImage(javafx.scene.image.Image.impl_fromPlatformImage((Object)this.getImage()));
        if (bufferedImage instanceof BufferedImage) {
            Iterator<ImageWriter> iterator = ImageIO.getImageWritersByMIMEType(string);
            while (iterator.hasNext()) {
                ByteArrayOutputStream byteArrayOutputStream;
                block7: {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageWriter imageWriter = iterator.next();
                    try {
                        imageWriter.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
                        imageWriter.write(bufferedImage);
                        break block7;
                    }
                    catch (IOException iOException) {}
                    continue;
                    finally {
                        imageWriter.dispose();
                        continue;
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("data:").append(string).append(";base64,");
                stringBuilder.append(Base64.getMimeEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
                return stringBuilder.toString();
            }
        }
        return null;
    }
}

