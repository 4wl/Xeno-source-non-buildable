/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.WCWidget;
import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;

final class WCFrameView
extends WCWidget {
    WCFrameView(WebPage webPage) {
        super(webPage);
    }

    @Override
    protected void requestFocus() {
        WebPageClient webPageClient = this.getPage().getPageClient();
        if (webPageClient != null) {
            webPageClient.setFocus(true);
        }
    }

    @Override
    protected void setCursor(long l) {
        WebPageClient webPageClient = this.getPage().getPageClient();
        if (webPageClient != null) {
            webPageClient.setCursor(l);
        }
    }
}

