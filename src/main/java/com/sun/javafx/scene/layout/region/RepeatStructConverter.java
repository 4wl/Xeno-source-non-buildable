/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.scene.layout.BackgroundRepeat
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

public final class RepeatStructConverter
extends StyleConverterImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> {
    private static final RepeatStructConverter REPEAT_STRUCT_CONVERTER = new RepeatStructConverter();
    private final EnumConverter<BackgroundRepeat> repeatConverter = new EnumConverter<BackgroundRepeat>(BackgroundRepeat.class);

    public static RepeatStructConverter getInstance() {
        return REPEAT_STRUCT_CONVERTER;
    }

    private RepeatStructConverter() {
    }

    public RepeatStruct[] convert(ParsedValue<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> parsedValue, Font font) {
        ParsedValue[][] arrparsedValue = (ParsedValue[][])parsedValue.getValue();
        RepeatStruct[] arrrepeatStruct = new RepeatStruct[arrparsedValue.length];
        for (int i = 0; i < arrparsedValue.length; ++i) {
            ParsedValue[] arrparsedValue2 = arrparsedValue[i];
            BackgroundRepeat backgroundRepeat = (BackgroundRepeat)this.repeatConverter.convert(arrparsedValue2[0], (Font)null);
            BackgroundRepeat backgroundRepeat2 = (BackgroundRepeat)this.repeatConverter.convert(arrparsedValue2[1], (Font)null);
            arrrepeatStruct[i] = new RepeatStruct(backgroundRepeat, backgroundRepeat2);
        }
        return arrrepeatStruct;
    }

    public String toString() {
        return "RepeatStructConverter";
    }
}

