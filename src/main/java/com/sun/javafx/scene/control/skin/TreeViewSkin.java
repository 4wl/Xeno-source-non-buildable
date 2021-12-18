/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.MapChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableMap
 *  javafx.event.EventHandler
 *  javafx.event.EventType
 *  javafx.event.WeakEventHandler
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.control.FocusModel
 *  javafx.scene.control.MultipleSelectionModel
 *  javafx.scene.control.TreeCell
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeItem$TreeModificationEvent
 *  javafx.scene.control.TreeView
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.StackPane
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TreeViewBehavior;
import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class TreeViewSkin<T>
extends VirtualContainerBase<TreeView<T>, TreeViewBehavior<T>, TreeCell<T>> {
    public static final String RECREATE = "treeRecreateKey";
    private static final boolean IS_PANNABLE = AccessController.doPrivileged(() -> Boolean.getBoolean("com.sun.javafx.scene.control.skin.TreeViewSkin.pannable"));
    private boolean needCellsRebuilt = true;
    private boolean needCellsReconfigured = false;
    private MapChangeListener<Object, Object> propertiesMapListener = change -> {
        if (!change.wasAdded()) {
            return;
        }
        if (RECREATE.equals(change.getKey())) {
            this.needCellsRebuilt = true;
            ((TreeView)this.getSkinnable()).requestLayout();
            ((TreeView)this.getSkinnable()).getProperties().remove((Object)RECREATE);
        }
    };
    private EventHandler<TreeItem.TreeModificationEvent<T>> rootListener = treeModificationEvent -> {
        if (treeModificationEvent.wasAdded() && treeModificationEvent.wasRemoved() && treeModificationEvent.getAddedSize() == treeModificationEvent.getRemovedSize()) {
            this.rowCountDirty = true;
            ((TreeView)this.getSkinnable()).requestLayout();
        } else if (treeModificationEvent.getEventType().equals((Object)TreeItem.valueChangedEvent())) {
            this.needCellsRebuilt = true;
            ((TreeView)this.getSkinnable()).requestLayout();
        } else {
            for (EventType eventType = treeModificationEvent.getEventType(); eventType != null; eventType = eventType.getSuperType()) {
                if (!eventType.equals((Object)TreeItem.expandedItemCountChangeEvent())) continue;
                this.rowCountDirty = true;
                ((TreeView)this.getSkinnable()).requestLayout();
                break;
            }
        }
        ((TreeView)this.getSkinnable()).edit(null);
    };
    private WeakEventHandler<TreeItem.TreeModificationEvent<T>> weakRootListener;
    private WeakReference<TreeItem<T>> weakRoot;

    public TreeViewSkin(TreeView treeView) {
        super(treeView, new TreeViewBehavior(treeView));
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setCreateCell(virtualFlow -> this.createCell());
        this.flow.setFixedCellSize(treeView.getFixedCellSize());
        this.getChildren().add((Object)this.flow);
        this.setRoot(((TreeView)this.getSkinnable()).getRoot());
        EventHandler eventHandler = mouseEvent -> {
            if (treeView.getEditingItem() != null) {
                treeView.edit(null);
            }
            if (treeView.isFocusTraversable()) {
                treeView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        ObservableMap observableMap = treeView.getProperties();
        observableMap.remove((Object)RECREATE);
        observableMap.addListener(this.propertiesMapListener);
        ((TreeViewBehavior)this.getBehavior()).setOnFocusPreviousRow(() -> this.onFocusPreviousCell());
        ((TreeViewBehavior)this.getBehavior()).setOnFocusNextRow(() -> this.onFocusNextCell());
        ((TreeViewBehavior)this.getBehavior()).setOnMoveToFirstCell(() -> this.onMoveToFirstCell());
        ((TreeViewBehavior)this.getBehavior()).setOnMoveToLastCell(() -> this.onMoveToLastCell());
        ((TreeViewBehavior)this.getBehavior()).setOnScrollPageDown((Callback<Boolean, Integer>)((Callback)bl -> this.onScrollPageDown((boolean)bl)));
        ((TreeViewBehavior)this.getBehavior()).setOnScrollPageUp((Callback<Boolean, Integer>)((Callback)bl -> this.onScrollPageUp((boolean)bl)));
        ((TreeViewBehavior)this.getBehavior()).setOnSelectPreviousRow(() -> this.onSelectPreviousCell());
        ((TreeViewBehavior)this.getBehavior()).setOnSelectNextRow(() -> this.onSelectNextCell());
        this.registerChangeListener((ObservableValue<?>)treeView.rootProperty(), "ROOT");
        this.registerChangeListener((ObservableValue<?>)treeView.showRootProperty(), "SHOW_ROOT");
        this.registerChangeListener((ObservableValue<?>)treeView.cellFactoryProperty(), "CELL_FACTORY");
        this.registerChangeListener((ObservableValue<?>)treeView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
        this.updateRowCount();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ROOT".equals(string)) {
            this.setRoot(((TreeView)this.getSkinnable()).getRoot());
        } else if ("SHOW_ROOT".equals(string)) {
            if (!((TreeView)this.getSkinnable()).isShowRoot() && this.getRoot() != null) {
                this.getRoot().setExpanded(true);
            }
            this.updateRowCount();
        } else if ("CELL_FACTORY".equals(string)) {
            this.flow.recreateCells();
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.flow.setFixedCellSize(((TreeView)this.getSkinnable()).getFixedCellSize());
        }
    }

    private TreeItem<T> getRoot() {
        return this.weakRoot == null ? null : (TreeItem)this.weakRoot.get();
    }

    private void setRoot(TreeItem<T> treeItem) {
        if (this.getRoot() != null && this.weakRootListener != null) {
            this.getRoot().removeEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.weakRoot = new WeakReference<TreeItem<TreeItem<T>>>(treeItem);
        if (this.getRoot() != null) {
            this.weakRootListener = new WeakEventHandler(this.rootListener);
            this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.updateRowCount();
    }

    @Override
    public int getItemCount() {
        return ((TreeView)this.getSkinnable()).getExpandedItemCount();
    }

    @Override
    protected void updateRowCount() {
        int n = this.getItemCount();
        this.flow.setCellCount(n);
        this.needCellsRebuilt = true;
        ((TreeView)this.getSkinnable()).requestLayout();
    }

    @Override
    public TreeCell<T> createCell() {
        TreeCell treeCell = ((TreeView)this.getSkinnable()).getCellFactory() != null ? (TreeCell)((TreeView)this.getSkinnable()).getCellFactory().call((Object)this.getSkinnable()) : this.createDefaultCellImpl();
        if (treeCell.getDisclosureNode() == null) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().setAll((Object[])new String[]{"tree-disclosure-node"});
            StackPane stackPane2 = new StackPane();
            stackPane2.getStyleClass().setAll((Object[])new String[]{"arrow"});
            stackPane.getChildren().add((Object)stackPane2);
            treeCell.setDisclosureNode((Node)stackPane);
        }
        treeCell.updateTreeView((TreeView)this.getSkinnable());
        return treeCell;
    }

    private TreeCell<T> createDefaultCellImpl() {
        return new TreeCell<T>(){
            private HBox hbox;
            private WeakReference<TreeItem<T>> treeItemRef;
            private InvalidationListener treeItemGraphicListener = observable -> this.updateDisplay(this.getItem(), this.isEmpty());
            private InvalidationListener treeItemListener = new InvalidationListener(){

                public void invalidated(Observable observable) {
                    TreeItem treeItem;
                    TreeItem treeItem2;
                    TreeItem treeItem3 = treeItem2 = treeItemRef == null ? null : (TreeItem)treeItemRef.get();
                    if (treeItem2 != null) {
                        treeItem2.graphicProperty().removeListener((InvalidationListener)weakTreeItemGraphicListener);
                    }
                    if ((treeItem = this.getTreeItem()) != null) {
                        treeItem.graphicProperty().addListener((InvalidationListener)weakTreeItemGraphicListener);
                        treeItemRef = new WeakReference<TreeItem>(treeItem);
                    }
                }
            };
            private WeakInvalidationListener weakTreeItemGraphicListener = new WeakInvalidationListener(this.treeItemGraphicListener);
            private WeakInvalidationListener weakTreeItemListener = new WeakInvalidationListener(this.treeItemListener);
            {
                this.treeItemProperty().addListener((InvalidationListener)this.weakTreeItemListener);
                if (this.getTreeItem() != null) {
                    this.getTreeItem().graphicProperty().addListener((InvalidationListener)this.weakTreeItemGraphicListener);
                }
            }

            private void updateDisplay(T t, boolean bl) {
                if (t == null || bl) {
                    this.hbox = null;
                    this.setText(null);
                    this.setGraphic(null);
                } else {
                    Node node;
                    TreeItem treeItem = this.getTreeItem();
                    Node node2 = node = treeItem == null ? null : treeItem.getGraphic();
                    if (node != null) {
                        if (t instanceof Node) {
                            this.setText(null);
                            if (this.hbox == null) {
                                this.hbox = new HBox(3.0);
                            }
                            this.hbox.getChildren().setAll((Object[])new Node[]{node, (Node)t});
                            this.setGraphic((Node)this.hbox);
                        } else {
                            this.hbox = null;
                            this.setText(t.toString());
                            this.setGraphic(node);
                        }
                    } else {
                        this.hbox = null;
                        if (t instanceof Node) {
                            this.setText(null);
                            this.setGraphic((Node)t);
                        } else {
                            this.setText(t.toString());
                            this.setGraphic(null);
                        }
                    }
                }
            }

            public void updateItem(T t, boolean bl) {
                super.updateItem(t, bl);
                this.updateDisplay(t, bl);
            }
        };
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        return this.computePrefHeight(-1.0, d2, d3, d4, d5) * 0.618033987;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return 400.0;
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
        this.flow.resizeRelocate(d, d2, d3, d4);
    }

    private void onFocusPreviousCell() {
        FocusModel focusModel = ((TreeView)this.getSkinnable()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        this.flow.show(focusModel.getFocusedIndex());
    }

    private void onFocusNextCell() {
        FocusModel focusModel = ((TreeView)this.getSkinnable()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        this.flow.show(focusModel.getFocusedIndex());
    }

    private void onSelectPreviousCell() {
        int n = ((TreeView)this.getSkinnable()).getSelectionModel().getSelectedIndex();
        this.flow.show(n);
    }

    private void onSelectNextCell() {
        int n = ((TreeView)this.getSkinnable()).getSelectionModel().getSelectedIndex();
        this.flow.show(n);
    }

    private void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0);
    }

    private void onMoveToLastCell() {
        this.flow.show(this.getItemCount());
        this.flow.setPosition(1.0);
    }

    public int onScrollPageDown(boolean bl) {
        int n;
        TreeCell treeCell = (TreeCell)this.flow.getLastVisibleCellWithinViewPort();
        if (treeCell == null) {
            return -1;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getSkinnable()).getSelectionModel();
        FocusModel focusModel = ((TreeView)this.getSkinnable()).getFocusModel();
        if (multipleSelectionModel == null || focusModel == null) {
            return -1;
        }
        int n2 = treeCell.getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = treeCell.isFocused() || focusModel.isFocused(n2);
        } else {
            boolean bl3 = bl2 = treeCell.isSelected() || multipleSelectionModel.isSelected(n2);
        }
        if (bl2) {
            int n3 = n = bl && focusModel.getFocusedIndex() == n2 || !bl && multipleSelectionModel.getSelectedIndex() == n2 ? 1 : 0;
            if (n != 0) {
                this.flow.showAsFirst(treeCell);
                TreeCell treeCell2 = (TreeCell)this.flow.getLastVisibleCellWithinViewPort();
                treeCell = treeCell2 == null ? treeCell : treeCell2;
            }
        }
        n = treeCell.getIndex();
        this.flow.show(treeCell);
        return n;
    }

    public int onScrollPageUp(boolean bl) {
        int n;
        TreeCell treeCell = (TreeCell)this.flow.getFirstVisibleCellWithinViewPort();
        if (treeCell == null) {
            return -1;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getSkinnable()).getSelectionModel();
        FocusModel focusModel = ((TreeView)this.getSkinnable()).getFocusModel();
        if (multipleSelectionModel == null || focusModel == null) {
            return -1;
        }
        int n2 = treeCell.getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = treeCell.isFocused() || focusModel.isFocused(n2);
        } else {
            boolean bl3 = bl2 = treeCell.isSelected() || multipleSelectionModel.isSelected(n2);
        }
        if (bl2) {
            int n3 = n = bl && focusModel.getFocusedIndex() == n2 || !bl && multipleSelectionModel.getSelectedIndex() == n2 ? 1 : 0;
            if (n != 0) {
                this.flow.showAsLast(treeCell);
                TreeCell treeCell2 = (TreeCell)this.flow.getFirstVisibleCellWithinViewPort();
                treeCell = treeCell2 == null ? treeCell : treeCell2;
            }
        }
        n = treeCell.getIndex();
        this.flow.show(treeCell);
        return n;
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                FocusModel focusModel = ((TreeView)this.getSkinnable()).getFocusModel();
                int n = focusModel.getFocusedIndex();
                if (n == -1) {
                    if (this.getItemCount() > 0) {
                        n = 0;
                    } else {
                        return null;
                    }
                }
                return this.flow.getPrivateCell(n);
            }
            case ROW_AT_INDEX: {
                int n = (Integer)arrobject[0];
                return n < 0 ? null : this.flow.getPrivateCell(n);
            }
            case SELECTED_ITEMS: {
                MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getSkinnable()).getSelectionModel();
                ObservableList observableList = multipleSelectionModel.getSelectedIndices();
                ArrayList<TreeCell> arrayList = new ArrayList<TreeCell>(observableList.size());
                Iterator iterator = observableList.iterator();
                while (iterator.hasNext()) {
                    int n = (Integer)iterator.next();
                    TreeCell treeCell = (TreeCell)this.flow.getPrivateCell(n);
                    if (treeCell == null) continue;
                    arrayList.add(treeCell);
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
                if (!(node instanceof TreeCell)) break;
                TreeCell treeCell = (TreeCell)node;
                this.flow.show(treeCell.getIndex());
                break;
            }
            case SET_SELECTED_ITEMS: {
                MultipleSelectionModel multipleSelectionModel;
                ObservableList observableList = (ObservableList)arrobject[0];
                if (observableList == null || (multipleSelectionModel = ((TreeView)this.getSkinnable()).getSelectionModel()) == null) break;
                multipleSelectionModel.clearSelection();
                for (Node node : observableList) {
                    if (!(node instanceof TreeCell)) continue;
                    TreeCell treeCell = (TreeCell)node;
                    multipleSelectionModel.select(treeCell.getIndex());
                }
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }
}

