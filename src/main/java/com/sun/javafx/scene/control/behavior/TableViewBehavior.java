/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.WeakChangeListener
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.scene.Node
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TableFocusModel
 *  javafx.scene.control.TablePosition
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.control.TableView
 *  javafx.scene.control.TableView$TableViewSelectionModel
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TableViewBehaviorBase;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;

public class TableViewBehavior<T>
extends TableViewBehaviorBase<TableView<T>, T, TableColumn<T, ?>> {
    private final ChangeListener<TableView.TableViewSelectionModel<T>> selectionModelListener = (observableValue, tableViewSelectionModel, tableViewSelectionModel2) -> {
        if (tableViewSelectionModel != null) {
            tableViewSelectionModel.getSelectedCells().removeListener((ListChangeListener)this.weakSelectedCellsListener);
        }
        if (tableViewSelectionModel2 != null) {
            tableViewSelectionModel2.getSelectedCells().addListener((ListChangeListener)this.weakSelectedCellsListener);
        }
    };
    private final WeakChangeListener<TableView.TableViewSelectionModel<T>> weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);
    private TwoLevelFocusBehavior tlFocus;

    public TableViewBehavior(TableView<T> tableView) {
        super(tableView);
        tableView.selectionModelProperty().addListener(this.weakSelectionModelListener);
        TableView.TableViewSelectionModel tableViewSelectionModel3 = tableView.getSelectionModel();
        if (tableViewSelectionModel3 != null) {
            tableViewSelectionModel3.getSelectedCells().addListener(this.selectedCellsListener);
        }
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior((Node)tableView);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    @Override
    protected int getItemCount() {
        return ((TableView)this.getControl()).getItems() == null ? 0 : ((TableView)this.getControl()).getItems().size();
    }

    @Override
    protected TableFocusModel getFocusModel() {
        return ((TableView)this.getControl()).getFocusModel();
    }

    @Override
    protected TableSelectionModel<T> getSelectionModel() {
        return ((TableView)this.getControl()).getSelectionModel();
    }

    @Override
    protected ObservableList<TablePosition> getSelectedCells() {
        return ((TableView)this.getControl()).getSelectionModel().getSelectedCells();
    }

    @Override
    protected TablePositionBase getFocusedCell() {
        return ((TableView)this.getControl()).getFocusModel().getFocusedCell();
    }

    @Override
    protected int getVisibleLeafIndex(TableColumnBase tableColumnBase) {
        return ((TableView)this.getControl()).getVisibleLeafIndex((TableColumn)tableColumnBase);
    }

    protected TableColumn<T, ?> getVisibleLeafColumn(int n) {
        return ((TableView)this.getControl()).getVisibleLeafColumn(n);
    }

    @Override
    protected void editCell(int n, TableColumnBase tableColumnBase) {
        ((TableView)this.getControl()).edit(n, (TableColumn)tableColumnBase);
    }

    @Override
    protected ObservableList<TableColumn<T, ?>> getVisibleLeafColumns() {
        return ((TableView)this.getControl()).getVisibleLeafColumns();
    }

    @Override
    protected TablePositionBase<TableColumn<T, ?>> getTablePosition(int n, TableColumnBase<T, ?> tableColumnBase) {
        return new TablePosition((TableView)this.getControl(), n, (TableColumn)tableColumnBase);
    }

    @Override
    protected void selectAllToFocus(boolean bl) {
        if (((TableView)this.getControl()).getEditingCell() != null) {
            return;
        }
        super.selectAllToFocus(bl);
    }
}

