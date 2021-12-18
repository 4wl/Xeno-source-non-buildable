/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import java.security.AccessController;

class GLPixelFormat {
    private final Attributes attributes;
    private final long nativeScreen;
    private long nativePFInfo;
    private static int defaultDepthSize;
    private static int defaultBufferSize;

    GLPixelFormat(long l, Attributes attributes) {
        this.nativeScreen = l;
        this.attributes = attributes;
    }

    Attributes getAttributes() {
        return this.attributes;
    }

    long getNativeScreen() {
        return this.nativeScreen;
    }

    void setNativePFInfo(long l) {
        this.nativePFInfo = l;
    }

    long getNativePFInfo() {
        return this.nativePFInfo;
    }

    static /* synthetic */ int access$000() {
        return defaultDepthSize;
    }

    static {
        AccessController.doPrivileged(() -> {
            defaultDepthSize = Integer.getInteger("prism.glDepthSize", 24);
            defaultBufferSize = Integer.getInteger("prism.glBufferSize", 32);
            return null;
        });
    }

    static class Attributes {
        static final int RED_SIZE = 0;
        static final int GREEN_SIZE = 1;
        static final int BLUE_SIZE = 2;
        static final int ALPHA_SIZE = 3;
        static final int DEPTH_SIZE = 4;
        static final int DOUBLEBUFFER = 5;
        static final int ONSCREEN = 6;
        static final int NUM_ITEMS = 7;
        private boolean onScreen = true;
        private boolean doubleBuffer = true;
        private int alphaSize;
        private int blueSize;
        private int greenSize;
        private int redSize;
        private int depthSize = GLPixelFormat.access$000();

        Attributes() {
            switch (defaultBufferSize) {
                case 32: {
                    this.alphaSize = 8;
                    this.blueSize = 8;
                    this.greenSize = 8;
                    this.redSize = 8;
                    break;
                }
                case 24: {
                    this.blueSize = 8;
                    this.greenSize = 8;
                    this.redSize = 8;
                    this.alphaSize = 0;
                    break;
                }
                case 16: {
                    this.blueSize = 5;
                    this.redSize = 5;
                    this.greenSize = 6;
                    this.alphaSize = 0;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("color buffer size " + defaultBufferSize + " not supported");
                }
            }
        }

        boolean isOnScreen() {
            return this.onScreen;
        }

        boolean isDoubleBuffer() {
            return this.doubleBuffer;
        }

        int getDepthSize() {
            return this.depthSize;
        }

        int getAlphaSize() {
            return this.alphaSize;
        }

        int getBlueSize() {
            return this.blueSize;
        }

        int getGreenSize() {
            return this.greenSize;
        }

        int getRedSize() {
            return this.redSize;
        }

        void setOnScreen(boolean bl) {
            this.onScreen = bl;
        }

        void setDoubleBuffer(boolean bl) {
            this.doubleBuffer = bl;
        }

        void setDepthSize(int n) {
            this.depthSize = n;
        }

        void setAlphaSize(int n) {
            this.alphaSize = n;
        }

        void setBlueSize(int n) {
            this.blueSize = n;
        }

        void setGreenSize(int n) {
            this.greenSize = n;
        }

        void setRedSize(int n) {
            this.redSize = n;
        }

        public String toString() {
            return "onScreen: " + this.onScreen + "redSize : " + this.redSize + ", " + "greenSize : " + this.greenSize + ", " + "blueSize : " + this.blueSize + ", " + "alphaSize : " + this.alphaSize + ", " + "depthSize : " + this.depthSize + ", " + "doubleBuffer : " + this.doubleBuffer;
        }
    }
}

