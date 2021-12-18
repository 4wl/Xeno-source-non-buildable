/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.beans.InvalidationListener
 *  javafx.scene.Node
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.DoubleField;
import com.sun.javafx.scene.control.skin.InputFieldSkin;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;

public class DoubleFieldSkin
extends InputFieldSkin {
    private InvalidationListener doubleFieldValueListener = observable -> this.updateText();

    public DoubleFieldSkin(DoubleField doubleField) {
        super(doubleField);
        doubleField.valueProperty().addListener(this.doubleFieldValueListener);
    }

    @Override
    public DoubleField getSkinnable() {
        return (DoubleField)this.control;
    }

    @Override
    public Node getNode() {
        return this.getTextField();
    }

    @Override
    public void dispose() {
        ((DoubleField)this.control).valueProperty().removeListener(this.doubleFieldValueListener);
        super.dispose();
    }

    @Override
    protected boolean accept(String string) {
        if (string.length() == 0) {
            return true;
        }
        if (string.matches("[0-9\\.]*")) {
            try {
                Double.parseDouble(string);
                return true;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return false;
    }

    @Override
    protected void updateText() {
        this.getTextField().setText("" + ((DoubleField)this.control).getValue());
    }

    @Override
    protected void updateValue() {
        double d = ((DoubleField)this.control).getValue();
        String string = this.getTextField().getText() == null ? "" : this.getTextField().getText().trim();
        try {
            double d2 = Double.parseDouble(string);
            if (d2 != d) {
                ((DoubleField)this.control).setValue(d2);
            }
        }
        catch (NumberFormatException numberFormatException) {
            ((DoubleField)this.control).setValue(0.0);
            Platform.runLater(() -> this.getTextField().positionCaret(1));
        }
    }
}

