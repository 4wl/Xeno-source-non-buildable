/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.ScrollBar
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.util.Utils;
import javafx.scene.control.ScrollBar;

public class VirtualScrollBar
extends ScrollBar {
    private final VirtualFlow flow;
    private boolean virtual;
    private boolean adjusting;

    public VirtualScrollBar(VirtualFlow virtualFlow) {
        this.flow = virtualFlow;
        super.valueProperty().addListener(observable -> {
            if (this.isVirtual() && !this.adjusting) {
                virtualFlow.setPosition(this.getValue());
            }
        });
    }

    public void setVirtual(boolean bl) {
        this.virtual = bl;
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    public void decrement() {
        if (this.isVirtual()) {
            this.flow.adjustPixels(-10.0);
        } else {
            super.decrement();
        }
    }

    public void increment() {
        if (this.isVirtual()) {
            this.flow.adjustPixels(10.0);
        } else {
            super.increment();
        }
    }

    public void adjustValue(double d) {
        if (this.isVirtual()) {
            this.adjusting = true;
            double d2 = this.flow.getPosition();
            double d3 = (this.getMax() - this.getMin()) * Utils.clamp(0.0, d, 1.0) + this.getMin();
            if (d3 < d2) {
                Object t = this.flow.getFirstVisibleCell();
                if (t == null) {
                    return;
                }
                this.flow.showAsLast(t);
            } else if (d3 > d2) {
                Object t = this.flow.getLastVisibleCell();
                if (t == null) {
                    return;
                }
                this.flow.showAsFirst(t);
            }
            this.adjusting = false;
        } else {
            super.adjustValue(d);
        }
    }
}

