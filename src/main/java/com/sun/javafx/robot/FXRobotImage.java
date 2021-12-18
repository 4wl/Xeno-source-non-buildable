/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.robot;

import java.nio.Buffer;
import java.nio.IntBuffer;

public class FXRobotImage {
    private final IntBuffer pixelBuffer;
    private final int width;
    private final int height;
    private final int scanlineStride;

    public static FXRobotImage create(Buffer buffer, int n, int n2, int n3) {
        return new FXRobotImage(buffer, n, n2, n3);
    }

    private FXRobotImage(Buffer buffer, int n, int n2, int n3) {
        if (buffer == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (n <= 0 || n2 <= 0) {
            throw new IllegalArgumentException("Image dimensions must be > 0");
        }
        this.pixelBuffer = (IntBuffer)buffer;
        this.width = n;
        this.height = n2;
        this.scanlineStride = n3;
    }

    public Buffer getPixelBuffer() {
        return this.pixelBuffer;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    public int getPixelStride() {
        return 4;
    }

    public int getArgbPre(int n, int n2) {
        if (n < 0 || n >= this.width || n2 < 0 || n2 >= this.height) {
            throw new IllegalArgumentException("x,y must be >0, <width, height");
        }
        return this.pixelBuffer.get(n + n2 * this.scanlineStride / 4);
    }

    public int getArgb(int n, int n2) {
        if (n < 0 || n >= this.width || n2 < 0 || n2 >= this.height) {
            throw new IllegalArgumentException("x,y must be >0, <width, height");
        }
        int n3 = this.pixelBuffer.get(n + n2 * this.scanlineStride / 4);
        if (n3 >> 24 == -1) {
            return n3;
        }
        int n4 = n3 >>> 24;
        int n5 = n3 >> 16 & 0xFF;
        int n6 = n3 >> 8 & 0xFF;
        int n7 = n3 & 0xFF;
        int n8 = n4 + (n4 >> 7);
        n5 = n5 * n8 >> 8;
        n6 = n6 * n8 >> 8;
        n7 = n7 * n8 >> 8;
        return n4 << 24 | n5 << 16 | n6 << 8 | n7;
    }

    public String toString() {
        return super.toString() + " [format=INT_ARGB_PRE width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " pixelStride=" + this.getPixelStride() + " pixelBuffer=" + this.pixelBuffer + "]";
    }
}

