/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.scene.control.FocusModel
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableRow
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.control.TableView
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TableRowBehaviorBase;
import javafx.collections.ObservableList;
import javafx.scene.control.FocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;

public class TableRowBehavior<T>
extends TableRowBehaviorBase<TableRow<T>> {
    public TableRowBehavior(TableRow<T> tableRow) {
        super(tableRow);
    }

    @Override
    protected TableSelectionModel<T> getSelectionModel() {
        return this.getCellContainer().getSelectionModel();
    }

    @Override
    protected TablePositionBase<?> getFocusedCell() {
        return this.getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override
    protected FocusModel<T> getFocusModel() {
        return this.getCellContainer().getFocusModel();
    }

    @Override
    protected ObservableList getVisibleLeafColumns() {
        return this.getCellContainer().getVisibleLeafColumns();
    }

    protected TableView<T> getCellContainer() {
        return ((TableRow)this.getControl()).getTableView();
    }

    @Override
    protected void edit(TableRow<T> tableRow) {
    }
}

