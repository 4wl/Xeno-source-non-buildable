/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.geometry.Insets
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

public final class InsetsConverter
extends StyleConverterImpl<ParsedValue[], Insets> {
    public static StyleConverter<ParsedValue[], Insets> getInstance() {
        return Holder.INSTANCE;
    }

    private InsetsConverter() {
    }

    public Insets convert(ParsedValue<ParsedValue[], Insets> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        double d = ((Size)arrparsedValue[0].convert(font)).pixels(font);
        double d2 = arrparsedValue.length > 1 ? ((Size)arrparsedValue[1].convert(font)).pixels(font) : d;
        double d3 = arrparsedValue.length > 2 ? ((Size)arrparsedValue[2].convert(font)).pixels(font) : d;
        double d4 = arrparsedValue.length > 3 ? ((Size)arrparsedValue[3].convert(font)).pixels(font) : d2;
        return new Insets(d, d2, d3, d4);
    }

    public String toString() {
        return "InsetsConverter";
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue<ParsedValue[], Insets>[], Insets[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        public Insets[] convert(ParsedValue<ParsedValue<ParsedValue[], Insets>[], Insets[]> parsedValue, Font font) {
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            Insets[] arrinsets = new Insets[arrparsedValue.length];
            for (int i = 0; i < arrparsedValue.length; ++i) {
                arrinsets[i] = (Insets)InsetsConverter.getInstance().convert(arrparsedValue[i], font);
            }
            return arrinsets;
        }

        public String toString() {
            return "InsetsSequenceConverter";
        }
    }

    private static class Holder {
        static final InsetsConverter INSTANCE = new InsetsConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

