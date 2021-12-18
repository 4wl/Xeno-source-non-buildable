/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.track;

public class VideoResolution {
    public int width;
    public int height;

    public VideoResolution(int n, int n2) {
        if (n <= 0) {
            throw new IllegalArgumentException("width <= 0");
        }
        if (n2 <= 0) {
            throw new IllegalArgumentException("height <= 0");
        }
        this.width = n;
        this.height = n2;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String toString() {
        return "VideoResolution {width: " + this.width + " height: " + this.height + "}";
    }
}

