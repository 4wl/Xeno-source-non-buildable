/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.Control
 *  javafx.scene.control.IndexedCell
 *  javafx.scene.control.ScrollToEvent
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollToEvent;

public abstract class VirtualContainerBase<C extends Control, B extends BehaviorBase<C>, I extends IndexedCell>
extends BehaviorSkinBase<C, B> {
    protected boolean rowCountDirty;
    protected final VirtualFlow<I> flow = this.createVirtualFlow();

    public VirtualContainerBase(C c, B b) {
        super(c, b);
        c.addEventHandler(ScrollToEvent.scrollToTopIndex(), scrollToEvent -> {
            if (this.rowCountDirty) {
                this.updateRowCount();
                this.rowCountDirty = false;
            }
            this.flow.scrollTo((Integer)scrollToEvent.getScrollTarget());
        });
    }

    public abstract I createCell();

    protected VirtualFlow<I> createVirtualFlow() {
        return new VirtualFlow();
    }

    public abstract int getItemCount();

    protected abstract void updateRowCount();

    double getMaxCellWidth(int n) {
        return this.snappedLeftInset() + this.flow.getMaxCellWidth(n) + this.snappedRightInset();
    }

    double getVirtualFlowPreferredHeight(int n) {
        double d = 1.0;
        for (int i = 0; i < n && i < this.getItemCount(); ++i) {
            d += this.flow.getCellLength(i);
        }
        return d + this.snappedTopInset() + this.snappedBottomInset();
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        this.checkState();
    }

    protected void checkState() {
        if (this.rowCountDirty) {
            this.updateRowCount();
            this.rowCountDirty = false;
        }
    }
}

