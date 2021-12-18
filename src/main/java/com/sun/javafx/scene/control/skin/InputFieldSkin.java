/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ObservableValue
 *  javafx.event.EventDispatchChain
 *  javafx.scene.Node
 *  javafx.scene.control.Skin
 *  javafx.scene.control.TextField
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.event.EventDispatchChainImpl;
import com.sun.javafx.scene.control.skin.InputField;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventDispatchChain;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

abstract class InputFieldSkin
implements Skin<InputField> {
    protected InputField control;
    private InnerTextField textField;
    private InvalidationListener InputFieldFocusListener;
    private InvalidationListener InputFieldStyleClassListener;

    public InputFieldSkin(InputField inputField) {
        this.control = inputField;
        this.textField = new InnerTextField(){

            public void replaceText(int n, int n2, String string) {
                String string2 = InputFieldSkin.this.textField.getText() == null ? "" : InputFieldSkin.this.textField.getText();
                if (InputFieldSkin.this.accept(string2 = string2.substring(0, n) + string + string2.substring(n2))) {
                    super.replaceText(n, n2, string);
                }
            }

            public void replaceSelection(String string) {
                String string2 = InputFieldSkin.this.textField.getText() == null ? "" : InputFieldSkin.this.textField.getText();
                int n = Math.min(InputFieldSkin.this.textField.getAnchor(), InputFieldSkin.this.textField.getCaretPosition());
                int n2 = Math.max(InputFieldSkin.this.textField.getAnchor(), InputFieldSkin.this.textField.getCaretPosition());
                if (InputFieldSkin.this.accept(string2 = string2.substring(0, n) + string + string2.substring(n2))) {
                    super.replaceSelection(string);
                }
            }
        };
        this.textField.setId("input-text-field");
        this.textField.setFocusTraversable(false);
        inputField.getStyleClass().addAll((Collection)this.textField.getStyleClass());
        this.textField.getStyleClass().setAll((Collection)inputField.getStyleClass());
        this.InputFieldStyleClassListener = observable -> this.textField.getStyleClass().setAll((Collection)inputField.getStyleClass());
        inputField.getStyleClass().addListener(this.InputFieldStyleClassListener);
        this.textField.promptTextProperty().bind((ObservableValue)inputField.promptTextProperty());
        this.textField.prefColumnCountProperty().bind((ObservableValue)inputField.prefColumnCountProperty());
        this.textField.textProperty().addListener(observable -> this.updateValue());
        this.InputFieldFocusListener = observable -> this.textField.handleFocus(inputField.isFocused());
        inputField.focusedProperty().addListener(this.InputFieldFocusListener);
        this.updateText();
    }

    public InputField getSkinnable() {
        return this.control;
    }

    public Node getNode() {
        return this.textField;
    }

    public void dispose() {
        this.control.getStyleClass().removeListener(this.InputFieldStyleClassListener);
        this.control.focusedProperty().removeListener(this.InputFieldFocusListener);
        this.textField = null;
    }

    protected abstract boolean accept(String var1);

    protected abstract void updateText();

    protected abstract void updateValue();

    protected TextField getTextField() {
        return this.textField;
    }

    private class InnerTextField
    extends TextField {
        private InnerTextField() {
        }

        public void handleFocus(boolean bl) {
            this.setFocused(bl);
        }

        public EventDispatchChain buildEventDispatchChain(EventDispatchChain eventDispatchChain) {
            EventDispatchChainImpl eventDispatchChainImpl = new EventDispatchChainImpl();
            eventDispatchChainImpl.append(InputFieldSkin.this.textField.getEventDispatcher());
            return eventDispatchChainImpl;
        }
    }
}

