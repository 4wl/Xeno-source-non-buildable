/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCRectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

class WCWidget {
    private static final Logger log = Logger.getLogger(WCWidget.class.getName());
    private int x;
    private int y;
    private int width;
    private int height;
    private final WebPage page;

    WCWidget(WebPage webPage) {
        this.page = webPage;
    }

    WebPage getPage() {
        return this.page;
    }

    WCRectangle getBounds() {
        return new WCRectangle(this.x, this.y, this.width, this.height);
    }

    void setBounds(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
    }

    protected void destroy() {
    }

    protected void requestFocus() {
    }

    protected void setCursor(long l) {
    }

    protected void setVisible(boolean bl) {
    }

    private void fwkDestroy() {
        log.log(Level.FINER, "destroy");
        this.destroy();
    }

    private void fwkSetBounds(int n, int n2, int n3, int n4) {
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "setBounds({0}, {1}, {2}, {3})", new Object[]{n, n2, n3, n4});
        }
        this.setBounds(n, n2, n3, n4);
    }

    private void fwkRequestFocus() {
        log.log(Level.FINER, "requestFocus");
        this.requestFocus();
    }

    private void fwkSetCursor(long l) {
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "setCursor({0})", l);
        }
        this.setCursor(l);
    }

    private void fwkSetVisible(boolean bl) {
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "setVisible({0})", bl);
        }
        this.setVisible(bl);
    }

    protected int fwkGetScreenDepth() {
        log.log(Level.FINER, "getScreenDepth");
        WebPageClient webPageClient = this.page.getPageClient();
        return webPageClient != null ? webPageClient.getScreenDepth() : 24;
    }

    protected WCRectangle fwkGetScreenRect(boolean bl) {
        WebPageClient webPageClient;
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getScreenRect({0})", bl);
        }
        return (webPageClient = this.page.getPageClient()) != null ? webPageClient.getScreenBounds(bl) : null;
    }

    private static native void initIDs();

    static {
        WCWidget.initIDs();
    }
}

