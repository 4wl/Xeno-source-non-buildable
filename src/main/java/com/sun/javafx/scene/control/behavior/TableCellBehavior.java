/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.TableCell
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableView
 *  javafx.scene.control.TableView$TableViewFocusModel
 *  javafx.scene.control.TableView$TableViewSelectionModel
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;

public class TableCellBehavior<S, T>
extends TableCellBehaviorBase<S, T, TableColumn<S, ?>, TableCell<S, T>> {
    public TableCellBehavior(TableCell<S, T> tableCell) {
        super(tableCell);
    }

    protected TableView<S> getCellContainer() {
        return ((TableCell)this.getControl()).getTableView();
    }

    @Override
    protected TableColumn<S, T> getTableColumn() {
        return ((TableCell)this.getControl()).getTableColumn();
    }

    @Override
    protected int getItemCount() {
        return this.getCellContainer().getItems().size();
    }

    @Override
    protected TableView.TableViewSelectionModel<S> getSelectionModel() {
        return this.getCellContainer().getSelectionModel();
    }

    @Override
    protected TableView.TableViewFocusModel<S> getFocusModel() {
        return this.getCellContainer().getFocusModel();
    }

    @Override
    protected TablePositionBase getFocusedCell() {
        return this.getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override
    protected boolean isTableRowSelected() {
        return ((TableCell)this.getControl()).getTableRow().isSelected();
    }

    @Override
    protected int getVisibleLeafIndex(TableColumnBase tableColumnBase) {
        return this.getCellContainer().getVisibleLeafIndex((TableColumn)tableColumnBase);
    }

    @Override
    protected void focus(int n, TableColumnBase tableColumnBase) {
        this.getFocusModel().focus(n, (TableColumn)tableColumnBase);
    }

    @Override
    protected void edit(TableCell<S, T> tableCell) {
        if (tableCell == null) {
            this.getCellContainer().edit(-1, null);
        } else {
            this.getCellContainer().edit(tableCell.getIndex(), tableCell.getTableColumn());
        }
    }
}

