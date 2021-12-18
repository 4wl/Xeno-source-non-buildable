/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.Node
 *  javafx.scene.paint.Color
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.sun.javafx.scene.control.skin.InputFieldSkin;
import com.sun.javafx.scene.control.skin.WebColorField;
import java.util.Locale;
import javafx.beans.InvalidationListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.paint.Color;

class WebColorFieldSkin
extends InputFieldSkin {
    private InvalidationListener integerFieldValueListener = observable -> this.updateText();
    private boolean noChangeInValue = false;

    public WebColorFieldSkin(WebColorField webColorField) {
        super(webColorField);
        webColorField.valueProperty().addListener(this.integerFieldValueListener);
        this.getTextField().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    @Override
    public WebColorField getSkinnable() {
        return (WebColorField)this.control;
    }

    @Override
    public Node getNode() {
        return this.getTextField();
    }

    @Override
    public void dispose() {
        ((WebColorField)this.control).valueProperty().removeListener(this.integerFieldValueListener);
        super.dispose();
    }

    @Override
    protected boolean accept(String string) {
        if (string.length() == 0) {
            return true;
        }
        return string.matches("#[a-fA-F0-9]{0,6}") || string.matches("[a-fA-F0-9]{0,6}");
    }

    @Override
    protected void updateText() {
        Color color = ((WebColorField)this.control).getValue();
        if (color == null) {
            color = Color.BLACK;
        }
        this.getTextField().setText(ColorPickerSkin.formatHexString(color));
    }

    @Override
    protected void updateValue() {
        String string;
        if (this.noChangeInValue) {
            return;
        }
        Color color = ((WebColorField)this.control).getValue();
        String string2 = string = this.getTextField().getText() == null ? "" : this.getTextField().getText().trim().toUpperCase(Locale.ROOT);
        if (string.matches("#[A-F0-9]{6}") || string.matches("[A-F0-9]{6}")) {
            try {
                Color color2;
                Color color3 = color2 = string.charAt(0) == '#' ? Color.web((String)string) : Color.web((String)("#" + string));
                if (!color2.equals((Object)color)) {
                    ((WebColorField)this.control).setValue(color2);
                } else {
                    this.noChangeInValue = true;
                    this.getTextField().setText(ColorPickerSkin.formatHexString(color2));
                    this.noChangeInValue = false;
                }
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Failed to parse [" + string + "]");
            }
        }
    }
}

