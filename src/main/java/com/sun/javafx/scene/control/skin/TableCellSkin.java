/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.ReadOnlyDoubleProperty
 *  javafx.scene.control.TableCell
 *  javafx.scene.control.TableColumn
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TableCellBehavior;
import com.sun.javafx.scene.control.skin.TableCellSkinBase;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class TableCellSkin<S, T>
extends TableCellSkinBase<TableCell<S, T>, TableCellBehavior<S, T>> {
    private final TableColumn<S, T> tableColumn;

    public TableCellSkin(TableCell<S, T> tableCell) {
        super(tableCell, new TableCellBehavior<S, T>(tableCell));
        this.tableColumn = tableCell.getTableColumn();
        super.init(tableCell);
    }

    @Override
    protected BooleanProperty columnVisibleProperty() {
        return this.tableColumn.visibleProperty();
    }

    @Override
    protected ReadOnlyDoubleProperty columnWidthProperty() {
        return this.tableColumn.widthProperty();
    }
}

