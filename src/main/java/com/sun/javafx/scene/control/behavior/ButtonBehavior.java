/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.scene.control.ButtonBase
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ButtonBehavior<C extends ButtonBase>
extends BehaviorBase<C> {
    private boolean keyDown;
    private static final String PRESS_ACTION = "Press";
    private static final String RELEASE_ACTION = "Release";
    protected static final List<KeyBinding> BUTTON_BINDINGS = new ArrayList<KeyBinding>();

    public ButtonBehavior(C c) {
        super(c, BUTTON_BINDINGS);
    }

    public ButtonBehavior(C c, List<KeyBinding> list) {
        super(c, list);
    }

    @Override
    protected void focusChanged() {
        ButtonBase buttonBase = (ButtonBase)this.getControl();
        if (this.keyDown && !buttonBase.isFocused()) {
            this.keyDown = false;
            buttonBase.disarm();
        }
    }

    @Override
    protected void callAction(String string) {
        if (!((ButtonBase)this.getControl()).isDisabled()) {
            if (PRESS_ACTION.equals(string)) {
                this.keyPressed();
            } else if (RELEASE_ACTION.equals(string)) {
                this.keyReleased();
            } else {
                super.callAction(string);
            }
        }
    }

    private void keyPressed() {
        ButtonBase buttonBase = (ButtonBase)this.getControl();
        if (!buttonBase.isPressed() && !buttonBase.isArmed()) {
            this.keyDown = true;
            buttonBase.arm();
        }
    }

    private void keyReleased() {
        ButtonBase buttonBase = (ButtonBase)this.getControl();
        if (this.keyDown) {
            this.keyDown = false;
            if (buttonBase.isArmed()) {
                buttonBase.disarm();
                buttonBase.fire();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        boolean bl;
        ButtonBase buttonBase = (ButtonBase)this.getControl();
        super.mousePressed(mouseEvent);
        if (!buttonBase.isFocused() && buttonBase.isFocusTraversable()) {
            buttonBase.requestFocus();
        }
        boolean bl2 = bl = mouseEvent.getButton() == MouseButton.PRIMARY && !mouseEvent.isMiddleButtonDown() && !mouseEvent.isSecondaryButtonDown() && !mouseEvent.isShiftDown() && !mouseEvent.isControlDown() && !mouseEvent.isAltDown() && !mouseEvent.isMetaDown();
        if (!buttonBase.isArmed() && bl) {
            buttonBase.arm();
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        ButtonBase buttonBase = (ButtonBase)this.getControl();
        if (!this.keyDown && buttonBase.isArmed()) {
            buttonBase.fire();
            buttonBase.disarm();
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        ButtonBase buttonBase = (ButtonBase)this.getControl();
        super.mouseEntered(mouseEvent);
        if (!this.keyDown && buttonBase.isPressed()) {
            buttonBase.arm();
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        ButtonBase buttonBase = (ButtonBase)this.getControl();
        super.mouseExited(mouseEvent);
        if (!this.keyDown && buttonBase.isArmed()) {
            buttonBase.disarm();
        }
    }

    static {
        BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, PRESS_ACTION));
        BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, RELEASE_ACTION));
    }
}

