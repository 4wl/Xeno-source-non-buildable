/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.effect.BlurType
 *  javafx.scene.effect.DropShadow
 *  javafx.scene.effect.Effect
 *  javafx.scene.effect.InnerShadow
 *  javafx.scene.paint.Color
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import java.util.Map;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EffectConverter
extends StyleConverterImpl<ParsedValue[], Effect> {
    private static Map<ParsedValue<ParsedValue[], Effect>, Effect> cache;

    public static StyleConverter<ParsedValue[], Effect> getInstance() {
        return Holder.EFFECT_CONVERTER;
    }

    public Effect convert(ParsedValue<ParsedValue[], Effect> parsedValue, Font font) {
        throw new IllegalArgumentException("Parsed value is not an Effect");
    }

    protected EffectConverter() {
    }

    public String toString() {
        return "EffectConverter";
    }

    public static void clearCache() {
        if (cache != null) {
            cache.clear();
        }
    }

    public static final class InnerShadowConverter
    extends EffectConverter {
        public static InnerShadowConverter getInstance() {
            return Holder.INNER_SHADOW_INSTANCE;
        }

        private InnerShadowConverter() {
        }

        @Override
        public Effect convert(ParsedValue<ParsedValue[], Effect> parsedValue, Font font) {
            Effect effect = (Effect)super.getCachedValue(parsedValue);
            if (effect != null) {
                return effect;
            }
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            BlurType blurType = (BlurType)arrparsedValue[0].convert(font);
            Color color = (Color)arrparsedValue[1].convert(font);
            Double d = ((Size)arrparsedValue[2].convert(font)).pixels(font);
            Double d2 = ((Size)arrparsedValue[3].convert(font)).pixels(font);
            Double d3 = ((Size)arrparsedValue[4].convert(font)).pixels(font);
            Double d4 = ((Size)arrparsedValue[5].convert(font)).pixels(font);
            InnerShadow innerShadow = new InnerShadow();
            if (blurType != null) {
                innerShadow.setBlurType(blurType);
            }
            if (color != null) {
                innerShadow.setColor(color);
            }
            if (d != null) {
                innerShadow.setRadius(d.doubleValue());
            }
            if (d2 != null) {
                innerShadow.setChoke(d2.doubleValue());
            }
            if (d3 != null) {
                innerShadow.setOffsetX(d3.doubleValue());
            }
            if (d4 != null) {
                innerShadow.setOffsetY(d4.doubleValue());
            }
            super.cacheValue(parsedValue, (Object)innerShadow);
            return innerShadow;
        }

        @Override
        public String toString() {
            return "InnerShadowConverter";
        }
    }

    public static final class DropShadowConverter
    extends EffectConverter {
        public static DropShadowConverter getInstance() {
            return Holder.DROP_SHADOW_INSTANCE;
        }

        private DropShadowConverter() {
        }

        @Override
        public Effect convert(ParsedValue<ParsedValue[], Effect> parsedValue, Font font) {
            Effect effect = (Effect)super.getCachedValue(parsedValue);
            if (effect != null) {
                return effect;
            }
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            BlurType blurType = (BlurType)arrparsedValue[0].convert(font);
            Color color = (Color)arrparsedValue[1].convert(font);
            Double d = ((Size)arrparsedValue[2].convert(font)).pixels(font);
            Double d2 = ((Size)arrparsedValue[3].convert(font)).pixels(font);
            Double d3 = ((Size)arrparsedValue[4].convert(font)).pixels(font);
            Double d4 = ((Size)arrparsedValue[5].convert(font)).pixels(font);
            DropShadow dropShadow = new DropShadow();
            if (blurType != null) {
                dropShadow.setBlurType(blurType);
            }
            if (color != null) {
                dropShadow.setColor(color);
            }
            if (d2 != null) {
                dropShadow.setSpread(d2.doubleValue());
            }
            if (d != null) {
                dropShadow.setRadius(d.doubleValue());
            }
            if (d3 != null) {
                dropShadow.setOffsetX(d3.doubleValue());
            }
            if (d4 != null) {
                dropShadow.setOffsetY(d4.doubleValue());
            }
            super.cacheValue(parsedValue, (Object)dropShadow);
            return dropShadow;
        }

        @Override
        public String toString() {
            return "DropShadowConverter";
        }
    }

    private static class Holder {
        static final EffectConverter EFFECT_CONVERTER = new EffectConverter();
        static final DropShadowConverter DROP_SHADOW_INSTANCE = new DropShadowConverter();
        static final InnerShadowConverter INNER_SHADOW_INSTANCE = new InnerShadowConverter();

        private Holder() {
        }
    }
}

