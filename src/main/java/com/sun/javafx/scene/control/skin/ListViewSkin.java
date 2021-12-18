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
 *  javafx.collections.MapChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableMap
 *  javafx.collections.WeakListChangeListener
 *  javafx.event.EventHandler
 *  javafx.geometry.Orientation
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.control.FocusModel
 *  javafx.scene.control.Label
 *  javafx.scene.control.ListCell
 *  javafx.scene.control.ListView
 *  javafx.scene.control.MultipleSelectionModel
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.StackPane
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListViewBehavior;
import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class ListViewSkin<T>
extends VirtualContainerBase<ListView<T>, ListViewBehavior<T>, ListCell<T>> {
    public static final String RECREATE = "listRecreateKey";
    private StackPane placeholderRegion;
    private Node placeholderNode;
    private static final String EMPTY_LIST_TEXT = ControlResources.getString("ListView.noContent");
    private static final boolean IS_PANNABLE = AccessController.doPrivileged(() -> Boolean.getBoolean("com.sun.javafx.scene.control.skin.ListViewSkin.pannable"));
    private ObservableList<T> listViewItems;
    private final InvalidationListener itemsChangeListener = observable -> this.updateListViewItems();
    private MapChangeListener<Object, Object> propertiesMapListener = change -> {
        if (!change.wasAdded()) {
            return;
        }
        if (RECREATE.equals(change.getKey())) {
            this.needCellsRebuilt = true;
            ((ListView)this.getSkinnable()).requestLayout();
            ((ListView)this.getSkinnable()).getProperties().remove((Object)RECREATE);
        }
    };
    private final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>(){

        public void onChanged(ListChangeListener.Change<? extends T> change) {
            while (change.next()) {
                if (change.wasReplaced()) {
                    for (int i = change.getFrom(); i < change.getTo(); ++i) {
                        ListViewSkin.this.flow.setCellDirty(i);
                    }
                    break;
                }
                if (change.getRemovedSize() != ListViewSkin.this.itemCount) continue;
                ListViewSkin.this.itemCount = 0;
                break;
            }
            ((ListView)ListViewSkin.this.getSkinnable()).edit(-1);
            ListViewSkin.this.rowCountDirty = true;
            ((ListView)ListViewSkin.this.getSkinnable()).requestLayout();
        }
    };
    private final WeakListChangeListener<T> weakListViewItemsListener = new WeakListChangeListener(this.listViewItemsListener);
    private int itemCount = -1;
    private boolean needCellsRebuilt = true;
    private boolean needCellsReconfigured = false;

    public ListViewSkin(ListView<T> listView) {
        super(listView, new ListViewBehavior<T>(listView));
        this.updateListViewItems();
        this.flow.setId("virtual-flow");
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setVertical(((ListView)this.getSkinnable()).getOrientation() == Orientation.VERTICAL);
        this.flow.setCreateCell(virtualFlow -> this.createCell());
        this.flow.setFixedCellSize(listView.getFixedCellSize());
        this.getChildren().add((Object)this.flow);
        EventHandler eventHandler = mouseEvent -> {
            if (listView.getEditingIndex() > -1) {
                listView.edit(-1);
            }
            if (listView.isFocusTraversable()) {
                listView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.updateRowCount();
        listView.itemsProperty().addListener((InvalidationListener)new WeakInvalidationListener(this.itemsChangeListener));
        ObservableMap observableMap = listView.getProperties();
        observableMap.remove((Object)RECREATE);
        observableMap.addListener(this.propertiesMapListener);
        ((ListViewBehavior)this.getBehavior()).setOnFocusPreviousRow(() -> this.onFocusPreviousCell());
        ((ListViewBehavior)this.getBehavior()).setOnFocusNextRow(() -> this.onFocusNextCell());
        ((ListViewBehavior)this.getBehavior()).setOnMoveToFirstCell(() -> this.onMoveToFirstCell());
        ((ListViewBehavior)this.getBehavior()).setOnMoveToLastCell(() -> this.onMoveToLastCell());
        ((ListViewBehavior)this.getBehavior()).setOnScrollPageDown((Callback<Boolean, Integer>)((Callback)bl -> this.onScrollPageDown((boolean)bl)));
        ((ListViewBehavior)this.getBehavior()).setOnScrollPageUp((Callback<Boolean, Integer>)((Callback)bl -> this.onScrollPageUp((boolean)bl)));
        ((ListViewBehavior)this.getBehavior()).setOnSelectPreviousRow(() -> this.onSelectPreviousCell());
        ((ListViewBehavior)this.getBehavior()).setOnSelectNextRow(() -> this.onSelectNextCell());
        this.registerChangeListener((ObservableValue<?>)listView.itemsProperty(), "ITEMS");
        this.registerChangeListener((ObservableValue<?>)listView.orientationProperty(), "ORIENTATION");
        this.registerChangeListener((ObservableValue<?>)listView.cellFactoryProperty(), "CELL_FACTORY");
        this.registerChangeListener((ObservableValue<?>)listView.parentProperty(), "PARENT");
        this.registerChangeListener((ObservableValue<?>)listView.placeholderProperty(), "PLACEHOLDER");
        this.registerChangeListener((ObservableValue<?>)listView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ITEMS".equals(string)) {
            this.updateListViewItems();
        } else if ("ORIENTATION".equals(string)) {
            this.flow.setVertical(((ListView)this.getSkinnable()).getOrientation() == Orientation.VERTICAL);
        } else if ("CELL_FACTORY".equals(string)) {
            this.flow.recreateCells();
        } else if ("PARENT".equals(string)) {
            if (((ListView)this.getSkinnable()).getParent() != null && ((ListView)this.getSkinnable()).isVisible()) {
                ((ListView)this.getSkinnable()).requestLayout();
            }
        } else if ("PLACEHOLDER".equals(string)) {
            this.updatePlaceholderRegionVisibility();
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.flow.setFixedCellSize(((ListView)this.getSkinnable()).getFixedCellSize());
        }
    }

    public void updateListViewItems() {
        if (this.listViewItems != null) {
            this.listViewItems.removeListener(this.weakListViewItemsListener);
        }
        this.listViewItems = ((ListView)this.getSkinnable()).getItems();
        if (this.listViewItems != null) {
            this.listViewItems.addListener(this.weakListViewItemsListener);
        }
        this.rowCountDirty = true;
        ((ListView)this.getSkinnable()).requestLayout();
    }

    @Override
    public int getItemCount() {
        return this.itemCount;
    }

    @Override
    protected void updateRowCount() {
        int n;
        if (this.flow == null) {
            return;
        }
        int n2 = this.itemCount;
        this.itemCount = n = this.listViewItems == null ? 0 : this.listViewItems.size();
        this.flow.setCellCount(n);
        this.updatePlaceholderRegionVisibility();
        if (n != n2) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    protected final void updatePlaceholderRegionVisibility() {
        boolean bl;
        boolean bl2 = bl = this.getItemCount() == 0;
        if (bl) {
            this.placeholderNode = ((ListView)this.getSkinnable()).getPlaceholder();
            if (this.placeholderNode == null && EMPTY_LIST_TEXT != null && !EMPTY_LIST_TEXT.isEmpty()) {
                this.placeholderNode = new Label();
                ((Label)this.placeholderNode).setText(EMPTY_LIST_TEXT);
            }
            if (this.placeholderNode != null) {
                if (this.placeholderRegion == null) {
                    this.placeholderRegion = new StackPane();
                    this.placeholderRegion.getStyleClass().setAll((Object[])new String[]{"placeholder"});
                    this.getChildren().add((Object)this.placeholderRegion);
                }
                this.placeholderRegion.getChildren().setAll((Object[])new Node[]{this.placeholderNode});
            }
        }
        this.flow.setVisible(!bl);
        if (this.placeholderRegion != null) {
            this.placeholderRegion.setVisible(bl);
        }
    }

    @Override
    public ListCell<T> createCell() {
        ListCell listCell = ((ListView)this.getSkinnable()).getCellFactory() != null ? (ListCell)((ListView)this.getSkinnable()).getCellFactory().call((Object)this.getSkinnable()) : ListViewSkin.createDefaultCellImpl();
        listCell.updateListView((ListView)this.getSkinnable());
        return listCell;
    }

    private static <T> ListCell<T> createDefaultCellImpl() {
        return new ListCell<T>(){

            public void updateItem(T t, boolean bl) {
                super.updateItem(t, bl);
                if (bl) {
                    this.setText(null);
                    this.setGraphic(null);
                } else if (t instanceof Node) {
                    this.setText(null);
                    Node node = this.getGraphic();
                    Node node2 = (Node)t;
                    if (node == null || !node.equals((Object)node2)) {
                        this.setGraphic(node2);
                    }
                } else {
                    this.setText(t == null ? "null" : t.toString());
                    this.setGraphic(null);
                }
            }
        };
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        super.layoutChildren(d, d2, d3, d4);
        if (this.needCellsRebuilt) {
            this.flow.rebuildCells();
        } else if (this.needCellsReconfigured) {
            this.flow.reconfigureCells();
        }
        this.needCellsRebuilt = false;
        this.needCellsReconfigured = false;
        if (this.getItemCount() == 0) {
            if (this.placeholderRegion != null) {
                this.placeholderRegion.setVisible(d3 > 0.0 && d4 > 0.0);
                this.placeholderRegion.resizeRelocate(d, d2, d3, d4);
            }
        } else {
            this.flow.resizeRelocate(d, d2, d3, d4);
        }
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        this.checkState();
        if (this.getItemCount() == 0) {
            if (this.placeholderRegion == null) {
                this.updatePlaceholderRegionVisibility();
            }
            if (this.placeholderRegion != null) {
                return this.placeholderRegion.prefWidth(d) + d5 + d3;
            }
        }
        return this.computePrefHeight(-1.0, d2, d3, d4, d5) * 0.618033987;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return 400.0;
    }

    private void onFocusPreviousCell() {
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        this.flow.show(focusModel.getFocusedIndex());
    }

    private void onFocusNextCell() {
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        this.flow.show(focusModel.getFocusedIndex());
    }

    private void onSelectPreviousCell() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        int n = multipleSelectionModel.getSelectedIndex();
        this.flow.show(n);
        Object t = this.flow.getFirstVisibleCell();
        if (t == null || n < t.getIndex()) {
            this.flow.setPosition((double)n / (double)this.getItemCount());
        }
    }

    private void onSelectNextCell() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        int n = multipleSelectionModel.getSelectedIndex();
        this.flow.show(n);
        ListCell listCell = (ListCell)this.flow.getLastVisibleCell();
        if (listCell == null || listCell.getIndex() < n) {
            this.flow.setPosition((double)n / (double)this.getItemCount());
        }
    }

    private void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0);
    }

    private void onMoveToLastCell() {
        int n = this.getItemCount() - 1;
        this.flow.show(n);
        this.flow.setPosition(1.0);
    }

    private int onScrollPageDown(boolean bl) {
        int n;
        ListCell listCell = (ListCell)this.flow.getLastVisibleCellWithinViewPort();
        if (listCell == null) {
            return -1;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (multipleSelectionModel == null || focusModel == null) {
            return -1;
        }
        int n2 = listCell.getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = listCell.isFocused() || focusModel.isFocused(n2);
        } else {
            boolean bl3 = bl2 = listCell.isSelected() || multipleSelectionModel.isSelected(n2);
        }
        if (bl2) {
            int n3 = n = bl && focusModel.getFocusedIndex() == n2 || !bl && multipleSelectionModel.getSelectedIndex() == n2 ? 1 : 0;
            if (n != 0) {
                this.flow.showAsFirst(listCell);
                ListCell listCell2 = (ListCell)this.flow.getLastVisibleCellWithinViewPort();
                listCell = listCell2 == null ? listCell : listCell2;
            }
        }
        n = listCell.getIndex();
        this.flow.show(listCell);
        return n;
    }

    private int onScrollPageUp(boolean bl) {
        int n;
        ListCell listCell = (ListCell)this.flow.getFirstVisibleCellWithinViewPort();
        if (listCell == null) {
            return -1;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
        FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
        if (multipleSelectionModel == null || focusModel == null) {
            return -1;
        }
        int n2 = listCell.getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = listCell.isFocused() || focusModel.isFocused(n2);
        } else {
            boolean bl3 = bl2 = listCell.isSelected() || multipleSelectionModel.isSelected(n2);
        }
        if (bl2) {
            int n3 = n = bl && focusModel.getFocusedIndex() == n2 || !bl && multipleSelectionModel.getSelectedIndex() == n2 ? 1 : 0;
            if (n != 0) {
                this.flow.showAsLast(listCell);
                ListCell listCell2 = (ListCell)this.flow.getFirstVisibleCellWithinViewPort();
                listCell = listCell2 == null ? listCell : listCell2;
            }
        }
        n = listCell.getIndex();
        this.flow.show(listCell);
        return n;
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                FocusModel focusModel = ((ListView)this.getSkinnable()).getFocusModel();
                int n = focusModel.getFocusedIndex();
                if (n == -1) {
                    if (this.placeholderRegion != null && this.placeholderRegion.isVisible()) {
                        return this.placeholderRegion.getChildren().get(0);
                    }
                    if (this.getItemCount() > 0) {
                        n = 0;
                    } else {
                        return null;
                    }
                }
                return this.flow.getPrivateCell(n);
            }
            case ITEM_COUNT: {
                return this.getItemCount();
            }
            case ITEM_AT_INDEX: {
                Integer n = (Integer)arrobject[0];
                if (n == null) {
                    return null;
                }
                if (0 <= n && n < this.getItemCount()) {
                    return this.flow.getPrivateCell(n);
                }
                return null;
            }
            case SELECTED_ITEMS: {
                MultipleSelectionModel multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel();
                ObservableList observableList = multipleSelectionModel.getSelectedIndices();
                ArrayList<ListCell> arrayList = new ArrayList<ListCell>(observableList.size());
                Iterator iterator = observableList.iterator();
                while (iterator.hasNext()) {
                    int n = (Integer)iterator.next();
                    ListCell listCell = (ListCell)this.flow.getPrivateCell(n);
                    if (listCell == null) continue;
                    arrayList.add(listCell);
                }
                return FXCollections.observableArrayList(arrayList);
            }
            case VERTICAL_SCROLLBAR: {
                return this.flow.getVbar();
            }
            case HORIZONTAL_SCROLLBAR: {
                return this.flow.getHbar();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_ITEM: {
                Node node = (Node)arrobject[0];
                if (!(node instanceof ListCell)) break;
                ListCell listCell = (ListCell)node;
                this.flow.show(listCell.getIndex());
                break;
            }
            case SET_SELECTED_ITEMS: {
                MultipleSelectionModel multipleSelectionModel;
                ObservableList observableList = (ObservableList)arrobject[0];
                if (observableList == null || (multipleSelectionModel = ((ListView)this.getSkinnable()).getSelectionModel()) == null) break;
                multipleSelectionModel.clearSelection();
                for (Node node : observableList) {
                    if (!(node instanceof ListCell)) continue;
                    ListCell listCell = (ListCell)node;
                    multipleSelectionModel.select(listCell.getIndex());
                }
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }
}

