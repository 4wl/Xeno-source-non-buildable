/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.control.RadioButton
 *  javafx.scene.layout.StackPane
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ToggleButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;

public class RadioButtonSkin
extends LabeledSkinBase<RadioButton, ToggleButtonBehavior<RadioButton>> {
    private StackPane radio = RadioButtonSkin.createRadio();

    public RadioButtonSkin(RadioButton radioButton) {
        super(radioButton, new ToggleButtonBehavior<RadioButton>(radioButton));
        this.updateChildren();
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        if (this.radio != null) {
            this.getChildren().add((Object)this.radio);
        }
    }

    private static StackPane createRadio() {
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().setAll((Object[])new String[]{"radio"});
        stackPane.setSnapToPixel(false);
        StackPane stackPane2 = new StackPane();
        stackPane2.getStyleClass().setAll((Object[])new String[]{"dot"});
        stackPane.getChildren().clear();
        stackPane.getChildren().addAll((Object[])new Node[]{stackPane2});
        return stackPane;
    }

    @Override
    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        return super.computeMinWidth(d, d2, d3, d4, d5) + this.snapSize(this.radio.minWidth(-1.0));
    }

    @Override
    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return Math.max(this.snapSize(super.computeMinHeight(d - this.radio.minWidth(-1.0), d2, d3, d4, d5)), d2 + this.radio.minHeight(-1.0) + d4);
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        return super.computePrefWidth(d, d2, d3, d4, d5) + this.snapSize(this.radio.prefWidth(-1.0));
    }

    @Override
    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return Math.max(this.snapSize(super.computePrefHeight(d - this.radio.prefWidth(-1.0), d2, d3, d4, d5)), d2 + this.radio.prefHeight(-1.0) + d4);
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        RadioButton radioButton = (RadioButton)this.getSkinnable();
        double d5 = this.radio.prefWidth(-1.0);
        double d6 = this.radio.prefHeight(-1.0);
        double d7 = Math.max(radioButton.prefWidth(-1.0), radioButton.minWidth(-1.0));
        double d8 = Math.min(d7 - d5, d3 - this.snapSize(d5));
        double d9 = Math.min(radioButton.prefHeight(d8), d4);
        double d10 = Math.max(d6, d9);
        double d11 = Utils.computeXOffset(d3, d8 + d5, radioButton.getAlignment().getHpos()) + d;
        double d12 = Utils.computeYOffset(d4, d10, radioButton.getAlignment().getVpos()) + d2;
        this.layoutLabelInArea(d11 + d5, d12, d8, d10, radioButton.getAlignment());
        this.radio.resize(this.snapSize(d5), this.snapSize(d6));
        this.positionInArea((Node)this.radio, d11, d12, d5, d10, 0.0, radioButton.getAlignment().getHpos(), radioButton.getAlignment().getVpos());
    }
}

