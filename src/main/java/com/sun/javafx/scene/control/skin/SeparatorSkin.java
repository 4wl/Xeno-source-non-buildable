/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.control.Separator
 *  javafx.scene.layout.Region
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import java.util.Collections;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

public class SeparatorSkin
extends BehaviorSkinBase<Separator, BehaviorBase<Separator>> {
    private static final double DEFAULT_LENGTH = 10.0;
    private final Region line = new Region();

    public SeparatorSkin(Separator separator) {
        super(separator, new BehaviorBase<Separator>(separator, Collections.emptyList()));
        this.line.getStyleClass().setAll((Object[])new String[]{"line"});
        this.getChildren().add((Object)this.line);
        this.registerChangeListener((ObservableValue<?>)separator.orientationProperty(), "ORIENTATION");
        this.registerChangeListener((ObservableValue<?>)separator.halignmentProperty(), "HALIGNMENT");
        this.registerChangeListener((ObservableValue<?>)separator.valignmentProperty(), "VALIGNMENT");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ORIENTATION".equals(string) || "HALIGNMENT".equals(string) || "VALIGNMENT".equals(string)) {
            ((Separator)this.getSkinnable()).requestLayout();
        }
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        Separator separator = (Separator)this.getSkinnable();
        if (separator.getOrientation() == Orientation.HORIZONTAL) {
            this.line.resize(d3, this.line.prefHeight(-1.0));
        } else {
            this.line.resize(this.line.prefWidth(-1.0), d4);
        }
        this.positionInArea((Node)this.line, d, d2, d3, d4, 0.0, separator.getHalignment(), separator.getValignment());
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        return this.computePrefWidth(d, d2, d3, d4, d5);
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return this.computePrefHeight(d, d2, d3, d4, d5);
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        Separator separator = (Separator)this.getSkinnable();
        double d6 = separator.getOrientation() == Orientation.VERTICAL ? this.line.prefWidth(-1.0) : 10.0;
        return d6 + d5 + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        Separator separator = (Separator)this.getSkinnable();
        double d6 = separator.getOrientation() == Orientation.VERTICAL ? 10.0 : this.line.prefHeight(-1.0);
        return d6 + d2 + d4;
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        Separator separator = (Separator)this.getSkinnable();
        return separator.getOrientation() == Orientation.VERTICAL ? separator.prefWidth(d) : Double.MAX_VALUE;
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        Separator separator = (Separator)this.getSkinnable();
        return separator.getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : separator.prefHeight(d);
    }
}

