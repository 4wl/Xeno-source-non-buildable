/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.TitledPane
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class TitledPaneBehavior
extends BehaviorBase<TitledPane> {
    private TitledPane titledPane;
    private static final String PRESS_ACTION = "Press";
    protected static final List<KeyBinding> TITLEDPANE_BINDINGS = new ArrayList<KeyBinding>();

    public TitledPaneBehavior(TitledPane titledPane) {
        super(titledPane, TITLEDPANE_BINDINGS);
        this.titledPane = titledPane;
    }

    @Override
    protected void callAction(String string) {
        switch (string) {
            case "Press": {
                if (!this.titledPane.isCollapsible() || !this.titledPane.isFocused()) break;
                this.titledPane.setExpanded(!this.titledPane.isExpanded());
                this.titledPane.requestFocus();
                break;
            }
            default: {
                super.callAction(string);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        TitledPane titledPane = (TitledPane)this.getControl();
        titledPane.requestFocus();
    }

    public void expand() {
        this.titledPane.setExpanded(true);
    }

    public void collapse() {
        this.titledPane.setExpanded(false);
    }

    public void toggle() {
        this.titledPane.setExpanded(!this.titledPane.isExpanded());
    }

    static {
        TITLEDPANE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, PRESS_ACTION));
    }
}

