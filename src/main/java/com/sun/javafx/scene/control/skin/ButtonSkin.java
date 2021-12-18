/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.scene.Scene
 *  javafx.scene.control.Button
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyCodeCombination
 *  javafx.scene.input.KeyCombination$Modifier
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class ButtonSkin
extends LabeledSkinBase<Button, ButtonBehavior<Button>> {
    Runnable defaultButtonRunnable = () -> {
        if (((Button)this.getSkinnable()).getScene() != null && ((Button)this.getSkinnable()).impl_isTreeVisible() && !((Button)this.getSkinnable()).isDisabled()) {
            ((Button)this.getSkinnable()).fire();
        }
    };
    Runnable cancelButtonRunnable = () -> {
        if (((Button)this.getSkinnable()).getScene() != null && ((Button)this.getSkinnable()).impl_isTreeVisible() && !((Button)this.getSkinnable()).isDisabled()) {
            ((Button)this.getSkinnable()).fire();
        }
    };
    private KeyCodeCombination defaultAcceleratorKeyCodeCombination;
    private KeyCodeCombination cancelAcceleratorKeyCodeCombination;

    public ButtonSkin(Button button) {
        super(button, new ButtonBehavior<Button>(button));
        this.registerChangeListener((ObservableValue<?>)button.defaultButtonProperty(), "DEFAULT_BUTTON");
        this.registerChangeListener((ObservableValue<?>)button.cancelButtonProperty(), "CANCEL_BUTTON");
        this.registerChangeListener((ObservableValue<?>)button.focusedProperty(), "FOCUSED");
        if (((Button)this.getSkinnable()).isDefaultButton()) {
            this.setDefaultButton(true);
        }
        if (((Button)this.getSkinnable()).isCancelButton()) {
            this.setCancelButton(true);
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("DEFAULT_BUTTON".equals(string)) {
            this.setDefaultButton(((Button)this.getSkinnable()).isDefaultButton());
        } else if ("CANCEL_BUTTON".equals(string)) {
            this.setCancelButton(((Button)this.getSkinnable()).isCancelButton());
        } else if ("FOCUSED".equals(string)) {
            ContextMenu contextMenu;
            if (!((Button)this.getSkinnable()).isFocused() && (contextMenu = ((Button)this.getSkinnable()).getContextMenu()) != null && contextMenu.isShowing()) {
                contextMenu.hide();
                Utils.removeMnemonics(contextMenu, ((Button)this.getSkinnable()).getScene());
            }
        } else if ("PARENT".equals(string) && ((Button)this.getSkinnable()).getParent() == null && ((Button)this.getSkinnable()).getScene() != null) {
            if (((Button)this.getSkinnable()).isDefaultButton()) {
                ((Button)this.getSkinnable()).getScene().getAccelerators().remove((Object)this.defaultAcceleratorKeyCodeCombination);
            }
            if (((Button)this.getSkinnable()).isCancelButton()) {
                ((Button)this.getSkinnable()).getScene().getAccelerators().remove((Object)this.cancelAcceleratorKeyCodeCombination);
            }
        }
    }

    private void setDefaultButton(boolean bl) {
        Scene scene = ((Button)this.getSkinnable()).getScene();
        if (scene != null) {
            KeyCode keyCode = KeyCode.ENTER;
            this.defaultAcceleratorKeyCodeCombination = new KeyCodeCombination(keyCode, new KeyCombination.Modifier[0]);
            Runnable runnable = (Runnable)scene.getAccelerators().get((Object)this.defaultAcceleratorKeyCodeCombination);
            if (!bl) {
                if (this.defaultButtonRunnable.equals(runnable)) {
                    scene.getAccelerators().remove((Object)this.defaultAcceleratorKeyCodeCombination);
                }
            } else if (!this.defaultButtonRunnable.equals(runnable)) {
                scene.getAccelerators().remove((Object)this.defaultAcceleratorKeyCodeCombination);
                scene.getAccelerators().put((Object)this.defaultAcceleratorKeyCodeCombination, (Object)this.defaultButtonRunnable);
            }
        }
    }

    private void setCancelButton(boolean bl) {
        Scene scene = ((Button)this.getSkinnable()).getScene();
        if (scene != null) {
            KeyCode keyCode = KeyCode.ESCAPE;
            this.cancelAcceleratorKeyCodeCombination = new KeyCodeCombination(keyCode, new KeyCombination.Modifier[0]);
            Runnable runnable = (Runnable)scene.getAccelerators().get((Object)this.cancelAcceleratorKeyCodeCombination);
            if (!bl) {
                if (this.cancelButtonRunnable.equals(runnable)) {
                    scene.getAccelerators().remove((Object)this.cancelAcceleratorKeyCodeCombination);
                }
            } else if (!this.cancelButtonRunnable.equals(runnable)) {
                scene.getAccelerators().remove((Object)this.cancelAcceleratorKeyCodeCombination);
                scene.getAccelerators().put((Object)this.cancelAcceleratorKeyCodeCombination, (Object)this.cancelButtonRunnable);
            }
        }
    }
}

