/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.scene.control.Skin
 *  javafx.scene.paint.Color
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.InputField;
import com.sun.javafx.scene.control.skin.WebColorFieldSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

class WebColorField
extends InputField {
    private ObjectProperty<Color> value = new SimpleObjectProperty((Object)this, "value");

    public final Color getValue() {
        return (Color)this.value.get();
    }

    public final void setValue(Color color) {
        this.value.set((Object)color);
    }

    public final ObjectProperty<Color> valueProperty() {
        return this.value;
    }

    public WebColorField() {
        this.getStyleClass().setAll((Object[])new String[]{"webcolor-field"});
    }

    protected Skin<?> createDefaultSkin() {
        return new WebColorFieldSkin(this);
    }
}

