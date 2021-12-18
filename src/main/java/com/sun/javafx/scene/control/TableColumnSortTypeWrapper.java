/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableColumn$SortType
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TreeTableColumn
 *  javafx.scene.control.TreeTableColumn$SortType
 */
package com.sun.javafx.scene.control;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

public class TableColumnSortTypeWrapper {
    public static boolean isAscending(TableColumnBase<?, ?> tableColumnBase) {
        String string = TableColumnSortTypeWrapper.getSortTypeName(tableColumnBase);
        return "ASCENDING".equals(string);
    }

    public static boolean isDescending(TableColumnBase<?, ?> tableColumnBase) {
        String string = TableColumnSortTypeWrapper.getSortTypeName(tableColumnBase);
        return "DESCENDING".equals(string);
    }

    public static void setSortType(TableColumnBase<?, ?> tableColumnBase, TableColumn.SortType sortType) {
        if (tableColumnBase instanceof TableColumn) {
            TableColumn tableColumn = (TableColumn)tableColumnBase;
            tableColumn.setSortType(sortType);
        } else if (tableColumnBase instanceof TreeTableColumn) {
            TreeTableColumn treeTableColumn = (TreeTableColumn)tableColumnBase;
            if (sortType == TableColumn.SortType.ASCENDING) {
                treeTableColumn.setSortType(TreeTableColumn.SortType.ASCENDING);
            } else if (sortType == TableColumn.SortType.DESCENDING) {
                treeTableColumn.setSortType(TreeTableColumn.SortType.DESCENDING);
            } else if (sortType == null) {
                treeTableColumn.setSortType(null);
            }
        }
    }

    public static String getSortTypeName(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase instanceof TableColumn) {
            TableColumn tableColumn = (TableColumn)tableColumnBase;
            TableColumn.SortType sortType = tableColumn.getSortType();
            return sortType == null ? null : sortType.name();
        }
        if (tableColumnBase instanceof TreeTableColumn) {
            TreeTableColumn treeTableColumn = (TreeTableColumn)tableColumnBase;
            TreeTableColumn.SortType sortType = treeTableColumn.getSortType();
            return sortType == null ? null : sortType.name();
        }
        return null;
    }

    public static ObservableValue getSortTypeProperty(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase instanceof TableColumn) {
            return ((TableColumn)tableColumnBase).sortTypeProperty();
        }
        if (tableColumnBase instanceof TreeTableColumn) {
            return ((TreeTableColumn)tableColumnBase).sortTypeProperty();
        }
        return null;
    }
}

