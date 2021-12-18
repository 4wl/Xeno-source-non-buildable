/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.scene.control.TableFocusModel
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeTableRow
 *  javafx.scene.control.TreeTableView
 *  javafx.scene.input.MouseButton
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TableRowBehaviorBase;
import javafx.collections.ObservableList;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;

public class TreeTableRowBehavior<T>
extends TableRowBehaviorBase<TreeTableRow<T>> {
    public TreeTableRowBehavior(TreeTableRow<T> treeTableRow) {
        super(treeTableRow);
    }

    @Override
    protected TableSelectionModel<TreeItem<T>> getSelectionModel() {
        return this.getCellContainer().getSelectionModel();
    }

    protected TableFocusModel<TreeItem<T>, ?> getFocusModel() {
        return this.getCellContainer().getFocusModel();
    }

    protected TreeTableView<T> getCellContainer() {
        return ((TreeTableRow)this.getControl()).getTreeTableView();
    }

    @Override
    protected TablePositionBase<?> getFocusedCell() {
        return this.getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override
    protected ObservableList getVisibleLeafColumns() {
        return this.getCellContainer().getVisibleLeafColumns();
    }

    @Override
    protected void edit(TreeTableRow<T> treeTableRow) {
    }

    @Override
    protected void handleClicks(MouseButton mouseButton, int n, boolean bl) {
        TreeItem treeItem = ((TreeTableRow)this.getControl()).getTreeItem();
        if (mouseButton == MouseButton.PRIMARY) {
            if (n == 1 && bl) {
                this.edit((TreeTableRow)this.getControl());
            } else if (n == 1) {
                this.edit((TreeTableRow<T>)null);
            } else if (n == 2 && treeItem.isLeaf()) {
                this.edit((TreeTableRow)this.getControl());
            } else if (n % 2 == 0) {
                treeItem.setExpanded(!treeItem.isExpanded());
            }
        }
    }
}

