/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.WCImageFrame;
import com.sun.webkit.graphics.WCRenderQueue;
import java.nio.ByteBuffer;

public abstract class WCImage
extends Ref {
    private WCRenderQueue rq;

    public abstract int getWidth();

    public abstract int getHeight();

    public Object getPlatformImage() {
        return null;
    }

    protected abstract String toDataURL(String var1);

    public ByteBuffer getPixelBuffer() {
        return null;
    }

    protected void drawPixelBuffer() {
    }

    public synchronized void setRQ(WCRenderQueue wCRenderQueue) {
        this.rq = wCRenderQueue;
    }

    protected synchronized void flushRQ() {
        if (this.rq != null) {
            this.rq.decode();
        }
    }

    protected synchronized boolean isDirty() {
        return this.rq == null ? false : !this.rq.isEmpty();
    }

    public static WCImage getImage(Object object) {
        WCImage wCImage = null;
        if (object instanceof WCImage) {
            wCImage = (WCImage)object;
        } else if (object instanceof WCImageFrame) {
            wCImage = ((WCImageFrame)object).getFrame();
        }
        return wCImage;
    }

    public abstract float getPixelScale();
}

