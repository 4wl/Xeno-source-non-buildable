/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.t2k;

import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.t2k.ICUGlyphLayout;
import com.sun.javafx.font.t2k.T2KFontFile;
import com.sun.javafx.text.GlyphLayout;

public class T2KFactory
extends PrismFontFactory {
    public static PrismFontFactory getFactory() {
        return new T2KFactory();
    }

    private T2KFactory() {
    }

    @Override
    protected PrismFontFile createFontFile(String string, String string2, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        return new T2KFontFile(string, string2, n, bl, bl2, bl3, bl4);
    }

    @Override
    public GlyphLayout createGlyphLayout() {
        return new ICUGlyphLayout();
    }
}

