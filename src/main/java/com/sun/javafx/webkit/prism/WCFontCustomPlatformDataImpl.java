/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.webkit.prism.WCFontImpl;
import com.sun.prism.GraphicsPipeline;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCFontCustomPlatformData;
import java.io.IOException;
import java.io.InputStream;

final class WCFontCustomPlatformDataImpl
extends WCFontCustomPlatformData {
    private final PGFont font;

    WCFontCustomPlatformDataImpl(InputStream inputStream) throws IOException {
        FontFactory fontFactory = GraphicsPipeline.getPipeline().getFontFactory();
        this.font = fontFactory.loadEmbeddedFont(null, inputStream, 10.0f, false);
        if (this.font == null) {
            throw new IOException("Error loading font");
        }
    }

    @Override
    protected WCFont createFont(int n, boolean bl, boolean bl2) {
        FontFactory fontFactory = GraphicsPipeline.getPipeline().getFontFactory();
        return new WCFontImpl(fontFactory.deriveFont(this.font, bl, bl2, n));
    }
}

