/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.css.PseudoClass
 *  javafx.event.ActionEvent
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.control.ComboBox
 *  javafx.scene.control.ComboBoxBase
 *  javafx.scene.control.ListCell
 *  javafx.scene.control.ListView
 *  javafx.scene.control.MultipleSelectionModel
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.control.TextField
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.MouseEvent
 *  javafx.util.Callback
 *  javafx.util.StringConverter
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxListViewBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxListViewSkin<T>
extends ComboBoxPopupControl<T> {
    private static final String COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY = "comboBoxRowsToMeasureWidth";
    private final ComboBox<T> comboBox;
    private ObservableList<T> comboBoxItems;
    private ListCell<T> buttonCell;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private final ListView<T> listView;
    private ObservableList<T> listViewItems;
    private boolean listSelectionLock = false;
    private boolean listViewSelectionDirty = false;
    private boolean itemCountDirty;
    private final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>(){

        public void onChanged(ListChangeListener.Change<? extends T> change) {
            ComboBoxListViewSkin.this.itemCountDirty = true;
            ((ComboBoxBase)ComboBoxListViewSkin.this.getSkinnable()).requestLayout();
        }
    };
    private final InvalidationListener itemsObserver;
    private final WeakListChangeListener<T> weakListViewItemsListener = new WeakListChangeListener(this.listViewItemsListener);
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass((String)"selected");
    private static final PseudoClass PSEUDO_CLASS_EMPTY = PseudoClass.getPseudoClass((String)"empty");
    private static final PseudoClass PSEUDO_CLASS_FILLED = PseudoClass.getPseudoClass((String)"filled");

    public ComboBoxListViewSkin(ComboBox<T> comboBox) {
        super(comboBox, new ComboBoxListViewBehavior<T>(comboBox));
        this.comboBox = comboBox;
        this.updateComboBoxItems();
        this.itemsObserver = observable -> {
            this.updateComboBoxItems();
            this.updateListViewItems();
        };
        this.comboBox.itemsProperty().addListener((InvalidationListener)new WeakInvalidationListener(this.itemsObserver));
        this.listView = this.createListView();
        this.listView.setManaged(false);
        this.getChildren().add(this.listView);
        this.updateListViewItems();
        this.updateCellFactory();
        this.updateButtonCell();
        this.updateValue();
        this.registerChangeListener((ObservableValue<?>)comboBox.itemsProperty(), "ITEMS");
        this.registerChangeListener((ObservableValue<?>)comboBox.promptTextProperty(), "PROMPT_TEXT");
        this.registerChangeListener((ObservableValue<?>)comboBox.cellFactoryProperty(), "CELL_FACTORY");
        this.registerChangeListener((ObservableValue<?>)comboBox.visibleRowCountProperty(), "VISIBLE_ROW_COUNT");
        this.registerChangeListener((ObservableValue<?>)comboBox.converterProperty(), "CONVERTER");
        this.registerChangeListener((ObservableValue<?>)comboBox.buttonCellProperty(), "BUTTON_CELL");
        this.registerChangeListener((ObservableValue<?>)comboBox.valueProperty(), "VALUE");
        this.registerChangeListener((ObservableValue<?>)comboBox.editableProperty(), "EDITABLE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ITEMS".equals(string)) {
            this.updateComboBoxItems();
            this.updateListViewItems();
        } else if ("PROMPT_TEXT".equals(string)) {
            this.updateDisplayNode();
        } else if ("CELL_FACTORY".equals(string)) {
            this.updateCellFactory();
        } else if ("VISIBLE_ROW_COUNT".equals(string)) {
            if (this.listView == null) {
                return;
            }
            this.listView.requestLayout();
        } else if ("CONVERTER".equals(string)) {
            this.updateListViewItems();
        } else if ("EDITOR".equals(string)) {
            this.getEditableInputNode();
        } else if ("BUTTON_CELL".equals(string)) {
            this.updateButtonCell();
        } else if ("VALUE".equals(string)) {
            this.updateValue();
            this.comboBox.fireEvent((Event)new ActionEvent());
        } else if ("EDITABLE".equals(string)) {
            this.updateEditable();
        }
    }

    @Override
    protected TextField getEditor() {
        return ((ComboBoxBase)this.getSkinnable()).isEditable() ? ((ComboBox)this.getSkinnable()).getEditor() : null;
    }

    @Override
    protected StringConverter<T> getConverter() {
        return ((ComboBox)this.getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        ListCell<T> listCell = this.comboBox.isEditable() ? this.getEditableInputNode() : this.buttonCell;
        this.updateDisplayNode();
        return listCell;
    }

    public void updateComboBoxItems() {
        this.comboBoxItems = this.comboBox.getItems();
        this.comboBoxItems = this.comboBoxItems == null ? FXCollections.emptyObservableList() : this.comboBoxItems;
    }

    public void updateListViewItems() {
        if (this.listViewItems != null) {
            this.listViewItems.removeListener(this.weakListViewItemsListener);
        }
        this.listViewItems = this.comboBoxItems;
        this.listView.setItems(this.listViewItems);
        if (this.listViewItems != null) {
            this.listViewItems.addListener(this.weakListViewItemsListener);
        }
        this.itemCountDirty = true;
        ((ComboBoxBase)this.getSkinnable()).requestLayout();
    }

    @Override
    public Node getPopupContent() {
        return this.listView;
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        this.reconfigurePopup();
        return 50.0;
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = super.computePrefWidth(d, d2, d3, d4, d5);
        double d7 = this.listView.prefWidth(d);
        double d8 = Math.max(d6, d7);
        this.reconfigurePopup();
        return d8;
    }

    @Override
    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        this.reconfigurePopup();
        return super.computeMaxWidth(d, d2, d3, d4, d5);
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        this.reconfigurePopup();
        return super.computeMinHeight(d, d2, d3, d4, d5);
    }

    @Override
    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        this.reconfigurePopup();
        return super.computePrefHeight(d, d2, d3, d4, d5);
    }

    @Override
    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        this.reconfigurePopup();
        return super.computeMaxHeight(d, d2, d3, d4, d5);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        if (this.listViewSelectionDirty) {
            try {
                this.listSelectionLock = true;
                Object object = this.comboBox.getSelectionModel().getSelectedItem();
                this.listView.getSelectionModel().clearSelection();
                this.listView.getSelectionModel().select(object);
            }
            finally {
                this.listSelectionLock = false;
                this.listViewSelectionDirty = false;
            }
        }
        super.layoutChildren(d, d2, d3, d4);
    }

    protected boolean isHideOnClickEnabled() {
        return true;
    }

    private void updateValue() {
        Object object = this.comboBox.getValue();
        MultipleSelectionModel multipleSelectionModel = this.listView.getSelectionModel();
        if (object == null) {
            multipleSelectionModel.clearSelection();
        } else {
            int n = this.getIndexOfComboBoxValueInItemsList();
            if (n == -1) {
                this.listSelectionLock = true;
                multipleSelectionModel.clearSelection();
                this.listSelectionLock = false;
            } else {
                int n2 = this.comboBox.getSelectionModel().getSelectedIndex();
                if (n2 >= 0 && n2 < this.comboBoxItems.size()) {
                    Object object2 = this.comboBoxItems.get(n2);
                    if (object2 != null && object2.equals(object)) {
                        multipleSelectionModel.select(n2);
                    } else {
                        multipleSelectionModel.select(object);
                    }
                } else {
                    int n3 = this.comboBoxItems.indexOf(object);
                    if (n3 == -1) {
                        this.updateDisplayNode();
                    } else {
                        multipleSelectionModel.select(n3);
                    }
                }
            }
        }
    }

    @Override
    protected void updateDisplayNode() {
        if (this.getEditor() != null) {
            super.updateDisplayNode();
        } else {
            Object object = this.comboBox.getValue();
            int n = this.getIndexOfComboBoxValueInItemsList();
            if (n > -1) {
                this.buttonCell.setItem(null);
                this.buttonCell.updateIndex(n);
            } else {
                this.buttonCell.updateIndex(-1);
                boolean bl = this.updateDisplayText(this.buttonCell, object, false);
                this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_EMPTY, bl);
                this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_FILLED, !bl);
                this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
            }
        }
    }

    private boolean updateDisplayText(ListCell<T> listCell, T t, boolean bl) {
        if (bl) {
            if (listCell == null) {
                return true;
            }
            listCell.setGraphic(null);
            listCell.setText(null);
            return true;
        }
        if (t instanceof Node) {
            Node node = listCell.getGraphic();
            Node node2 = (Node)t;
            if (node == null || !node.equals((Object)node2)) {
                listCell.setText(null);
                listCell.setGraphic(node2);
            }
            return node2 == null;
        }
        StringConverter stringConverter = this.comboBox.getConverter();
        String string = t == null ? this.comboBox.getPromptText() : (stringConverter == null ? t.toString() : stringConverter.toString(t));
        listCell.setText(string);
        listCell.setGraphic(null);
        return string == null || string.isEmpty();
    }

    private int getIndexOfComboBoxValueInItemsList() {
        Object object = this.comboBox.getValue();
        int n = this.comboBoxItems.indexOf(object);
        return n;
    }

    private void updateButtonCell() {
        this.buttonCell = this.comboBox.getButtonCell() != null ? this.comboBox.getButtonCell() : (ListCell)this.getDefaultCellFactory().call(this.listView);
        this.buttonCell.setMouseTransparent(true);
        this.buttonCell.updateListView(this.listView);
        this.updateDisplayArea();
        this.buttonCell.setAccessibleRole(AccessibleRole.NODE);
    }

    private void updateCellFactory() {
        Callback<ListView<T>, ListCell<T>> callback = this.comboBox.getCellFactory();
        this.cellFactory = callback != null ? callback : this.getDefaultCellFactory();
        this.listView.setCellFactory(this.cellFactory);
    }

    private Callback<ListView<T>, ListCell<T>> getDefaultCellFactory() {
        return new Callback<ListView<T>, ListCell<T>>(){

            public ListCell<T> call(ListView<T> listView) {
                return new ListCell<T>(){

                    public void updateItem(T t, boolean bl) {
                        super.updateItem(t, bl);
                        ComboBoxListViewSkin.this.updateDisplayText(this, t, bl);
                    }
                };
            }
        };
    }

    private ListView<T> createListView() {
        ListView listView = new ListView<T>(){
            {
                this.getProperties().put((Object)"selectFirstRowByDefault", (Object)false);
            }

            protected double computeMinHeight(double d) {
                return 30.0;
            }

            protected double computePrefWidth(double d) {
                double d2;
                if (this.getSkin() instanceof ListViewSkin) {
                    ListViewSkin listViewSkin = (ListViewSkin)this.getSkin();
                    if (ComboBoxListViewSkin.this.itemCountDirty) {
                        listViewSkin.updateRowCount();
                        ComboBoxListViewSkin.this.itemCountDirty = false;
                    }
                    int n = -1;
                    if (ComboBoxListViewSkin.this.comboBox.getProperties().containsKey((Object)ComboBoxListViewSkin.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY)) {
                        n = (Integer)ComboBoxListViewSkin.this.comboBox.getProperties().get((Object)ComboBoxListViewSkin.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY);
                    }
                    d2 = Math.max(ComboBoxListViewSkin.this.comboBox.getWidth(), listViewSkin.getMaxCellWidth(n) + 30.0);
                } else {
                    d2 = Math.max(100.0, ComboBoxListViewSkin.this.comboBox.getWidth());
                }
                if (this.getItems().isEmpty() && this.getPlaceholder() != null) {
                    d2 = Math.max(super.computePrefWidth(d), d2);
                }
                return Math.max(50.0, d2);
            }

            protected double computePrefHeight(double d) {
                return ComboBoxListViewSkin.this.getListViewPrefHeight();
            }
        };
        listView.setId("list-view");
        listView.placeholderProperty().bind((ObservableValue)this.comboBox.placeholderProperty());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setFocusTraversable(false);
        listView.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            if (this.listSelectionLock) {
                return;
            }
            int n = this.listView.getSelectionModel().getSelectedIndex();
            this.comboBox.getSelectionModel().select(n);
            this.updateDisplayNode();
            this.comboBox.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
        });
        this.comboBox.getSelectionModel().selectedItemProperty().addListener(observable -> {
            this.listViewSelectionDirty = true;
        });
        listView.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            ObservableList observableList;
            EventTarget eventTarget = mouseEvent.getTarget();
            if (eventTarget instanceof Parent && ((observableList = ((Parent)eventTarget).getStyleClass()).contains("thumb") || observableList.contains("track") || observableList.contains("decrement-arrow") || observableList.contains("increment-arrow"))) {
                return;
            }
            if (this.isHideOnClickEnabled()) {
                this.comboBox.hide();
            }
        });
        listView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ESCAPE) {
                this.comboBox.hide();
            }
        });
        return listView;
    }

    private double getListViewPrefHeight() {
        double d;
        if (this.listView.getSkin() instanceof VirtualContainerBase) {
            int n = this.comboBox.getVisibleRowCount();
            VirtualContainerBase virtualContainerBase = (VirtualContainerBase)this.listView.getSkin();
            d = virtualContainerBase.getVirtualFlowPreferredHeight(n);
        } else {
            double d2 = this.comboBoxItems.size() * 25;
            d = Math.min(d2, 200.0);
        }
        return d;
    }

    public ListView<T> getListView() {
        return this.listView;
    }

    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                if (this.comboBox.isShowing()) {
                    return this.listView.queryAccessibleAttribute(accessibleAttribute, arrobject);
                }
                return null;
            }
            case TEXT: {
                String string;
                String string2 = this.comboBox.getAccessibleText();
                if (string2 != null && !string2.isEmpty()) {
                    return string2;
                }
                String string3 = string = this.comboBox.isEditable() ? this.getEditor().getText() : this.buttonCell.getText();
                if (string == null || string.isEmpty()) {
                    string = this.comboBox.getPromptText();
                }
                return string;
            }
            case SELECTION_START: {
                return this.getEditor().getSelection().getStart();
            }
            case SELECTION_END: {
                return this.getEditor().getSelection().getEnd();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

