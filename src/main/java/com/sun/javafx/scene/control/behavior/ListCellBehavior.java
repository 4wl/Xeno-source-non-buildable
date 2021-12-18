/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.FocusModel
 *  javafx.scene.control.ListCell
 *  javafx.scene.control.ListView
 *  javafx.scene.control.MultipleSelectionModel
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import java.util.Collections;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

public class ListCellBehavior<T>
extends CellBehaviorBase<ListCell<T>> {
    public ListCellBehavior(ListCell<T> listCell) {
        super(listCell, Collections.emptyList());
    }

    @Override
    protected MultipleSelectionModel<T> getSelectionModel() {
        return this.getCellContainer().getSelectionModel();
    }

    @Override
    protected FocusModel<T> getFocusModel() {
        return this.getCellContainer().getFocusModel();
    }

    protected ListView<T> getCellContainer() {
        return ((ListCell)this.getControl()).getListView();
    }

    @Override
    protected void edit(ListCell<T> listCell) {
        int n = listCell == null ? -1 : listCell.getIndex();
        this.getCellContainer().edit(n);
    }
}

