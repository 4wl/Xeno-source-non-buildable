/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.Label
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import javafx.scene.control.Label;

public class UITextArea
extends Label {
    double preferred_width = 360.0;

    public UITextArea(String string) {
        this.setText(string);
        this.setPrefWidth(this.preferred_width);
    }

    public UITextArea(double d) {
        this.preferred_width = d;
        this.setPrefWidth(this.preferred_width);
        this.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }
}

