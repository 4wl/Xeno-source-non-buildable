/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Stop
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public final class StopConverter
extends StyleConverterImpl<ParsedValue[], Stop> {
    public static StopConverter getInstance() {
        return Holder.INSTANCE;
    }

    private StopConverter() {
    }

    public Stop convert(ParsedValue<ParsedValue[], Stop> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        Double d = ((Size)arrparsedValue[0].convert(font)).pixels(font);
        Color color = (Color)arrparsedValue[1].convert(font);
        return new Stop(d.doubleValue(), color);
    }

    public String toString() {
        return "StopConverter";
    }

    private static class Holder {
        static final StopConverter INSTANCE = new StopConverter();

        private Holder() {
        }
    }
}

