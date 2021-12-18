/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.value.ObservableValue
 *  javafx.css.CssMetaData
 *  javafx.css.FontCssMetaData
 *  javafx.css.SimpleStyleableObjectProperty
 *  javafx.css.StyleOrigin
 *  javafx.css.Styleable
 *  javafx.css.StyleableProperty
 *  javafx.scene.control.Labeled
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.text.Font
 *  javafx.scene.text.Text
 *  javafx.scene.text.TextAlignment
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LabeledText
extends Text {
    private final Labeled labeled;
    private StyleablePropertyMirror<Font> fontMirror = null;
    private static final CssMetaData<LabeledText, Font> FONT = new FontCssMetaData<LabeledText>("-fx-font", Font.getDefault()){

        public boolean isSettable(LabeledText labeledText) {
            return labeledText.labeled != null ? !labeledText.labeled.fontProperty().isBound() : true;
        }

        public StyleableProperty<Font> getStyleableProperty(LabeledText labeledText) {
            return labeledText.fontMirror();
        }
    };
    private StyleablePropertyMirror<Paint> fillMirror;
    private static final CssMetaData<LabeledText, Paint> FILL = new CssMetaData<LabeledText, Paint>("-fx-fill", PaintConverter.getInstance(), (Paint)Color.BLACK){

        public boolean isSettable(LabeledText labeledText) {
            return !labeledText.labeled.textFillProperty().isBound();
        }

        public StyleableProperty<Paint> getStyleableProperty(LabeledText labeledText) {
            return labeledText.fillMirror();
        }
    };
    private StyleablePropertyMirror<TextAlignment> textAlignmentMirror;
    private static final CssMetaData<LabeledText, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<LabeledText, TextAlignment>("-fx-text-alignment", new EnumConverter<TextAlignment>(TextAlignment.class), TextAlignment.LEFT){

        public boolean isSettable(LabeledText labeledText) {
            return !labeledText.labeled.textAlignmentProperty().isBound();
        }

        public StyleableProperty<TextAlignment> getStyleableProperty(LabeledText labeledText) {
            return labeledText.textAlignmentMirror();
        }
    };
    private StyleablePropertyMirror<Boolean> underlineMirror;
    private static final CssMetaData<LabeledText, Boolean> UNDERLINE = new CssMetaData<LabeledText, Boolean>("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE){

        public boolean isSettable(LabeledText labeledText) {
            return !labeledText.labeled.underlineProperty().isBound();
        }

        public StyleableProperty<Boolean> getStyleableProperty(LabeledText labeledText) {
            return labeledText.underlineMirror();
        }
    };
    private StyleablePropertyMirror<Number> lineSpacingMirror;
    private static final CssMetaData<LabeledText, Number> LINE_SPACING = new CssMetaData<LabeledText, Number>("-fx-line-spacing", SizeConverter.getInstance(), (Number)0){

        public boolean isSettable(LabeledText labeledText) {
            return !labeledText.labeled.lineSpacingProperty().isBound();
        }

        public StyleableProperty<Number> getStyleableProperty(LabeledText labeledText) {
            return labeledText.lineSpacingMirror();
        }
    };
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    public LabeledText(Labeled labeled) {
        if (labeled == null) {
            throw new IllegalArgumentException("labeled cannot be null");
        }
        this.labeled = labeled;
        this.setFill(this.labeled.getTextFill());
        this.setFont(this.labeled.getFont());
        this.setTextAlignment(this.labeled.getTextAlignment());
        this.setUnderline(this.labeled.isUnderline());
        this.setLineSpacing(this.labeled.getLineSpacing());
        this.fillProperty().bind((ObservableValue)this.labeled.textFillProperty());
        this.fontProperty().bind((ObservableValue)this.labeled.fontProperty());
        this.textAlignmentProperty().bind((ObservableValue)this.labeled.textAlignmentProperty());
        this.underlineProperty().bind((ObservableValue)this.labeled.underlineProperty());
        this.lineSpacingProperty().bind((ObservableValue)this.labeled.lineSpacingProperty());
        this.getStyleClass().addAll((Object[])new String[]{"text"});
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return LabeledText.getClassCssMetaData();
    }

    private StyleableProperty<Font> fontMirror() {
        if (this.fontMirror == null) {
            this.fontMirror = new StyleablePropertyMirror(FONT, "fontMirror", (Object)Font.getDefault(), (StyleableProperty)this.labeled.fontProperty());
            this.fontProperty().addListener(this.fontMirror);
        }
        return this.fontMirror;
    }

    private StyleableProperty<Paint> fillMirror() {
        if (this.fillMirror == null) {
            this.fillMirror = new StyleablePropertyMirror(FILL, "fillMirror", (Object)Color.BLACK, (StyleableProperty)this.labeled.textFillProperty());
            this.fillProperty().addListener(this.fillMirror);
        }
        return this.fillMirror;
    }

    private StyleableProperty<TextAlignment> textAlignmentMirror() {
        if (this.textAlignmentMirror == null) {
            this.textAlignmentMirror = new StyleablePropertyMirror(TEXT_ALIGNMENT, "textAlignmentMirror", (Object)TextAlignment.LEFT, (StyleableProperty)this.labeled.textAlignmentProperty());
            this.textAlignmentProperty().addListener(this.textAlignmentMirror);
        }
        return this.textAlignmentMirror;
    }

    private StyleableProperty<Boolean> underlineMirror() {
        if (this.underlineMirror == null) {
            this.underlineMirror = new StyleablePropertyMirror(UNDERLINE, "underLineMirror", Boolean.FALSE, (StyleableProperty)this.labeled.underlineProperty());
            this.underlineProperty().addListener(this.underlineMirror);
        }
        return this.underlineMirror;
    }

    private StyleableProperty<Number> lineSpacingMirror() {
        if (this.lineSpacingMirror == null) {
            this.lineSpacingMirror = new StyleablePropertyMirror(LINE_SPACING, "lineSpacingMirror", 0.0, (StyleableProperty)this.labeled.lineSpacingProperty());
            this.lineSpacingProperty().addListener(this.lineSpacingMirror);
        }
        return this.lineSpacingMirror;
    }

    static {
        ArrayList<Object> arrayList = new ArrayList<Object>(Text.getClassCssMetaData());
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            String string = ((CssMetaData)arrayList.get(i)).getProperty();
            if ("-fx-fill".equals(string)) {
                arrayList.set(i, FILL);
                continue;
            }
            if ("-fx-font".equals(string)) {
                arrayList.set(i, FONT);
                continue;
            }
            if ("-fx-text-alignment".equals(string)) {
                arrayList.set(i, TEXT_ALIGNMENT);
                continue;
            }
            if ("-fx-underline".equals(string)) {
                arrayList.set(i, UNDERLINE);
                continue;
            }
            if (!"-fx-line-spacing".equals(string)) continue;
            arrayList.set(i, LINE_SPACING);
        }
        STYLEABLES = Collections.unmodifiableList(arrayList);
    }

    private class StyleablePropertyMirror<T>
    extends SimpleStyleableObjectProperty<T>
    implements InvalidationListener {
        boolean applying;
        private final StyleableProperty<T> property;

        private StyleablePropertyMirror(CssMetaData<LabeledText, T> cssMetaData, String string, T t, StyleableProperty<T> styleableProperty) {
            super(cssMetaData, (Object)LabeledText.this, string, t);
            this.property = styleableProperty;
            this.applying = false;
        }

        public void invalidated(Observable observable) {
            if (!this.applying) {
                super.applyStyle(null, ((ObservableValue)observable).getValue());
            }
        }

        public void applyStyle(StyleOrigin styleOrigin, T t) {
            this.applying = true;
            StyleOrigin styleOrigin2 = this.property.getStyleOrigin();
            if (styleOrigin2 == null || (styleOrigin != null ? styleOrigin2.compareTo((Enum)styleOrigin) <= 0 : styleOrigin2 != StyleOrigin.USER)) {
                super.applyStyle(styleOrigin, t);
                this.property.applyStyle(styleOrigin, t);
            }
            this.applying = false;
        }

        public StyleOrigin getStyleOrigin() {
            return this.property.getStyleOrigin();
        }
    }
}

