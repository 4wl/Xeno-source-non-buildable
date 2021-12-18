/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.geometry.Side
 *  javafx.scene.control.MenuButton
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.geometry.Side;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class MenuButtonBehaviorBase<C extends MenuButton>
extends ButtonBehavior<C> {
    protected static final String OPEN_ACTION = "Open";
    protected static final String CLOSE_ACTION = "Close";
    protected static final List<KeyBinding> BASE_MENU_BUTTON_BINDINGS = new ArrayList<KeyBinding>();

    public MenuButtonBehaviorBase(C c, List<KeyBinding> list) {
        super(c, list);
    }

    @Override
    protected void callAction(String string) {
        MenuButton menuButton = (MenuButton)this.getControl();
        Side side = menuButton.getPopupSide();
        if (CLOSE_ACTION.equals(string)) {
            menuButton.hide();
        } else if (OPEN_ACTION.equals(string)) {
            if (menuButton.isShowing()) {
                menuButton.hide();
            } else {
                menuButton.show();
            }
        } else if (!menuButton.isShowing() && "TraverseUp".equals(string) && side == Side.TOP || "TraverseDown".equals(string) && (side == Side.BOTTOM || side == Side.TOP) || "TraverseLeft".equals(string) && (side == Side.RIGHT || side == Side.LEFT) || "TraverseRight".equals(string) && (side == Side.RIGHT || side == Side.LEFT)) {
            menuButton.show();
        } else {
            super.callAction(string);
        }
    }

    public void mousePressed(MouseEvent mouseEvent, boolean bl) {
        MenuButton menuButton = (MenuButton)this.getControl();
        if (bl) {
            if (menuButton.isShowing()) {
                menuButton.hide();
            }
            super.mousePressed(mouseEvent);
        } else {
            if (!menuButton.isFocused() && menuButton.isFocusTraversable()) {
                menuButton.requestFocus();
            }
            if (menuButton.isShowing()) {
                menuButton.hide();
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                menuButton.show();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent, boolean bl) {
        if (bl) {
            super.mouseReleased(mouseEvent);
        } else {
            if (((MenuButton)this.getControl()).isShowing() && !((MenuButton)this.getControl()).contains(mouseEvent.getX(), mouseEvent.getY())) {
                ((MenuButton)this.getControl()).hide();
            }
            ((MenuButton)this.getControl()).disarm();
        }
    }

    static {
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, CLOSE_ACTION));
        BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.CANCEL, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, CLOSE_ACTION));
    }
}

