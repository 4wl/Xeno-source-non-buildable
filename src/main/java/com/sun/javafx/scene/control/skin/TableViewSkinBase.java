/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.MapChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableMap
 *  javafx.collections.WeakListChangeListener
 *  javafx.geometry.HPos
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.IndexedCell
 *  javafx.scene.control.Label
 *  javafx.scene.control.ResizeFeaturesBase
 *  javafx.scene.control.ScrollToEvent
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TableFocusModel
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.control.TableView
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public abstract class TableViewSkinBase<M, S, C extends Control, B extends BehaviorBase<C>, I extends IndexedCell<M>, TC extends TableColumnBase<S, ?>>
extends VirtualContainerBase<C, B, I> {
    public static final String REFRESH = "tableRefreshKey";
    public static final String RECREATE = "tableRecreateKey";
    private boolean contentWidthDirty = true;
    private Region columnReorderLine;
    private Region columnReorderOverlay;
    private TableHeaderRow tableHeaderRow;
    private Callback<C, I> rowFactory;
    private StackPane placeholderRegion;
    private Label placeholderLabel;
    private static final String EMPTY_TABLE_TEXT = ControlResources.getString("TableView.noContent");
    private static final String NO_COLUMNS_TEXT = ControlResources.getString("TableView.noColumns");
    private int visibleColCount;
    protected boolean needCellsRebuilt = true;
    protected boolean needCellsRecreated = true;
    protected boolean needCellsReconfigured = false;
    private int itemCount = -1;
    protected boolean forceCellRecreate = false;
    private static final boolean IS_PANNABLE = AccessController.doPrivileged(() -> Boolean.getBoolean("com.sun.javafx.scene.control.skin.TableViewSkin.pannable"));
    private MapChangeListener<Object, Object> propertiesMapListener = change -> {
        if (!change.wasAdded()) {
            return;
        }
        if ("tableRefreshKey".equals(change.getKey())) {
            this.refreshView();
            this.getSkinnable().getProperties().remove((Object)"tableRefreshKey");
        } else if ("tableRecreateKey".equals(change.getKey())) {
            this.forceCellRecreate = true;
            this.refreshView();
            this.getSkinnable().getProperties().remove((Object)"tableRecreateKey");
        }
    };
    private ListChangeListener<S> rowCountListener = change -> {
        while (change.next()) {
            if (change.wasReplaced()) {
                this.itemCount = 0;
                break;
            }
            if (change.getRemovedSize() != this.itemCount) continue;
            this.itemCount = 0;
            break;
        }
        if (this.getSkinnable() instanceof TableView) {
            this.edit(-1, null);
        }
        this.rowCountDirty = true;
        this.getSkinnable().requestLayout();
    };
    private ListChangeListener<TC> visibleLeafColumnsListener = change -> {
        this.updateVisibleColumnCount();
        while (change.next()) {
            this.updateVisibleLeafColumnWidthListeners(change.getAddedSubList(), change.getRemoved());
        }
    };
    private InvalidationListener widthListener = observable -> {
        this.needCellsReconfigured = true;
        if (this.getSkinnable() != null) {
            this.getSkinnable().requestLayout();
        }
    };
    private InvalidationListener itemsChangeListener;
    private WeakListChangeListener<S> weakRowCountListener = new WeakListChangeListener(this.rowCountListener);
    private WeakListChangeListener<TC> weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
    private WeakInvalidationListener weakWidthListener = new WeakInvalidationListener(this.widthListener);
    private WeakInvalidationListener weakItemsChangeListener;
    private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987;

    public TableViewSkinBase(C c, B b) {
        super(c, b);
    }

    protected void init(C c) {
        this.flow.setPannable(IS_PANNABLE);
        this.flow.setCreateCell(virtualFlow -> this.createCell());
        InvalidationListener invalidationListener = observable -> this.horizontalScroll();
        this.flow.getHbar().valueProperty().addListener(invalidationListener);
        this.flow.getHbar().setUnitIncrement(15.0);
        this.flow.getHbar().setBlockIncrement(80.0);
        this.columnReorderLine = new Region();
        this.columnReorderLine.getStyleClass().setAll((Object[])new String[]{"column-resize-line"});
        this.columnReorderLine.setManaged(false);
        this.columnReorderLine.setVisible(false);
        this.columnReorderOverlay = new Region();
        this.columnReorderOverlay.getStyleClass().setAll((Object[])new String[]{"column-overlay"});
        this.columnReorderOverlay.setVisible(false);
        this.columnReorderOverlay.setManaged(false);
        this.tableHeaderRow = this.createTableHeaderRow();
        this.tableHeaderRow.setFocusTraversable(false);
        this.getChildren().addAll((Object[])new Node[]{this.tableHeaderRow, this.flow, this.columnReorderOverlay, this.columnReorderLine});
        this.updateVisibleColumnCount();
        this.updateVisibleLeafColumnWidthListeners((List<? extends TC>)this.getVisibleLeafColumns(), (List<? extends TC>)FXCollections.emptyObservableList());
        this.tableHeaderRow.reorderingProperty().addListener(observable -> this.getSkinnable().requestLayout());
        this.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        this.updateTableItems(null, (ObservableList)this.itemsProperty().get());
        this.itemsChangeListener = new InvalidationListener(){
            private WeakReference<ObservableList<S>> weakItemsRef;
            {
                this.weakItemsRef = new WeakReference<Object>(TableViewSkinBase.this.itemsProperty().get());
            }

            public void invalidated(Observable observable) {
                ObservableList observableList = (ObservableList)this.weakItemsRef.get();
                this.weakItemsRef = new WeakReference<Object>(TableViewSkinBase.this.itemsProperty().get());
                TableViewSkinBase.this.updateTableItems(observableList, (ObservableList)TableViewSkinBase.this.itemsProperty().get());
            }
        };
        this.weakItemsChangeListener = new WeakInvalidationListener(this.itemsChangeListener);
        this.itemsProperty().addListener((InvalidationListener)this.weakItemsChangeListener);
        ObservableMap observableMap = c.getProperties();
        observableMap.remove((Object)REFRESH);
        observableMap.remove((Object)RECREATE);
        observableMap.addListener(this.propertiesMapListener);
        c.addEventHandler(ScrollToEvent.scrollToColumn(), scrollToEvent -> this.scrollHorizontally((TC)((TableColumnBase)scrollToEvent.getScrollTarget())));
        InvalidationListener invalidationListener2 = observable -> {
            this.contentWidthDirty = true;
            this.getSkinnable().requestLayout();
        };
        this.flow.widthProperty().addListener(invalidationListener2);
        this.flow.getVbar().widthProperty().addListener(invalidationListener2);
        this.registerChangeListener((ObservableValue<?>)this.rowFactoryProperty(), "ROW_FACTORY");
        this.registerChangeListener((ObservableValue<?>)this.placeholderProperty(), "PLACEHOLDER");
        this.registerChangeListener((ObservableValue<?>)c.widthProperty(), "WIDTH");
        this.registerChangeListener((ObservableValue<?>)this.flow.getVbar().visibleProperty(), "VBAR_VISIBLE");
    }

    protected abstract TableSelectionModel<S> getSelectionModel();

    protected abstract TableFocusModel<S, TC> getFocusModel();

    protected abstract TablePositionBase<? extends TC> getFocusedCell();

    protected abstract ObservableList<? extends TC> getVisibleLeafColumns();

    protected abstract int getVisibleLeafIndex(TC var1);

    protected abstract TC getVisibleLeafColumn(int var1);

    protected abstract ObservableList<TC> getColumns();

    protected abstract ObservableList<TC> getSortOrder();

    protected abstract ObjectProperty<ObservableList<S>> itemsProperty();

    protected abstract ObjectProperty<Callback<C, I>> rowFactoryProperty();

    protected abstract ObjectProperty<Node> placeholderProperty();

    protected abstract BooleanProperty tableMenuButtonVisibleProperty();

    protected abstract ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty();

    protected abstract boolean resizeColumn(TC var1, double var2);

    protected abstract void resizeColumnToFitContent(TC var1, int var2);

    protected abstract void edit(int var1, TC var2);

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ROW_FACTORY".equals(string)) {
            Callback<C, I> callback = this.rowFactory;
            this.rowFactory = (Callback)this.rowFactoryProperty().get();
            if (callback != this.rowFactory) {
                this.needCellsRebuilt = true;
                this.getSkinnable().requestLayout();
            }
        } else if ("PLACEHOLDER".equals(string)) {
            this.updatePlaceholderRegionVisibility();
        } else if ("VBAR_VISIBLE".equals(string)) {
            this.updateContentWidth();
        }
    }

    @Override
    public void dispose() {
        this.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
        this.itemsProperty().removeListener((InvalidationListener)this.weakItemsChangeListener);
        this.getSkinnable().getProperties().removeListener(this.propertiesMapListener);
        this.updateTableItems((ObservableList)this.itemsProperty().get(), null);
        super.dispose();
    }

    protected TableHeaderRow createTableHeaderRow() {
        return new TableHeaderRow(this);
    }

    public TableHeaderRow getTableHeaderRow() {
        return this.tableHeaderRow;
    }

    public Region getColumnReorderLine() {
        return this.columnReorderLine;
    }

    public int onScrollPageDown(boolean bl) {
        int n;
        boolean bl2;
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return -1;
        }
        int n2 = this.getItemCount();
        Object t = this.flow.getLastVisibleCellWithinViewPort();
        if (t == null) {
            return -1;
        }
        int n3 = t.getIndex();
        int n4 = n3 = n3 >= n2 ? n2 - 1 : n3;
        if (bl) {
            bl2 = t.isFocused() || this.isCellFocused(n3);
        } else {
            boolean bl3 = bl2 = t.isSelected() || this.isCellSelected(n3);
        }
        if (bl2 && (n = (int)(this.isLeadIndex(bl, n3) ? 1 : 0)) != 0) {
            this.flow.showAsFirst(t);
            Object t2 = this.flow.getLastVisibleCellWithinViewPort();
            t = t2 == null ? t : t2;
        }
        n = (n = t.getIndex()) >= n2 ? n2 - 1 : n;
        this.flow.show(n);
        return n;
    }

    public int onScrollPageUp(boolean bl) {
        int n;
        Object t = this.flow.getFirstVisibleCellWithinViewPort();
        if (t == null) {
            return -1;
        }
        int n2 = t.getIndex();
        boolean bl2 = false;
        if (bl) {
            bl2 = t.isFocused() || this.isCellFocused(n2);
        } else {
            boolean bl3 = bl2 = t.isSelected() || this.isCellSelected(n2);
        }
        if (bl2 && (n = (int)(this.isLeadIndex(bl, n2) ? 1 : 0)) != 0) {
            this.flow.showAsLast(t);
            Object t2 = this.flow.getFirstVisibleCellWithinViewPort();
            t = t2 == null ? t : t2;
        }
        n = t.getIndex();
        this.flow.show(n);
        return n;
    }

    private boolean isLeadIndex(boolean bl, int n) {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        return bl && tableFocusModel.getFocusedIndex() == n || !bl && tableSelectionModel.getSelectedIndex() == n;
    }

    boolean isColumnPartiallyOrFullyVisible(TC TC) {
        TableColumnBase tableColumnBase;
        if (TC == null || !TC.isVisible()) {
            return false;
        }
        double d = this.flow.getHbar().getValue();
        double d2 = 0.0;
        ObservableList<TC> observableList = this.getVisibleLeafColumns();
        int n = observableList.size();
        for (int i = 0; i < n && !(tableColumnBase = (TableColumnBase)observableList.get(i)).equals(TC); ++i) {
            d2 += tableColumnBase.getWidth();
        }
        double d3 = d2 + TC.getWidth();
        tableColumnBase = this.getSkinnable().getPadding();
        double d4 = this.getSkinnable().getWidth() - tableColumnBase.getLeft() + tableColumnBase.getRight();
        return (d2 >= d || d3 > d) && (d2 < d4 + d || d3 <= d4 + d);
    }

    protected void horizontalScroll() {
        this.tableHeaderRow.updateScrollX();
    }

    @Override
    protected void updateRowCount() {
        int n;
        this.updatePlaceholderRegionVisibility();
        int n2 = this.itemCount;
        this.itemCount = n = this.getItemCount();
        this.flow.setCellCount(n);
        if (this.forceCellRecreate) {
            this.needCellsRecreated = true;
            this.forceCellRecreate = false;
        } else if (n != n2) {
            this.needCellsRebuilt = true;
        } else {
            this.needCellsReconfigured = true;
        }
    }

    protected void onFocusPreviousCell() {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        this.flow.show(tableFocusModel.getFocusedIndex());
    }

    protected void onFocusNextCell() {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        this.flow.show(tableFocusModel.getFocusedIndex());
    }

    protected void onSelectPreviousCell() {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        this.flow.show(tableSelectionModel.getSelectedIndex());
    }

    protected void onSelectNextCell() {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        this.flow.show(tableSelectionModel.getSelectedIndex());
    }

    protected void onSelectLeftCell() {
        this.scrollHorizontally();
    }

    protected void onSelectRightCell() {
        this.scrollHorizontally();
    }

    protected void onMoveToFirstCell() {
        this.flow.show(0);
        this.flow.setPosition(0.0);
    }

    protected void onMoveToLastCell() {
        int n = this.getItemCount();
        this.flow.show(n);
        this.flow.setPosition(1.0);
    }

    public void updateTableItems(ObservableList<S> observableList, ObservableList<S> observableList2) {
        if (observableList != null) {
            observableList.removeListener(this.weakRowCountListener);
        }
        if (observableList2 != null) {
            observableList2.addListener(this.weakRowCountListener);
        }
        this.rowCountDirty = true;
        this.getSkinnable().requestLayout();
    }

    private void checkContentWidthState() {
        if (this.contentWidthDirty || this.getItemCount() == 0) {
            this.updateContentWidth();
            this.contentWidthDirty = false;
        }
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return 400.0;
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = this.computePrefHeight(-1.0, d2, d3, d4, d5);
        ObservableList<TC> observableList = this.getVisibleLeafColumns();
        if (observableList == null || observableList.isEmpty()) {
            return d6 * 0.618033987;
        }
        double d7 = d5 + d3;
        int n = observableList.size();
        for (int i = 0; i < n; ++i) {
            TableColumnBase tableColumnBase = (TableColumnBase)observableList.get(i);
            d7 += Math.max(tableColumnBase.getPrefWidth(), tableColumnBase.getMinWidth());
        }
        return Math.max(d7, d6 * 0.618033987);
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        Control control = this.getSkinnable();
        if (control == null) {
            return;
        }
        super.layoutChildren(d, d2, d3, d4);
        if (this.needCellsRecreated) {
            this.flow.recreateCells();
        } else if (this.needCellsRebuilt) {
            this.flow.rebuildCells();
        } else if (this.needCellsReconfigured) {
            this.flow.reconfigureCells();
        }
        this.needCellsRebuilt = false;
        this.needCellsRecreated = false;
        this.needCellsReconfigured = false;
        double d5 = control.getLayoutBounds().getHeight() / 2.0;
        double d6 = this.tableHeaderRow.prefHeight(-1.0);
        this.layoutInArea((Node)this.tableHeaderRow, d, d2, d3, d6, d5, HPos.CENTER, VPos.CENTER);
        d2 += d6;
        double d7 = Math.floor(d4 - d6);
        if (this.getItemCount() == 0 || this.visibleColCount == 0) {
            this.layoutInArea((Node)this.placeholderRegion, d, d2, d3, d7, d5, HPos.CENTER, VPos.CENTER);
        } else {
            this.layoutInArea((Node)this.flow, d, d2, d3, d7, d5, HPos.CENTER, VPos.CENTER);
        }
        if (this.tableHeaderRow.getReorderingRegion() != null) {
            TableColumnHeader tableColumnHeader = this.tableHeaderRow.getReorderingRegion();
            TableColumnBase tableColumnBase = tableColumnHeader.getTableColumn();
            if (tableColumnBase != null) {
                TableColumnHeader tableColumnHeader2 = this.tableHeaderRow.getReorderingRegion();
                double d8 = this.tableHeaderRow.sceneToLocal(tableColumnHeader2.localToScene(tableColumnHeader2.getBoundsInLocal())).getMinX();
                double d9 = tableColumnHeader.getWidth();
                if (d8 < 0.0) {
                    d9 += d8;
                }
                double d10 = d8 = d8 < 0.0 ? 0.0 : d8;
                if (d8 + d9 > d3) {
                    d9 = d3 - d8;
                    if (this.flow.getVbar().isVisible()) {
                        d9 -= this.flow.getVbar().getWidth() - 1.0;
                    }
                }
                double d11 = d7;
                if (this.flow.getHbar().isVisible()) {
                    d11 -= this.flow.getHbar().getHeight();
                }
                this.columnReorderOverlay.resize(d9, d11);
                this.columnReorderOverlay.setLayoutX(d8);
                this.columnReorderOverlay.setLayoutY(this.tableHeaderRow.getHeight());
            }
            double d12 = this.columnReorderLine.snappedLeftInset() + this.columnReorderLine.snappedRightInset();
            double d13 = d4 - (this.flow.getHbar().isVisible() ? this.flow.getHbar().getHeight() - 1.0 : 0.0);
            this.columnReorderLine.resizeRelocate(0.0, this.columnReorderLine.snappedTopInset(), d12, d13);
        }
        this.columnReorderLine.setVisible(this.tableHeaderRow.isReordering());
        this.columnReorderOverlay.setVisible(this.tableHeaderRow.isReordering());
        this.checkContentWidthState();
    }

    private void updateVisibleColumnCount() {
        this.visibleColCount = this.getVisibleLeafColumns().size();
        this.updatePlaceholderRegionVisibility();
        this.needCellsRebuilt = true;
        this.getSkinnable().requestLayout();
    }

    private void updateVisibleLeafColumnWidthListeners(List<? extends TC> list, List<? extends TC> list2) {
        TableColumnBase tableColumnBase;
        int n;
        int n2 = list2.size();
        for (n = 0; n < n2; ++n) {
            tableColumnBase = (TableColumnBase)list2.get(n);
            tableColumnBase.widthProperty().removeListener((InvalidationListener)this.weakWidthListener);
        }
        n2 = list.size();
        for (n = 0; n < n2; ++n) {
            tableColumnBase = (TableColumnBase)list.get(n);
            tableColumnBase.widthProperty().addListener((InvalidationListener)this.weakWidthListener);
        }
        this.needCellsRebuilt = true;
        this.getSkinnable().requestLayout();
    }

    protected final void updatePlaceholderRegionVisibility() {
        boolean bl;
        boolean bl2 = bl = this.visibleColCount == 0 || this.getItemCount() == 0;
        if (bl) {
            Node node;
            if (this.placeholderRegion == null) {
                this.placeholderRegion = new StackPane();
                this.placeholderRegion.getStyleClass().setAll((Object[])new String[]{"placeholder"});
                this.getChildren().add((Object)this.placeholderRegion);
            }
            if ((node = (Node)this.placeholderProperty().get()) == null) {
                if (this.placeholderLabel == null) {
                    this.placeholderLabel = new Label();
                }
                String string = this.visibleColCount == 0 ? NO_COLUMNS_TEXT : EMPTY_TABLE_TEXT;
                this.placeholderLabel.setText(string);
                this.placeholderRegion.getChildren().setAll((Object[])new Node[]{this.placeholderLabel});
            } else {
                this.placeholderRegion.getChildren().setAll((Object[])new Node[]{node});
            }
        }
        this.flow.setVisible(!bl);
        if (this.placeholderRegion != null) {
            this.placeholderRegion.setVisible(bl);
        }
    }

    private void updateContentWidth() {
        double d = this.flow.getWidth();
        if (this.flow.getVbar().isVisible()) {
            d -= this.flow.getVbar().getWidth();
        }
        if (d <= 0.0) {
            Control control = this.getSkinnable();
            d = control.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
        }
        d = Math.max(0.0, d);
        this.getSkinnable().getProperties().put((Object)"TableView.contentWidth", (Object)Math.floor(d));
    }

    private void refreshView() {
        this.rowCountDirty = true;
        Control control = this.getSkinnable();
        if (control != null) {
            control.requestLayout();
        }
    }

    protected void scrollHorizontally() {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TableColumnBase tableColumnBase = this.getFocusedCell().getTableColumn();
        this.scrollHorizontally((TC)tableColumnBase);
    }

    protected void scrollHorizontally(TC TC) {
        double d;
        TableColumnBase tableColumnBase;
        if (TC == null || !TC.isVisible()) {
            return;
        }
        Control control = this.getSkinnable();
        TableColumnHeader tableColumnHeader = this.tableHeaderRow.getColumnHeaderFor((TableColumnBase<?, ?>)TC);
        if (tableColumnHeader == null || tableColumnHeader.getWidth() <= 0.0) {
            Platform.runLater(() -> this.scrollHorizontally(TC));
            return;
        }
        double d2 = 0.0;
        Iterator iterator = this.getVisibleLeafColumns().iterator();
        while (iterator.hasNext() && !(tableColumnBase = (TableColumnBase)iterator.next()).equals(TC)) {
            d2 += tableColumnBase.getWidth();
        }
        double d3 = d2 + TC.getWidth();
        double d4 = control.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
        double d5 = this.flow.getHbar().getValue();
        double d6 = this.flow.getHbar().getMax();
        if (d2 < d5 && d2 >= 0.0) {
            d = d2;
        } else {
            double d7 = d2 < 0.0 || d3 > d4 ? d2 - d5 : 0.0;
            d = d5 + d7 > d6 ? d6 : d5 + d7;
        }
        this.flow.getHbar().setValue(d);
    }

    private boolean isCellSelected(int n) {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return false;
        }
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            return false;
        }
        int n2 = this.getVisibleLeafColumns().size();
        for (int i = 0; i < n2; ++i) {
            if (!tableSelectionModel.isSelected(n, this.getVisibleLeafColumn(i))) continue;
            return true;
        }
        return false;
    }

    private boolean isCellFocused(int n) {
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return false;
        }
        int n2 = this.getVisibleLeafColumns().size();
        for (int i = 0; i < n2; ++i) {
            if (!tableFocusModel.isFocused(n, this.getVisibleLeafColumn(i))) continue;
            return true;
        }
        return false;
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
                int n = tableFocusModel.getFocusedIndex();
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
            case CELL_AT_ROW_COLUMN: {
                int n = (Integer)arrobject[0];
                return this.flow.getPrivateCell(n);
            }
            case COLUMN_AT_INDEX: {
                int n = (Integer)arrobject[0];
                TC TC = this.getVisibleLeafColumn(n);
                return this.getTableHeaderRow().getColumnHeaderFor((TableColumnBase<?, ?>)TC);
            }
            case HEADER: {
                return this.getTableHeaderRow();
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
}

