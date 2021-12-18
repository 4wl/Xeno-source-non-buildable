/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class BooleanConverter
extends StyleConverterImpl<String, Boolean> {
    public static StyleConverter<String, Boolean> getInstance() {
        return Holder.INSTANCE;
    }

    private BooleanConverter() {
    }

    public Boolean convert(ParsedValue<String, Boolean> parsedValue, Font font) {
        String string = (String)parsedValue.getValue();
        return Boolean.valueOf(string);
    }

    public String toString() {
        return "BooleanConverter";
    }

    private static class Holder {
        static final BooleanConverter INSTANCE = new BooleanConverter();

        private Holder() {
        }
    }
}

