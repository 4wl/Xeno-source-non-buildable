/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.scene.Node
 *  javafx.scene.control.ChoiceBox
 *  javafx.scene.control.SingleSelectionModel
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
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ChoiceBoxBehavior<T>
extends BehaviorBase<ChoiceBox<T>> {
    protected static final List<KeyBinding> CHOICE_BUTTON_BINDINGS = new ArrayList<KeyBinding>();
    private TwoLevelFocusComboBehavior tlFocus;

    @Override
    protected void callAction(String string) {
        if (string.equals("Cancel")) {
            this.cancel();
        } else if (string.equals("Press")) {
            this.keyPressed();
        } else if (string.equals("Release")) {
            this.keyReleased();
        } else if (string.equals("Down")) {
            this.showPopup();
        } else {
            super.callAction(string);
        }
    }

    public ChoiceBoxBehavior(ChoiceBox<T> choiceBox) {
        super(choiceBox, CHOICE_BUTTON_BINDINGS);
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusComboBehavior((Node)choiceBox);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    public void select(int n) {
        SingleSelectionModel singleSelectionModel = ((ChoiceBox)this.getControl()).getSelectionModel();
        if (singleSelectionModel == null) {
            return;
        }
        singleSelectionModel.select(n);
    }

    public void close() {
        ((ChoiceBox)this.getControl()).hide();
    }

    public void showPopup() {
        ((ChoiceBox)this.getControl()).show();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        ChoiceBox choiceBox = (ChoiceBox)this.getControl();
        super.mousePressed(mouseEvent);
        if (choiceBox.isFocusTraversable()) {
            choiceBox.requestFocus();
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        ChoiceBox choiceBox = (ChoiceBox)this.getControl();
        super.mouseReleased(mouseEvent);
        if (choiceBox.isShowing() || !choiceBox.contains(mouseEvent.getX(), mouseEvent.getY())) {
            choiceBox.hide();
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            choiceBox.show();
        }
    }

    private void keyPressed() {
        ChoiceBox choiceBox = (ChoiceBox)this.getControl();
        if (!choiceBox.isShowing()) {
            choiceBox.show();
        }
    }

    private void keyReleased() {
    }

    public void cancel() {
        ChoiceBox choiceBox = (ChoiceBox)this.getControl();
        choiceBox.hide();
    }

    static {
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Press"));
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Release"));
        if (Utils.isTwoLevelFocus()) {
            CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Press"));
            CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Release"));
        }
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Cancel"));
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Down"));
        CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.CANCEL, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Cancel"));
    }
}

