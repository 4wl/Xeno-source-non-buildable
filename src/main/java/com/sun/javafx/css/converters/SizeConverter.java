/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class SizeConverter
extends StyleConverterImpl<ParsedValue<?, Size>, Number> {
    public static StyleConverter<ParsedValue<?, Size>, Number> getInstance() {
        return Holder.INSTANCE;
    }

    private SizeConverter() {
    }

    public Number convert(ParsedValue<ParsedValue<?, Size>, Number> parsedValue, Font font) {
        ParsedValue parsedValue2 = (ParsedValue)parsedValue.getValue();
        return ((Size)parsedValue2.convert(font)).pixels(font);
    }

    public String toString() {
        return "SizeConverter";
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue[], Number[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        public Number[] convert(ParsedValue<ParsedValue[], Number[]> parsedValue, Font font) {
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            Number[] arrnumber = new Number[arrparsedValue.length];
            for (int i = 0; i < arrparsedValue.length; ++i) {
                arrnumber[i] = ((Size)arrparsedValue[i].convert(font)).pixels(font);
            }
            return arrnumber;
        }

        public String toString() {
            return "Size.SequenceConverter";
        }
    }

    private static class Holder {
        static final SizeConverter INSTANCE = new SizeConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

