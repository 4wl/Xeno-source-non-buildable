/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ObservableValue
 *  javafx.css.Styleable
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.event.EventTarget
 *  javafx.geometry.Bounds
 *  javafx.geometry.HPos
 *  javafx.geometry.Point2D
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.control.ComboBoxBase
 *  javafx.scene.control.PopupControl
 *  javafx.scene.control.Skin
 *  javafx.scene.control.Skinnable
 *  javafx.scene.control.TextField
 *  javafx.scene.input.DragEvent
 *  javafx.scene.input.InputMethodRequests
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.Region
 *  javafx.stage.WindowEvent
 *  javafx.util.StringConverter
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxBaseSkin;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import com.sun.javafx.util.Utils;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public abstract class ComboBoxPopupControl<T>
extends ComboBoxBaseSkin<T> {
    protected PopupControl popup;
    public static final String COMBO_BOX_STYLE_CLASS = "combo-box-popup";
    private boolean popupNeedsReconfiguring = true;
    private final ComboBoxBase<T> comboBoxBase;
    private TextField textField;
    private EventHandler<MouseEvent> textFieldMouseEventHandler = mouseEvent -> {
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getSkinnable();
        if (!mouseEvent.getTarget().equals((Object)comboBoxBase)) {
            comboBoxBase.fireEvent((Event)mouseEvent.copyFor((Object)comboBoxBase, (EventTarget)comboBoxBase));
            mouseEvent.consume();
        }
    };
    private EventHandler<DragEvent> textFieldDragEventHandler = dragEvent -> {
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getSkinnable();
        if (!dragEvent.getTarget().equals((Object)comboBoxBase)) {
            comboBoxBase.fireEvent((Event)dragEvent.copyFor((Object)comboBoxBase, (EventTarget)comboBoxBase));
            dragEvent.consume();
        }
    };
    private String initialTextFieldValue = null;

    public ComboBoxPopupControl(ComboBoxBase<T> comboBoxBase, ComboBoxBaseBehavior<T> comboBoxBaseBehavior) {
        super(comboBoxBase, comboBoxBaseBehavior);
        this.comboBoxBase = comboBoxBase;
        TextField textField = this.textField = this.getEditor() != null ? this.getEditableInputNode() : null;
        if (this.textField != null) {
            this.getChildren().add((Object)this.textField);
        }
        comboBoxBase.focusedProperty().addListener((observableValue, bl, bl2) -> {
            if (this.getEditor() != null) {
                ((FakeFocusTextField)this.textField).setFakeFocus((boolean)bl2);
                if (!bl2.booleanValue()) {
                    this.setTextFromTextFieldIntoComboBoxValue();
                }
            }
        });
        comboBoxBase.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (this.textField == null || this.getEditor() == null) {
                this.handleKeyEvent((KeyEvent)keyEvent, false);
            } else {
                if (keyEvent.getTarget().equals((Object)this.textField)) {
                    return;
                }
                switch (keyEvent.getCode()) {
                    case ESCAPE: 
                    case F10: {
                        break;
                    }
                    case ENTER: {
                        this.handleKeyEvent((KeyEvent)keyEvent, true);
                        break;
                    }
                    default: {
                        this.textField.fireEvent((Event)keyEvent.copyFor((Object)this.textField, (EventTarget)this.textField));
                        keyEvent.consume();
                    }
                }
            }
        });
        if (comboBoxBase.getOnInputMethodTextChanged() == null) {
            comboBoxBase.setOnInputMethodTextChanged(inputMethodEvent -> {
                if (this.textField != null && this.getEditor() != null && comboBoxBase.getScene().getFocusOwner() == comboBoxBase && this.textField.getOnInputMethodTextChanged() != null) {
                    this.textField.getOnInputMethodTextChanged().handle(inputMethodEvent);
                }
            });
        }
        comboBoxBase.setImpl_traversalEngine(new ParentTraversalEngine((Parent)comboBoxBase, new Algorithm(){

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                return null;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                return null;
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                return null;
            }
        }));
        this.updateEditable();
    }

    protected abstract Node getPopupContent();

    protected PopupControl getPopup() {
        if (this.popup == null) {
            this.createPopup();
        }
        return this.popup;
    }

    @Override
    public void show() {
        if (this.getSkinnable() == null) {
            throw new IllegalStateException("ComboBox is null");
        }
        Node node = this.getPopupContent();
        if (node == null) {
            throw new IllegalStateException("Popup node is null");
        }
        if (this.getPopup().isShowing()) {
            return;
        }
        this.positionAndShowPopup();
    }

    @Override
    public void hide() {
        if (this.popup != null && this.popup.isShowing()) {
            this.popup.hide();
        }
    }

    private Point2D getPrefPopupPosition() {
        return Utils.pointRelativeTo((Node)this.getSkinnable(), this.getPopupContent(), HPos.CENTER, VPos.BOTTOM, 0.0, 0.0, true);
    }

    private void positionAndShowPopup() {
        PopupControl popupControl = this.getPopup();
        popupControl.getScene().setNodeOrientation(((ComboBoxBase)this.getSkinnable()).getEffectiveNodeOrientation());
        Node node = this.getPopupContent();
        this.sizePopup();
        Point2D point2D = this.getPrefPopupPosition();
        this.popupNeedsReconfiguring = true;
        this.reconfigurePopup();
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getSkinnable();
        popupControl.show(comboBoxBase.getScene().getWindow(), this.snapPosition(point2D.getX()), this.snapPosition(point2D.getY()));
        node.requestFocus();
        this.sizePopup();
    }

    private void sizePopup() {
        Node node = this.getPopupContent();
        if (node instanceof Region) {
            Region region = (Region)node;
            double d = this.snapSize(region.prefHeight(0.0));
            double d2 = this.snapSize(region.minHeight(0.0));
            double d3 = this.snapSize(region.maxHeight(0.0));
            double d4 = this.snapSize(Math.min(Math.max(d, d2), Math.max(d2, d3)));
            double d5 = this.snapSize(region.prefWidth(d4));
            double d6 = this.snapSize(region.minWidth(d4));
            double d7 = this.snapSize(region.maxWidth(d4));
            double d8 = this.snapSize(Math.min(Math.max(d5, d6), Math.max(d6, d7)));
            node.resize(d8, d4);
        } else {
            node.autosize();
        }
    }

    private void createPopup() {
        this.popup = new PopupControl(){
            {
                this.setSkin((Skin)new Skin<Skinnable>(){

                    public Skinnable getSkinnable() {
                        return ComboBoxPopupControl.this.getSkinnable();
                    }

                    public Node getNode() {
                        return ComboBoxPopupControl.this.getPopupContent();
                    }

                    public void dispose() {
                    }
                });
            }

            public Styleable getStyleableParent() {
                return ComboBoxPopupControl.this.getSkinnable();
            }
        };
        this.popup.getStyleClass().add((Object)"combo-box-popup");
        this.popup.setConsumeAutoHidingEvents(false);
        this.popup.setAutoHide(true);
        this.popup.setAutoFix(true);
        this.popup.setHideOnEscape(true);
        this.popup.setOnAutoHide(event -> ((ComboBoxBaseBehavior)this.getBehavior()).onAutoHide());
        this.popup.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> ((ComboBoxBaseBehavior)this.getBehavior()).onAutoHide());
        this.popup.addEventHandler(WindowEvent.WINDOW_HIDDEN, windowEvent -> ((ComboBoxBase)this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE));
        InvalidationListener invalidationListener = observable -> {
            this.popupNeedsReconfiguring = true;
            this.reconfigurePopup();
        };
        ((ComboBoxBase)this.getSkinnable()).layoutXProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).layoutYProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).widthProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).heightProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).sceneProperty().addListener(observable -> {
            if (((ObservableValue)observable).getValue() == null) {
                this.hide();
            }
        });
    }

    void reconfigurePopup() {
        double d;
        if (this.popup == null) {
            return;
        }
        boolean bl = this.popup.isShowing();
        if (!bl) {
            return;
        }
        if (!this.popupNeedsReconfiguring) {
            return;
        }
        this.popupNeedsReconfiguring = false;
        Point2D point2D = this.getPrefPopupPosition();
        Node node = this.getPopupContent();
        double d2 = node.prefWidth(-1.0);
        double d3 = node.prefHeight(-1.0);
        if (point2D.getX() > -1.0) {
            this.popup.setAnchorX(point2D.getX());
        }
        if (point2D.getY() > -1.0) {
            this.popup.setAnchorY(point2D.getY());
        }
        if (d2 > -1.0) {
            this.popup.setMinWidth(d2);
        }
        if (d3 > -1.0) {
            this.popup.setMinHeight(d3);
        }
        Bounds bounds = node.getLayoutBounds();
        double d4 = bounds.getWidth();
        double d5 = bounds.getHeight();
        double d6 = d4 < d2 ? d2 : d4;
        double d7 = d = d5 < d3 ? d3 : d5;
        if (d6 != d4 || d != d5) {
            node.resize(d6, d);
            if (node instanceof Region) {
                ((Region)node).setMinSize(d6, d);
                ((Region)node).setPrefSize(d6, d);
            }
        }
    }

    protected abstract TextField getEditor();

    protected abstract StringConverter<T> getConverter();

    protected TextField getEditableInputNode() {
        if (this.textField == null && this.getEditor() != null) {
            this.textField = this.getEditor();
            this.textField.setFocusTraversable(false);
            this.textField.promptTextProperty().bind((ObservableValue)this.comboBoxBase.promptTextProperty());
            this.textField.tooltipProperty().bind((ObservableValue)this.comboBoxBase.tooltipProperty());
            this.initialTextFieldValue = this.textField.getText();
        }
        return this.textField;
    }

    protected void setTextFromTextFieldIntoComboBoxValue() {
        StringConverter<T> stringConverter;
        if (this.getEditor() != null && (stringConverter = this.getConverter()) != null) {
            Object object;
            Object object2 = object = this.comboBoxBase.getValue();
            String string = this.textField.getText();
            if (object == null && (string == null || string.isEmpty())) {
                object2 = null;
            } else {
                try {
                    object2 = stringConverter.fromString(string);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (!(object2 == null && object == null || object2 != null && object2.equals(object))) {
                this.comboBoxBase.setValue(object2);
            }
            this.updateDisplayNode();
        }
    }

    protected void updateDisplayNode() {
        if (this.textField != null && this.getEditor() != null) {
            Object object = this.comboBoxBase.getValue();
            StringConverter<T> stringConverter = this.getConverter();
            if (this.initialTextFieldValue != null && !this.initialTextFieldValue.isEmpty()) {
                this.textField.setText(this.initialTextFieldValue);
                this.initialTextFieldValue = null;
            } else {
                String string = stringConverter.toString(object);
                if (object == null || string == null) {
                    this.textField.setText("");
                } else if (!string.equals(this.textField.getText())) {
                    this.textField.setText(string);
                }
            }
        }
    }

    private void handleKeyEvent(KeyEvent keyEvent, boolean bl) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.setTextFromTextFieldIntoComboBoxValue();
            if (bl && this.comboBoxBase.getOnAction() != null) {
                keyEvent.consume();
            } else {
                this.forwardToParent(keyEvent);
            }
        } else if (keyEvent.getCode() == KeyCode.F4) {
            if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                if (this.comboBoxBase.isShowing()) {
                    this.comboBoxBase.hide();
                } else {
                    this.comboBoxBase.show();
                }
            }
            keyEvent.consume();
        }
    }

    private void forwardToParent(KeyEvent keyEvent) {
        if (this.comboBoxBase.getParent() != null) {
            this.comboBoxBase.getParent().fireEvent((Event)keyEvent);
        }
    }

    protected void updateEditable() {
        final TextField textField = this.getEditor();
        if (this.getEditor() == null) {
            if (this.textField != null) {
                this.textField.removeEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
                this.textField.removeEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
                this.comboBoxBase.setInputMethodRequests(null);
            }
        } else if (textField != null) {
            textField.addEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
            textField.addEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
            this.comboBoxBase.setInputMethodRequests((InputMethodRequests)new ExtendedInputMethodRequests(){

                public Point2D getTextLocation(int n) {
                    return textField.getInputMethodRequests().getTextLocation(n);
                }

                public int getLocationOffset(int n, int n2) {
                    return textField.getInputMethodRequests().getLocationOffset(n, n2);
                }

                public void cancelLatestCommittedText() {
                    textField.getInputMethodRequests().cancelLatestCommittedText();
                }

                public String getSelectedText() {
                    return textField.getInputMethodRequests().getSelectedText();
                }

                @Override
                public int getInsertPositionOffset() {
                    return ((ExtendedInputMethodRequests)textField.getInputMethodRequests()).getInsertPositionOffset();
                }

                @Override
                public String getCommittedText(int n, int n2) {
                    return ((ExtendedInputMethodRequests)textField.getInputMethodRequests()).getCommittedText(n, n2);
                }

                @Override
                public int getCommittedTextLength() {
                    return ((ExtendedInputMethodRequests)textField.getInputMethodRequests()).getCommittedTextLength();
                }
            });
        }
        this.textField = textField;
    }

    public static final class FakeFocusTextField
    extends TextField {
        public void requestFocus() {
            if (this.getParent() != null) {
                this.getParent().requestFocus();
            }
        }

        public void setFakeFocus(boolean bl) {
            this.setFocused(bl);
        }

        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case FOCUS_ITEM: {
                    return this.getParent();
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }
    }
}

