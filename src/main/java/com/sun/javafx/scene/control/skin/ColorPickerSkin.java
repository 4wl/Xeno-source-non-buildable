/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.StringProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ObservableList
 *  javafx.css.CssMetaData
 *  javafx.css.StyleOrigin
 *  javafx.css.Styleable
 *  javafx.css.StyleableBooleanProperty
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableProperty
 *  javafx.css.StyleableStringProperty
 *  javafx.geometry.Pos
 *  javafx.scene.Node
 *  javafx.scene.control.ColorPicker
 *  javafx.scene.control.ComboBoxBase
 *  javafx.scene.control.Label
 *  javafx.scene.control.TextField
 *  javafx.scene.image.ImageView
 *  javafx.scene.layout.StackPane
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Rectangle
 *  javafx.util.StringConverter
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ColorPickerBehavior;
import com.sun.javafx.scene.control.skin.ColorPalette;
import com.sun.javafx.scene.control.skin.ComboBoxBaseSkin;
import com.sun.javafx.scene.control.skin.ComboBoxMode;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

public class ColorPickerSkin
extends ComboBoxPopupControl<Color> {
    private Label displayNode;
    private StackPane pickerColorBox;
    private Rectangle colorRect;
    private ColorPalette popupContent;
    BooleanProperty colorLabelVisible = new StyleableBooleanProperty(true){

        public void invalidated() {
            if (ColorPickerSkin.this.displayNode != null) {
                if (ColorPickerSkin.this.colorLabelVisible.get()) {
                    ColorPickerSkin.this.displayNode.setText(ColorPickerSkin.colorDisplayName((Color)((ColorPicker)ColorPickerSkin.this.getSkinnable()).getValue()));
                } else {
                    ColorPickerSkin.this.displayNode.setText("");
                }
            }
        }

        public Object getBean() {
            return ColorPickerSkin.this;
        }

        public String getName() {
            return "colorLabelVisible";
        }

        public CssMetaData<ColorPicker, Boolean> getCssMetaData() {
            return StyleableProperties.COLOR_LABEL_VISIBLE;
        }
    };
    private final StyleableStringProperty imageUrl = new StyleableStringProperty(){

        public void applyStyle(StyleOrigin styleOrigin, String string) {
            super.applyStyle(styleOrigin, string);
            if (string == null) {
                if (ColorPickerSkin.this.pickerColorBox.getChildren().size() == 2) {
                    ColorPickerSkin.this.pickerColorBox.getChildren().remove(1);
                }
            } else if (ColorPickerSkin.this.pickerColorBox.getChildren().size() == 2) {
                ImageView imageView = (ImageView)ColorPickerSkin.this.pickerColorBox.getChildren().get(1);
                imageView.setImage(StyleManager.getInstance().getCachedImage(string));
            } else {
                ColorPickerSkin.this.pickerColorBox.getChildren().add((Object)new ImageView(StyleManager.getInstance().getCachedImage(string)));
            }
        }

        public Object getBean() {
            return ColorPickerSkin.this;
        }

        public String getName() {
            return "imageUrl";
        }

        public CssMetaData<ColorPicker, String> getCssMetaData() {
            return StyleableProperties.GRAPHIC;
        }
    };
    private final StyleableDoubleProperty colorRectWidth = new StyleableDoubleProperty(12.0){

        protected void invalidated() {
            if (ColorPickerSkin.this.pickerColorBox != null) {
                ColorPickerSkin.this.pickerColorBox.requestLayout();
            }
        }

        public CssMetaData<ColorPicker, Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_WIDTH;
        }

        public Object getBean() {
            return ColorPickerSkin.this;
        }

        public String getName() {
            return "colorRectWidth";
        }
    };
    private final StyleableDoubleProperty colorRectHeight = new StyleableDoubleProperty(12.0){

        protected void invalidated() {
            if (ColorPickerSkin.this.pickerColorBox != null) {
                ColorPickerSkin.this.pickerColorBox.requestLayout();
            }
        }

        public CssMetaData<ColorPicker, Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_HEIGHT;
        }

        public Object getBean() {
            return ColorPickerSkin.this;
        }

        public String getName() {
            return "colorRectHeight";
        }
    };
    private final StyleableDoubleProperty colorRectX = new StyleableDoubleProperty(0.0){

        protected void invalidated() {
            if (ColorPickerSkin.this.pickerColorBox != null) {
                ColorPickerSkin.this.pickerColorBox.requestLayout();
            }
        }

        public CssMetaData<ColorPicker, Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_X;
        }

        public Object getBean() {
            return ColorPickerSkin.this;
        }

        public String getName() {
            return "colorRectX";
        }
    };
    private final StyleableDoubleProperty colorRectY = new StyleableDoubleProperty(0.0){

        protected void invalidated() {
            if (ColorPickerSkin.this.pickerColorBox != null) {
                ColorPickerSkin.this.pickerColorBox.requestLayout();
            }
        }

        public CssMetaData<ColorPicker, Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_Y;
        }

        public Object getBean() {
            return ColorPickerSkin.this;
        }

        public String getName() {
            return "colorRectY";
        }
    };
    private static final Map<Color, String> colorNameMap = new HashMap<Color, String>(24);
    private static final Map<Color, String> cssNameMap = new HashMap<Color, String>(139);

    public StringProperty imageUrlProperty() {
        return this.imageUrl;
    }

    public ColorPickerSkin(ColorPicker colorPicker) {
        super(colorPicker, new ColorPickerBehavior(colorPicker));
        this.updateComboBoxMode();
        this.registerChangeListener((ObservableValue<?>)colorPicker.valueProperty(), "VALUE");
        this.displayNode = new Label();
        this.displayNode.getStyleClass().add((Object)"color-picker-label");
        this.displayNode.setManaged(false);
        this.pickerColorBox = new PickerColorBox();
        this.pickerColorBox.getStyleClass().add((Object)"picker-color");
        this.colorRect = new Rectangle(12.0, 12.0);
        this.colorRect.getStyleClass().add((Object)"picker-color-rect");
        this.updateColor();
        this.pickerColorBox.getChildren().add((Object)this.colorRect);
        this.displayNode.setGraphic((Node)this.pickerColorBox);
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        if (!this.colorLabelVisible.get()) {
            return super.computePrefWidth(d, d2, d3, d4, d5);
        }
        String string = this.displayNode.getText();
        double d6 = 0.0;
        for (String string2 : colorNameMap.values()) {
            this.displayNode.setText(string2);
            d6 = Math.max(d6, super.computePrefWidth(d, d2, d3, d4, d5));
        }
        this.displayNode.setText(ColorPickerSkin.formatHexString(Color.BLACK));
        d6 = Math.max(d6, super.computePrefWidth(d, d2, d3, d4, d5));
        this.displayNode.setText(string);
        return d6;
    }

    private void updateComboBoxMode() {
        ObservableList observableList = ((ComboBoxBase)this.getSkinnable()).getStyleClass();
        if (observableList.contains("button")) {
            this.setMode(ComboBoxMode.BUTTON);
        } else if (observableList.contains("split-button")) {
            this.setMode(ComboBoxMode.SPLITBUTTON);
        }
    }

    static String colorDisplayName(Color color) {
        if (color != null) {
            String string = colorNameMap.get((Object)color);
            if (string == null) {
                string = ColorPickerSkin.formatHexString(color);
            }
            return string;
        }
        return null;
    }

    static String tooltipString(Color color) {
        if (color != null) {
            String string = "";
            String string2 = colorNameMap.get((Object)color);
            if (string2 != null) {
                string = string + string2 + " ";
            }
            string = string + ColorPickerSkin.formatHexString(color);
            String string3 = cssNameMap.get((Object)color);
            if (string3 != null) {
                string = string + " (css: " + string3 + ")";
            }
            return string;
        }
        return null;
    }

    static String formatHexString(Color color) {
        if (color != null) {
            return String.format((Locale)null, "#%02x%02x%02x", Math.round(color.getRed() * 255.0), Math.round(color.getGreen() * 255.0), Math.round(color.getBlue() * 255.0));
        }
        return null;
    }

    @Override
    protected Node getPopupContent() {
        if (this.popupContent == null) {
            this.popupContent = new ColorPalette((ColorPicker)this.getSkinnable());
            this.popupContent.setPopupControl(this.getPopup());
        }
        return this.popupContent;
    }

    @Override
    protected void focusLost() {
    }

    @Override
    public void show() {
        super.show();
        ColorPicker colorPicker = (ColorPicker)this.getSkinnable();
        this.popupContent.updateSelection((Color)colorPicker.getValue());
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("SHOWING".equals(string)) {
            if (((ComboBoxBase)this.getSkinnable()).isShowing()) {
                this.show();
            } else if (!this.popupContent.isCustomColorDialogShowing()) {
                this.hide();
            }
        } else if ("VALUE".equals(string)) {
            this.updateColor();
            if (this.popupContent != null) {
                // empty if block
            }
        }
    }

    @Override
    public Node getDisplayNode() {
        return this.displayNode;
    }

    private void updateColor() {
        ColorPicker colorPicker = (ColorPicker)this.getSkinnable();
        this.colorRect.setFill((Paint)colorPicker.getValue());
        if (this.colorLabelVisible.get()) {
            this.displayNode.setText(ColorPickerSkin.colorDisplayName((Color)colorPicker.getValue()));
        } else {
            this.displayNode.setText("");
        }
    }

    public void syncWithAutoUpdate() {
        if (!this.getPopup().isShowing() && ((ComboBoxBase)this.getSkinnable()).isShowing()) {
            ((ComboBoxBase)this.getSkinnable()).hide();
        }
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        this.updateComboBoxMode();
        super.layoutChildren(d, d2, d3, d4);
    }

    static String getString(String string) {
        return ControlResources.getString("ColorPicker." + string);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ColorPickerSkin.getClassCssMetaData();
    }

    @Override
    protected StringConverter<Color> getConverter() {
        return null;
    }

    @Override
    protected TextField getEditor() {
        return null;
    }

    static {
        colorNameMap.put(Color.TRANSPARENT, ColorPickerSkin.getString("colorName.transparent"));
        colorNameMap.put(Color.BLACK, ColorPickerSkin.getString("colorName.black"));
        colorNameMap.put(Color.BLUE, ColorPickerSkin.getString("colorName.blue"));
        colorNameMap.put(Color.CYAN, ColorPickerSkin.getString("colorName.cyan"));
        colorNameMap.put(Color.DARKBLUE, ColorPickerSkin.getString("colorName.darkblue"));
        colorNameMap.put(Color.DARKCYAN, ColorPickerSkin.getString("colorName.darkcyan"));
        colorNameMap.put(Color.DARKGRAY, ColorPickerSkin.getString("colorName.darkgray"));
        colorNameMap.put(Color.DARKGREEN, ColorPickerSkin.getString("colorName.darkgreen"));
        colorNameMap.put(Color.DARKMAGENTA, ColorPickerSkin.getString("colorName.darkmagenta"));
        colorNameMap.put(Color.DARKRED, ColorPickerSkin.getString("colorName.darkred"));
        colorNameMap.put(Color.GRAY, ColorPickerSkin.getString("colorName.gray"));
        colorNameMap.put(Color.GREEN, ColorPickerSkin.getString("colorName.green"));
        colorNameMap.put(Color.LIGHTBLUE, ColorPickerSkin.getString("colorName.lightblue"));
        colorNameMap.put(Color.LIGHTCYAN, ColorPickerSkin.getString("colorName.lightcyan"));
        colorNameMap.put(Color.LIGHTGRAY, ColorPickerSkin.getString("colorName.lightgray"));
        colorNameMap.put(Color.LIGHTGREEN, ColorPickerSkin.getString("colorName.lightgreen"));
        colorNameMap.put(Color.LIGHTYELLOW, ColorPickerSkin.getString("colorName.lightyellow"));
        colorNameMap.put(Color.MAGENTA, ColorPickerSkin.getString("colorName.magenta"));
        colorNameMap.put(Color.MEDIUMBLUE, ColorPickerSkin.getString("colorName.mediumblue"));
        colorNameMap.put(Color.ORANGE, ColorPickerSkin.getString("colorName.orange"));
        colorNameMap.put(Color.PINK, ColorPickerSkin.getString("colorName.pink"));
        colorNameMap.put(Color.RED, ColorPickerSkin.getString("colorName.red"));
        colorNameMap.put(Color.WHITE, ColorPickerSkin.getString("colorName.white"));
        colorNameMap.put(Color.YELLOW, ColorPickerSkin.getString("colorName.yellow"));
        cssNameMap.put(Color.ALICEBLUE, "aliceblue");
        cssNameMap.put(Color.ANTIQUEWHITE, "antiquewhite");
        cssNameMap.put(Color.AQUAMARINE, "aquamarine");
        cssNameMap.put(Color.AZURE, "azure");
        cssNameMap.put(Color.BEIGE, "beige");
        cssNameMap.put(Color.BISQUE, "bisque");
        cssNameMap.put(Color.BLACK, "black");
        cssNameMap.put(Color.BLANCHEDALMOND, "blanchedalmond");
        cssNameMap.put(Color.BLUE, "blue");
        cssNameMap.put(Color.BLUEVIOLET, "blueviolet");
        cssNameMap.put(Color.BROWN, "brown");
        cssNameMap.put(Color.BURLYWOOD, "burlywood");
        cssNameMap.put(Color.CADETBLUE, "cadetblue");
        cssNameMap.put(Color.CHARTREUSE, "chartreuse");
        cssNameMap.put(Color.CHOCOLATE, "chocolate");
        cssNameMap.put(Color.CORAL, "coral");
        cssNameMap.put(Color.CORNFLOWERBLUE, "cornflowerblue");
        cssNameMap.put(Color.CORNSILK, "cornsilk");
        cssNameMap.put(Color.CRIMSON, "crimson");
        cssNameMap.put(Color.CYAN, "cyan");
        cssNameMap.put(Color.DARKBLUE, "darkblue");
        cssNameMap.put(Color.DARKCYAN, "darkcyan");
        cssNameMap.put(Color.DARKGOLDENROD, "darkgoldenrod");
        cssNameMap.put(Color.DARKGRAY, "darkgray");
        cssNameMap.put(Color.DARKGREEN, "darkgreen");
        cssNameMap.put(Color.DARKKHAKI, "darkkhaki");
        cssNameMap.put(Color.DARKMAGENTA, "darkmagenta");
        cssNameMap.put(Color.DARKOLIVEGREEN, "darkolivegreen");
        cssNameMap.put(Color.DARKORANGE, "darkorange");
        cssNameMap.put(Color.DARKORCHID, "darkorchid");
        cssNameMap.put(Color.DARKRED, "darkred");
        cssNameMap.put(Color.DARKSALMON, "darksalmon");
        cssNameMap.put(Color.DARKSEAGREEN, "darkseagreen");
        cssNameMap.put(Color.DARKSLATEBLUE, "darkslateblue");
        cssNameMap.put(Color.DARKSLATEGRAY, "darkslategray");
        cssNameMap.put(Color.DARKTURQUOISE, "darkturquoise");
        cssNameMap.put(Color.DARKVIOLET, "darkviolet");
        cssNameMap.put(Color.DEEPPINK, "deeppink");
        cssNameMap.put(Color.DEEPSKYBLUE, "deepskyblue");
        cssNameMap.put(Color.DIMGRAY, "dimgray");
        cssNameMap.put(Color.DODGERBLUE, "dodgerblue");
        cssNameMap.put(Color.FIREBRICK, "firebrick");
        cssNameMap.put(Color.FLORALWHITE, "floralwhite");
        cssNameMap.put(Color.FORESTGREEN, "forestgreen");
        cssNameMap.put(Color.GAINSBORO, "gainsboro");
        cssNameMap.put(Color.GHOSTWHITE, "ghostwhite");
        cssNameMap.put(Color.GOLD, "gold");
        cssNameMap.put(Color.GOLDENROD, "goldenrod");
        cssNameMap.put(Color.GRAY, "gray");
        cssNameMap.put(Color.GREEN, "green");
        cssNameMap.put(Color.GREENYELLOW, "greenyellow");
        cssNameMap.put(Color.HONEYDEW, "honeydew");
        cssNameMap.put(Color.HOTPINK, "hotpink");
        cssNameMap.put(Color.INDIANRED, "indianred");
        cssNameMap.put(Color.INDIGO, "indigo");
        cssNameMap.put(Color.IVORY, "ivory");
        cssNameMap.put(Color.KHAKI, "khaki");
        cssNameMap.put(Color.LAVENDER, "lavender");
        cssNameMap.put(Color.LAVENDERBLUSH, "lavenderblush");
        cssNameMap.put(Color.LAWNGREEN, "lawngreen");
        cssNameMap.put(Color.LEMONCHIFFON, "lemonchiffon");
        cssNameMap.put(Color.LIGHTBLUE, "lightblue");
        cssNameMap.put(Color.LIGHTCORAL, "lightcoral");
        cssNameMap.put(Color.LIGHTCYAN, "lightcyan");
        cssNameMap.put(Color.LIGHTGOLDENRODYELLOW, "lightgoldenrodyellow");
        cssNameMap.put(Color.LIGHTGRAY, "lightgray");
        cssNameMap.put(Color.LIGHTGREEN, "lightgreen");
        cssNameMap.put(Color.LIGHTPINK, "lightpink");
        cssNameMap.put(Color.LIGHTSALMON, "lightsalmon");
        cssNameMap.put(Color.LIGHTSEAGREEN, "lightseagreen");
        cssNameMap.put(Color.LIGHTSKYBLUE, "lightskyblue");
        cssNameMap.put(Color.LIGHTSLATEGRAY, "lightslategray");
        cssNameMap.put(Color.LIGHTSTEELBLUE, "lightsteelblue");
        cssNameMap.put(Color.LIGHTYELLOW, "lightyellow");
        cssNameMap.put(Color.LIME, "lime");
        cssNameMap.put(Color.LIMEGREEN, "limegreen");
        cssNameMap.put(Color.LINEN, "linen");
        cssNameMap.put(Color.MAGENTA, "magenta");
        cssNameMap.put(Color.MAROON, "maroon");
        cssNameMap.put(Color.MEDIUMAQUAMARINE, "mediumaquamarine");
        cssNameMap.put(Color.MEDIUMBLUE, "mediumblue");
        cssNameMap.put(Color.MEDIUMORCHID, "mediumorchid");
        cssNameMap.put(Color.MEDIUMPURPLE, "mediumpurple");
        cssNameMap.put(Color.MEDIUMSEAGREEN, "mediumseagreen");
        cssNameMap.put(Color.MEDIUMSLATEBLUE, "mediumslateblue");
        cssNameMap.put(Color.MEDIUMSPRINGGREEN, "mediumspringgreen");
        cssNameMap.put(Color.MEDIUMTURQUOISE, "mediumturquoise");
        cssNameMap.put(Color.MEDIUMVIOLETRED, "mediumvioletred");
        cssNameMap.put(Color.MIDNIGHTBLUE, "midnightblue");
        cssNameMap.put(Color.MINTCREAM, "mintcream");
        cssNameMap.put(Color.MISTYROSE, "mistyrose");
        cssNameMap.put(Color.MOCCASIN, "moccasin");
        cssNameMap.put(Color.NAVAJOWHITE, "navajowhite");
        cssNameMap.put(Color.NAVY, "navy");
        cssNameMap.put(Color.OLDLACE, "oldlace");
        cssNameMap.put(Color.OLIVE, "olive");
        cssNameMap.put(Color.OLIVEDRAB, "olivedrab");
        cssNameMap.put(Color.ORANGE, "orange");
        cssNameMap.put(Color.ORANGERED, "orangered");
        cssNameMap.put(Color.ORCHID, "orchid");
        cssNameMap.put(Color.PALEGOLDENROD, "palegoldenrod");
        cssNameMap.put(Color.PALEGREEN, "palegreen");
        cssNameMap.put(Color.PALETURQUOISE, "paleturquoise");
        cssNameMap.put(Color.PALEVIOLETRED, "palevioletred");
        cssNameMap.put(Color.PAPAYAWHIP, "papayawhip");
        cssNameMap.put(Color.PEACHPUFF, "peachpuff");
        cssNameMap.put(Color.PERU, "peru");
        cssNameMap.put(Color.PINK, "pink");
        cssNameMap.put(Color.PLUM, "plum");
        cssNameMap.put(Color.POWDERBLUE, "powderblue");
        cssNameMap.put(Color.PURPLE, "purple");
        cssNameMap.put(Color.RED, "red");
        cssNameMap.put(Color.ROSYBROWN, "rosybrown");
        cssNameMap.put(Color.ROYALBLUE, "royalblue");
        cssNameMap.put(Color.SADDLEBROWN, "saddlebrown");
        cssNameMap.put(Color.SALMON, "salmon");
        cssNameMap.put(Color.SANDYBROWN, "sandybrown");
        cssNameMap.put(Color.SEAGREEN, "seagreen");
        cssNameMap.put(Color.SEASHELL, "seashell");
        cssNameMap.put(Color.SIENNA, "sienna");
        cssNameMap.put(Color.SILVER, "silver");
        cssNameMap.put(Color.SKYBLUE, "skyblue");
        cssNameMap.put(Color.SLATEBLUE, "slateblue");
        cssNameMap.put(Color.SLATEGRAY, "slategray");
        cssNameMap.put(Color.SNOW, "snow");
        cssNameMap.put(Color.SPRINGGREEN, "springgreen");
        cssNameMap.put(Color.STEELBLUE, "steelblue");
        cssNameMap.put(Color.TAN, "tan");
        cssNameMap.put(Color.TEAL, "teal");
        cssNameMap.put(Color.THISTLE, "thistle");
        cssNameMap.put(Color.TOMATO, "tomato");
        cssNameMap.put(Color.TRANSPARENT, "transparent");
        cssNameMap.put(Color.TURQUOISE, "turquoise");
        cssNameMap.put(Color.VIOLET, "violet");
        cssNameMap.put(Color.WHEAT, "wheat");
        cssNameMap.put(Color.WHITE, "white");
        cssNameMap.put(Color.WHITESMOKE, "whitesmoke");
        cssNameMap.put(Color.YELLOW, "yellow");
        cssNameMap.put(Color.YELLOWGREEN, "yellowgreen");
    }

    private static class StyleableProperties {
        private static final CssMetaData<ColorPicker, Boolean> COLOR_LABEL_VISIBLE = new CssMetaData<ColorPicker, Boolean>("-fx-color-label-visible", BooleanConverter.getInstance(), Boolean.TRUE){

            public boolean isSettable(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return colorPickerSkin.colorLabelVisible == null || !colorPickerSkin.colorLabelVisible.isBound();
            }

            public StyleableProperty<Boolean> getStyleableProperty(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return (StyleableProperty)colorPickerSkin.colorLabelVisible;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_WIDTH = new CssMetaData<ColorPicker, Number>("-fx-color-rect-width", SizeConverter.getInstance(), (Number)12.0){

            public boolean isSettable(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return !colorPickerSkin.colorRectWidth.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return colorPickerSkin.colorRectWidth;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_HEIGHT = new CssMetaData<ColorPicker, Number>("-fx-color-rect-height", SizeConverter.getInstance(), (Number)12.0){

            public boolean isSettable(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return !colorPickerSkin.colorRectHeight.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return colorPickerSkin.colorRectHeight;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_X = new CssMetaData<ColorPicker, Number>("-fx-color-rect-x", SizeConverter.getInstance(), (Number)0){

            public boolean isSettable(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return !colorPickerSkin.colorRectX.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return colorPickerSkin.colorRectX;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_Y = new CssMetaData<ColorPicker, Number>("-fx-color-rect-y", SizeConverter.getInstance(), (Number)0){

            public boolean isSettable(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return !colorPickerSkin.colorRectY.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return colorPickerSkin.colorRectY;
            }
        };
        private static final CssMetaData<ColorPicker, String> GRAPHIC = new CssMetaData<ColorPicker, String>("-fx-graphic", com.sun.javafx.css.converters.StringConverter.getInstance()){

            public boolean isSettable(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return !colorPickerSkin.imageUrl.isBound();
            }

            public StyleableProperty<String> getStyleableProperty(ColorPicker colorPicker) {
                ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
                return colorPickerSkin.imageUrl;
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList<Object> arrayList = new ArrayList<Object>(ComboBoxBaseSkin.getClassCssMetaData());
            arrayList.add(COLOR_LABEL_VISIBLE);
            arrayList.add(COLOR_RECT_WIDTH);
            arrayList.add(COLOR_RECT_HEIGHT);
            arrayList.add(COLOR_RECT_X);
            arrayList.add(COLOR_RECT_Y);
            arrayList.add(GRAPHIC);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    private class PickerColorBox
    extends StackPane {
        private PickerColorBox() {
        }

        protected void layoutChildren() {
            double d = this.snappedTopInset();
            double d2 = this.snappedLeftInset();
            double d3 = this.getWidth();
            double d4 = this.getHeight();
            double d5 = this.snappedRightInset();
            double d6 = this.snappedBottomInset();
            ColorPickerSkin.this.colorRect.setX(this.snapPosition(ColorPickerSkin.this.colorRectX.get()));
            ColorPickerSkin.this.colorRect.setY(this.snapPosition(ColorPickerSkin.this.colorRectY.get()));
            ColorPickerSkin.this.colorRect.setWidth(this.snapSize(ColorPickerSkin.this.colorRectWidth.get()));
            ColorPickerSkin.this.colorRect.setHeight(this.snapSize(ColorPickerSkin.this.colorRectHeight.get()));
            if (this.getChildren().size() == 2) {
                ImageView imageView = (ImageView)this.getChildren().get(1);
                Pos pos = StackPane.getAlignment((Node)imageView);
                this.layoutInArea((Node)imageView, d2, d, d3 - d2 - d5, d4 - d - d6, 0.0, PickerColorBox.getMargin((Node)imageView), pos != null ? pos.getHpos() : this.getAlignment().getHpos(), pos != null ? pos.getVpos() : this.getAlignment().getVpos());
                ColorPickerSkin.this.colorRect.setLayoutX(imageView.getLayoutX());
                ColorPickerSkin.this.colorRect.setLayoutY(imageView.getLayoutY());
            } else {
                Pos pos = StackPane.getAlignment((Node)ColorPickerSkin.this.colorRect);
                this.layoutInArea((Node)ColorPickerSkin.this.colorRect, d2, d, d3 - d2 - d5, d4 - d - d6, 0.0, PickerColorBox.getMargin((Node)ColorPickerSkin.this.colorRect), pos != null ? pos.getHpos() : this.getAlignment().getHpos(), pos != null ? pos.getVpos() : this.getAlignment().getVpos());
            }
        }
    }
}

