/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.scene.layout.BackgroundSize
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.converters.BooleanConverter;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

public final class BackgroundSizeConverter
extends StyleConverterImpl<ParsedValue[], BackgroundSize> {
    private static final BackgroundSizeConverter BACKGROUND_SIZE_CONVERTER = new BackgroundSizeConverter();

    public static BackgroundSizeConverter getInstance() {
        return BACKGROUND_SIZE_CONVERTER;
    }

    private BackgroundSizeConverter() {
    }

    public BackgroundSize convert(ParsedValue<ParsedValue[], BackgroundSize> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        Size size = arrparsedValue[0] != null ? (Size)arrparsedValue[0].convert(font) : null;
        Size size2 = arrparsedValue[1] != null ? (Size)arrparsedValue[1].convert(font) : null;
        boolean bl = true;
        boolean bl2 = true;
        if (size != null) {
            boolean bl3 = bl = size.getUnits() == SizeUnits.PERCENT;
        }
        if (size2 != null) {
            bl2 = size2.getUnits() == SizeUnits.PERCENT;
        }
        double d = size != null ? size.pixels(font) : -1.0;
        double d2 = size2 != null ? size2.pixels(font) : -1.0;
        boolean bl4 = arrparsedValue[2] != null ? (Boolean)BooleanConverter.getInstance().convert(arrparsedValue[2], font) : false;
        boolean bl5 = arrparsedValue[3] != null ? (Boolean)BooleanConverter.getInstance().convert(arrparsedValue[3], font) : false;
        return new BackgroundSize(d, d2, bl, bl2, bl5, bl4);
    }

    public String toString() {
        return "BackgroundSizeConverter";
    }
}

