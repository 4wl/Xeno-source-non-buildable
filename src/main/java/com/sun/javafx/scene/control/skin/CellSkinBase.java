/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.ReadOnlyDoubleProperty
 *  javafx.css.CssMetaData
 *  javafx.css.StyleOrigin
 *  javafx.css.Styleable
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableProperty
 *  javafx.scene.control.Cell
 *  javafx.scene.control.SkinBase
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Cell;
import javafx.scene.control.SkinBase;

public class CellSkinBase<C extends Cell, B extends BehaviorBase<C>>
extends LabeledSkinBase<C, B> {
    private DoubleProperty cellSize;
    static final double DEFAULT_CELL_SIZE = 24.0;

    public final double getCellSize() {
        return this.cellSize == null ? 24.0 : this.cellSize.get();
    }

    public final ReadOnlyDoubleProperty cellSizeProperty() {
        return this.cellSizePropertyImpl();
    }

    private DoubleProperty cellSizePropertyImpl() {
        if (this.cellSize == null) {
            this.cellSize = new StyleableDoubleProperty(24.0){

                public void applyStyle(StyleOrigin styleOrigin, Number number) {
                    double d = number == null ? 24.0 : number.doubleValue();
                    super.applyStyle(styleOrigin, (Number)(d <= 0.0 ? 24.0 : d));
                }

                public void set(double d) {
                    super.set(d);
                    ((Cell)CellSkinBase.this.getSkinnable()).requestLayout();
                }

                public Object getBean() {
                    return CellSkinBase.this;
                }

                public String getName() {
                    return "cellSize";
                }

                public CssMetaData<Cell<?>, Number> getCssMetaData() {
                    return StyleableProperties.CELL_SIZE;
                }
            };
        }
        return this.cellSize;
    }

    public CellSkinBase(C c, B b) {
        super(c, b);
        this.consumeMouseEvents(false);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return CellSkinBase.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<Cell<?>, Number> CELL_SIZE = new CssMetaData<Cell<?>, Number>("-fx-cell-size", SizeConverter.getInstance(), 24.0){

            public boolean isSettable(Cell<?> cell) {
                CellSkinBase cellSkinBase = (CellSkinBase)cell.getSkin();
                return cellSkinBase.cellSize == null || !cellSkinBase.cellSize.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(Cell<?> cell) {
                CellSkinBase cellSkinBase = (CellSkinBase)cell.getSkin();
                return (StyleableProperty)cellSkinBase.cellSizePropertyImpl();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(SkinBase.getClassCssMetaData());
            arrayList.add(CELL_SIZE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

