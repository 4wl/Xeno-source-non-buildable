/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.event.EventType
 *  javafx.scene.Node
 *  javafx.scene.control.ComboBox
 *  javafx.scene.control.ComboBoxBase
 *  javafx.scene.control.DatePicker
 *  javafx.scene.control.TextField
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusComboBehavior;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ComboBoxBaseBehavior<T>
extends BehaviorBase<ComboBoxBase<T>> {
    private TwoLevelFocusComboBehavior tlFocus;
    private KeyEvent lastEvent;
    private boolean keyDown;
    private static final String PRESS_ACTION = "Press";
    private static final String RELEASE_ACTION = "Release";
    protected static final List<KeyBinding> COMBO_BOX_BASE_BINDINGS = new ArrayList<KeyBinding>();
    private boolean showPopupOnMouseRelease = true;
    private boolean mouseInsideButton = false;

    public ComboBoxBaseBehavior(ComboBoxBase<T> comboBoxBase, List<KeyBinding> list) {
        super(comboBoxBase, list);
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusComboBehavior((Node)comboBoxBase);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    @Override
    protected void focusChanged() {
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getControl();
        if (this.keyDown && !comboBoxBase.isFocused()) {
            this.keyDown = false;
            comboBoxBase.disarm();
        }
    }

    @Override
    protected void callActionForEvent(KeyEvent keyEvent) {
        this.lastEvent = keyEvent;
        this.showPopupOnMouseRelease = true;
        super.callActionForEvent(keyEvent);
    }

    @Override
    protected void callAction(String string) {
        if (PRESS_ACTION.equals(string)) {
            this.keyPressed();
        } else if (RELEASE_ACTION.equals(string)) {
            this.keyReleased();
        } else if ("showPopup".equals(string)) {
            this.show();
        } else if ("togglePopup".equals(string)) {
            if (((ComboBoxBase)this.getControl()).isShowing()) {
                this.hide();
            } else {
                this.show();
            }
        } else if ("Cancel".equals(string)) {
            this.cancelEdit(this.lastEvent);
        } else if ("ToParent".equals(string)) {
            this.forwardToParent(this.lastEvent);
        } else {
            super.callAction(string);
        }
    }

    private void keyPressed() {
        if (Utils.isTwoLevelFocus()) {
            this.show();
            if (this.tlFocus != null) {
                this.tlFocus.setExternalFocus(false);
            }
        } else if (!((ComboBoxBase)this.getControl()).isPressed() && !((ComboBoxBase)this.getControl()).isArmed()) {
            this.keyDown = true;
            ((ComboBoxBase)this.getControl()).arm();
        }
    }

    private void keyReleased() {
        if (!Utils.isTwoLevelFocus() && this.keyDown) {
            this.keyDown = false;
            if (((ComboBoxBase)this.getControl()).isArmed()) {
                ((ComboBoxBase)this.getControl()).disarm();
            }
        }
    }

    protected void forwardToParent(KeyEvent keyEvent) {
        if (((ComboBoxBase)this.getControl()).getParent() != null) {
            ((ComboBoxBase)this.getControl()).getParent().fireEvent((Event)keyEvent);
        }
    }

    protected void cancelEdit(KeyEvent keyEvent) {
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getControl();
        TextField textField = null;
        if (comboBoxBase instanceof DatePicker) {
            textField = ((DatePicker)comboBoxBase).getEditor();
        } else if (comboBoxBase instanceof ComboBox) {
            TextField textField2 = textField = comboBoxBase.isEditable() ? ((ComboBox)comboBoxBase).getEditor() : null;
        }
        if (textField != null && textField.getTextFormatter() != null) {
            textField.cancelEdit();
        } else {
            this.forwardToParent(keyEvent);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        this.arm(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        super.mouseReleased(mouseEvent);
        this.disarm();
        if (this.showPopupOnMouseRelease) {
            this.show();
        } else {
            this.showPopupOnMouseRelease = true;
            this.hide();
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        EventTarget eventTarget;
        super.mouseEntered(mouseEvent);
        this.mouseInsideButton = !((ComboBoxBase)this.getControl()).isEditable() ? true : (eventTarget = mouseEvent.getTarget()) instanceof Node && "arrow-button".equals(((Node)eventTarget).getId());
        this.arm();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        super.mouseExited(mouseEvent);
        this.mouseInsideButton = false;
        this.disarm();
    }

    private void getFocus() {
        if (!((ComboBoxBase)this.getControl()).isFocused() && ((ComboBoxBase)this.getControl()).isFocusTraversable()) {
            ((ComboBoxBase)this.getControl()).requestFocus();
        }
    }

    private void arm(MouseEvent mouseEvent) {
        boolean bl;
        boolean bl2 = bl = mouseEvent.getButton() == MouseButton.PRIMARY && !mouseEvent.isMiddleButtonDown() && !mouseEvent.isSecondaryButtonDown() && !mouseEvent.isShiftDown() && !mouseEvent.isControlDown() && !mouseEvent.isAltDown() && !mouseEvent.isMetaDown();
        if (!((ComboBoxBase)this.getControl()).isArmed() && bl) {
            ((ComboBoxBase)this.getControl()).arm();
        }
    }

    public void show() {
        if (!((ComboBoxBase)this.getControl()).isShowing()) {
            ((ComboBoxBase)this.getControl()).requestFocus();
            ((ComboBoxBase)this.getControl()).show();
        }
    }

    public void hide() {
        if (((ComboBoxBase)this.getControl()).isShowing()) {
            ((ComboBoxBase)this.getControl()).hide();
        }
    }

    public void onAutoHide() {
        this.hide();
        this.showPopupOnMouseRelease = this.mouseInsideButton ? !this.showPopupOnMouseRelease : true;
    }

    public void arm() {
        if (((ComboBoxBase)this.getControl()).isPressed()) {
            ((ComboBoxBase)this.getControl()).arm();
        }
    }

    public void disarm() {
        if (!this.keyDown && ((ComboBoxBase)this.getControl()).isArmed()) {
            ((ComboBoxBase)this.getControl()).disarm();
        }
    }

    static {
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.F4, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "togglePopup"));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.UP, "togglePopup").alt());
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "togglePopup").alt());
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, PRESS_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, RELEASE_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, PRESS_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, RELEASE_ACTION));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "Cancel"));
        COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.F10, "ToParent"));
    }
}

