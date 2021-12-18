/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.Control
 *  javafx.scene.control.IndexedCell
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TableFocusModel
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.input.MouseButton
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import java.util.Collections;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.MouseButton;

public abstract class TableCellBehaviorBase<S, T, TC extends TableColumnBase<S, ?>, C extends IndexedCell<T>>
extends CellBehaviorBase<C> {
    public TableCellBehaviorBase(C c) {
        super(c, Collections.emptyList());
    }

    protected abstract TableColumnBase<S, T> getTableColumn();

    protected abstract int getItemCount();

    @Override
    protected abstract TableSelectionModel<S> getSelectionModel();

    protected abstract TableFocusModel<S, TC> getFocusModel();

    protected abstract TablePositionBase getFocusedCell();

    protected abstract boolean isTableRowSelected();

    protected abstract int getVisibleLeafIndex(TableColumnBase<S, T> var1);

    protected abstract void focus(int var1, TableColumnBase<S, T> var2);

    @Override
    protected void doSelect(double d, double d2, MouseButton mouseButton, int n, boolean bl, boolean bl2) {
        IndexedCell indexedCell = (IndexedCell)this.getControl();
        if (!indexedCell.contains(d, d2)) {
            return;
        }
        Control control = this.getCellContainer();
        if (control == null) {
            return;
        }
        int n2 = this.getItemCount();
        if (indexedCell.getIndex() >= n2) {
            return;
        }
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        boolean bl3 = this.isSelected();
        int n3 = indexedCell.getIndex();
        int n4 = this.getColumn();
        TableColumnBase tableColumnBase = this.getTableColumn();
        TableFocusModel<S, TC> tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        if (this.handleDisclosureNode(d, d2)) {
            return;
        }
        if (bl) {
            if (!TableCellBehaviorBase.hasNonDefaultAnchor(control)) {
                TableCellBehaviorBase.setAnchor(control, tablePositionBase, false);
            }
        } else {
            TableCellBehaviorBase.removeAnchor(control);
        }
        if (mouseButton == MouseButton.PRIMARY || mouseButton == MouseButton.SECONDARY && !bl3) {
            if (tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
                this.simpleSelect(mouseButton, n, bl2);
            } else if (bl2) {
                if (bl3) {
                    tableSelectionModel.clearSelection(n3, tableColumnBase);
                    tableFocusModel.focus(n3, tableColumnBase);
                } else {
                    tableSelectionModel.select(n3, tableColumnBase);
                }
            } else if (bl) {
                TableColumnBase tableColumnBase2;
                TablePositionBase tablePositionBase2 = TableCellBehaviorBase.getAnchor(control, tablePositionBase);
                int n5 = tablePositionBase2.getRow();
                boolean bl4 = n5 < n3;
                tableSelectionModel.clearSelection();
                int n6 = Math.min(n5, n3);
                int n7 = Math.max(n5, n3);
                TableColumnBase tableColumnBase3 = tablePositionBase2.getColumn() < n4 ? tablePositionBase2.getTableColumn() : tableColumnBase;
                TableColumnBase tableColumnBase4 = tableColumnBase2 = tablePositionBase2.getColumn() >= n4 ? tablePositionBase2.getTableColumn() : tableColumnBase;
                if (bl4) {
                    tableSelectionModel.selectRange(n6, tableColumnBase3, n7, tableColumnBase2);
                } else {
                    tableSelectionModel.selectRange(n7, tableColumnBase3, n6, tableColumnBase2);
                }
            } else {
                this.simpleSelect(mouseButton, n, bl2);
            }
        }
    }

    @Override
    protected void simpleSelect(MouseButton mouseButton, int n, boolean bl) {
        TableColumnBase<S, T> tableColumnBase;
        int n2;
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        boolean bl2 = tableSelectionModel.isSelected(n2 = ((IndexedCell)this.getControl()).getIndex(), tableColumnBase = this.getTableColumn());
        if (bl2 && bl) {
            tableSelectionModel.clearSelection(n2, tableColumnBase);
            this.getFocusModel().focus(n2, tableColumnBase);
            bl2 = false;
        } else {
            tableSelectionModel.clearAndSelect(n2, tableColumnBase);
        }
        this.handleClicks(mouseButton, n, bl2);
    }

    private int getColumn() {
        if (this.getSelectionModel().isCellSelectionEnabled()) {
            TableColumnBase<S, T> tableColumnBase = this.getTableColumn();
            return this.getVisibleLeafIndex(tableColumnBase);
        }
        return -1;
    }

    @Override
    protected boolean isSelected() {
        TableSelectionModel<S> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return false;
        }
        if (tableSelectionModel.isCellSelectionEnabled()) {
            IndexedCell indexedCell = (IndexedCell)this.getControl();
            return indexedCell.isSelected();
        }
        return this.isTableRowSelected();
    }
}

