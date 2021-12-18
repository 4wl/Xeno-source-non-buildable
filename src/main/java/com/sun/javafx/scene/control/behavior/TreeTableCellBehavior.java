/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeTableCell
 *  javafx.scene.control.TreeTableColumn
 *  javafx.scene.control.TreeTableView
 *  javafx.scene.control.TreeTableView$TreeTableViewFocusModel
 *  javafx.scene.control.TreeTableView$TreeTableViewSelectionModel
 *  javafx.scene.input.MouseButton
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import java.util.Iterator;
import javafx.scene.Node;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;

public class TreeTableCellBehavior<S, T>
extends TableCellBehaviorBase<TreeItem<S>, T, TreeTableColumn<S, ?>, TreeTableCell<S, T>> {
    public TreeTableCellBehavior(TreeTableCell<S, T> treeTableCell) {
        super(treeTableCell);
    }

    protected TreeTableView<S> getCellContainer() {
        return ((TreeTableCell)this.getControl()).getTreeTableView();
    }

    @Override
    protected TreeTableColumn<S, T> getTableColumn() {
        return ((TreeTableCell)this.getControl()).getTableColumn();
    }

    @Override
    protected int getItemCount() {
        return this.getCellContainer().getExpandedItemCount();
    }

    @Override
    protected TreeTableView.TreeTableViewSelectionModel<S> getSelectionModel() {
        return this.getCellContainer().getSelectionModel();
    }

    @Override
    protected TreeTableView.TreeTableViewFocusModel<S> getFocusModel() {
        return this.getCellContainer().getFocusModel();
    }

    @Override
    protected TablePositionBase getFocusedCell() {
        return this.getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override
    protected boolean isTableRowSelected() {
        return ((TreeTableCell)this.getControl()).getTreeTableRow().isSelected();
    }

    @Override
    protected int getVisibleLeafIndex(TableColumnBase tableColumnBase) {
        return this.getCellContainer().getVisibleLeafIndex((TreeTableColumn)tableColumnBase);
    }

    @Override
    protected void focus(int n, TableColumnBase tableColumnBase) {
        this.getFocusModel().focus(n, (TreeTableColumn)tableColumnBase);
    }

    @Override
    protected void edit(TreeTableCell<S, T> treeTableCell) {
        if (treeTableCell == null) {
            this.getCellContainer().edit(-1, null);
        } else {
            this.getCellContainer().edit(treeTableCell.getIndex(), treeTableCell.getTableColumn());
        }
    }

    @Override
    protected boolean handleDisclosureNode(double d, double d2) {
        Node node;
        TreeTableColumn treeTableColumn;
        TreeItem treeItem = ((TreeTableCell)this.getControl()).getTreeTableRow().getTreeItem();
        TreeTableView treeTableView = ((TreeTableCell)this.getControl()).getTreeTableView();
        TreeTableColumn<S, T> treeTableColumn2 = this.getTableColumn();
        TreeTableColumn treeTableColumn3 = treeTableColumn = treeTableView.getTreeColumn() == null ? treeTableView.getVisibleLeafColumn(0) : treeTableView.getTreeColumn();
        if (treeTableColumn2 == treeTableColumn && (node = ((TreeTableCell)this.getControl()).getTreeTableRow().getDisclosureNode()) != null) {
            TreeTableColumn treeTableColumn4;
            double d3 = 0.0;
            Iterator iterator = treeTableView.getVisibleLeafColumns().iterator();
            while (iterator.hasNext() && (treeTableColumn4 = (TreeTableColumn)iterator.next()) != treeTableColumn) {
                d3 += treeTableColumn4.getWidth();
            }
            double d4 = node.getBoundsInParent().getMaxX();
            if (d < d4 - d3) {
                if (treeItem != null) {
                    treeItem.setExpanded(!treeItem.isExpanded());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void handleClicks(MouseButton mouseButton, int n, boolean bl) {
        TreeItem treeItem = ((TreeTableCell)this.getControl()).getTreeTableRow().getTreeItem();
        if (mouseButton == MouseButton.PRIMARY) {
            if (n == 1 && bl) {
                this.edit((TreeTableCell)this.getControl());
            } else if (n == 1) {
                this.edit((TreeTableCell<S, T>)null);
            } else if (n == 2 && treeItem.isLeaf()) {
                this.edit((TreeTableCell)this.getControl());
            } else if (n % 2 == 0) {
                treeItem.setExpanded(!treeItem.isExpanded());
            }
        }
    }
}

