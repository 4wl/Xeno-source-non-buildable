/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.scene.control.SplitMenuButton
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.MenuButtonBehaviorBase;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SplitMenuButtonBehavior
extends MenuButtonBehaviorBase<SplitMenuButton> {
    protected static final List<KeyBinding> SPLIT_MENU_BUTTON_BINDINGS = new ArrayList<KeyBinding>();

    public SplitMenuButtonBehavior(SplitMenuButton splitMenuButton) {
        super(splitMenuButton, SPLIT_MENU_BUTTON_BINDINGS);
    }

    static {
        SPLIT_MENU_BUTTON_BINDINGS.addAll(BASE_MENU_BUTTON_BINDINGS);
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Press"));
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Release"));
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Press"));
        SPLIT_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Release"));
    }
}

