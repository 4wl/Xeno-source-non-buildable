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
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class StringConverter
extends StyleConverterImpl<String, String> {
    public static StyleConverter<String, String> getInstance() {
        return Holder.INSTANCE;
    }

    private StringConverter() {
    }

    public String convert(ParsedValue<String, String> parsedValue, Font font) {
        String string = (String)parsedValue.getValue();
        if (string == null) {
            return null;
        }
        return Utils.convertUnicode(string);
    }

    public String toString() {
        return "StringConverter";
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue<String, String>[], String[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        public String[] convert(ParsedValue<ParsedValue<String, String>[], String[]> parsedValue, Font font) {
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            String[] arrstring = new String[arrparsedValue.length];
            for (int i = 0; i < arrparsedValue.length; ++i) {
                arrstring[i] = (String)StringConverter.getInstance().convert(arrparsedValue[i], font);
            }
            return arrstring;
        }

        public String toString() {
            return "String.SequenceConverter";
        }
    }

    private static class Holder {
        static final StringConverter INSTANCE = new StringConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

