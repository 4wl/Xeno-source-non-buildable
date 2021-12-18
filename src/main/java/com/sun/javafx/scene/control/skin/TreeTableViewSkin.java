/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.event.EventHandler
 *  javafx.event.EventType
 *  javafx.event.WeakEventHandler
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.control.ResizeFeaturesBase
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeItem$TreeModificationEvent
 *  javafx.scene.control.TreeTableCell
 *  javafx.scene.control.TreeTableColumn
 *  javafx.scene.control.TreeTablePosition
 *  javafx.scene.control.TreeTableRow
 *  javafx.scene.control.TreeTableView
 *  javafx.scene.control.TreeTableView$TreeTableViewFocusModel
 *  javafx.scene.control.TreeTableView$TreeTableViewSelectionModel
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.behavior.TreeTableViewBehavior;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.Utils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class TreeTableViewSkin<S>
extends TableViewSkinBase<S, TreeItem<S>, TreeTableView<S>, TreeTableViewBehavior<S>, TreeTableRow<S>, TreeTableColumn<S, ?>> {
    private TreeTableViewBackingList<S> tableBackingList;
    private ObjectProperty<ObservableList<TreeItem<S>>> tableBackingListProperty;
    private TreeTableView<S> treeTableView;
    private WeakReference<TreeItem<S>> weakRootRef;
    private EventHandler<TreeItem.TreeModificationEvent<S>> rootListener = treeModificationEvent -> {
        if (treeModificationEvent.wasAdded() && treeModificationEvent.wasRemoved() && treeModificationEvent.getAddedSize() == treeModificationEvent.getRemovedSize()) {
            this.rowCountDirty = true;
            ((TreeTableView)this.getSkinnable()).requestLayout();
        } else if (treeModificationEvent.getEventType().equals((Object)TreeItem.valueChangedEvent())) {
            this.needCellsRebuilt = true;
            ((TreeTableView)this.getSkinnable()).requestLayout();
        } else {
            for (EventType eventType = treeModificationEvent.getEventType(); eventType != null; eventType = eventType.getSuperType()) {
                if (!eventType.equals((Object)TreeItem.expandedItemCountChangeEvent())) continue;
                this.rowCountDirty = true;
                ((TreeTableView)this.getSkinnable()).requestLayout();
                break;
            }
        }
        ((TreeTableView)this.getSkinnable()).edit(-1, null);
    };
    private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakRootListener;

    public TreeTableViewSkin(TreeTableView<S> treeTableView) {
        super(treeTableView, new TreeTableViewBehavior<S>(treeTableView));
        this.treeTableView = treeTableView;
        this.tableBackingList = new TreeTableViewBackingList<S>(treeTableView);
        this.tableBackingListProperty = new SimpleObjectProperty(this.tableBackingList);
        this.flow.setFixedCellSize(treeTableView.getFixedCellSize());
        super.init(treeTableView);
        this.setRoot(((TreeTableView)this.getSkinnable()).getRoot());
        EventHandler eventHandler = mouseEvent -> {
            if (treeTableView.getEditingCell() != null) {
                treeTableView.edit(-1, null);
            }
            if (treeTableView.isFocusTraversable()) {
                treeTableView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        TreeTableViewBehavior treeTableViewBehavior = (TreeTableViewBehavior)this.getBehavior();
        treeTableViewBehavior.setOnFocusPreviousRow(() -> this.onFocusPreviousCell());
        treeTableViewBehavior.setOnFocusNextRow(() -> this.onFocusNextCell());
        treeTableViewBehavior.setOnMoveToFirstCell(() -> this.onMoveToFirstCell());
        treeTableViewBehavior.setOnMoveToLastCell(() -> this.onMoveToLastCell());
        treeTableViewBehavior.setOnScrollPageDown((Callback<Boolean, Integer>)((Callback)bl -> this.onScrollPageDown((boolean)bl)));
        treeTableViewBehavior.setOnScrollPageUp((Callback<Boolean, Integer>)((Callback)bl -> this.onScrollPageUp((boolean)bl)));
        treeTableViewBehavior.setOnSelectPreviousRow(() -> this.onSelectPreviousCell());
        treeTableViewBehavior.setOnSelectNextRow(() -> this.onSelectNextCell());
        treeTableViewBehavior.setOnSelectLeftCell(() -> this.onSelectLeftCell());
        treeTableViewBehavior.setOnSelectRightCell(() -> this.onSelectRightCell());
        this.registerChangeListener((ObservableValue<?>)treeTableView.rootProperty(), "ROOT");
        this.registerChangeListener((ObservableValue<?>)treeTableView.showRootProperty(), "SHOW_ROOT");
        this.registerChangeListener((ObservableValue<?>)treeTableView.rowFactoryProperty(), "ROW_FACTORY");
        this.registerChangeListener((ObservableValue<?>)treeTableView.expandedItemCountProperty(), "TREE_ITEM_COUNT");
        this.registerChangeListener((ObservableValue<?>)treeTableView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ROOT".equals(string)) {
            ((TreeTableView)this.getSkinnable()).edit(-1, null);
            this.setRoot(((TreeTableView)this.getSkinnable()).getRoot());
        } else if ("SHOW_ROOT".equals(string)) {
            if (!((TreeTableView)this.getSkinnable()).isShowRoot() && this.getRoot() != null) {
                this.getRoot().setExpanded(true);
            }
            this.updateRowCount();
        } else if ("ROW_FACTORY".equals(string)) {
            this.flow.recreateCells();
        } else if ("TREE_ITEM_COUNT".equals(string)) {
            this.rowCountDirty = true;
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.flow.setFixedCellSize(((TreeTableView)this.getSkinnable()).getFixedCellSize());
        }
    }

    private TreeItem<S> getRoot() {
        return this.weakRootRef == null ? null : (TreeItem)this.weakRootRef.get();
    }

    private void setRoot(TreeItem<S> treeItem) {
        if (this.getRoot() != null && this.weakRootListener != null) {
            this.getRoot().removeEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.weakRootRef = new WeakReference<TreeItem<S>>(treeItem);
        if (this.getRoot() != null) {
            this.weakRootListener = new WeakEventHandler(this.rootListener);
            this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
        }
        this.updateRowCount();
    }

    @Override
    protected ObservableList<TreeTableColumn<S, ?>> getVisibleLeafColumns() {
        return this.treeTableView.getVisibleLeafColumns();
    }

    @Override
    protected int getVisibleLeafIndex(TreeTableColumn<S, ?> treeTableColumn) {
        return this.treeTableView.getVisibleLeafIndex(treeTableColumn);
    }

    @Override
    protected TreeTableColumn<S, ?> getVisibleLeafColumn(int n) {
        return this.treeTableView.getVisibleLeafColumn(n);
    }

    protected TreeTableView.TreeTableViewFocusModel<S> getFocusModel() {
        return this.treeTableView.getFocusModel();
    }

    protected TreeTablePosition<S, ?> getFocusedCell() {
        return this.treeTableView.getFocusModel().getFocusedCell();
    }

    @Override
    protected TableSelectionModel<TreeItem<S>> getSelectionModel() {
        return this.treeTableView.getSelectionModel();
    }

    @Override
    protected ObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>> rowFactoryProperty() {
        return this.treeTableView.rowFactoryProperty();
    }

    @Override
    protected ObjectProperty<Node> placeholderProperty() {
        return this.treeTableView.placeholderProperty();
    }

    @Override
    protected ObjectProperty<ObservableList<TreeItem<S>>> itemsProperty() {
        return this.tableBackingListProperty;
    }

    @Override
    protected ObservableList<TreeTableColumn<S, ?>> getColumns() {
        return this.treeTableView.getColumns();
    }

    @Override
    protected BooleanProperty tableMenuButtonVisibleProperty() {
        return this.treeTableView.tableMenuButtonVisibleProperty();
    }

    @Override
    protected ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty() {
        return this.treeTableView.columnResizePolicyProperty();
    }

    @Override
    protected ObservableList<TreeTableColumn<S, ?>> getSortOrder() {
        return this.treeTableView.getSortOrder();
    }

    @Override
    protected boolean resizeColumn(TreeTableColumn<S, ?> treeTableColumn, double d) {
        return this.treeTableView.resizeColumn(treeTableColumn, d);
    }

    @Override
    protected void edit(int n, TreeTableColumn<S, ?> treeTableColumn) {
        this.treeTableView.edit(n, treeTableColumn);
    }

    @Override
    protected void resizeColumnToFitContent(TreeTableColumn<S, ?> treeTableColumn, int n) {
        double d;
        Region region;
        Node node;
        TreeTableColumn<S, ?> treeTableColumn2 = treeTableColumn;
        List list = (List)this.itemsProperty().get();
        if (list == null || list.isEmpty()) {
            return;
        }
        Callback callback = treeTableColumn2.getCellFactory();
        if (callback == null) {
            return;
        }
        TreeTableCell treeTableCell = (TreeTableCell)callback.call(treeTableColumn2);
        if (treeTableCell == null) {
            return;
        }
        treeTableCell.getProperties().put((Object)"deferToParentPrefWidth", (Object)Boolean.TRUE);
        double d2 = 10.0;
        Node node2 = node = treeTableCell.getSkin() == null ? null : treeTableCell.getSkin().getNode();
        if (node instanceof Region) {
            region = (Region)node;
            d2 = region.snappedLeftInset() + region.snappedRightInset();
        }
        region = new TreeTableRow();
        region.updateTreeTableView(this.treeTableView);
        int n2 = n == -1 ? list.size() : Math.min(list.size(), n);
        double d3 = 0.0;
        for (int i = 0; i < n2; ++i) {
            region.updateIndex(i);
            region.updateTreeItem(this.treeTableView.getTreeItem(i));
            treeTableCell.updateTreeTableColumn(treeTableColumn2);
            treeTableCell.updateTreeTableView(this.treeTableView);
            treeTableCell.updateTreeTableRow((TreeTableRow)region);
            treeTableCell.updateIndex(i);
            if ((treeTableCell.getText() == null || treeTableCell.getText().isEmpty()) && treeTableCell.getGraphic() == null) continue;
            this.getChildren().add((Object)treeTableCell);
            treeTableCell.applyCss();
            d = treeTableCell.prefWidth(-1.0);
            d3 = Math.max(d3, d);
            this.getChildren().remove((Object)treeTableCell);
        }
        treeTableCell.updateIndex(-1);
        TableColumnHeader tableColumnHeader = this.getTableHeaderRow().getColumnHeaderFor((TableColumnBase<?, ?>)treeTableColumn);
        d = Utils.computeTextWidth(tableColumnHeader.label.getFont(), treeTableColumn.getText(), -1.0);
        Node node3 = tableColumnHeader.label.getGraphic();
        double d4 = node3 == null ? 0.0 : node3.prefWidth(-1.0) + tableColumnHeader.label.getGraphicTextGap();
        double d5 = d + d4 + 10.0 + tableColumnHeader.snappedLeftInset() + tableColumnHeader.snappedRightInset();
        d3 = Math.max(d3, d5);
        d3 += d2;
        if (this.treeTableView.getColumnResizePolicy() == TreeTableView.CONSTRAINED_RESIZE_POLICY) {
            d3 = Math.max(d3, treeTableColumn2.getWidth());
        }
        treeTableColumn2.impl_setWidth(d3);
    }

    @Override
    public int getItemCount() {
        return this.treeTableView.getExpandedItemCount();
    }

    @Override
    public TreeTableRow<S> createCell() {
        TreeTableRow treeTableRow = this.treeTableView.getRowFactory() != null ? (TreeTableRow)this.treeTableView.getRowFactory().call(this.treeTableView) : new TreeTableRow();
        if (treeTableRow.getDisclosureNode() == null) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().setAll((Object[])new String[]{"tree-disclosure-node"});
            stackPane.setMouseTransparent(true);
            StackPane stackPane2 = new StackPane();
            stackPane2.getStyleClass().setAll((Object[])new String[]{"arrow"});
            stackPane.getChildren().add((Object)stackPane2);
            treeTableRow.setDisclosureNode((Node)stackPane);
        }
        treeTableRow.updateTreeTableView(this.treeTableView);
        return treeTableRow;
    }

    @Override
    protected void horizontalScroll() {
        super.horizontalScroll();
        if (((TreeTableView)this.getSkinnable()).getFixedCellSize() > 0.0) {
            this.flow.requestCellLayout();
        }
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case ROW_AT_INDEX: {
                int n = (Integer)arrobject[0];
                return n < 0 ? null : this.flow.getPrivateCell(n);
            }
            case SELECTED_ITEMS: {
                ArrayList<TreeTableRow> arrayList = new ArrayList<TreeTableRow>();
                TreeTableView.TreeTableViewSelectionModel treeTableViewSelectionModel = ((TreeTableView)this.getSkinnable()).getSelectionModel();
                for (TreeTablePosition treeTablePosition : treeTableViewSelectionModel.getSelectedCells()) {
                    TreeTableRow treeTableRow = (TreeTableRow)this.flow.getPrivateCell(treeTablePosition.getRow());
                    if (treeTableRow == null) continue;
                    arrayList.add(treeTableRow);
                }
                return FXCollections.observableArrayList(arrayList);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_ITEM: {
                Node node = (Node)arrobject[0];
                if (!(node instanceof TreeTableCell)) break;
                TreeTableCell treeTableCell = (TreeTableCell)node;
                this.flow.show(treeTableCell.getIndex());
                break;
            }
            case SET_SELECTED_ITEMS: {
                TreeTableView.TreeTableViewSelectionModel treeTableViewSelectionModel;
                ObservableList observableList = (ObservableList)arrobject[0];
                if (observableList == null || (treeTableViewSelectionModel = ((TreeTableView)this.getSkinnable()).getSelectionModel()) == null) break;
                treeTableViewSelectionModel.clearSelection();
                for (Node node : observableList) {
                    if (!(node instanceof TreeTableCell)) continue;
                    TreeTableCell treeTableCell = (TreeTableCell)node;
                    treeTableViewSelectionModel.select(treeTableCell.getIndex(), (TableColumnBase)treeTableCell.getTableColumn());
                }
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }

    @Override
    protected void updateRowCount() {
        this.updatePlaceholderRegionVisibility();
        this.tableBackingList.resetSize();
        int n = this.flow.getCellCount();
        int n2 = this.getItemCount();
        this.flow.setCellCount(n2);
        if (this.forceCellRecreate) {
            this.needCellsRecreated = true;
            this.forceCellRecreate = false;
        } else if (n2 != n) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    private static class TreeTableViewBackingList<S>
    extends ReadOnlyUnbackedObservableList<TreeItem<S>> {
        private final TreeTableView<S> treeTable;
        private int size = -1;

        TreeTableViewBackingList(TreeTableView<S> treeTableView) {
            this.treeTable = treeTableView;
        }

        void resetSize() {
            int n = this.size;
            this.size = -1;
            this.callObservers(new NonIterableChange.GenericAddRemoveChange(0, n, FXCollections.emptyObservableList(), this));
        }

        @Override
        public TreeItem<S> get(int n) {
            return this.treeTable.getTreeItem(n);
        }

        @Override
        public int size() {
            if (this.size == -1) {
                this.size = this.treeTable.getExpandedItemCount();
            }
            return this.size;
        }
    }
}

