/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.CssMetaData
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.css.Styleable
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontPosture
 *  javafx.scene.text.FontWeight
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import java.util.Locale;
import java.util.Map;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public final class FontConverter
extends StyleConverterImpl<ParsedValue[], Font> {
    public static StyleConverter<ParsedValue[], Font> getInstance() {
        return Holder.INSTANCE;
    }

    private FontConverter() {
    }

    public Font convert(ParsedValue<ParsedValue[], Font> parsedValue, Font font) {
        Size size;
        ParsedValue parsedValue2;
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        Font font2 = font != null ? font : Font.getDefault();
        String string = arrparsedValue[0] != null ? Utils.stripQuotes((String)arrparsedValue[0].convert(font2)) : font2.getFamily();
        double d = font2.getSize();
        if (arrparsedValue[1] != null) {
            parsedValue2 = (ParsedValue)arrparsedValue[1].getValue();
            size = (Size)parsedValue2.convert(font2);
            d = size.pixels(font2.getSize(), font2);
        }
        parsedValue2 = arrparsedValue[2] != null ? (FontWeight)arrparsedValue[2].convert(font2) : FontWeight.NORMAL;
        size = arrparsedValue[3] != null ? (FontPosture)arrparsedValue[3].convert(font2) : FontPosture.REGULAR;
        Font font3 = Font.font((String)string, (FontWeight)parsedValue2, (FontPosture)size, (double)d);
        return font3;
    }

    @Override
    public Font convert(Map<CssMetaData<? extends Styleable, ?>, Object> map) {
        Font font = Font.getDefault();
        double d = font.getSize();
        String string = font.getFamily();
        FontWeight fontWeight = FontWeight.NORMAL;
        FontPosture fontPosture = FontPosture.REGULAR;
        for (Map.Entry<CssMetaData<Styleable, ?>, Object> entry : map.entrySet()) {
            Object object = entry.getValue();
            if (object == null) continue;
            String string2 = entry.getKey().getProperty();
            if (string2.endsWith("font-size")) {
                d = ((Number)object).doubleValue();
                continue;
            }
            if (string2.endsWith("font-family")) {
                string = Utils.stripQuotes((String)object);
                continue;
            }
            if (string2.endsWith("font-weight")) {
                fontWeight = (FontWeight)object;
                continue;
            }
            if (!string2.endsWith("font-style")) continue;
            fontPosture = (FontPosture)object;
        }
        Font font2 = Font.font((String)string, (FontWeight)fontWeight, (FontPosture)fontPosture, (double)d);
        return font2;
    }

    public String toString() {
        return "FontConverter";
    }

    public static final class FontSizeConverter
    extends StyleConverterImpl<ParsedValue<?, Size>, Number> {
        public static FontSizeConverter getInstance() {
            return Holder.INSTANCE;
        }

        private FontSizeConverter() {
        }

        public Number convert(ParsedValue<ParsedValue<?, Size>, Number> parsedValue, Font font) {
            ParsedValue parsedValue2 = (ParsedValue)parsedValue.getValue();
            return ((Size)parsedValue2.convert(font)).pixels(font.getSize(), font);
        }

        public String toString() {
            return "FontConverter.FontSizeConverter";
        }

        private static class Holder {
            static final FontSizeConverter INSTANCE = new FontSizeConverter();

            private Holder() {
            }
        }
    }

    public static final class FontWeightConverter
    extends StyleConverterImpl<String, FontWeight> {
        public static FontWeightConverter getInstance() {
            return Holder.INSTANCE;
        }

        private FontWeightConverter() {
        }

        public FontWeight convert(ParsedValue<String, FontWeight> parsedValue, Font font) {
            Object object = parsedValue.getValue();
            FontWeight fontWeight = null;
            if (object instanceof String) {
                try {
                    String string = ((String)object).toUpperCase(Locale.ROOT);
                    fontWeight = Enum.valueOf(FontWeight.class, string);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    fontWeight = FontWeight.NORMAL;
                }
                catch (NullPointerException nullPointerException) {
                    fontWeight = FontWeight.NORMAL;
                }
            } else if (object instanceof FontWeight) {
                fontWeight = (FontWeight)object;
            }
            return fontWeight;
        }

        public String toString() {
            return "FontConverter.WeightConverter";
        }

        private static class Holder {
            static final FontWeightConverter INSTANCE = new FontWeightConverter();

            private Holder() {
            }
        }
    }

    public static final class FontStyleConverter
    extends StyleConverterImpl<String, FontPosture> {
        public static FontStyleConverter getInstance() {
            return Holder.INSTANCE;
        }

        private FontStyleConverter() {
        }

        public FontPosture convert(ParsedValue<String, FontPosture> parsedValue, Font font) {
            Object object = parsedValue.getValue();
            FontPosture fontPosture = null;
            if (object instanceof String) {
                try {
                    String string = ((String)object).toUpperCase(Locale.ROOT);
                    fontPosture = Enum.valueOf(FontPosture.class, string);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    fontPosture = FontPosture.REGULAR;
                }
                catch (NullPointerException nullPointerException) {
                    fontPosture = FontPosture.REGULAR;
                }
            } else if (object instanceof FontPosture) {
                fontPosture = (FontPosture)object;
            }
            return fontPosture;
        }

        public String toString() {
            return "FontConverter.StyleConverter";
        }

        private static class Holder {
            static final FontStyleConverter INSTANCE = new FontStyleConverter();

            private Holder() {
            }
        }
    }

    private static class Holder {
        static final FontConverter INSTANCE = new FontConverter();

        private Holder() {
        }
    }
}

