/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ObservableList
 *  javafx.geometry.HPos
 *  javafx.geometry.VPos
 *  javafx.scene.Node
 *  javafx.scene.control.ComboBoxBase
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ComboBoxMode;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public abstract class ComboBoxBaseSkin<T>
extends BehaviorSkinBase<ComboBoxBase<T>, ComboBoxBaseBehavior<T>> {
    private Node displayNode;
    protected StackPane arrowButton;
    protected Region arrow;
    private ComboBoxMode mode = ComboBoxMode.COMBOBOX;

    protected final ComboBoxMode getMode() {
        return this.mode;
    }

    protected final void setMode(ComboBoxMode comboBoxMode) {
        this.mode = comboBoxMode;
    }

    public ComboBoxBaseSkin(ComboBoxBase<T> comboBoxBase, ComboBoxBaseBehavior<T> comboBoxBaseBehavior) {
        super(comboBoxBase, comboBoxBaseBehavior);
        this.arrow = new Region();
        this.arrow.setFocusTraversable(false);
        this.arrow.getStyleClass().setAll((Object[])new String[]{"arrow"});
        this.arrow.setId("arrow");
        this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.arrow.setMouseTransparent(true);
        this.arrowButton = new StackPane();
        this.arrowButton.setFocusTraversable(false);
        this.arrowButton.setId("arrow-button");
        this.arrowButton.getStyleClass().setAll((Object[])new String[]{"arrow-button"});
        this.arrowButton.getChildren().add((Object)this.arrow);
        if (comboBoxBase.isEditable()) {
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> ((ComboBoxBaseBehavior)this.getBehavior()).mouseEntered((MouseEvent)mouseEvent));
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                ((ComboBoxBaseBehavior)this.getBehavior()).mousePressed((MouseEvent)mouseEvent);
                mouseEvent.consume();
            });
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                ((ComboBoxBaseBehavior)this.getBehavior()).mouseReleased((MouseEvent)mouseEvent);
                mouseEvent.consume();
            });
            this.arrowButton.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> ((ComboBoxBaseBehavior)this.getBehavior()).mouseExited((MouseEvent)mouseEvent));
        }
        this.getChildren().add((Object)this.arrowButton);
        ((ComboBoxBase)this.getSkinnable()).focusedProperty().addListener((observableValue, bl, bl2) -> {
            if (!bl2.booleanValue()) {
                this.focusLost();
            }
        });
        this.registerChangeListener((ObservableValue<?>)comboBoxBase.editableProperty(), "EDITABLE");
        this.registerChangeListener((ObservableValue<?>)comboBoxBase.showingProperty(), "SHOWING");
        this.registerChangeListener((ObservableValue<?>)comboBoxBase.focusedProperty(), "FOCUSED");
        this.registerChangeListener((ObservableValue<?>)comboBoxBase.valueProperty(), "VALUE");
    }

    protected void focusLost() {
        ((ComboBoxBase)this.getSkinnable()).hide();
    }

    public abstract Node getDisplayNode();

    public abstract void show();

    public abstract void hide();

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("SHOWING".equals(string)) {
            if (((ComboBoxBase)this.getSkinnable()).isShowing()) {
                this.show();
            } else {
                this.hide();
            }
        } else if ("EDITABLE".equals(string)) {
            this.updateDisplayArea();
        } else if ("VALUE".equals(string)) {
            this.updateDisplayArea();
        }
    }

    protected void updateDisplayArea() {
        ObservableList observableList = this.getChildren();
        Node node = this.displayNode;
        this.displayNode = this.getDisplayNode();
        if (node != null && node != this.displayNode) {
            observableList.remove((Object)node);
        }
        if (this.displayNode != null && !observableList.contains((Object)this.displayNode)) {
            observableList.add(this.displayNode);
            this.displayNode.applyCss();
        }
    }

    private boolean isButton() {
        return this.getMode() == ComboBoxMode.BUTTON;
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        double d5;
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        double d6 = this.snapSize(this.arrow.prefWidth(-1.0));
        double d7 = d5 = this.isButton() ? 0.0 : this.arrowButton.snappedLeftInset() + d6 + this.arrowButton.snappedRightInset();
        if (this.displayNode != null) {
            this.displayNode.resizeRelocate(d, d2, d3 - d5, d4);
        }
        this.arrowButton.setVisible(!this.isButton());
        if (!this.isButton()) {
            this.arrowButton.resize(d5, d4);
            this.positionInArea((Node)this.arrowButton, d + d3 - d5, d2, d5, d4, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        double d6 = this.snapSize(this.arrow.prefWidth(-1.0));
        double d7 = this.isButton() ? 0.0 : this.arrowButton.snappedLeftInset() + d6 + this.arrowButton.snappedRightInset();
        double d8 = this.displayNode == null ? 0.0 : this.displayNode.prefWidth(d);
        double d9 = d8 + d7;
        return d5 + d9 + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6;
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        if (this.displayNode == null) {
            double d7 = this.isButton() ? 0.0 : this.arrowButton.snappedTopInset() + this.arrow.prefHeight(-1.0) + this.arrowButton.snappedBottomInset();
            d6 = Math.max(21.0, d7);
        } else {
            d6 = this.displayNode.prefHeight(d);
        }
        return d2 + d6 + d4;
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        return ((ComboBoxBase)this.getSkinnable()).prefWidth(d);
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((ComboBoxBase)this.getSkinnable()).prefHeight(d);
    }

    protected double computeBaselineOffset(double d, double d2, double d3, double d4) {
        if (this.displayNode == null) {
            this.updateDisplayArea();
        }
        if (this.displayNode != null) {
            return this.displayNode.getLayoutBounds().getMinY() + this.displayNode.getLayoutY() + this.displayNode.getBaselineOffset();
        }
        return super.computeBaselineOffset(d, d2, d3, d4);
    }
}

