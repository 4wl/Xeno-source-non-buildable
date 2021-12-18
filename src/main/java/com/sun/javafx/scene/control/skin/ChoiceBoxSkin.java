/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.geometry.HPos
 *  javafx.geometry.Side
 *  javafx.geometry.VPos
 *  javafx.scene.Node
 *  javafx.scene.control.ChoiceBox
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.Label
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.RadioMenuItem
 *  javafx.scene.control.SelectionModel
 *  javafx.scene.control.Separator
 *  javafx.scene.control.SeparatorMenuItem
 *  javafx.scene.control.SingleSelectionModel
 *  javafx.scene.control.ToggleGroup
 *  javafx.scene.layout.StackPane
 *  javafx.scene.text.Text
 *  javafx.util.StringConverter
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ChoiceBoxBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class ChoiceBoxSkin<T>
extends BehaviorSkinBase<ChoiceBox<T>, ChoiceBoxBehavior<T>> {
    private ObservableList<T> choiceBoxItems;
    private ContextMenu popup;
    private StackPane openButton;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private SelectionModel<T> selectionModel;
    private Label label;
    private final ListChangeListener<T> choiceBoxItemsListener = new ListChangeListener<T>(){

        public void onChanged(ListChangeListener.Change<? extends T> change) {
            while (change.next()) {
                int n;
                if (change.getRemovedSize() > 0 || change.wasPermutated()) {
                    ChoiceBoxSkin.this.toggleGroup.getToggles().clear();
                    ChoiceBoxSkin.this.popup.getItems().clear();
                    n = 0;
                    for (Object e : change.getList()) {
                        ChoiceBoxSkin.this.addPopupItem(e, n);
                        ++n;
                    }
                    continue;
                }
                for (n = change.getFrom(); n < change.getTo(); ++n) {
                    Object object = change.getList().get(n);
                    ChoiceBoxSkin.this.addPopupItem(object, n);
                }
            }
            ChoiceBoxSkin.this.updateSelection();
            ((ChoiceBox)ChoiceBoxSkin.this.getSkinnable()).requestLayout();
        }
    };
    private final WeakListChangeListener<T> weakChoiceBoxItemsListener = new WeakListChangeListener(this.choiceBoxItemsListener);
    private final InvalidationListener itemsObserver;
    private InvalidationListener selectionChangeListener = observable -> this.updateSelection();

    public ChoiceBoxSkin(ChoiceBox<T> choiceBox) {
        super(choiceBox, new ChoiceBoxBehavior<T>(choiceBox));
        this.initialize();
        this.itemsObserver = observable -> this.updateChoiceBoxItems();
        choiceBox.itemsProperty().addListener((InvalidationListener)new WeakInvalidationListener(this.itemsObserver));
        choiceBox.requestLayout();
        this.registerChangeListener((ObservableValue<?>)choiceBox.selectionModelProperty(), "SELECTION_MODEL");
        this.registerChangeListener((ObservableValue<?>)choiceBox.showingProperty(), "SHOWING");
        this.registerChangeListener((ObservableValue<?>)choiceBox.itemsProperty(), "ITEMS");
        this.registerChangeListener((ObservableValue<?>)choiceBox.getSelectionModel().selectedItemProperty(), "SELECTION_CHANGED");
        this.registerChangeListener((ObservableValue<?>)choiceBox.converterProperty(), "CONVERTER");
    }

    private void initialize() {
        this.updateChoiceBoxItems();
        this.label = new Label();
        this.label.setMnemonicParsing(false);
        this.openButton = new StackPane();
        this.openButton.getStyleClass().setAll((Object[])new String[]{"open-button"});
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().setAll((Object[])new String[]{"arrow"});
        this.openButton.getChildren().clear();
        this.openButton.getChildren().addAll((Object[])new Node[]{stackPane});
        this.popup = new ContextMenu();
        this.popup.showingProperty().addListener((observableValue, bl, bl2) -> {
            if (!bl2.booleanValue()) {
                ((ChoiceBox)this.getSkinnable()).hide();
            }
        });
        this.popup.setId("choice-box-popup-menu");
        this.getChildren().setAll((Object[])new Node[]{this.label, this.openButton});
        this.updatePopupItems();
        this.updateSelectionModel();
        this.updateSelection();
        if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
            this.label.setText("");
        }
    }

    private void updateChoiceBoxItems() {
        if (this.choiceBoxItems != null) {
            this.choiceBoxItems.removeListener(this.weakChoiceBoxItemsListener);
        }
        this.choiceBoxItems = ((ChoiceBox)this.getSkinnable()).getItems();
        if (this.choiceBoxItems != null) {
            this.choiceBoxItems.addListener(this.weakChoiceBoxItemsListener);
        }
    }

    String getChoiceBoxSelectedText() {
        return this.label.getText();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ITEMS".equals(string)) {
            this.updateChoiceBoxItems();
            this.updatePopupItems();
            this.updateSelectionModel();
            this.updateSelection();
            if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
                this.label.setText("");
            }
        } else if ("SELECTION_MODEL".equals(string)) {
            this.updateSelectionModel();
        } else if ("SELECTION_CHANGED".equals(string)) {
            MenuItem menuItem;
            int n;
            if (((ChoiceBox)this.getSkinnable()).getSelectionModel() != null && (n = ((ChoiceBox)this.getSkinnable()).getSelectionModel().getSelectedIndex()) != -1 && (menuItem = (MenuItem)this.popup.getItems().get(n)) instanceof RadioMenuItem) {
                ((RadioMenuItem)menuItem).setSelected(true);
            }
        } else if ("SHOWING".equals(string)) {
            if (((ChoiceBox)this.getSkinnable()).isShowing()) {
                ContextMenuContent contextMenuContent;
                boolean bl;
                MenuItem menuItem = null;
                SingleSelectionModel singleSelectionModel = ((ChoiceBox)this.getSkinnable()).getSelectionModel();
                if (singleSelectionModel == null) {
                    return;
                }
                long l = singleSelectionModel.getSelectedIndex();
                int n = this.choiceBoxItems.size();
                boolean bl2 = bl = l >= 0L && l < (long)n;
                if (bl) {
                    menuItem = (MenuItem)this.popup.getItems().get((int)l);
                    if (menuItem != null && menuItem instanceof RadioMenuItem) {
                        ((RadioMenuItem)menuItem).setSelected(true);
                    }
                } else if (n > 0) {
                    menuItem = (MenuItem)this.popup.getItems().get(0);
                }
                ((ChoiceBox)this.getSkinnable()).autosize();
                double d = 0.0;
                if (this.popup.getSkin() != null && (contextMenuContent = (ContextMenuContent)this.popup.getSkin().getNode()) != null && l != -1L) {
                    d = -contextMenuContent.getMenuYOffset((int)l);
                }
                this.popup.show((Node)this.getSkinnable(), Side.BOTTOM, 2.0, d);
            } else {
                this.popup.hide();
            }
        } else if ("CONVERTER".equals(string)) {
            this.updateChoiceBoxItems();
            this.updatePopupItems();
        }
    }

    private void addPopupItem(T t, int n) {
        SeparatorMenuItem separatorMenuItem = null;
        if (t instanceof Separator) {
            separatorMenuItem = new SeparatorMenuItem();
        } else if (t instanceof SeparatorMenuItem) {
            separatorMenuItem = (SeparatorMenuItem)t;
        } else {
            StringConverter stringConverter = ((ChoiceBox)this.getSkinnable()).getConverter();
            String string = stringConverter == null ? (t == null ? "" : t.toString()) : stringConverter.toString(t);
            RadioMenuItem radioMenuItem = new RadioMenuItem(string);
            radioMenuItem.setId("choice-box-menu-item");
            radioMenuItem.setToggleGroup(this.toggleGroup);
            radioMenuItem.setOnAction(actionEvent -> {
                if (this.selectionModel == null) {
                    return;
                }
                int n = ((ChoiceBox)this.getSkinnable()).getItems().indexOf(t);
                this.selectionModel.select(n);
                radioMenuItem.setSelected(true);
            });
            separatorMenuItem = radioMenuItem;
        }
        separatorMenuItem.setMnemonicParsing(false);
        this.popup.getItems().add(n, (Object)separatorMenuItem);
    }

    private void updatePopupItems() {
        this.toggleGroup.getToggles().clear();
        this.popup.getItems().clear();
        this.toggleGroup.selectToggle(null);
        for (int i = 0; i < this.choiceBoxItems.size(); ++i) {
            Object object = this.choiceBoxItems.get(i);
            this.addPopupItem(object, i);
        }
    }

    private void updateSelectionModel() {
        if (this.selectionModel != null) {
            this.selectionModel.selectedIndexProperty().removeListener(this.selectionChangeListener);
        }
        this.selectionModel = ((ChoiceBox)this.getSkinnable()).getSelectionModel();
        if (this.selectionModel != null) {
            this.selectionModel.selectedIndexProperty().addListener(this.selectionChangeListener);
        }
    }

    private void updateSelection() {
        if (this.selectionModel == null || this.selectionModel.isEmpty()) {
            this.toggleGroup.selectToggle(null);
            this.label.setText("");
        } else {
            int n = this.selectionModel.getSelectedIndex();
            if (n == -1 || n > this.popup.getItems().size()) {
                this.label.setText("");
                return;
            }
            if (n < this.popup.getItems().size()) {
                MenuItem menuItem = (MenuItem)this.popup.getItems().get(n);
                if (menuItem instanceof RadioMenuItem) {
                    ((RadioMenuItem)menuItem).setSelected(true);
                    this.toggleGroup.selectToggle(null);
                }
                this.label.setText(((MenuItem)this.popup.getItems().get(n)).getText());
            }
        }
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        double d5 = this.openButton.prefWidth(-1.0);
        ChoiceBox choiceBox = (ChoiceBox)this.getSkinnable();
        this.label.resizeRelocate(d, d2, d3, d4);
        this.openButton.resize(d5, this.openButton.prefHeight(-1.0));
        this.positionInArea((Node)this.openButton, d + d3 - d5, d2, d5, d4, 0.0, HPos.CENTER, VPos.CENTER);
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = this.label.minWidth(-1.0) + this.openButton.minWidth(-1.0);
        double d7 = this.popup.minWidth(-1.0);
        return d5 + Math.max(d6, d7) + d3;
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = this.label.minHeight(-1.0);
        double d7 = this.openButton.minHeight(-1.0);
        return d2 + Math.max(d6, d7) + d4;
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = this.label.prefWidth(-1.0) + this.openButton.prefWidth(-1.0);
        double d7 = this.popup.prefWidth(-1.0);
        if (d7 <= 0.0 && this.popup.getItems().size() > 0) {
            d7 = new Text(((MenuItem)this.popup.getItems().get(0)).getText()).prefWidth(-1.0);
        }
        return this.popup.getItems().size() == 0 ? 50.0 : d5 + Math.max(d6, d7) + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = this.label.prefHeight(-1.0);
        double d7 = this.openButton.prefHeight(-1.0);
        return d2 + Math.max(d6, d7) + d4;
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((ChoiceBox)this.getSkinnable()).prefHeight(d);
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        return ((ChoiceBox)this.getSkinnable()).prefWidth(d);
    }
}

