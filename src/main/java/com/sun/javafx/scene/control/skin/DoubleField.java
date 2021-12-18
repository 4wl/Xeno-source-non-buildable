/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.scene.control.Skin
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.DoubleFieldSkin;
import com.sun.javafx.scene.control.skin.InputField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Skin;

class DoubleField
extends InputField {
    private DoubleProperty value = new SimpleDoubleProperty((Object)this, "value");

    public final double getValue() {
        return this.value.get();
    }

    public final void setValue(double d) {
        this.value.set(d);
    }

    public final DoubleProperty valueProperty() {
        return this.value;
    }

    public DoubleField() {
        this.getStyleClass().setAll((Object[])new String[]{"double-field"});
    }

    protected Skin<?> createDefaultSkin() {
        return new DoubleFieldSkin(this);
    }
}

