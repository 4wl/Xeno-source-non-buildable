/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.event.EventHandler
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.Cursor
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TableView
 *  javafx.scene.control.TreeTableView
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Rectangle
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class NestedTableColumnHeader
extends TableColumnHeader {
    private static final int DRAG_RECT_WIDTH = 4;
    private static final String TABLE_COLUMN_KEY = "TableColumn";
    private static final String TABLE_COLUMN_HEADER_KEY = "TableColumnHeader";
    private ObservableList<? extends TableColumnBase> columns;
    private TableColumnHeader label;
    private ObservableList<TableColumnHeader> columnHeaders;
    private double lastX = 0.0;
    private double dragAnchorX = 0.0;
    private Map<TableColumnBase<?, ?>, Rectangle> dragRects = new WeakHashMap();
    boolean updateColumns = true;
    private final ListChangeListener<TableColumnBase> columnsListener = change -> this.setHeadersNeedUpdate();
    private final WeakListChangeListener weakColumnsListener = new WeakListChangeListener(this.columnsListener);
    private static final EventHandler<MouseEvent> rectMousePressed = new EventHandler<MouseEvent>(){

        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)((Object)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY));
            if (!nestedTableColumnHeader.isColumnResizingEnabled()) {
                return;
            }
            if (mouseEvent.getClickCount() == 2 && mouseEvent.isPrimaryButtonDown()) {
                nestedTableColumnHeader.getTableViewSkin().resizeColumnToFitContent(tableColumnBase, -1);
            } else {
                Rectangle rectangle2 = (Rectangle)mouseEvent.getSource();
                double d = nestedTableColumnHeader.getTableHeaderRow().sceneToLocal(rectangle2.localToScene(rectangle2.getBoundsInLocal())).getMinX() + 2.0;
                nestedTableColumnHeader.dragAnchorX = mouseEvent.getSceneX();
                nestedTableColumnHeader.columnResizingStarted(d);
            }
            mouseEvent.consume();
        }
    };
    private static final EventHandler<MouseEvent> rectMouseDragged = new EventHandler<MouseEvent>(){

        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)((Object)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY));
            if (!nestedTableColumnHeader.isColumnResizingEnabled()) {
                return;
            }
            nestedTableColumnHeader.columnResizing(tableColumnBase, mouseEvent);
            mouseEvent.consume();
        }
    };
    private static final EventHandler<MouseEvent> rectMouseReleased = new EventHandler<MouseEvent>(){

        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)((Object)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY));
            if (!nestedTableColumnHeader.isColumnResizingEnabled()) {
                return;
            }
            nestedTableColumnHeader.columnResizingComplete(tableColumnBase, mouseEvent);
            mouseEvent.consume();
        }
    };
    private static final EventHandler<MouseEvent> rectCursorChangeListener = new EventHandler<MouseEvent>(){

        public void handle(MouseEvent mouseEvent) {
            Rectangle rectangle = (Rectangle)mouseEvent.getSource();
            TableColumnBase tableColumnBase = (TableColumnBase)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader nestedTableColumnHeader = (NestedTableColumnHeader)((Object)rectangle.getProperties().get((Object)NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY));
            if (nestedTableColumnHeader.getCursor() == null) {
                rectangle.setCursor(nestedTableColumnHeader.isColumnResizingEnabled() && rectangle.isHover() && tableColumnBase.isResizable() ? Cursor.H_RESIZE : null);
            }
        }
    };

    public NestedTableColumnHeader(TableViewSkinBase tableViewSkinBase, TableColumnBase tableColumnBase) {
        super(tableViewSkinBase, tableColumnBase);
        this.getStyleClass().setAll((Object[])new String[]{"nested-column-header"});
        this.setFocusTraversable(false);
        this.label = new TableColumnHeader(tableViewSkinBase, this.getTableColumn());
        this.label.setTableHeaderRow(this.getTableHeaderRow());
        this.label.setParentHeader(this.getParentHeader());
        this.label.setNestedColumnHeader(this);
        if (this.getTableColumn() != null) {
            this.changeListenerHandler.registerChangeListener((ObservableValue<?>)this.getTableColumn().textProperty(), "TABLE_COLUMN_TEXT");
        }
        this.changeListenerHandler.registerChangeListener((ObservableValue<?>)tableViewSkinBase.columnResizePolicyProperty(), "TABLE_VIEW_COLUMN_RESIZE_POLICY");
    }

    @Override
    protected void handlePropertyChanged(String string) {
        super.handlePropertyChanged(string);
        if ("TABLE_VIEW_COLUMN_RESIZE_POLICY".equals(string)) {
            this.updateContent();
        } else if ("TABLE_COLUMN_TEXT".equals(string)) {
            this.label.setVisible(this.getTableColumn().getText() != null && !this.getTableColumn().getText().isEmpty());
        }
    }

    @Override
    public void setTableHeaderRow(TableHeaderRow tableHeaderRow) {
        super.setTableHeaderRow(tableHeaderRow);
        this.label.setTableHeaderRow(tableHeaderRow);
        for (TableColumnHeader tableColumnHeader : this.getColumnHeaders()) {
            tableColumnHeader.setTableHeaderRow(tableHeaderRow);
        }
    }

    @Override
    public void setParentHeader(NestedTableColumnHeader nestedTableColumnHeader) {
        super.setParentHeader(nestedTableColumnHeader);
        this.label.setParentHeader(nestedTableColumnHeader);
    }

    ObservableList<? extends TableColumnBase> getColumns() {
        return this.columns;
    }

    void setColumns(ObservableList<? extends TableColumnBase> observableList) {
        if (this.columns != null) {
            this.columns.removeListener((ListChangeListener)this.weakColumnsListener);
        }
        this.columns = observableList;
        if (this.columns != null) {
            this.columns.addListener((ListChangeListener)this.weakColumnsListener);
        }
    }

    void updateTableColumnHeaders() {
        int n;
        Object object;
        Object object22;
        if (this.getTableColumn() == null && this.getTableViewSkin() != null) {
            this.setColumns(this.getTableViewSkin().getColumns());
        } else if (this.getTableColumn() != null) {
            this.setColumns((ObservableList<? extends TableColumnBase>)this.getTableColumn().getColumns());
        }
        if (this.getColumns().isEmpty()) {
            for (int i = 0; i < this.getColumnHeaders().size(); ++i) {
                object22 = (TableColumnHeader)((Object)this.getColumnHeaders().get(i));
                ((TableColumnHeader)((Object)object22)).dispose();
            }
            object = this.getParentHeader();
            if (object != null) {
                object22 = ((NestedTableColumnHeader)((Object)object)).getColumnHeaders();
                n = object22.indexOf((Object)this);
                if (n >= 0 && n < object22.size()) {
                    object22.set(n, this.createColumnHeader(this.getTableColumn()));
                }
            } else {
                this.getColumnHeaders().clear();
            }
        } else {
            object = new ArrayList<TableColumnHeader>((Collection<TableColumnHeader>)this.getColumnHeaders());
            object22 = new ArrayList();
            for (n = 0; n < this.getColumns().size(); ++n) {
                TableColumnBase tableColumnBase = (TableColumnBase)this.getColumns().get(n);
                if (tableColumnBase == null || !tableColumnBase.isVisible()) continue;
                boolean bl = false;
                for (int i = 0; i < object.size(); ++i) {
                    TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)object.get(i));
                    if (tableColumnBase != tableColumnHeader.getTableColumn()) continue;
                    object22.add(tableColumnHeader);
                    bl = true;
                    break;
                }
                if (bl) continue;
                object22.add(this.createColumnHeader(tableColumnBase));
            }
            this.getColumnHeaders().setAll((Collection)object22);
            object.removeAll((Collection<?>)object22);
            for (n = 0; n < object.size(); ++n) {
                ((TableColumnHeader)((Object)object.get(n))).dispose();
            }
        }
        this.updateContent();
        for (Object object22 : this.getColumnHeaders()) {
            object22.applyCss();
        }
    }

    @Override
    void dispose() {
        super.dispose();
        if (this.label != null) {
            this.label.dispose();
        }
        if (this.getColumns() != null) {
            this.getColumns().removeListener((ListChangeListener)this.weakColumnsListener);
        }
        for (int i = 0; i < this.getColumnHeaders().size(); ++i) {
            TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)this.getColumnHeaders().get(i));
            tableColumnHeader.dispose();
        }
        for (Rectangle rectangle : this.dragRects.values()) {
            if (rectangle == null) continue;
            rectangle.visibleProperty().unbind();
        }
        this.dragRects.clear();
        this.getChildren().clear();
        this.changeListenerHandler.dispose();
    }

    public ObservableList<TableColumnHeader> getColumnHeaders() {
        if (this.columnHeaders == null) {
            this.columnHeaders = FXCollections.observableArrayList();
        }
        return this.columnHeaders;
    }

    @Override
    protected void layoutChildren() {
        double d = this.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
        double d2 = this.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
        int n = (int)this.label.prefHeight(-1.0);
        if (this.label.isVisible()) {
            this.label.resize(d, n);
            this.label.relocate(this.snappedLeftInset(), this.snappedTopInset());
        }
        double d3 = this.snappedLeftInset();
        boolean bl = false;
        int n2 = this.getColumnHeaders().size();
        for (int i = 0; i < n2; ++i) {
            TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)this.getColumnHeaders().get(i));
            if (!tableColumnHeader.isVisible()) continue;
            double d4 = this.snapSize(tableColumnHeader.prefWidth(-1.0));
            tableColumnHeader.resize(d4, this.snapSize(d2 - (double)n));
            tableColumnHeader.relocate(d3, (double)n + this.snappedTopInset());
            d3 += d4;
            Rectangle rectangle = this.dragRects.get((Object)tableColumnHeader.getTableColumn());
            if (rectangle == null) continue;
            rectangle.setHeight(tableColumnHeader.getDragRectHeight());
            rectangle.relocate(d3 - 2.0, this.snappedTopInset() + (double)n);
        }
    }

    @Override
    double getDragRectHeight() {
        return this.label.prefHeight(-1.0);
    }

    @Override
    protected double computePrefWidth(double d) {
        this.checkState();
        double d2 = 0.0;
        if (this.getColumns() != null) {
            for (TableColumnHeader tableColumnHeader : this.getColumnHeaders()) {
                if (!tableColumnHeader.isVisible()) continue;
                d2 += this.snapSize(tableColumnHeader.computePrefWidth(d));
            }
        }
        return d2;
    }

    @Override
    protected double computePrefHeight(double d) {
        this.checkState();
        double d2 = 0.0;
        if (this.getColumnHeaders() != null) {
            for (TableColumnHeader tableColumnHeader : this.getColumnHeaders()) {
                d2 = Math.max(d2, tableColumnHeader.prefHeight(-1.0));
            }
        }
        return d2 + this.label.prefHeight(-1.0) + this.snappedTopInset() + this.snappedBottomInset();
    }

    protected TableColumnHeader createTableColumnHeader(TableColumnBase tableColumnBase) {
        return tableColumnBase.getColumns().isEmpty() ? new TableColumnHeader(this.getTableViewSkin(), tableColumnBase) : new NestedTableColumnHeader(this.getTableViewSkin(), tableColumnBase);
    }

    protected void setHeadersNeedUpdate() {
        this.updateColumns = true;
        for (int i = 0; i < this.getColumnHeaders().size(); ++i) {
            TableColumnHeader tableColumnHeader = (TableColumnHeader)((Object)this.getColumnHeaders().get(i));
            if (!(tableColumnHeader instanceof NestedTableColumnHeader)) continue;
            ((NestedTableColumnHeader)tableColumnHeader).setHeadersNeedUpdate();
        }
        this.requestLayout();
    }

    private void updateContent() {
        ArrayList<TableColumnHeader> arrayList = new ArrayList<TableColumnHeader>();
        arrayList.add(this.label);
        arrayList.addAll((Collection<TableColumnHeader>)this.getColumnHeaders());
        if (this.isColumnResizingEnabled()) {
            this.rebuildDragRects();
            arrayList.addAll(this.dragRects.values());
        }
        this.getChildren().setAll(arrayList);
    }

    private void rebuildDragRects() {
        boolean bl;
        if (!this.isColumnResizingEnabled()) {
            return;
        }
        this.getChildren().removeAll(this.dragRects.values());
        for (Rectangle object2 : this.dragRects.values()) {
            object2.visibleProperty().unbind();
        }
        this.dragRects.clear();
        Iterator<Rectangle> iterator = this.getColumns();
        if (iterator == null) {
            return;
        }
        TableViewSkinBase<?, ?, ?, ?, ?, TableColumnBase<?, ?>> tableViewSkinBase = this.getTableViewSkin();
        Callback callback = (Callback)tableViewSkinBase.columnResizePolicyProperty().get();
        boolean bl2 = tableViewSkinBase instanceof TableViewSkin ? TableView.CONSTRAINED_RESIZE_POLICY.equals((Object)callback) : (bl = tableViewSkinBase instanceof TreeTableViewSkin ? TreeTableView.CONSTRAINED_RESIZE_POLICY.equals((Object)callback) : false);
        if (bl && tableViewSkinBase.getVisibleLeafColumns().size() == 1) {
            return;
        }
        for (int i = 0; !(i >= iterator.size() || bl && i == this.getColumns().size() - 1); ++i) {
            TableColumnBase tableColumnBase = (TableColumnBase)iterator.get(i);
            Rectangle rectangle = new Rectangle();
            rectangle.getProperties().put((Object)TABLE_COLUMN_KEY, (Object)tableColumnBase);
            rectangle.getProperties().put((Object)TABLE_COLUMN_HEADER_KEY, (Object)this);
            rectangle.setWidth(4.0);
            rectangle.setHeight(this.getHeight() - this.label.getHeight());
            rectangle.setFill((Paint)Color.TRANSPARENT);
            rectangle.visibleProperty().bind((ObservableValue)tableColumnBase.visibleProperty());
            rectangle.setOnMousePressed(rectMousePressed);
            rectangle.setOnMouseDragged(rectMouseDragged);
            rectangle.setOnMouseReleased(rectMouseReleased);
            rectangle.setOnMouseEntered(rectCursorChangeListener);
            rectangle.setOnMouseExited(rectCursorChangeListener);
            this.dragRects.put(tableColumnBase, rectangle);
        }
    }

    private void checkState() {
        if (this.updateColumns) {
            this.updateTableColumnHeaders();
            this.updateColumns = false;
        }
    }

    private TableColumnHeader createColumnHeader(TableColumnBase tableColumnBase) {
        TableColumnHeader tableColumnHeader = this.createTableColumnHeader(tableColumnBase);
        tableColumnHeader.setTableHeaderRow(this.getTableHeaderRow());
        tableColumnHeader.setParentHeader(this);
        return tableColumnHeader;
    }

    private boolean isColumnResizingEnabled() {
        return true;
    }

    private void columnResizingStarted(double d) {
        this.setCursor(Cursor.H_RESIZE);
        this.columnReorderLine.setLayoutX(d);
    }

    private void columnResizing(TableColumnBase tableColumnBase, MouseEvent mouseEvent) {
        double d = mouseEvent.getSceneX() - this.dragAnchorX;
        if (this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            d = -d;
        }
        double d2 = d - this.lastX;
        boolean bl = this.getTableViewSkin().resizeColumn(tableColumnBase, d2);
        if (bl) {
            this.lastX = d;
        }
    }

    private void columnResizingComplete(TableColumnBase tableColumnBase, MouseEvent mouseEvent) {
        this.setCursor(null);
        this.columnReorderLine.setTranslateX(0.0);
        this.columnReorderLine.setLayoutX(0.0);
        this.lastX = 0.0;
    }
}

