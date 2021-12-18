/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit;

import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.theme.RenderThemeImpl;
import com.sun.javafx.webkit.theme.ScrollBarThemeImpl;
import com.sun.webkit.ThemeClient;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;

public final class ThemeClientImpl
extends ThemeClient {
    private final Accessor accessor;

    public ThemeClientImpl(Accessor accessor) {
        this.accessor = accessor;
    }

    @Override
    protected RenderTheme createRenderTheme() {
        return new RenderThemeImpl(this.accessor);
    }

    @Override
    protected ScrollBarTheme createScrollBarTheme() {
        return new ScrollBarThemeImpl(this.accessor);
    }
}

