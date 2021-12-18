/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.Toggle
 *  javafx.scene.control.ToggleButton
 *  javafx.scene.control.ToggleGroup
 *  javafx.scene.input.KeyCode
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;

public class ToggleButtonBehavior<C extends ToggleButton>
extends ButtonBehavior<C> {
    protected static final List<KeyBinding> TOGGLE_BUTTON_BINDINGS = new ArrayList<KeyBinding>();

    public ToggleButtonBehavior(C c) {
        super(c, TOGGLE_BUTTON_BINDINGS);
    }

    private int nextToggleIndex(ObservableList<Toggle> observableList, int n) {
        Toggle toggle;
        if (n < 0 || n >= observableList.size()) {
            return 0;
        }
        int n2 = (n + 1) % observableList.size();
        while (n2 != n && (toggle = (Toggle)observableList.get(n2)) instanceof Node && ((Node)toggle).isDisabled()) {
            n2 = (n2 + 1) % observableList.size();
        }
        return n2;
    }

    private int previousToggleIndex(ObservableList<Toggle> observableList, int n) {
        Toggle toggle;
        if (n < 0 || n >= observableList.size()) {
            return observableList.size();
        }
        int n2 = Math.floorMod(n - 1, observableList.size());
        while (n2 != n && (toggle = (Toggle)observableList.get(n2)) instanceof Node && ((Node)toggle).isDisabled()) {
            n2 = Math.floorMod(n2 - 1, observableList.size());
        }
        return n2;
    }

    @Override
    protected void callAction(String string) {
        ToggleButton toggleButton = (ToggleButton)this.getControl();
        ToggleGroup toggleGroup = toggleButton.getToggleGroup();
        if (toggleGroup == null) {
            super.callAction(string);
            return;
        }
        ObservableList observableList = toggleGroup.getToggles();
        int n = observableList.indexOf((Object)toggleButton);
        switch (string) {
            case "ToggleNext-Right": 
            case "ToggleNext-Down": 
            case "TogglePrevious-Left": 
            case "TogglePrevious-Up": {
                boolean bl = this.traversingToNext(string, toggleButton.getEffectiveNodeOrientation());
                if (Utils.isTwoLevelFocus()) {
                    super.callAction(this.toggleToTraverseAction(string));
                    break;
                }
                if (bl) {
                    int n2 = this.nextToggleIndex((ObservableList<Toggle>)observableList, n);
                    if (n2 == n) {
                        super.callAction(this.toggleToTraverseAction(string));
                        break;
                    }
                    Toggle toggle = (Toggle)observableList.get(n2);
                    toggleGroup.selectToggle(toggle);
                    ((Control)toggle).requestFocus();
                    break;
                }
                int n3 = this.previousToggleIndex((ObservableList<Toggle>)observableList, n);
                if (n3 == n) {
                    super.callAction(this.toggleToTraverseAction(string));
                    break;
                }
                Toggle toggle = (Toggle)observableList.get(n3);
                toggleGroup.selectToggle(toggle);
                ((Control)toggle).requestFocus();
                break;
            }
            default: {
                super.callAction(string);
            }
        }
    }

    private boolean traversingToNext(String string, NodeOrientation nodeOrientation) {
        boolean bl = nodeOrientation == NodeOrientation.RIGHT_TO_LEFT;
        switch (string) {
            case "ToggleNext-Right": {
                return !bl;
            }
            case "ToggleNext-Down": {
                return true;
            }
            case "TogglePrevious-Left": {
                return bl;
            }
            case "TogglePrevious-Up": {
                return false;
            }
        }
        throw new IllegalArgumentException("Not a toggle action");
    }

    private String toggleToTraverseAction(String string) {
        switch (string) {
            case "ToggleNext-Right": {
                return "TraverseRight";
            }
            case "ToggleNext-Down": {
                return "TraverseDown";
            }
            case "TogglePrevious-Left": {
                return "TraverseLeft";
            }
            case "TogglePrevious-Up": {
                return "TraverseUp";
            }
        }
        throw new IllegalArgumentException("Not a toggle action");
    }

    static {
        TOGGLE_BUTTON_BINDINGS.addAll(BUTTON_BINDINGS);
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ToggleNext-Right"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TogglePrevious-Left"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "ToggleNext-Down"));
        TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.UP, "TogglePrevious-Up"));
    }
}

