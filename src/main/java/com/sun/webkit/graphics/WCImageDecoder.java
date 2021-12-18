/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.WCImageFrame;

public abstract class WCImageDecoder {
    protected abstract void addImageData(byte[] var1);

    protected abstract void getImageSize(int[] var1);

    protected abstract int getFrameCount();

    protected abstract WCImageFrame getFrame(int var1, int[] var2);

    protected abstract void loadFromResource(String var1);

    protected abstract void destroy();

    protected abstract String getFilenameExtension();
}

