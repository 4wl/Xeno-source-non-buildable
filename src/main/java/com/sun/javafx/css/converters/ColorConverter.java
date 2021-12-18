/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.paint.Color
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class ColorConverter
extends StyleConverterImpl<String, Color> {
    public static StyleConverter<String, Color> getInstance() {
        return Holder.COLOR_INSTANCE;
    }

    private ColorConverter() {
    }

    public Color convert(ParsedValue<String, Color> parsedValue, Font font) {
        Object object = parsedValue.getValue();
        if (object == null) {
            return null;
        }
        if (object instanceof Color) {
            return (Color)object;
        }
        if (object instanceof String) {
            String string = (String)object;
            if (string.isEmpty() || "null".equals(string)) {
                return null;
            }
            try {
                return Color.web((String)((String)object));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        System.err.println("not a color: " + parsedValue);
        return Color.BLACK;
    }

    public String toString() {
        return "ColorConverter";
    }

    private static class Holder {
        static final ColorConverter COLOR_INSTANCE = new ColorConverter();

        private Holder() {
        }
    }
}

