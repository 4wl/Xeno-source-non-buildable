/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.css.CssMetaData
 *  javafx.css.StyleOrigin
 *  javafx.css.Styleable
 *  javafx.css.StyleableProperty
 *  javafx.scene.control.Label
 *  javafx.scene.control.Labeled
 *  javafx.scene.layout.Region
 */
package com.sun.javafx.scene.control.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;

public class LabeledImpl
extends Label {
    private final Shuttler shuttler;

    public LabeledImpl(Labeled labeled) {
        this.shuttler = new Shuttler(this, labeled);
    }

    private static void initialize(Shuttler shuttler, LabeledImpl labeledImpl, Labeled labeled) {
        labeledImpl.setText(labeled.getText());
        labeled.textProperty().addListener((InvalidationListener)shuttler);
        labeledImpl.setGraphic(labeled.getGraphic());
        labeled.graphicProperty().addListener((InvalidationListener)shuttler);
        List<CssMetaData<Styleable, ?>> list = StyleableProperties.STYLEABLES_TO_MIRROR;
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            StyleableProperty styleableProperty;
            CssMetaData<? extends Styleable, ?> cssMetaData = list.get(i);
            if ("-fx-skin".equals(cssMetaData.getProperty()) || !((styleableProperty = cssMetaData.getStyleableProperty((Styleable)labeled)) instanceof Observable)) continue;
            ((Observable)styleableProperty).addListener((InvalidationListener)shuttler);
            StyleOrigin styleOrigin = styleableProperty.getStyleOrigin();
            if (styleOrigin == null) continue;
            StyleableProperty styleableProperty2 = cssMetaData.getStyleableProperty((Styleable)labeledImpl);
            styleableProperty2.applyStyle(styleOrigin, styleableProperty.getValue());
        }
    }

    static final class StyleableProperties {
        static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES_TO_MIRROR;

        StyleableProperties() {
        }

        static {
            List list = Labeled.getClassCssMetaData();
            List list2 = Region.getClassCssMetaData();
            ArrayList arrayList = new ArrayList(list);
            arrayList.removeAll(list2);
            STYLEABLES_TO_MIRROR = Collections.unmodifiableList(arrayList);
        }
    }

    private static class Shuttler
    implements InvalidationListener {
        private final LabeledImpl labeledImpl;
        private final Labeled labeled;

        Shuttler(LabeledImpl labeledImpl, Labeled labeled) {
            this.labeledImpl = labeledImpl;
            this.labeled = labeled;
            LabeledImpl.initialize(this, labeledImpl, labeled);
        }

        public void invalidated(Observable observable) {
            StyleableProperty styleableProperty;
            CssMetaData cssMetaData;
            if (observable == this.labeled.textProperty()) {
                this.labeledImpl.setText(this.labeled.getText());
            } else if (observable == this.labeled.graphicProperty()) {
                StyleOrigin styleOrigin = ((StyleableProperty)this.labeled.graphicProperty()).getStyleOrigin();
                if (styleOrigin == null || styleOrigin == StyleOrigin.USER) {
                    this.labeledImpl.setGraphic(this.labeled.getGraphic());
                }
            } else if (observable instanceof StyleableProperty && (cssMetaData = (styleableProperty = (StyleableProperty)observable).getCssMetaData()) != null) {
                StyleOrigin styleOrigin = styleableProperty.getStyleOrigin();
                StyleableProperty styleableProperty2 = cssMetaData.getStyleableProperty((Styleable)this.labeledImpl);
                styleableProperty2.applyStyle(styleOrigin, styleableProperty.getValue());
            }
        }
    }
}

