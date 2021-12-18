/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.DateCell
 *  javafx.scene.text.Text
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.DateCellBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import javafx.scene.control.DateCell;
import javafx.scene.text.Text;

public class DateCellSkin
extends CellSkinBase<DateCell, DateCellBehavior> {
    public DateCellSkin(DateCell dateCell) {
        super(dateCell, new DateCellBehavior(dateCell));
        dateCell.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        Text text = (Text)((DateCell)this.getSkinnable()).getProperties().get((Object)"DateCell.secondaryText");
        if (text != null) {
            text.setManaged(false);
            this.getChildren().add((Object)text);
        }
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        super.layoutChildren(d, d2, d3, d4);
        Text text = (Text)((DateCell)this.getSkinnable()).getProperties().get((Object)"DateCell.secondaryText");
        if (text != null) {
            double d5 = d + d3 - this.rightLabelPadding() - text.getLayoutBounds().getWidth();
            double d6 = d2 + d4 - this.bottomLabelPadding() - text.getLayoutBounds().getHeight();
            text.relocate(this.snapPosition(d5), this.snapPosition(d6));
        }
    }

    private double cellSize() {
        double d = this.getCellSize();
        Text text = (Text)((DateCell)this.getSkinnable()).getProperties().get((Object)"DateCell.secondaryText");
        if (text != null && d == 24.0) {
            d = 36.0;
        }
        return d;
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = super.computePrefWidth(d, d2, d3, d4, d5);
        return this.snapSize(Math.max(d6, this.cellSize()));
    }

    @Override
    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = super.computePrefHeight(d, d2, d3, d4, d5);
        return this.snapSize(Math.max(d6, this.cellSize()));
    }
}

