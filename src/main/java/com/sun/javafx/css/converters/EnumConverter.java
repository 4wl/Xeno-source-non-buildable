/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.geometry.HPos
 *  javafx.geometry.Orientation
 *  javafx.geometry.Pos
 *  javafx.geometry.Side
 *  javafx.geometry.VPos
 *  javafx.scene.effect.BlendMode
 *  javafx.scene.effect.BlurType
 *  javafx.scene.layout.BackgroundRepeat
 *  javafx.scene.paint.CycleMethod
 *  javafx.scene.shape.ArcType
 *  javafx.scene.shape.StrokeLineCap
 *  javafx.scene.shape.StrokeLineJoin
 *  javafx.scene.shape.StrokeType
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontPosture
 *  javafx.scene.text.FontSmoothingType
 *  javafx.scene.text.FontWeight
 *  javafx.scene.text.TextAlignment
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.cursor.CursorType;
import com.sun.javafx.util.Logging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import sun.util.logging.PlatformLogger;

public final class EnumConverter<E extends Enum<E>>
extends StyleConverterImpl<String, E> {
    final Class<E> enumClass;
    private static Map<String, StyleConverter<?, ?>> converters;

    public EnumConverter(Class<E> class_) {
        this.enumClass = class_;
    }

    public E convert(ParsedValue<String, E> parsedValue, Font font) {
        if (this.enumClass == null) {
            return null;
        }
        String string = (String)parsedValue.getValue();
        int n = string.lastIndexOf(46);
        if (n > -1) {
            string = string.substring(n + 1);
        }
        try {
            string = string.replace('-', '_');
            return Enum.valueOf(this.enumClass, string.toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return Enum.valueOf(this.enumClass, string);
        }
    }

    @Override
    public void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        super.writeBinary(dataOutputStream, stringStore);
        String string = this.enumClass.getName();
        int n = stringStore.addString(string);
        dataOutputStream.writeShort(n);
    }

    public static StyleConverter<?, ?> readBinary(DataInputStream dataInputStream, String[] arrstring) throws IOException {
        String string;
        short s = dataInputStream.readShort();
        String string2 = string = 0 <= s && s <= arrstring.length ? arrstring[s] : null;
        if (string == null || string.isEmpty()) {
            return null;
        }
        if (converters == null || !converters.containsKey(string)) {
            PlatformLogger platformLogger;
            StyleConverter<?, ?> styleConverter = EnumConverter.getInstance(string);
            if (styleConverter == null && (platformLogger = Logging.getCSSLogger()).isLoggable(PlatformLogger.Level.SEVERE)) {
                platformLogger.severe("could not deserialize EnumConverter for " + string);
            }
            if (converters == null) {
                converters = new HashMap();
            }
            converters.put(string, styleConverter);
            return styleConverter;
        }
        return converters.get(string);
    }

    public static StyleConverter<?, ?> getInstance(String string) {
        EnumConverter<CursorType> enumConverter = null;
        switch (string) {
            case "com.sun.javafx.cursor.CursorType": {
                enumConverter = new EnumConverter<CursorType>(CursorType.class);
                break;
            }
            case "javafx.scene.layout.BackgroundRepeat": 
            case "com.sun.javafx.scene.layout.region.Repeat": {
                enumConverter = new EnumConverter<BackgroundRepeat>(BackgroundRepeat.class);
                break;
            }
            case "javafx.geometry.HPos": {
                enumConverter = new EnumConverter<HPos>(HPos.class);
                break;
            }
            case "javafx.geometry.Orientation": {
                enumConverter = new EnumConverter<Orientation>(Orientation.class);
                break;
            }
            case "javafx.geometry.Pos": {
                enumConverter = new EnumConverter<Pos>(Pos.class);
                break;
            }
            case "javafx.geometry.Side": {
                enumConverter = new EnumConverter<Side>(Side.class);
                break;
            }
            case "javafx.geometry.VPos": {
                enumConverter = new EnumConverter<VPos>(VPos.class);
                break;
            }
            case "javafx.scene.effect.BlendMode": {
                enumConverter = new EnumConverter<BlendMode>(BlendMode.class);
                break;
            }
            case "javafx.scene.effect.BlurType": {
                enumConverter = new EnumConverter<BlurType>(BlurType.class);
                break;
            }
            case "javafx.scene.paint.CycleMethod": {
                enumConverter = new EnumConverter<CycleMethod>(CycleMethod.class);
                break;
            }
            case "javafx.scene.shape.ArcType": {
                enumConverter = new EnumConverter<ArcType>(ArcType.class);
                break;
            }
            case "javafx.scene.shape.StrokeLineCap": {
                enumConverter = new EnumConverter<StrokeLineCap>(StrokeLineCap.class);
                break;
            }
            case "javafx.scene.shape.StrokeLineJoin": {
                enumConverter = new EnumConverter<StrokeLineJoin>(StrokeLineJoin.class);
                break;
            }
            case "javafx.scene.shape.StrokeType": {
                enumConverter = new EnumConverter<StrokeType>(StrokeType.class);
                break;
            }
            case "javafx.scene.text.FontPosture": {
                enumConverter = new EnumConverter<FontPosture>(FontPosture.class);
                break;
            }
            case "javafx.scene.text.FontSmoothingType": {
                enumConverter = new EnumConverter<FontSmoothingType>(FontSmoothingType.class);
                break;
            }
            case "javafx.scene.text.FontWeight": {
                enumConverter = new EnumConverter<FontWeight>(FontWeight.class);
                break;
            }
            case "javafx.scene.text.TextAlignment": {
                enumConverter = new EnumConverter<TextAlignment>(TextAlignment.class);
                break;
            }
            default: {
                assert (false) : "EnumConverter<" + string + "> not expected";
                PlatformLogger platformLogger = Logging.getCSSLogger();
                if (!platformLogger.isLoggable(PlatformLogger.Level.SEVERE)) break;
                platformLogger.severe("EnumConverter : converter Class is null for : " + string);
            }
        }
        return enumConverter;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || !(object instanceof EnumConverter)) {
            return false;
        }
        return this.enumClass.equals(((EnumConverter)object).enumClass);
    }

    public int hashCode() {
        return this.enumClass.hashCode();
    }

    public String toString() {
        return "EnumConveter[" + this.enumClass.getName() + "]";
    }
}

