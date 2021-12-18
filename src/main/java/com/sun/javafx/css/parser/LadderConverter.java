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

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public final class LadderConverter
extends StyleConverterImpl<ParsedValue[], Color> {
    public static LadderConverter getInstance() {
        return Holder.INSTANCE;
    }

    private LadderConverter() {
    }

    public Color convert(ParsedValue<ParsedValue[], Color> parsedValue, Font font) {
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        Color color = (Color)arrparsedValue[0].convert(font);
        Stop[] arrstop = new Stop[arrparsedValue.length - 1];
        for (int i = 1; i < arrparsedValue.length; ++i) {
            arrstop[i - 1] = (Stop)arrparsedValue[i].convert(font);
        }
        return Utils.ladder(color, arrstop);
    }

    public String toString() {
        return "LadderConverter";
    }

    private static class Holder {
        static final LadderConverter INSTANCE = new LadderConverter();

        private Holder() {
        }
    }
}

