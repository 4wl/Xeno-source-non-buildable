/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.geometry.Orientation
 *  javafx.scene.control.ListCell
 *  javafx.scene.control.ListView
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ListCellSkin<T>
extends CellSkinBase<ListCell<T>, ListCellBehavior<T>> {
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;

    public ListCellSkin(ListCell<T> listCell) {
        super(listCell, new ListCellBehavior<T>(listCell));
        this.fixedCellSize = listCell.getListView().getFixedCellSize();
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        this.registerChangeListener((ObservableValue<?>)listCell.getListView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("FIXED_CELL_SIZE".equals(string)) {
            this.fixedCellSize = ((ListCell)this.getSkinnable()).getListView().getFixedCellSize();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = super.computePrefWidth(d, d2, d3, d4, d5);
        ListView listView = ((ListCell)this.getSkinnable()).getListView();
        return listView == null ? 0.0 : (listView.getOrientation() == Orientation.VERTICAL ? d6 : Math.max(d6, this.getCellSize()));
    }

    @Override
    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        double d6 = this.getCellSize();
        double d7 = d6 == 24.0 ? super.computePrefHeight(d, d2, d3, d4, d5) : d6;
        return d7;
    }

    @Override
    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMinHeight(d, d2, d3, d4, d5);
    }

    @Override
    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(d, d2, d3, d4, d5);
    }
}

