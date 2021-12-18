/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.scene.paint.Color
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class DeriveColorConverter
extends StyleConverterImpl<ParsedValue[], Color> {
    public static DeriveColorConverter getInstance() {
        return Holder.INSTANCE;
    }

    private DeriveColorConverter() {
    }

    public Color convert(ParsedValue<ParsedValue[], Color> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        Color color = (Color)arrparsedValue[0].convert(font);
        Size size = (Size)arrparsedValue[1].convert(font);
        return Utils.deriveColor(color, size.pixels(font));
    }

    public String toString() {
        return "DeriveColorConverter";
    }

    private static class Holder {
        static final DeriveColorConverter INSTANCE = new DeriveColorConverter();

        private Holder() {
        }
    }
}

