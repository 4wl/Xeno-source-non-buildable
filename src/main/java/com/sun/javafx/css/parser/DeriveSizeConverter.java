/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;

public final class DeriveSizeConverter
extends StyleConverterImpl<ParsedValue<Size, Size>[], Size> {
    public static DeriveSizeConverter getInstance() {
        return Holder.INSTANCE;
    }

    private DeriveSizeConverter() {
    }

    public Size convert(ParsedValue<ParsedValue<Size, Size>[], Size> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        double d = ((Size)arrparsedValue[0].convert(font)).pixels(font);
        double d2 = ((Size)arrparsedValue[1].convert(font)).pixels(font);
        return new Size(d + d2, SizeUnits.PX);
    }

    public String toString() {
        return "DeriveSizeConverter";
    }

    private static class Holder {
        static final DeriveSizeConverter INSTANCE = new DeriveSizeConverter();

        private Holder() {
        }
    }
}

