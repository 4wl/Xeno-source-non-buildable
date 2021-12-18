/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.Cursor
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.Cursor;
import javafx.scene.text.Font;

public final class CursorConverter
extends StyleConverterImpl<String, Cursor> {
    public static StyleConverter<String, Cursor> getInstance() {
        return Holder.INSTANCE;
    }

    private CursorConverter() {
    }

    public Cursor convert(ParsedValue<String, Cursor> parsedValue, Font font) {
        String string = (String)parsedValue.getValue();
        if (string != null) {
            int n = string.indexOf("Cursor.");
            if (n > -1) {
                string = string.substring(n + "Cursor.".length());
            }
            string = string.replace('-', '_').toUpperCase();
        }
        try {
            return Cursor.cursor((String)string);
        }
        catch (IllegalArgumentException | NullPointerException runtimeException) {
            return Cursor.DEFAULT;
        }
    }

    public String toString() {
        return "CursorConverter";
    }

    private static class Holder {
        static final CursorConverter INSTANCE = new CursorConverter();

        private Holder() {
        }
    }
}

