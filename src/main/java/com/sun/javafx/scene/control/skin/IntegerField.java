/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.IntegerProperty
 *  javafx.beans.property.SimpleIntegerProperty
 *  javafx.scene.control.Skin
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.InputField;
import com.sun.javafx.scene.control.skin.IntegerFieldSkin;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Skin;

class IntegerField
extends InputField {
    private IntegerProperty value = new SimpleIntegerProperty((Object)this, "value");
    private IntegerProperty maxValue = new SimpleIntegerProperty((Object)this, "maxValue", -1);

    public final int getValue() {
        return this.value.get();
    }

    public final void setValue(int n) {
        this.value.set(n);
    }

    public final IntegerProperty valueProperty() {
        return this.value;
    }

    public final int getMaxValue() {
        return this.maxValue.get();
    }

    public final void setMaxValue(int n) {
        this.maxValue.set(n);
    }

    public final IntegerProperty maxValueProperty() {
        return this.maxValue;
    }

    public IntegerField() {
        this(-1);
    }

    public IntegerField(int n) {
        this.getStyleClass().setAll((Object[])new String[]{"integer-field"});
        this.setMaxValue(n);
    }

    protected Skin<?> createDefaultSkin() {
        return new IntegerFieldSkin(this);
    }
}

