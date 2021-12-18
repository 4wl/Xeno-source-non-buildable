/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.Property
 *  javafx.beans.property.SimpleBooleanProperty
 *  javafx.beans.property.StringProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.geometry.HPos
 *  javafx.geometry.Insets
 *  javafx.geometry.Side
 *  javafx.geometry.VPos
 *  javafx.scene.Node
 *  javafx.scene.control.CheckMenuItem
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.Control
 *  javafx.scene.control.Label
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.scene.shape.Rectangle
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class TableHeaderRow
extends StackPane {
    private static final String MENU_SEPARATOR = ControlResources.getString("TableView.nestedColumnControlMenuSeparator");
    private final VirtualFlow flow;
    private final TableViewSkinBase tableSkin;
    private Map<TableColumnBase, CheckMenuItem> columnMenuItems = new HashMap<TableColumnBase, CheckMenuItem>();
    private double scrollX;
    private double tableWidth;
    private Rectangle clip;
    private TableColumnHeader reorderingRegion;
    private StackPane dragHeader;
    private final Label dragHeaderLabel = new Label();
    private final NestedTableColumnHeader header;
    private Region filler;
    private Pane cornerRegion;
    private ContextMenu columnPopupMenu;
    private BooleanProperty reordering = new SimpleBooleanProperty((Object)this, "reordering", false){

        protected void invalidated() {
            TableColumnHeader tableColumnHeader = TableHeaderRow.this.getReorderingRegion();
            if (tableColumnHeader != null) {
                double d = tableColumnHeader.getNestedColumnHeader() != null ? tableColumnHeader.getNestedColumnHeader().getHeight() : TableHeaderRow.this.getReorderingRegion().getHeight();
                TableHeaderRow.this.dragHeader.resize(TableHeaderRow.this.dragHeader.getWidth(), d);
                TableHeaderRow.this.dragHeader.setTranslateY(TableHeaderRow.this.getHeight() - d);
            }
            TableHeaderRow.this.dragHeader.setVisible(TableHeaderRow.this.isReordering());
        }
    };
    private InvalidationListener tableWidthListener = observable -> this.updateTableWidth();
    private InvalidationListener tablePaddingListener = observable -> this.updateTableWidth();
    private ListChangeListener visibleLeafColumnsListener = new ListChangeListener<TableColumn<?, ?>>(){

        public void onChanged(ListChangeListener.Change<? extends TableColumn<?, ?>> change) {
            TableHeaderRow.this.header.setHeadersNeedUpdate();
        }
    };
    private final ListChangeListener tableColumnsListener = change -> {
        while (change.next()) {
            this.updateTableColumnListeners(change.getAddedSubList(), change.getRemoved());
        }
    };
    private final InvalidationListener columnTextListener = observable -> {
        TableColumnBase tableColumnBase = (TableColumnBase)((StringProperty)observable).getBean();
        CheckMenuItem checkMenuItem = this.columnMenuItems.get((Object)tableColumnBase);
        if (checkMenuItem != null) {
            checkMenuItem.setText(this.getText(tableColumnBase.getText(), tableColumnBase));
        }
    };
    private final WeakInvalidationListener weakTableWidthListener = new WeakInvalidationListener(this.tableWidthListener);
    private final WeakInvalidationListener weakTablePaddingListener = new WeakInvalidationListener(this.tablePaddingListener);
    private final WeakListChangeListener weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
    private final WeakListChangeListener weakTableColumnsListener = new WeakListChangeListener(this.tableColumnsListener);
    private final WeakInvalidationListener weakColumnTextListener = new WeakInvalidationListener(this.columnTextListener);

    public TableHeaderRow(TableViewSkinBase tableViewSkinBase) {
        this.tableSkin = tableViewSkinBase;
        this.flow = tableViewSkinBase.flow;
        this.getStyleClass().setAll((Object[])new String[]{"column-header-background"});
        this.clip = new Rectangle();
        this.clip.setSmooth(false);
        this.clip.heightProperty().bind((ObservableValue)this.heightProperty());
        this.setClip((Node)this.clip);
        this.updateTableWidth();
        this.tableSkin.getSkinnable().widthProperty().addListener((InvalidationListener)this.weakTableWidthListener);
        this.tableSkin.getSkinnable().paddingProperty().addListener((InvalidationListener)this.weakTablePaddingListener);
        tableViewSkinBase.getVisibleLeafColumns().addListener((ListChangeListener)this.weakVisibleLeafColumnsListener);
        this.columnPopupMenu = new ContextMenu();
        this.updateTableColumnListeners((List<? extends TableColumnBase<?, ?>>)this.tableSkin.getColumns(), Collections.emptyList());
        this.tableSkin.getVisibleLeafColumns().addListener((ListChangeListener)this.weakTableColumnsListener);
        this.tableSkin.getColumns().addListener((ListChangeListener)this.weakTableColumnsListener);
        this.dragHeader = new StackPane();
        this.dragHeader.setVisible(false);
        this.dragHeader.getStyleClass().setAll((Object[])new String[]{"column-drag-header"});
        this.dragHeader.setManaged(false);
        this.dragHeader.getChildren().add((Object)this.dragHeaderLabel);
        this.header = this.createRootHeader();
        this.header.setFocusTraversable(false);
        this.header.setTableHeaderRow(this);
        this.filler = new Region();
        this.filler.getStyleClass().setAll((Object[])new String[]{"filler"});
        this.setOnMousePressed(mouseEvent -> tableViewSkinBase.getSkinnable().requestFocus());
        final StackPane stackPane = new StackPane();
        stackPane.setSnapToPixel(false);
        stackPane.getStyleClass().setAll((Object[])new String[]{"show-hide-column-image"});
        this.cornerRegion = new StackPane(){

            protected void layoutChildren() {
                double d = stackPane.snappedLeftInset() + stackPane.snappedRightInset();
                double d2 = stackPane.snappedTopInset() + stackPane.snappedBottomInset();
                stackPane.resize(d, d2);
                this.positionInArea((Node)stackPane, 0.0, 0.0, this.getWidth(), this.getHeight() - 3.0, 0.0, HPos.CENTER, VPos.CENTER);
            }
        };
        this.cornerRegion.getStyleClass().setAll((Object[])new String[]{"show-hide-columns-button"});
        this.cornerRegion.getChildren().addAll((Object[])new Node[]{stackPane});
        this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
        this.tableSkin.tableMenuButtonVisibleProperty().addListener(observable -> {
            this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
            this.requestLayout();
        });
        this.cornerRegion.setOnMousePressed(mouseEvent -> {
            this.columnPopupMenu.show((Node)this.cornerRegion, Side.BOTTOM, 0.0, 0.0);
            mouseEvent.consume();
        });
        this.getChildren().addAll((Object[])new Node[]{this.filler, this.header, this.cornerRegion, this.dragHeader});
    }

    protected void layoutChildren() {
        double d = this.scrollX;
        double d2 = this.snapSize(this.header.prefWidth(-1.0));
        double d3 = this.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
        double d4 = this.snapSize(this.flow.getVbar().prefWidth(-1.0));
        this.header.resizeRelocate(d, this.snappedTopInset(), d2, d3);
        Control control = this.tableSkin.getSkinnable();
        if (control == null) {
            return;
        }
        double d5 = control.snappedLeftInset() + control.snappedRightInset();
        double d6 = this.tableWidth - d2 + this.filler.getInsets().getLeft() - d5;
        this.filler.setVisible((d6 -= this.tableSkin.tableMenuButtonVisibleProperty().get() ? d4 : 0.0) > 0.0);
        if (d6 > 0.0) {
            this.filler.resizeRelocate(d + d2, this.snappedTopInset(), d6, d3);
        }
        this.cornerRegion.resizeRelocate(this.tableWidth - d4, this.snappedTopInset(), d4, d3);
    }

    protected double computePrefWidth(double d) {
        return this.header.prefWidth(d);
    }

    protected double computeMinHeight(double d) {
        return this.computePrefHeight(d);
    }

    protected double computePrefHeight(double d) {
        double d2 = this.header.prefHeight(d);
        d2 = d2 == 0.0 ? 24.0 : d2;
        return this.snappedTopInset() + d2 + this.snappedBottomInset();
    }

    protected NestedTableColumnHeader createRootHeader() {
        return new NestedTableColumnHeader(this.tableSkin, null);
    }

    protected TableViewSkinBase<?, ?, ?, ?, ?, ?> getTableSkin() {
        return this.tableSkin;
    }

    protected void updateScrollX() {
        this.scrollX = this.flow.getHbar().isVisible() ? -this.flow.getHbar().getValue() : 0.0;
        this.requestLayout();
        this.layout();
    }

    public final void setReordering(boolean bl) {
        this.reordering.set(bl);
    }

    public final boolean isReordering() {
        return this.reordering.get();
    }

    public final BooleanProperty reorderingProperty() {
        return this.reordering;
    }

    public TableColumnHeader getReorderingRegion() {
        return this.reorderingRegion;
    }

    public void setReorderingColumn(TableColumnBase tableColumnBase) {
        this.dragHeaderLabel.setText(tableColumnBase == null ? "" : tableColumnBase.getText());
    }

    public void setReorderingRegion(TableColumnHeader tableColumnHeader) {
        this.reorderingRegion = tableColumnHeader;
        if (tableColumnHeader != null) {
            this.dragHeader.resize(tableColumnHeader.getWidth(), this.dragHeader.getHeight());
        }
    }

    public void setDragHeaderX(double d) {
        this.dragHeader.setTranslateX(d);
    }

    public NestedTableColumnHeader getRootHeader() {
        return this.header;
    }

    protected void updateTableWidth() {
        Control control = this.tableSkin.getSkinnable();
        if (control == null) {
            this.tableWidth = 0.0;
        } else {
            Insets insets = control.getInsets() == null ? Insets.EMPTY : control.getInsets();
            double d = this.snapSize(insets.getLeft()) + this.snapSize(insets.getRight());
            this.tableWidth = this.snapSize(control.getWidth()) - d;
        }
        this.clip.setWidth(this.tableWidth);
    }

    public TableColumnHeader getColumnHeaderFor(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase == null) {
            return null;
        }
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(tableColumnBase);
        for (TableColumnBase tableColumnBase2 = tableColumnBase.getParentColumn(); tableColumnBase2 != null; tableColumnBase2 = tableColumnBase2.getParentColumn()) {
            arrayList.add(0, (Object)tableColumnBase2);
        }
        TableColumnHeader tableColumnHeader = this.getRootHeader();
        for (int i = 0; i < arrayList.size(); ++i) {
            TableColumnBase tableColumnBase3 = (TableColumnBase)arrayList.get(i);
            tableColumnHeader = this.getColumnHeaderFor(tableColumnBase3, tableColumnHeader);
        }
        return tableColumnHeader;
    }

    public TableColumnHeader getColumnHeaderFor(TableColumnBase<?, ?> tableColumnBase, TableColumnHeader tableColumnHeader) {
        if (tableColumnHeader instanceof NestedTableColumnHeader) {
            ObservableList<TableColumnHeader> observableList = ((NestedTableColumnHeader)tableColumnHeader).getColumnHeaders();
            for (int i = 0; i < observableList.size(); ++i) {
                TableColumnHeader tableColumnHeader2 = (TableColumnHeader)((Object)observableList.get(i));
                if (tableColumnHeader2.getTableColumn() != tableColumnBase) continue;
                return tableColumnHeader2;
            }
        }
        return null;
    }

    private void updateTableColumnListeners(List<? extends TableColumnBase<?, ?>> list, List<? extends TableColumnBase<?, ?>> list2) {
        for (TableColumnBase<?, ?> tableColumnBase : list2) {
            this.remove(tableColumnBase);
        }
        this.rebuildColumnMenu();
    }

    private void remove(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase == null) {
            return;
        }
        CheckMenuItem checkMenuItem = this.columnMenuItems.remove(tableColumnBase);
        if (checkMenuItem != null) {
            tableColumnBase.textProperty().removeListener((InvalidationListener)this.weakColumnTextListener);
            checkMenuItem.selectedProperty().unbindBidirectional((Property)tableColumnBase.visibleProperty());
            this.columnPopupMenu.getItems().remove((Object)checkMenuItem);
        }
        if (!tableColumnBase.getColumns().isEmpty()) {
            for (TableColumnBase tableColumnBase2 : tableColumnBase.getColumns()) {
                this.remove(tableColumnBase2);
            }
        }
    }

    private void rebuildColumnMenu() {
        this.columnPopupMenu.getItems().clear();
        for (TableColumnBase tableColumnBase : this.getTableSkin().getColumns()) {
            if (tableColumnBase.getColumns().isEmpty()) {
                this.createMenuItem(tableColumnBase);
                continue;
            }
            List<TableColumnBase<?, ?>> list = this.getLeafColumns(tableColumnBase);
            for (TableColumnBase<?, ?> tableColumnBase2 : list) {
                this.createMenuItem(tableColumnBase2);
            }
        }
    }

    private List<TableColumnBase<?, ?>> getLeafColumns(TableColumnBase<?, ?> tableColumnBase) {
        ArrayList arrayList = new ArrayList();
        for (TableColumnBase tableColumnBase2 : tableColumnBase.getColumns()) {
            if (tableColumnBase2.getColumns().isEmpty()) {
                arrayList.add(tableColumnBase2);
                continue;
            }
            arrayList.addAll(this.getLeafColumns(tableColumnBase2));
        }
        return arrayList;
    }

    private void createMenuItem(TableColumnBase<?, ?> tableColumnBase) {
        CheckMenuItem checkMenuItem = this.columnMenuItems.get(tableColumnBase);
        if (checkMenuItem == null) {
            checkMenuItem = new CheckMenuItem();
            this.columnMenuItems.put(tableColumnBase, checkMenuItem);
        }
        checkMenuItem.setText(this.getText(tableColumnBase.getText(), tableColumnBase));
        tableColumnBase.textProperty().addListener((InvalidationListener)this.weakColumnTextListener);
        checkMenuItem.selectedProperty().bindBidirectional((Property)tableColumnBase.visibleProperty());
        this.columnPopupMenu.getItems().add((Object)checkMenuItem);
    }

    private String getText(String string, TableColumnBase tableColumnBase) {
        String string2 = string;
        for (TableColumnBase tableColumnBase2 = tableColumnBase.getParentColumn(); tableColumnBase2 != null; tableColumnBase2 = tableColumnBase2.getParentColumn()) {
            if (!this.isColumnVisibleInHeader(tableColumnBase2, (List)this.tableSkin.getColumns())) continue;
            string2 = tableColumnBase2.getText() + MENU_SEPARATOR + string2;
        }
        return string2;
    }

    private boolean isColumnVisibleInHeader(TableColumnBase tableColumnBase, List list) {
        if (tableColumnBase == null) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            boolean bl;
            TableColumnBase tableColumnBase2 = (TableColumnBase)list.get(i);
            if (tableColumnBase.equals((Object)tableColumnBase2)) {
                return true;
            }
            if (tableColumnBase2.getColumns().isEmpty() || !(bl = this.isColumnVisibleInHeader(tableColumnBase, (List)tableColumnBase2.getColumns()))) continue;
            return true;
        }
        return false;
    }
}

