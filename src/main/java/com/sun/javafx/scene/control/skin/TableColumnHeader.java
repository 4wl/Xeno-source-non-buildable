/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.css.CssMetaData
 *  javafx.css.PseudoClass
 *  javafx.css.Styleable
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableProperty
 *  javafx.event.EventHandler
 *  javafx.geometry.HPos
 *  javafx.geometry.Insets
 *  javafx.geometry.Pos
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.AccessibleRole
 *  javafx.scene.Node
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.Label
 *  javafx.scene.control.TableColumn$SortType
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.input.ContextMenuEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.GridPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Priority
 *  javafx.scene.layout.Region
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.TableColumnSortTypeWrapper;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class TableColumnHeader
extends Region {
    static final double DEFAULT_COLUMN_WIDTH = 80.0;
    private boolean autoSizeComplete = false;
    private double dragOffset;
    private final TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> skin;
    private NestedTableColumnHeader nestedColumnHeader;
    private final TableColumnBase<?, ?> column;
    private TableHeaderRow tableHeaderRow;
    private NestedTableColumnHeader parentHeader;
    Label label;
    int sortPos = -1;
    private Region arrow;
    private Label sortOrderLabel;
    private HBox sortOrderDots;
    private Node sortArrow;
    private boolean isSortColumn;
    private boolean isSizeDirty = false;
    boolean isLastVisibleColumn = false;
    int columnIndex = -1;
    private int newColumnPos;
    protected final Region columnReorderLine;
    protected final MultiplePropertyChangeListenerHandler changeListenerHandler;
    private ListChangeListener<TableColumnBase<?, ?>> sortOrderListener = change -> this.updateSortPosition();
    private ListChangeListener<TableColumnBase<?, ?>> visibleLeafColumnsListener = change -> {
        this.updateColumnIndex();
        this.updateSortPosition();
    };
    private ListChangeListener<String> styleClassListener = change -> this.updateStyleClass();
    private WeakListChangeListener<TableColumnBase<?, ?>> weakSortOrderListener = new WeakListChangeListener(this.sortOrderListener);
    private final WeakListChangeListener<TableColumnBase<?, ?>> weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
    private final WeakListChangeListener<String> weakStyleClassListener = new WeakListChangeListener(this.styleClassListener);
    private static final EventHandler<MouseEvent> mousePressedHandler = mouseEvent -> {
        TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)((Object)mouseEvent.getSource()));
        tableColumnHeader.getTableViewSkin().getSkinnable().requestFocus();
        if (mouseEvent.isPrimaryButtonDown() && tableColumnHeader.isColumnReorderingEnabled()) {
            tableColumnHeader.columnReorderingStarted(mouseEvent.getX());
        }
        mouseEvent.consume();
    };
    private static final EventHandler<MouseEvent> mouseDraggedHandler = mouseEvent -> {
        TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)((Object)mouseEvent.getSource()));
        if (mouseEvent.isPrimaryButtonDown() && tableColumnHeader.isColumnReorderingEnabled()) {
            tableColumnHeader.columnReordering(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        }
        mouseEvent.consume();
    };
    private static final EventHandler<MouseEvent> mouseReleasedHandler = mouseEvent -> {
        if (mouseEvent.isPopupTrigger()) {
            return;
        }
        TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)((Object)mouseEvent.getSource()));
        TableColumnBase tableColumnBase = tableColumnHeader.getTableColumn();
        ContextMenu contextMenu = tableColumnBase.getContextMenu();
        if (contextMenu != null && contextMenu.isShowing()) {
            return;
        }
        if (tableColumnHeader.getTableHeaderRow().isReordering() && tableColumnHeader.isColumnReorderingEnabled()) {
            tableColumnHeader.columnReorderingComplete();
        } else if (mouseEvent.isStillSincePress()) {
            tableColumnHeader.sortColumn(mouseEvent.isShiftDown());
        }
        mouseEvent.consume();
    };
    private static final EventHandler<ContextMenuEvent> contextMenuRequestedHandler = contextMenuEvent -> {
        TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)((Object)contextMenuEvent.getSource()));
        TableColumnBase tableColumnBase = tableColumnHeader.getTableColumn();
        ContextMenu contextMenu = tableColumnBase.getContextMenu();
        if (contextMenu != null) {
            contextMenu.show((Node)tableColumnHeader, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            contextMenuEvent.consume();
        }
    };
    private DoubleProperty size;
    private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass((String)"last-visible");

    public TableColumnHeader(TableViewSkinBase tableViewSkinBase, TableColumnBase tableColumnBase) {
        this.skin = tableViewSkinBase;
        this.column = tableColumnBase;
        this.columnReorderLine = tableViewSkinBase.getColumnReorderLine();
        this.setFocusTraversable(false);
        this.updateColumnIndex();
        this.initUI();
        this.changeListenerHandler = new MultiplePropertyChangeListenerHandler((Callback<String, Void>)((Callback)string -> {
            this.handlePropertyChanged((String)string);
            return null;
        }));
        this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.sceneProperty(), "SCENE");
        if (this.column != null && tableViewSkinBase != null) {
            this.updateSortPosition();
            tableViewSkinBase.getSortOrder().addListener(this.weakSortOrderListener);
            tableViewSkinBase.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        }
        if (this.column != null) {
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.idProperty(), "TABLE_COLUMN_ID");
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.styleProperty(), "TABLE_COLUMN_STYLE");
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.widthProperty(), "TABLE_COLUMN_WIDTH");
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.visibleProperty(), "TABLE_COLUMN_VISIBLE");
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.sortNodeProperty(), "TABLE_COLUMN_SORT_NODE");
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.sortableProperty(), "TABLE_COLUMN_SORTABLE");
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.textProperty(), "TABLE_COLUMN_TEXT");
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.column.graphicProperty(), "TABLE_COLUMN_GRAPHIC");
            this.column.getStyleClass().addListener(this.weakStyleClassListener);
            this.setId(this.column.getId());
            this.setStyle(this.column.getStyle());
            this.updateStyleClass();
            this.setAccessibleRole(AccessibleRole.TABLE_COLUMN);
        }
    }

    private double getSize() {
        return this.size == null ? 20.0 : this.size.doubleValue();
    }

    private DoubleProperty sizeProperty() {
        if (this.size == null) {
            this.size = new StyleableDoubleProperty(20.0){

                protected void invalidated() {
                    double d = this.get();
                    if (d <= 0.0) {
                        if (this.isBound()) {
                            this.unbind();
                        }
                        this.set(20.0);
                        throw new IllegalArgumentException("Size cannot be 0 or negative");
                    }
                }

                public Object getBean() {
                    return TableColumnHeader.this;
                }

                public String getName() {
                    return "size";
                }

                public CssMetaData<TableColumnHeader, Number> getCssMetaData() {
                    return StyleableProperties.SIZE;
                }
            };
        }
        return this.size;
    }

    protected void handlePropertyChanged(String string) {
        if ("SCENE".equals(string)) {
            this.updateScene();
        } else if ("TABLE_COLUMN_VISIBLE".equals(string)) {
            this.setVisible(this.getTableColumn().isVisible());
        } else if ("TABLE_COLUMN_WIDTH".equals(string)) {
            this.isSizeDirty = true;
            this.requestLayout();
        } else if ("TABLE_COLUMN_ID".equals(string)) {
            this.setId(this.column.getId());
        } else if ("TABLE_COLUMN_STYLE".equals(string)) {
            this.setStyle(this.column.getStyle());
        } else if ("TABLE_COLUMN_SORT_TYPE".equals(string)) {
            this.updateSortGrid();
            if (this.arrow != null) {
                this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0 : 0.0);
            }
        } else if ("TABLE_COLUMN_SORT_NODE".equals(string)) {
            this.updateSortGrid();
        } else if ("TABLE_COLUMN_SORTABLE".equals(string)) {
            if (this.skin.getSortOrder().contains((Object)this.getTableColumn())) {
                NestedTableColumnHeader nestedTableColumnHeader = this.getTableHeaderRow().getRootHeader();
                this.updateAllHeaders(nestedTableColumnHeader);
            }
        } else if ("TABLE_COLUMN_TEXT".equals(string)) {
            this.label.setText(this.column.getText());
        } else if ("TABLE_COLUMN_GRAPHIC".equals(string)) {
            this.label.setGraphic(this.column.getGraphic());
        }
    }

    protected TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> getTableViewSkin() {
        return this.skin;
    }

    NestedTableColumnHeader getNestedColumnHeader() {
        return this.nestedColumnHeader;
    }

    void setNestedColumnHeader(NestedTableColumnHeader nestedTableColumnHeader) {
        this.nestedColumnHeader = nestedTableColumnHeader;
    }

    public TableColumnBase getTableColumn() {
        return this.column;
    }

    TableHeaderRow getTableHeaderRow() {
        return this.tableHeaderRow;
    }

    void setTableHeaderRow(TableHeaderRow tableHeaderRow) {
        this.tableHeaderRow = tableHeaderRow;
    }

    NestedTableColumnHeader getParentHeader() {
        return this.parentHeader;
    }

    void setParentHeader(NestedTableColumnHeader nestedTableColumnHeader) {
        this.parentHeader = nestedTableColumnHeader;
    }

    protected void layoutChildren() {
        if (this.isSizeDirty) {
            this.resize(this.getTableColumn().getWidth(), this.getHeight());
            this.isSizeDirty = false;
        }
        double d = 0.0;
        double d2 = this.snapSize(this.getWidth()) - (this.snappedLeftInset() + this.snappedRightInset());
        double d3 = this.getHeight() - (this.snappedTopInset() + this.snappedBottomInset());
        double d4 = d2;
        if (this.arrow != null) {
            this.arrow.setMaxSize(this.arrow.prefWidth(-1.0), this.arrow.prefHeight(-1.0));
        }
        if (this.sortArrow != null && this.sortArrow.isVisible()) {
            d = this.sortArrow.prefWidth(-1.0);
            this.sortArrow.resize(d, this.sortArrow.prefHeight(-1.0));
            this.positionInArea(this.sortArrow, d4 -= d, this.snappedTopInset(), d, d3, 0.0, HPos.CENTER, VPos.CENTER);
        }
        if (this.label != null) {
            double d5 = d2 - d;
            this.label.resizeRelocate(this.snappedLeftInset(), 0.0, d5, this.getHeight());
        }
    }

    protected double computePrefWidth(double d) {
        if (this.getNestedColumnHeader() != null) {
            double d2 = this.getNestedColumnHeader().prefWidth(d);
            if (this.column != null) {
                this.column.impl_setWidth(d2);
            }
            return d2;
        }
        if (this.column != null && this.column.isVisible()) {
            return this.column.getWidth();
        }
        return 0.0;
    }

    protected double computeMinHeight(double d) {
        return this.label == null ? 0.0 : this.label.minHeight(d);
    }

    protected double computePrefHeight(double d) {
        if (this.getTableColumn() == null) {
            return 0.0;
        }
        return Math.max(this.getSize(), this.label.prefHeight(-1.0));
    }

    private void updateAllHeaders(TableColumnHeader tableColumnHeader) {
        if (tableColumnHeader instanceof NestedTableColumnHeader) {
            ObservableList<TableColumnHeader> observableList = ((NestedTableColumnHeader)tableColumnHeader).getColumnHeaders();
            for (int i = 0; i < observableList.size(); ++i) {
                this.updateAllHeaders((TableColumnHeader)((Object)observableList.get(i)));
            }
        } else {
            tableColumnHeader.updateSortPosition();
        }
    }

    private void updateStyleClass() {
        this.getStyleClass().setAll((Object[])new String[]{"column-header"});
        this.getStyleClass().addAll((Collection)this.column.getStyleClass());
    }

    private void updateScene() {
        if (!this.autoSizeComplete) {
            if (this.getTableColumn() == null || this.getTableColumn().getWidth() != 80.0 || this.getScene() == null) {
                return;
            }
            this.doColumnAutoSize(this.getTableColumn(), 30);
            this.autoSizeComplete = true;
        }
    }

    void dispose() {
        TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> tableViewSkinBase = this.getTableViewSkin();
        if (tableViewSkinBase != null) {
            tableViewSkinBase.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
            tableViewSkinBase.getSortOrder().removeListener(this.weakSortOrderListener);
        }
        this.changeListenerHandler.dispose();
    }

    private boolean isSortingEnabled() {
        return true;
    }

    private boolean isColumnReorderingEnabled() {
        return !BehaviorSkinBase.IS_TOUCH_SUPPORTED && this.getTableViewSkin().getVisibleLeafColumns().size() > 1;
    }

    private void initUI() {
        if (this.column == null) {
            return;
        }
        this.setOnMousePressed(mousePressedHandler);
        this.setOnMouseDragged(mouseDraggedHandler);
        this.setOnDragDetected(mouseEvent -> mouseEvent.consume());
        this.setOnContextMenuRequested(contextMenuRequestedHandler);
        this.setOnMouseReleased(mouseReleasedHandler);
        this.label = new Label();
        this.label.setText(this.column.getText());
        this.label.setGraphic(this.column.getGraphic());
        this.label.setVisible(this.column.isVisible());
        if (this.isSortingEnabled()) {
            this.updateSortGrid();
        }
    }

    private void doColumnAutoSize(TableColumnBase<?, ?> tableColumnBase, int n) {
        double d = tableColumnBase.getPrefWidth();
        if (d == 80.0) {
            this.getTableViewSkin().resizeColumnToFitContent(tableColumnBase, n);
        }
    }

    private void updateSortPosition() {
        this.sortPos = !this.column.isSortable() ? -1 : this.getSortPosition();
        this.updateSortGrid();
    }

    private void updateSortGrid() {
        if (this instanceof NestedTableColumnHeader) {
            return;
        }
        this.getChildren().clear();
        this.getChildren().add((Object)this.label);
        if (!this.isSortingEnabled()) {
            return;
        }
        boolean bl = this.isSortColumn = this.sortPos != -1;
        if (!this.isSortColumn) {
            if (this.sortArrow != null) {
                this.sortArrow.setVisible(false);
            }
            return;
        }
        int n = this.skin.getVisibleLeafIndex(this.getTableColumn());
        if (n == -1) {
            return;
        }
        int n2 = this.getVisibleSortOrderColumnCount();
        boolean bl2 = this.sortPos <= 3 && n2 > 1;
        Node node = null;
        if (this.getTableColumn().getSortNode() != null) {
            node = this.getTableColumn().getSortNode();
            this.getChildren().add((Object)node);
        } else {
            GridPane gridPane = new GridPane();
            node = gridPane;
            gridPane.setPadding(new Insets(0.0, 3.0, 0.0, 0.0));
            this.getChildren().add((Object)gridPane);
            if (this.arrow == null) {
                this.arrow = new Region();
                this.arrow.getStyleClass().setAll((Object[])new String[]{"arrow"});
                this.arrow.setVisible(true);
                this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0 : 0.0);
                this.changeListenerHandler.registerChangeListener(TableColumnSortTypeWrapper.getSortTypeProperty(this.column), "TABLE_COLUMN_SORT_TYPE");
            }
            this.arrow.setVisible(this.isSortColumn);
            if (this.sortPos > 2) {
                if (this.sortOrderLabel == null) {
                    this.sortOrderLabel = new Label();
                    this.sortOrderLabel.getStyleClass().add((Object)"sort-order");
                }
                this.sortOrderLabel.setText("" + (this.sortPos + 1));
                this.sortOrderLabel.setVisible(n2 > 1);
                gridPane.add((Node)this.arrow, 1, 1);
                GridPane.setHgrow((Node)this.arrow, (Priority)Priority.NEVER);
                GridPane.setVgrow((Node)this.arrow, (Priority)Priority.NEVER);
                gridPane.add((Node)this.sortOrderLabel, 2, 1);
            } else if (bl2) {
                boolean bl3;
                if (this.sortOrderDots == null) {
                    this.sortOrderDots = new HBox(0.0);
                    this.sortOrderDots.getStyleClass().add((Object)"sort-order-dots-container");
                }
                int n3 = (bl3 = TableColumnSortTypeWrapper.isAscending(this.column)) ? 1 : 2;
                int n4 = bl3 ? 2 : 1;
                gridPane.add((Node)this.arrow, 1, n3);
                GridPane.setHalignment((Node)this.arrow, (HPos)HPos.CENTER);
                gridPane.add((Node)this.sortOrderDots, 1, n4);
                this.updateSortOrderDots(this.sortPos);
            } else {
                gridPane.add((Node)this.arrow, 1, 1);
                GridPane.setHgrow((Node)this.arrow, (Priority)Priority.NEVER);
                GridPane.setVgrow((Node)this.arrow, (Priority)Priority.ALWAYS);
            }
        }
        this.sortArrow = node;
        if (this.sortArrow != null) {
            this.sortArrow.setVisible(this.isSortColumn);
        }
        this.requestLayout();
    }

    private void updateSortOrderDots(int n) {
        double d = this.arrow.prefWidth(-1.0);
        this.sortOrderDots.getChildren().clear();
        for (int i = 0; i <= n; ++i) {
            Region region = new Region();
            region.getStyleClass().add((Object)"sort-order-dot");
            String string = TableColumnSortTypeWrapper.getSortTypeName(this.column);
            if (string != null && !string.isEmpty()) {
                region.getStyleClass().add((Object)string.toLowerCase(Locale.ROOT));
            }
            this.sortOrderDots.getChildren().add((Object)region);
            if (i >= n) continue;
            Region region2 = new Region();
            double d2 = n == 1 ? 1.0 : 1.0;
            double d3 = n == 1 ? 1.0 : 0.0;
            region2.setPadding(new Insets(0.0, d2, 0.0, d3));
            this.sortOrderDots.getChildren().add((Object)region2);
        }
        this.sortOrderDots.setAlignment(Pos.TOP_CENTER);
        this.sortOrderDots.setMaxWidth(d);
    }

    void moveColumn(TableColumnBase tableColumnBase, int n) {
        int n2;
        if (tableColumnBase == null || n < 0) {
            return;
        }
        ObservableList<TableColumnBase<?, ?>> observableList = this.getColumns(tableColumnBase);
        int n3 = observableList.size();
        int n4 = observableList.indexOf((Object)tableColumnBase);
        int n5 = n2 = n;
        for (int i = 0; i <= n5 && i < n3; ++i) {
            n2 += ((TableColumnBase)observableList.get(i)).isVisible() ? 0 : 1;
        }
        if (n2 >= n3) {
            n2 = n3 - 1;
        } else if (n2 < 0) {
            n2 = 0;
        }
        if (n2 == n4) {
            return;
        }
        ArrayList arrayList = new ArrayList((Collection<TableColumnBase<?, ?>>)observableList);
        arrayList.remove((Object)tableColumnBase);
        arrayList.add(n2, tableColumnBase);
        observableList.setAll(arrayList);
    }

    private ObservableList<TableColumnBase<?, ?>> getColumns(TableColumnBase tableColumnBase) {
        return tableColumnBase.getParentColumn() == null ? this.getTableViewSkin().getColumns() : tableColumnBase.getParentColumn().getColumns();
    }

    private int getIndex(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase == null) {
            return -1;
        }
        ObservableList<TableColumnBase<?, ?>> observableList = this.getColumns(tableColumnBase);
        int n = -1;
        for (int i = 0; i < observableList.size(); ++i) {
            TableColumnBase tableColumnBase2 = (TableColumnBase)observableList.get(i);
            if (!tableColumnBase2.isVisible()) continue;
            ++n;
            if (tableColumnBase.equals((Object)tableColumnBase2)) break;
        }
        return n;
    }

    private void updateColumnIndex() {
        TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> tableViewSkinBase = this.getTableViewSkin();
        TableColumnBase tableColumnBase = this.getTableColumn();
        this.columnIndex = tableViewSkinBase == null || tableColumnBase == null ? -1 : tableViewSkinBase.getVisibleLeafIndex(tableColumnBase);
        this.isLastVisibleColumn = this.getTableColumn() != null && this.columnIndex != -1 && this.columnIndex == this.getTableViewSkin().getVisibleLeafColumns().size() - 1;
        this.pseudoClassStateChanged(PSEUDO_CLASS_LAST_VISIBLE, this.isLastVisibleColumn);
    }

    private void sortColumn(boolean bl) {
        if (!this.isSortingEnabled()) {
            return;
        }
        if (this.column == null || this.column.getColumns().size() != 0 || this.column.getComparator() == null || !this.column.isSortable()) {
            return;
        }
        ObservableList<TableColumnBase<?, ?>> observableList = this.getTableViewSkin().getSortOrder();
        if (bl) {
            if (!this.isSortColumn) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
                observableList.add(this.column);
            } else if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
            } else {
                int n = observableList.indexOf(this.column);
                if (n != -1) {
                    observableList.remove(n);
                }
            }
        } else if (this.isSortColumn && observableList.size() == 1) {
            if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
            } else {
                observableList.remove(this.column);
            }
        } else if (this.isSortColumn) {
            if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
            } else if (TableColumnSortTypeWrapper.isDescending(this.column)) {
                TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
            }
            ArrayList arrayList = new ArrayList((Collection<TableColumnBase<?, ?>>)observableList);
            arrayList.remove(this.column);
            arrayList.add(0, this.column);
            observableList.setAll((Object[])new TableColumnBase[]{this.column});
        } else {
            TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
            observableList.setAll((Object[])new TableColumnBase[]{this.column});
        }
    }

    private int getSortPosition() {
        if (this.column == null) {
            return -1;
        }
        List<TableColumnBase> list = this.getVisibleSortOrderColumns();
        int n = 0;
        for (int i = 0; i < list.size(); ++i) {
            TableColumnBase tableColumnBase = list.get(i);
            if (this.column.equals((Object)tableColumnBase)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    private List<TableColumnBase> getVisibleSortOrderColumns() {
        ObservableList<TableColumnBase<?, ?>> observableList = this.getTableViewSkin().getSortOrder();
        ArrayList<TableColumnBase> arrayList = new ArrayList<TableColumnBase>();
        for (int i = 0; i < observableList.size(); ++i) {
            TableColumnBase tableColumnBase = (TableColumnBase)observableList.get(i);
            if (tableColumnBase == null || !tableColumnBase.isSortable() || !tableColumnBase.isVisible()) continue;
            arrayList.add(tableColumnBase);
        }
        return arrayList;
    }

    private int getVisibleSortOrderColumnCount() {
        return this.getVisibleSortOrderColumns().size();
    }

    void columnReorderingStarted(double d) {
        if (!this.column.impl_isReorderable()) {
            return;
        }
        this.dragOffset = d;
        this.getTableHeaderRow().setReorderingColumn(this.column);
        this.getTableHeaderRow().setReorderingRegion(this);
    }

    void columnReordering(double d, double d2) {
        if (!this.column.impl_isReorderable()) {
            return;
        }
        this.getTableHeaderRow().setReordering(true);
        TableColumnHeader tableColumnHeader = null;
        double d3 = this.getParentHeader().sceneToLocal(d, d2).getX();
        double d4 = this.getTableViewSkin().getSkinnable().sceneToLocal(d, d2).getX() - this.dragOffset;
        this.getTableHeaderRow().setDragHeaderX(d4);
        double d5 = 0.0;
        double d6 = 0.0;
        double d7 = 0.0;
        this.newColumnPos = 0;
        for (TableColumnHeader tableColumnHeader2 : this.getParentHeader().getColumnHeaders()) {
            if (!tableColumnHeader2.isVisible()) continue;
            double d8 = tableColumnHeader2.prefWidth(-1.0);
            d7 += d8;
            d5 = tableColumnHeader2.getBoundsInParent().getMinX();
            d6 = d5 + d8;
            if (d3 >= d5 && d3 < d6) {
                tableColumnHeader = tableColumnHeader2;
                break;
            }
            ++this.newColumnPos;
        }
        if (tableColumnHeader == null) {
            this.newColumnPos = d3 > d7 ? this.getParentHeader().getColumns().size() - 1 : 0;
            return;
        }
        double d9 = d5 + (d6 - d5) / 2.0;
        boolean bl = d3 <= d9;
        int n = this.getIndex(this.column);
        this.newColumnPos += this.newColumnPos > n && bl ? -1 : (this.newColumnPos < n && !bl ? 1 : 0);
        double d10 = this.getTableHeaderRow().sceneToLocal(tableColumnHeader.localToScene(tableColumnHeader.getBoundsInLocal())).getMinX();
        if ((d10 += bl ? 0.0 : tableColumnHeader.getWidth()) >= -0.5 && d10 <= this.getTableViewSkin().getSkinnable().getWidth()) {
            this.columnReorderLine.setTranslateX(d10);
            this.columnReorderLine.setVisible(true);
        }
        this.getTableHeaderRow().setReordering(true);
    }

    void columnReorderingComplete() {
        if (!this.column.impl_isReorderable()) {
            return;
        }
        this.moveColumn(this.getTableColumn(), this.newColumnPos);
        this.columnReorderLine.setTranslateX(0.0);
        this.columnReorderLine.setLayoutX(0.0);
        this.newColumnPos = 0;
        this.getTableHeaderRow().setReordering(false);
        this.columnReorderLine.setVisible(false);
        this.getTableHeaderRow().setReorderingColumn(null);
        this.getTableHeaderRow().setReorderingRegion(null);
        this.dragOffset = 0.0;
    }

    double getDragRectHeight() {
        return this.getHeight();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TableColumnHeader.getClassCssMetaData();
    }

    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case INDEX: {
                return this.getIndex(this.column);
            }
            case TEXT: {
                return this.column != null ? this.column.getText() : null;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    private static class StyleableProperties {
        private static final CssMetaData<TableColumnHeader, Number> SIZE = new CssMetaData<TableColumnHeader, Number>("-fx-size", SizeConverter.getInstance(), (Number)20.0){

            public boolean isSettable(TableColumnHeader tableColumnHeader) {
                return tableColumnHeader.size == null || !tableColumnHeader.size.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(TableColumnHeader tableColumnHeader) {
                return (StyleableProperty)tableColumnHeader.sizeProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList<CssMetaData<TableColumnHeader, Number>> arrayList = new ArrayList<CssMetaData<TableColumnHeader, Number>>(Region.getClassCssMetaData());
            arrayList.add(SIZE);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

