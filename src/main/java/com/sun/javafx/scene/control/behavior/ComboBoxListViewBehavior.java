/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.scene.control.ComboBox
 *  javafx.scene.control.SingleSelectionModel
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ComboBoxListViewBehavior<T>
extends ComboBoxBaseBehavior<T> {
    protected static final List<KeyBinding> COMBO_BOX_BINDINGS = new ArrayList<KeyBinding>();

    public ComboBoxListViewBehavior(ComboBox<T> comboBox) {
        super(comboBox, COMBO_BOX_BINDINGS);
    }

    @Override
    protected void callAction(String string) {
        if ("selectPrevious".equals(string)) {
            this.selectPrevious();
        } else if ("selectNext".equals(string)) {
            this.selectNext();
        } else {
            super.callAction(string);
        }
    }

    private ComboBox<T> getComboBox() {
        return (ComboBox)this.getControl();
    }

    private void selectPrevious() {
        SingleSelectionModel singleSelectionModel = this.getComboBox().getSelectionModel();
        if (singleSelectionModel == null) {
            return;
        }
        singleSelectionModel.selectPrevious();
    }

    private void selectNext() {
        SingleSelectionModel singleSelectionModel = this.getComboBox().getSelectionModel();
        if (singleSelectionModel == null) {
            return;
        }
        singleSelectionModel.selectNext();
    }

    static {
        COMBO_BOX_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "selectPrevious"));
        COMBO_BOX_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "selectNext"));
        COMBO_BOX_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
    }
}

