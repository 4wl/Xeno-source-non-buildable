/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.text.Font
 *  javafx.util.Duration
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import javafx.util.Duration;

public final class DurationConverter
extends StyleConverterImpl<ParsedValue<?, Size>, Duration> {
    public static StyleConverter<ParsedValue<?, Size>, Duration> getInstance() {
        return Holder.INSTANCE;
    }

    private DurationConverter() {
    }

    public Duration convert(ParsedValue<ParsedValue<?, Size>, Duration> parsedValue, Font font) {
        ParsedValue parsedValue2 = (ParsedValue)parsedValue.getValue();
        Size size = (Size)parsedValue2.convert(font);
        double d = size.getValue();
        Duration duration = null;
        if (d < Double.POSITIVE_INFINITY) {
            switch (size.getUnits()) {
                case S: {
                    duration = Duration.seconds((double)d);
                    break;
                }
                case MS: {
                    duration = Duration.millis((double)d);
                    break;
                }
                default: {
                    duration = Duration.UNKNOWN;
                    break;
                }
            }
        } else {
            duration = Duration.INDEFINITE;
        }
        return duration;
    }

    public String toString() {
        return "DurationConverter";
    }

    private static class Holder {
        static final DurationConverter INSTANCE = new DurationConverter();

        private Holder() {
        }
    }
}

