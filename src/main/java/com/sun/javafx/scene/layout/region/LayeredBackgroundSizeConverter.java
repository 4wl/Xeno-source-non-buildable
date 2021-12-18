/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.scene.layout.BackgroundSize
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

public final class LayeredBackgroundSizeConverter
extends StyleConverterImpl<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> {
    private static final LayeredBackgroundSizeConverter LAYERED_BACKGROUND_SIZE_CONVERTER = new LayeredBackgroundSizeConverter();

    public static LayeredBackgroundSizeConverter getInstance() {
        return LAYERED_BACKGROUND_SIZE_CONVERTER;
    }

    private LayeredBackgroundSizeConverter() {
    }

    public BackgroundSize[] convert(ParsedValue<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        BackgroundSize[] arrbackgroundSize = new BackgroundSize[arrparsedValue.length];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            arrbackgroundSize[i] = (BackgroundSize)arrparsedValue[i].convert(font);
        }
        return arrbackgroundSize;
    }
}

