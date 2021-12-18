/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.Node
 *  javafx.scene.control.CheckBox
 *  javafx.scene.layout.StackPane
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;

public class CheckBoxSkin
extends LabeledSkinBase<CheckBox, ButtonBehavior<CheckBox>> {
    private final StackPane box = new StackPane();
    private StackPane innerbox;

    public CheckBoxSkin(CheckBox checkBox) {
        super(checkBox, new ButtonBehavior<CheckBox>(checkBox));
        this.box.getStyleClass().setAll((Object[])new String[]{"box"});
        this.innerbox = new StackPane();
        this.innerbox.getStyleClass().setAll((Object[])new String[]{"mark"});
        this.innerbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.box.getChildren().add((Object)this.innerbox);
        this.updateChildren();
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        if (this.box != null) {
            this.getChildren().add((Object)this.box);
        }
    }

    @Override
    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        return super.computeMinWidth(d, d2, d3, d4, d5) + this.snapSize(this.box.minWidth(-1.0));
    }

    @Override
    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return Math.max(super.computeMinHeight(d - this.box.minWidth(-1.0), d2, d3, d4, d5), d2 + this.box.minHeight(-1.0) + d4);
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        return super.computePrefWidth(d, d2, d3, d4, d5) + this.snapSize(this.box.prefWidth(-1.0));
    }

    @Override
    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return Math.max(super.computePrefHeight(d - this.box.prefWidth(-1.0), d2, d3, d4, d5), d2 + this.box.prefHeight(-1.0) + d4);
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        CheckBox checkBox = (CheckBox)this.getSkinnable();
        double d5 = this.snapSize(this.box.prefWidth(-1.0));
        double d6 = this.snapSize(this.box.prefHeight(-1.0));
        double d7 = Math.max(checkBox.prefWidth(-1.0), checkBox.minWidth(-1.0));
        double d8 = Math.min(d7 - d5, d3 - this.snapSize(d5));
        double d9 = Math.min(checkBox.prefHeight(d8), d4);
        double d10 = Math.max(d6, d9);
        double d11 = Utils.computeXOffset(d3, d8 + d5, checkBox.getAlignment().getHpos()) + d;
        double d12 = Utils.computeYOffset(d4, d10, checkBox.getAlignment().getVpos()) + d;
        this.layoutLabelInArea(d11 + d5, d12, d8, d10, checkBox.getAlignment());
        this.box.resize(d5, d6);
        this.positionInArea((Node)this.box, d11, d12, d5, d10, 0.0, checkBox.getAlignment().getHpos(), checkBox.getAlignment().getVpos());
    }
}

