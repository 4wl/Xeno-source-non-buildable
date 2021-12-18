/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.event.ActionEvent
 *  javafx.event.Event
 *  javafx.scene.Node
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.Labeled
 *  javafx.scene.control.MenuButton
 *  javafx.scene.control.MenuItem
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.StackPane
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.behavior.MenuButtonBehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.LabeledImpl;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.Collection;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class MenuButtonSkinBase<C extends MenuButton, B extends MenuButtonBehaviorBase<C>>
extends BehaviorSkinBase<C, B> {
    protected final LabeledImpl label;
    protected final StackPane arrow;
    protected final StackPane arrowButton;
    protected ContextMenu popup;
    protected boolean behaveLikeButton = false;
    private ListChangeListener<MenuItem> itemsChangedListener;

    public MenuButtonSkinBase(C c, B b) {
        super(c, b);
        if (c.getOnMousePressed() == null) {
            c.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> ((MenuButtonBehaviorBase)this.getBehavior()).mousePressed((MouseEvent)mouseEvent, this.behaveLikeButton));
        }
        if (c.getOnMouseReleased() == null) {
            c.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> ((MenuButtonBehaviorBase)this.getBehavior()).mouseReleased((MouseEvent)mouseEvent, this.behaveLikeButton));
        }
        this.label = new MenuLabeledImpl((MenuButton)this.getSkinnable());
        this.label.setMnemonicParsing(c.isMnemonicParsing());
        this.label.setLabelFor((Node)c);
        this.arrow = new StackPane();
        this.arrow.getStyleClass().setAll((Object[])new String[]{"arrow"});
        this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
        this.arrowButton = new StackPane();
        this.arrowButton.getStyleClass().setAll((Object[])new String[]{"arrow-button"});
        this.arrowButton.getChildren().add((Object)this.arrow);
        this.popup = new ContextMenu();
        this.popup.getItems().clear();
        this.popup.getItems().addAll((Collection)((MenuButton)this.getSkinnable()).getItems());
        this.getChildren().clear();
        this.getChildren().addAll((Object[])new Node[]{this.label, this.arrowButton});
        ((MenuButton)this.getSkinnable()).requestLayout();
        this.itemsChangedListener = change -> {
            while (change.next()) {
                this.popup.getItems().removeAll((Collection)change.getRemoved());
                this.popup.getItems().addAll(change.getFrom(), (Collection)change.getAddedSubList());
            }
        };
        c.getItems().addListener(this.itemsChangedListener);
        if (((MenuButton)this.getSkinnable()).getScene() != null) {
            ControlAcceleratorSupport.addAcceleratorsIntoScene((ObservableList<MenuItem>)((MenuButton)this.getSkinnable()).getItems(), (Node)this.getSkinnable());
        }
        c.sceneProperty().addListener((observableValue, scene, scene2) -> {
            if (this.getSkinnable() != null && ((MenuButton)this.getSkinnable()).getScene() != null) {
                ControlAcceleratorSupport.addAcceleratorsIntoScene((ObservableList<MenuItem>)((MenuButton)this.getSkinnable()).getItems(), (Node)this.getSkinnable());
            }
        });
        this.registerChangeListener((ObservableValue<?>)c.showingProperty(), "SHOWING");
        this.registerChangeListener((ObservableValue<?>)c.focusedProperty(), "FOCUSED");
        this.registerChangeListener((ObservableValue<?>)c.mnemonicParsingProperty(), "MNEMONIC_PARSING");
        this.registerChangeListener((ObservableValue<?>)this.popup.showingProperty(), "POPUP_VISIBLE");
    }

    @Override
    public void dispose() {
        ((MenuButton)this.getSkinnable()).getItems().removeListener(this.itemsChangedListener);
        super.dispose();
        if (this.popup != null) {
            if (this.popup.getSkin() != null && this.popup.getSkin().getNode() != null) {
                ContextMenuContent contextMenuContent = (ContextMenuContent)this.popup.getSkin().getNode();
                contextMenuContent.dispose();
                contextMenuContent = null;
            }
            this.popup.setSkin(null);
            this.popup = null;
        }
    }

    private void show() {
        if (!this.popup.isShowing()) {
            this.popup.show((Node)this.getSkinnable(), ((MenuButton)this.getSkinnable()).getPopupSide(), 0.0, 0.0);
        }
    }

    private void hide() {
        if (this.popup.isShowing()) {
            this.popup.hide();
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("SHOWING".equals(string)) {
            if (((MenuButton)this.getSkinnable()).isShowing()) {
                this.show();
            } else {
                this.hide();
            }
        } else if ("FOCUSED".equals(string)) {
            if (!((MenuButton)this.getSkinnable()).isFocused() && ((MenuButton)this.getSkinnable()).isShowing()) {
                this.hide();
            }
            if (!((MenuButton)this.getSkinnable()).isFocused() && this.popup.isShowing()) {
                this.hide();
            }
        } else if ("POPUP_VISIBLE".equals(string)) {
            if (!this.popup.isShowing() && ((MenuButton)this.getSkinnable()).isShowing()) {
                ((MenuButton)this.getSkinnable()).hide();
            }
            if (this.popup.isShowing()) {
                Utils.addMnemonics(this.popup, ((MenuButton)this.getSkinnable()).getScene(), ((MenuButton)this.getSkinnable()).impl_isShowMnemonics());
            } else {
                Utils.removeMnemonics(this.popup, ((MenuButton)this.getSkinnable()).getScene());
            }
        } else if ("MNEMONIC_PARSING".equals(string)) {
            this.label.setMnemonicParsing(((MenuButton)this.getSkinnable()).isMnemonicParsing());
            ((MenuButton)this.getSkinnable()).requestLayout();
        }
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        return d5 + this.label.minWidth(d) + this.snapSize(this.arrowButton.minWidth(d)) + d3;
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return d2 + Math.max(this.label.minHeight(d), this.snapSize(this.arrowButton.minHeight(-1.0))) + d4;
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        return d5 + this.label.prefWidth(d) + this.snapSize(this.arrowButton.prefWidth(d)) + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return d2 + Math.max(this.label.prefHeight(d), this.snapSize(this.arrowButton.prefHeight(-1.0))) + d4;
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        return ((MenuButton)this.getSkinnable()).prefWidth(d);
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((MenuButton)this.getSkinnable()).prefHeight(d);
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        double d5 = this.snapSize(this.arrowButton.prefWidth(-1.0));
        this.label.resizeRelocate(d, d2, d3 - d5, d4);
        this.arrowButton.resizeRelocate(d + (d3 - d5), d2, d5, d4);
    }

    private class MenuLabeledImpl
    extends LabeledImpl {
        MenuButton button;

        public MenuLabeledImpl(MenuButton menuButton) {
            super((Labeled)menuButton);
            this.button = menuButton;
            this.addEventHandler(ActionEvent.ACTION, actionEvent -> {
                this.button.fireEvent((Event)new ActionEvent());
                actionEvent.consume();
            });
        }
    }
}

