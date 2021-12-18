/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.control.ToolBar
 *  javafx.scene.input.KeyCode
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;

public class ToolBarBehavior
extends BehaviorBase<ToolBar> {
    private static final String CTRL_F5 = "Ctrl_F5";
    protected static final List<KeyBinding> TOOLBAR_BINDINGS = new ArrayList<KeyBinding>();

    public ToolBarBehavior(ToolBar toolBar) {
        super(toolBar, TOOLBAR_BINDINGS);
    }

    @Override
    protected void callAction(String string) {
        if (CTRL_F5.equals(string)) {
            ToolBar toolBar = (ToolBar)this.getControl();
            if (!toolBar.getItems().isEmpty()) {
                ((Node)toolBar.getItems().get(0)).requestFocus();
            }
        } else {
            super.callAction(string);
        }
    }

    static {
        TOOLBAR_BINDINGS.add(new KeyBinding(KeyCode.F5, CTRL_F5).ctrl());
    }
}

