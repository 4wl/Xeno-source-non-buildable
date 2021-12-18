/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.concurrent.Service
 *  javafx.concurrent.Task
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.javafx.webkit.prism.PrismImage;
import com.sun.javafx.webkit.prism.WCImageImpl;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageDecoder;
import com.sun.webkit.graphics.WCImageFrame;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

final class WCImageDecoderImpl
extends WCImageDecoder {
    private static final Logger log = Logger.getLogger(WCImageDecoderImpl.class.getName());
    private Service<ImageFrame[]> loader;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private ImageFrame[] frames;
    private int frameCount = 0;
    private boolean fullDataReceived = false;
    private boolean framesDecoded = false;
    private PrismImage[] images;
    private volatile byte[] data;
    private volatile int dataSize = 0;
    private final ImageLoadListener readerListener = new ImageLoadListener(){

        @Override
        public void imageLoadProgress(ImageLoader imageLoader, float f) {
        }

        @Override
        public void imageLoadWarning(ImageLoader imageLoader, String string) {
        }

        @Override
        public void imageLoadMetaData(ImageLoader imageLoader, ImageMetadata imageMetadata) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format("%X Image size %dx%d", this.hashCode(), imageMetadata.imageWidth, imageMetadata.imageHeight));
            }
            if (WCImageDecoderImpl.this.imageWidth < imageMetadata.imageWidth) {
                WCImageDecoderImpl.this.imageWidth = imageMetadata.imageWidth;
            }
            if (WCImageDecoderImpl.this.imageHeight < imageMetadata.imageHeight) {
                WCImageDecoderImpl.this.imageHeight = imageMetadata.imageHeight;
            }
        }
    };

    WCImageDecoderImpl() {
    }

    @Override
    protected void destroy() {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X Destroy image decoder", this.hashCode()));
        }
        this.destroyLoader();
        this.frames = null;
        this.images = null;
        this.framesDecoded = false;
    }

    @Override
    protected String getFilenameExtension() {
        return ".img";
    }

    private boolean imageSizeAvilable() {
        return this.imageWidth > 0 && this.imageHeight > 0;
    }

    @Override
    protected void addImageData(byte[] arrby) {
        if (arrby != null) {
            this.fullDataReceived = false;
            if (this.data == null) {
                this.data = Arrays.copyOf(arrby, arrby.length * 2);
                this.dataSize = arrby.length;
            } else {
                int n = this.dataSize + arrby.length;
                if (n > this.data.length) {
                    this.resizeDataArray(Math.max(n, this.data.length * 2));
                }
                System.arraycopy(arrby, 0, this.data, this.dataSize, arrby.length);
                this.dataSize = n;
            }
            if (!this.imageSizeAvilable()) {
                this.loadFrames();
            }
        } else if (this.data != null && !this.fullDataReceived) {
            if (this.data.length > this.dataSize) {
                this.resizeDataArray(this.dataSize);
            }
            this.fullDataReceived = true;
        }
    }

    private void destroyLoader() {
        if (this.loader != null) {
            this.loader.cancel();
            this.loader = null;
        }
    }

    private void startLoader() {
        if (this.loader == null) {
            this.loader = new Service<ImageFrame[]>(){

                protected Task<ImageFrame[]> createTask() {
                    return new Task<ImageFrame[]>(){

                        protected ImageFrame[] call() throws Exception {
                            return WCImageDecoderImpl.this.loadFrames();
                        }
                    };
                }
            };
            this.loader.valueProperty().addListener((observableValue, arrimageFrame, arrimageFrame2) -> {
                if (arrimageFrame2 != null && this.loader != null) {
                    this.setFrames((ImageFrame[])arrimageFrame2);
                }
            });
        }
        if (!this.loader.isRunning()) {
            this.loader.restart();
        }
    }

    private void resizeDataArray(int n) {
        byte[] arrby = new byte[n];
        System.arraycopy(this.data, 0, arrby, 0, this.dataSize);
        this.data = arrby;
    }

    @Override
    protected void loadFromResource(String string) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X Load image from resource '%s'", this.hashCode(), string));
        }
        String string2 = WCGraphicsManager.getResourceName(string);
        InputStream inputStream = this.getClass().getResourceAsStream(string2);
        if (inputStream == null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format("%X Unable to open resource '%s'", this.hashCode(), string2));
            }
            return;
        }
        this.setFrames(this.loadFrames(inputStream));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ImageFrame[] loadFrames(InputStream inputStream) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X Decoding frames", this.hashCode()));
        }
        try {
            ImageFrame[] arrimageFrame = ImageStorage.loadAll(inputStream, this.readerListener, 0, 0, true, 1.0f, false);
            return arrimageFrame;
        }
        catch (ImageStorageException imageStorageException) {
            ImageFrame[] arrimageFrame = null;
            return arrimageFrame;
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException iOException) {}
        }
    }

    private ImageFrame[] loadFrames() {
        return this.loadFrames(new ByteArrayInputStream(this.data, 0, this.dataSize));
    }

    @Override
    protected void getImageSize(int[] arrn) {
        arrn[0] = this.imageWidth;
        arrn[1] = this.imageHeight;
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X image size = %dx%d", this.hashCode(), arrn[0], arrn[1]));
        }
    }

    private void setFrames(ImageFrame[] arrimageFrame) {
        this.frames = arrimageFrame;
        this.images = null;
        this.frameCount = arrimageFrame == null ? 0 : arrimageFrame.length;
    }

    @Override
    protected int getFrameCount() {
        return this.frameCount;
    }

    @Override
    protected WCImageFrame getFrame(int n, int[] arrn) {
        ImageFrame imageFrame = this.getImageFrame(n);
        if (imageFrame != null) {
            Object object;
            if (log.isLoggable(Level.FINE)) {
                object = imageFrame.getImageType();
                log.fine(String.format("%X getFrame(%d): image type = %s", this.hashCode(), n, object));
            }
            object = this.getPrismImage(n, imageFrame);
            if (arrn != null) {
                int n2;
                ImageMetadata imageMetadata = imageFrame.getMetadata();
                int n3 = n2 = imageMetadata == null || imageMetadata.delayTime == null ? 0 : imageMetadata.delayTime;
                if (n2 < 11) {
                    n2 = 100;
                }
                arrn[0] = n < this.frames.length - 1 ? 1 : 0;
                arrn[1] = ((WCImage)object).getWidth();
                arrn[2] = ((WCImage)object).getHeight();
                arrn[3] = n2;
                arrn[4] = 1;
                if (log.isLoggable(Level.FINE)) {
                    log.fine(String.format("%X getFrame(%d): complete=%d, size=%dx%d, duration=%d, hasAlpha=%d", this.hashCode(), n, arrn[0], arrn[1], arrn[2], arrn[3], arrn[4]));
                }
            }
            return new Frame((WCImage)object);
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X FAILED getFrame(%d)", this.hashCode(), n));
        }
        return null;
    }

    private ImageFrame getImageFrame(int n) {
        if (!this.fullDataReceived) {
            this.startLoader();
        } else if (this.fullDataReceived && !this.framesDecoded) {
            this.destroyLoader();
            this.setFrames(this.loadFrames());
            this.framesDecoded = true;
        }
        return n >= 0 && this.frames != null && this.frames.length > n ? this.frames[n] : null;
    }

    private PrismImage getPrismImage(int n, ImageFrame imageFrame) {
        if (this.images == null) {
            this.images = new PrismImage[this.frames.length];
        }
        if (this.images[n] == null) {
            this.images[n] = new WCImageImpl(imageFrame);
        }
        return this.images[n];
    }

    private static final class Frame
    extends WCImageFrame {
        private WCImage image;

        private Frame(WCImage wCImage) {
            this.image = wCImage;
        }

        @Override
        public WCImage getFrame() {
            return this.image;
        }

        @Override
        protected void destroyDecodedData() {
            this.image = null;
        }
    }
}

