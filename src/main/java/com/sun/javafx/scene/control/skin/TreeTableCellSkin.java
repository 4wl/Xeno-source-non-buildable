/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.ReadOnlyDoubleProperty
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeTableCell
 *  javafx.scene.control.TreeTableColumn
 *  javafx.scene.control.TreeTableRow
 *  javafx.scene.control.TreeTableView
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import com.sun.javafx.scene.control.skin.TableCellSkinBase;
import com.sun.javafx.scene.control.skin.TableRowSkinBase;
import com.sun.javafx.scene.control.skin.TreeTableRowSkin;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

public class TreeTableCellSkin<S, T>
extends TableCellSkinBase<TreeTableCell<S, T>, TreeTableCellBehavior<S, T>> {
    private final TreeTableCell<S, T> treeTableCell;
    private final TreeTableColumn<S, T> tableColumn;

    public TreeTableCellSkin(TreeTableCell<S, T> treeTableCell) {
        super(treeTableCell, new TreeTableCellBehavior<S, T>(treeTableCell));
        this.treeTableCell = treeTableCell;
        this.tableColumn = treeTableCell.getTableColumn();
        super.init(treeTableCell);
    }

    @Override
    protected BooleanProperty columnVisibleProperty() {
        return this.tableColumn.visibleProperty();
    }

    @Override
    protected ReadOnlyDoubleProperty columnWidthProperty() {
        return this.tableColumn.widthProperty();
    }

    @Override
    protected double leftLabelPadding() {
        double d = super.leftLabelPadding();
        double d2 = this.getCellSize();
        TreeTableCell treeTableCell = (TreeTableCell)this.getSkinnable();
        TreeTableColumn treeTableColumn = treeTableCell.getTableColumn();
        if (treeTableColumn == null) {
            return d;
        }
        TreeTableView treeTableView = treeTableCell.getTreeTableView();
        if (treeTableView == null) {
            return d;
        }
        int n = treeTableView.getVisibleLeafIndex(treeTableColumn);
        TreeTableColumn treeTableColumn2 = treeTableView.getTreeColumn();
        if (treeTableColumn2 == null && n != 0 || treeTableColumn2 != null && !treeTableColumn.equals((Object)treeTableColumn2)) {
            return d;
        }
        TreeTableRow treeTableRow = treeTableCell.getTreeTableRow();
        if (treeTableRow == null) {
            return d;
        }
        TreeItem treeItem = treeTableRow.getTreeItem();
        if (treeItem == null) {
            return d;
        }
        int n2 = treeTableView.getTreeItemLevel(treeItem);
        if (!treeTableView.isShowRoot()) {
            --n2;
        }
        double d3 = 10.0;
        if (treeTableRow.getSkin() instanceof TreeTableRowSkin) {
            d3 = ((TreeTableRowSkin)treeTableRow.getSkin()).getIndentationPerLevel();
        }
        d += (double)n2 * d3;
        Map<Control, Double> map = TableRowSkinBase.maxDisclosureWidthMap;
        d += map.containsKey((Object)treeTableView) ? map.get((Object)treeTableView) : 0.0;
        Node node = treeItem.getGraphic();
        return d += node == null ? 0.0 : node.prefWidth(d2);
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        if (this.isDeferToParentForPrefWidth) {
            return super.computePrefWidth(d, d2, d3, d4, d5);
        }
        return this.columnWidthProperty().get();
    }
}

