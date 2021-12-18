/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ObservableList
 *  javafx.css.PseudoClass
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.geometry.HPos
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.control.Spinner
 *  javafx.scene.control.TextField
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SpinnerBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class SpinnerSkin<T>
extends BehaviorSkinBase<Spinner<T>, SpinnerBehavior<T>> {
    private TextField textField;
    private Region incrementArrow;
    private StackPane incrementArrowButton;
    private Region decrementArrow;
    private StackPane decrementArrowButton;
    private static final int ARROWS_ON_RIGHT_VERTICAL = 0;
    private static final int ARROWS_ON_LEFT_VERTICAL = 1;
    private static final int ARROWS_ON_RIGHT_HORIZONTAL = 2;
    private static final int ARROWS_ON_LEFT_HORIZONTAL = 3;
    private static final int SPLIT_ARROWS_VERTICAL = 4;
    private static final int SPLIT_ARROWS_HORIZONTAL = 5;
    private int layoutMode = 0;
    private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"contains-focus");

    public SpinnerSkin(Spinner<T> spinner) {
        super(spinner, new SpinnerBehavior<T>(spinner));
        this.textField = spinner.getEditor();
        this.getChildren().add((Object)this.textField);
        this.updateStyleClass();
        spinner.getStyleClass().addListener(change -> this.updateStyleClass());
        this.incrementArrow = new Region();
        this.incrementArrow.setFocusTraversable(false);
        this.incrementArrow.getStyleClass().setAll((Object[])new String[]{"increment-arrow"});
        this.incrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.incrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.incrementArrow.setMouseTransparent(true);
        this.incrementArrowButton = new StackPane(){

            public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                switch (accessibleAction) {
                    case FIRE: {
                        ((Spinner)SpinnerSkin.this.getSkinnable()).increment();
                    }
                }
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        };
        this.incrementArrowButton.setAccessibleRole(AccessibleRole.INCREMENT_BUTTON);
        this.incrementArrowButton.setFocusTraversable(false);
        this.incrementArrowButton.getStyleClass().setAll((Object[])new String[]{"increment-arrow-button"});
        this.incrementArrowButton.getChildren().add((Object)this.incrementArrow);
        this.incrementArrowButton.setOnMousePressed(mouseEvent -> {
            ((Spinner)this.getSkinnable()).requestFocus();
            ((SpinnerBehavior)this.getBehavior()).startSpinning(true);
        });
        this.incrementArrowButton.setOnMouseReleased(mouseEvent -> ((SpinnerBehavior)this.getBehavior()).stopSpinning());
        this.decrementArrow = new Region();
        this.decrementArrow.setFocusTraversable(false);
        this.decrementArrow.getStyleClass().setAll((Object[])new String[]{"decrement-arrow"});
        this.decrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.decrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.decrementArrow.setMouseTransparent(true);
        this.decrementArrowButton = new StackPane(){

            public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                switch (accessibleAction) {
                    case FIRE: {
                        ((Spinner)SpinnerSkin.this.getSkinnable()).decrement();
                    }
                }
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        };
        this.decrementArrowButton.setAccessibleRole(AccessibleRole.DECREMENT_BUTTON);
        this.decrementArrowButton.setFocusTraversable(false);
        this.decrementArrowButton.getStyleClass().setAll((Object[])new String[]{"decrement-arrow-button"});
        this.decrementArrowButton.getChildren().add((Object)this.decrementArrow);
        this.decrementArrowButton.setOnMousePressed(mouseEvent -> {
            ((Spinner)this.getSkinnable()).requestFocus();
            ((SpinnerBehavior)this.getBehavior()).startSpinning(false);
        });
        this.decrementArrowButton.setOnMouseReleased(mouseEvent -> ((SpinnerBehavior)this.getBehavior()).stopSpinning());
        this.getChildren().addAll((Object[])new Node[]{this.incrementArrowButton, this.decrementArrowButton});
        spinner.focusedProperty().addListener((observableValue, bl, bl2) -> ((ComboBoxPopupControl.FakeFocusTextField)this.textField).setFakeFocus((boolean)bl2));
        spinner.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (spinner.isEditable()) {
                if (keyEvent.getTarget().equals((Object)this.textField)) {
                    return;
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    return;
                }
                this.textField.fireEvent((Event)keyEvent.copyFor((Object)this.textField, (EventTarget)this.textField));
                keyEvent.consume();
            }
        });
        this.textField.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (!spinner.isEditable()) {
                spinner.fireEvent((Event)keyEvent.copyFor((Object)spinner, (EventTarget)spinner));
                keyEvent.consume();
            }
        });
        this.textField.focusedProperty().addListener((observableValue, bl, bl2) -> {
            spinner.getProperties().put((Object)"FOCUSED", bl2);
            if (!bl2.booleanValue()) {
                this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, false);
            } else {
                this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, true);
            }
        });
        this.textField.focusTraversableProperty().bind((ObservableValue)spinner.editableProperty());
        spinner.setImpl_traversalEngine(new ParentTraversalEngine((Parent)spinner, new Algorithm(){

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
    }

    private void updateStyleClass() {
        ObservableList observableList = ((Spinner)this.getSkinnable()).getStyleClass();
        this.layoutMode = observableList.contains("arrows-on-left-vertical") ? 1 : (observableList.contains("arrows-on-left-horizontal") ? 3 : (observableList.contains("arrows-on-right-horizontal") ? 2 : (observableList.contains("split-arrows-vertical") ? 4 : (observableList.contains("split-arrows-horizontal") ? 5 : 0))));
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        double d5 = this.incrementArrowButton.snappedLeftInset() + this.snapSize(this.incrementArrow.prefWidth(-1.0)) + this.incrementArrowButton.snappedRightInset();
        double d6 = this.decrementArrowButton.snappedLeftInset() + this.snapSize(this.decrementArrow.prefWidth(-1.0)) + this.decrementArrowButton.snappedRightInset();
        double d7 = Math.max(d5, d6);
        if (this.layoutMode == 0 || this.layoutMode == 1) {
            double d8 = this.layoutMode == 0 ? d : d + d7;
            double d9 = this.layoutMode == 0 ? d + d3 - d7 : d;
            double d10 = Math.floor(d4 / 2.0);
            this.textField.resizeRelocate(d8, d2, d3 - d7, d4);
            this.incrementArrowButton.resize(d7, d10);
            this.positionInArea((Node)this.incrementArrowButton, d9, d2, d7, d10, 0.0, HPos.CENTER, VPos.CENTER);
            this.decrementArrowButton.resize(d7, d10);
            this.positionInArea((Node)this.decrementArrowButton, d9, d2 + d10, d7, d4 - d10, 0.0, HPos.CENTER, VPos.BOTTOM);
        } else if (this.layoutMode == 2 || this.layoutMode == 3) {
            double d11 = d5 + d6;
            double d12 = this.layoutMode == 2 ? d : d + d11;
            double d13 = this.layoutMode == 2 ? d + d3 - d11 : d;
            this.textField.resizeRelocate(d12, d2, d3 - d11, d4);
            this.decrementArrowButton.resize(d6, d4);
            this.positionInArea((Node)this.decrementArrowButton, d13, d2, d6, d4, 0.0, HPos.CENTER, VPos.CENTER);
            this.incrementArrowButton.resize(d5, d4);
            this.positionInArea((Node)this.incrementArrowButton, d13 + d6, d2, d5, d4, 0.0, HPos.CENTER, VPos.CENTER);
        } else if (this.layoutMode == 4) {
            double d14 = this.incrementArrowButton.snappedTopInset() + this.snapSize(this.incrementArrow.prefHeight(-1.0)) + this.incrementArrowButton.snappedBottomInset();
            double d15 = this.decrementArrowButton.snappedTopInset() + this.snapSize(this.decrementArrow.prefHeight(-1.0)) + this.decrementArrowButton.snappedBottomInset();
            double d16 = Math.max(d14, d15);
            this.incrementArrowButton.resize(d3, d16);
            this.positionInArea((Node)this.incrementArrowButton, d, d2, d3, d16, 0.0, HPos.CENTER, VPos.CENTER);
            this.textField.resizeRelocate(d, d2 + d16, d3, d4 - 2.0 * d16);
            this.decrementArrowButton.resize(d3, d16);
            this.positionInArea((Node)this.decrementArrowButton, d, d4 - d16, d3, d16, 0.0, HPos.CENTER, VPos.CENTER);
        } else if (this.layoutMode == 5) {
            this.decrementArrowButton.resize(d7, d4);
            this.positionInArea((Node)this.decrementArrowButton, d, d2, d7, d4, 0.0, HPos.CENTER, VPos.CENTER);
            this.textField.resizeRelocate(d + d7, d2, d3 - 2.0 * d7, d4);
            this.incrementArrowButton.resize(d7, d4);
            this.positionInArea((Node)this.incrementArrowButton, d3 - d7, d2, d7, d4, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return this.computePrefHeight(d, d2, d3, d4, d5);
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = this.textField.prefWidth(d);
        return d5 + d6 + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = this.textField.prefHeight(d);
        double d7 = this.layoutMode == 4 ? d2 + this.incrementArrowButton.prefHeight(d) + d6 + this.decrementArrowButton.prefHeight(d) + d4 : d2 + d6 + d4;
        return d7;
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        return ((Spinner)this.getSkinnable()).prefWidth(d);
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((Spinner)this.getSkinnable()).prefHeight(d);
    }

    protected double computeBaselineOffset(double d, double d2, double d3, double d4) {
        return this.textField.getLayoutBounds().getMinY() + this.textField.getLayoutY() + this.textField.getBaselineOffset();
    }
}

