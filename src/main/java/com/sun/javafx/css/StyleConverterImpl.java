/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.CssMetaData
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.css.Styleable
 */
package com.sun.javafx.css;

import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.ColorConverter;
import com.sun.javafx.css.converters.CursorConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.css.parser.DeriveColorConverter;
import com.sun.javafx.css.parser.DeriveSizeConverter;
import com.sun.javafx.css.parser.LadderConverter;
import com.sun.javafx.css.parser.StopConverter;
import com.sun.javafx.scene.layout.region.BackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStyleConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import com.sun.javafx.util.Logging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import sun.util.logging.PlatformLogger;

public class StyleConverterImpl<F, T>
extends StyleConverter<F, T> {
    private static Map<ParsedValue, Object> cache;
    private static Map<String, StyleConverter<?, ?>> tmap;

    public T convert(Map<CssMetaData<? extends Styleable, ?>, Object> map) {
        return null;
    }

    protected StyleConverterImpl() {
    }

    public void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        String string = ((Object)((Object)this)).getClass().getName();
        int n = stringStore.addString(string);
        dataOutputStream.writeShort(n);
    }

    static void clearCache() {
        if (cache != null) {
            cache.clear();
        }
    }

    protected T getCachedValue(ParsedValue parsedValue) {
        if (cache != null) {
            return (T)cache.get((Object)parsedValue);
        }
        return null;
    }

    protected void cacheValue(ParsedValue parsedValue, Object object) {
        if (cache == null) {
            cache = new WeakHashMap<ParsedValue, Object>();
        }
        cache.put(parsedValue, object);
    }

    public static StyleConverter<?, ?> readBinary(DataInputStream dataInputStream, String[] arrstring) throws IOException {
        short s = dataInputStream.readShort();
        String string = arrstring[s];
        if (string == null || string.isEmpty()) {
            return null;
        }
        if (string.startsWith("com.sun.javafx.css.converters.EnumConverter")) {
            return EnumConverter.readBinary(dataInputStream, arrstring);
        }
        if (tmap == null || !tmap.containsKey(string)) {
            PlatformLogger platformLogger;
            StyleConverter<?, ?> styleConverter = StyleConverterImpl.getInstance(string);
            if (styleConverter == null && (platformLogger = Logging.getCSSLogger()).isLoggable(PlatformLogger.Level.SEVERE)) {
                platformLogger.severe("could not deserialize " + string);
            }
            if (styleConverter == null) {
                System.err.println("could not deserialize " + string);
            }
            if (tmap == null) {
                tmap = new HashMap();
            }
            tmap.put(string, styleConverter);
            return styleConverter;
        }
        return tmap.get(string);
    }

    static StyleConverter<?, ?> getInstance(String string) {
        StyleConverterImpl styleConverterImpl = null;
        switch (string) {
            case "com.sun.javafx.css.converters.BooleanConverter": {
                styleConverterImpl = BooleanConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.ColorConverter": {
                styleConverterImpl = ColorConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.CursorConverter": {
                styleConverterImpl = CursorConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.EffectConverter": {
                styleConverterImpl = EffectConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.EffectConverter$DropShadowConverter": {
                styleConverterImpl = EffectConverter.DropShadowConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.EffectConverter$InnerShadowConverter": {
                styleConverterImpl = EffectConverter.InnerShadowConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.FontConverter": {
                styleConverterImpl = FontConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.FontConverter$FontStyleConverter": 
            case "com.sun.javafx.css.converters.FontConverter$StyleConverter": {
                styleConverterImpl = FontConverter.FontStyleConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.FontConverter$FontWeightConverter": 
            case "com.sun.javafx.css.converters.FontConverter$WeightConverter": {
                styleConverterImpl = FontConverter.FontWeightConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.FontConverter$FontSizeConverter": 
            case "com.sun.javafx.css.converters.FontConverter$SizeConverter": {
                styleConverterImpl = FontConverter.FontSizeConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.InsetsConverter": {
                styleConverterImpl = InsetsConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.InsetsConverter$SequenceConverter": {
                styleConverterImpl = InsetsConverter.SequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.PaintConverter": {
                styleConverterImpl = PaintConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.PaintConverter$SequenceConverter": {
                styleConverterImpl = PaintConverter.SequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.PaintConverter$LinearGradientConverter": {
                styleConverterImpl = PaintConverter.LinearGradientConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.PaintConverter$RadialGradientConverter": {
                styleConverterImpl = PaintConverter.RadialGradientConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.SizeConverter": {
                styleConverterImpl = SizeConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.SizeConverter$SequenceConverter": {
                styleConverterImpl = SizeConverter.SequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.StringConverter": {
                styleConverterImpl = StringConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.StringConverter$SequenceConverter": {
                styleConverterImpl = StringConverter.SequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.URLConverter": {
                styleConverterImpl = URLConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.converters.URLConverter$SequenceConverter": {
                styleConverterImpl = URLConverter.SequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.BackgroundPositionConverter": 
            case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundPositionConverter": {
                styleConverterImpl = BackgroundPositionConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.BackgroundSizeConverter": 
            case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundSizeConverter": {
                styleConverterImpl = BackgroundSizeConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.BorderImageSliceConverter": 
            case "com.sun.javafx.scene.layout.region.BorderImage$SliceConverter": {
                styleConverterImpl = BorderImageSliceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.BorderImageWidthConverter": {
                styleConverterImpl = BorderImageWidthConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter": {
                styleConverterImpl = BorderImageWidthsSequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter": 
            case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderStyleSequenceConverter": {
                styleConverterImpl = BorderStrokeStyleSequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.BorderStyleConverter": 
            case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderStyleConverter": {
                styleConverterImpl = BorderStyleConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter": 
            case "com.sun.javafx.scene.layout.region.BackgroundImage$LayeredBackgroundPositionConverter": {
                styleConverterImpl = LayeredBackgroundPositionConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter": 
            case "com.sun.javafx.scene.layout.region.BackgroundImage$LayeredBackgroundSizeConverter": {
                styleConverterImpl = LayeredBackgroundSizeConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter": 
            case "com.sun.javafx.scene.layout.region.StrokeBorder$LayeredBorderPaintConverter": {
                styleConverterImpl = LayeredBorderPaintConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter": 
            case "com.sun.javafx.scene.layout.region.StrokeBorder$LayeredBorderStyleConverter": {
                styleConverterImpl = LayeredBorderStyleConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.RepeatStructConverter": 
            case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundRepeatConverter": 
            case "com.sun.javafx.scene.layout.region.BorderImage$RepeatConverter": {
                styleConverterImpl = RepeatStructConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.SliceSequenceConverter": 
            case "com.sun.javafx.scene.layout.region.BorderImage$SliceSequenceConverter": {
                styleConverterImpl = SliceSequenceConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter": 
            case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderPaintConverter": {
                styleConverterImpl = StrokeBorderPaintConverter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.Margins$Converter": {
                styleConverterImpl = Margins.Converter.getInstance();
                break;
            }
            case "com.sun.javafx.scene.layout.region.Margins$SequenceConverter": {
                styleConverterImpl = Margins.SequenceConverter.getInstance();
                break;
            }
            case "javafx.scene.layout.CornerRadiiConverter": 
            case "com.sun.javafx.scene.layout.region.CornerRadiiConverter": {
                styleConverterImpl = CornerRadiiConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.parser.DeriveColorConverter": {
                styleConverterImpl = DeriveColorConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.parser.DeriveSizeConverter": {
                styleConverterImpl = DeriveSizeConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.parser.LadderConverter": {
                styleConverterImpl = LadderConverter.getInstance();
                break;
            }
            case "com.sun.javafx.css.parser.StopConverter": {
                styleConverterImpl = StopConverter.getInstance();
                break;
            }
            default: {
                PlatformLogger platformLogger = Logging.getCSSLogger();
                if (!platformLogger.isLoggable(PlatformLogger.Level.SEVERE)) break;
                platformLogger.severe("StyleConverterImpl : converter Class is null for : " + string);
            }
        }
        return styleConverterImpl;
    }
}

