/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism.web;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.prism.Graphics;
import com.sun.prism.PrinterGraphics;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCRectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NGWebView
extends NGGroup {
    private static final Logger log = Logger.getLogger(NGWebView.class.getName());
    private volatile WebPage page;
    private volatile float width;
    private volatile float height;

    public void setPage(WebPage webPage) {
        this.page = webPage;
    }

    public void resize(float f, float f2) {
        if (this.width != f || this.height != f2) {
            this.width = f;
            this.height = f2;
            this.geometryChanged();
            if (this.page != null) {
                this.page.setBounds(0, 0, (int)f, (int)f2);
            }
        }
    }

    public void update() {
        BaseBounds baseBounds;
        if (this.page != null && !(baseBounds = this.getClippedBounds(new RectBounds(), BaseTransform.IDENTITY_TRANSFORM)).isEmpty()) {
            log.log(Level.FINEST, "updating rectangle: {0}", baseBounds);
            this.page.updateContent(new WCRectangle(baseBounds.getMinX(), baseBounds.getMinY(), baseBounds.getWidth(), baseBounds.getHeight()));
        }
    }

    public void requestRender() {
        this.visualsChanged();
    }

    @Override
    protected void renderContent(Graphics graphics) {
        log.log(Level.FINEST, "rendering into {0}", graphics);
        if (graphics == null || this.page == null || this.width <= 0.0f || this.height <= 0.0f) {
            return;
        }
        WCGraphicsContext wCGraphicsContext = WCGraphicsManager.getGraphicsManager().createGraphicsContext(graphics);
        try {
            if (graphics instanceof PrinterGraphics) {
                this.page.print(wCGraphicsContext, 0, 0, (int)this.width, (int)this.height);
            } else {
                this.page.paint(wCGraphicsContext, 0, 0, (int)this.width, (int)this.height);
            }
            wCGraphicsContext.flush();
        }
        finally {
            wCGraphicsContext.dispose();
        }
    }

    @Override
    public boolean hasOverlappingContents() {
        return false;
    }

    @Override
    protected boolean hasVisuals() {
        return true;
    }
}

