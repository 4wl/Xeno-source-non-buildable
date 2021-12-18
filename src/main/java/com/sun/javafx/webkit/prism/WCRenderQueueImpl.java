/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.webkit.prism.PrismInvoker;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;

final class WCRenderQueueImpl
extends WCRenderQueue {
    WCRenderQueueImpl(WCGraphicsContext wCGraphicsContext) {
        super(wCGraphicsContext);
    }

    WCRenderQueueImpl(WCRectangle wCRectangle, boolean bl) {
        super(wCRectangle, bl);
    }

    @Override
    protected void flush() {
        if (!this.isEmpty()) {
            PrismInvoker.invokeOnRenderThread(() -> this.decode());
        }
    }

    @Override
    protected void disposeGraphics() {
        PrismInvoker.invokeOnRenderThread(() -> {
            if (this.gc != null) {
                this.gc.dispose();
            }
        });
    }
}

