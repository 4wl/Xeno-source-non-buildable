/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.WeakInvalidationListener
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.ReadOnlyDoubleProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.scene.Node
 *  javafx.scene.control.IndexedCell
 *  javafx.scene.shape.Rectangle
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.IndexedCell;
import javafx.scene.shape.Rectangle;

public abstract class TableCellSkinBase<C extends IndexedCell, B extends CellBehaviorBase<C>>
extends CellSkinBase<C, B> {
    static final String DEFER_TO_PARENT_PREF_WIDTH = "deferToParentPrefWidth";
    boolean isDeferToParentForPrefWidth = false;
    private InvalidationListener columnWidthListener = observable -> ((IndexedCell)this.getSkinnable()).requestLayout();
    private WeakInvalidationListener weakColumnWidthListener = new WeakInvalidationListener(this.columnWidthListener);

    protected abstract ReadOnlyDoubleProperty columnWidthProperty();

    protected abstract BooleanProperty columnVisibleProperty();

    public TableCellSkinBase(C c, B b) {
        super(c, b);
    }

    protected void init(C c) {
        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind((ObservableValue)c.widthProperty());
        rectangle.heightProperty().bind((ObservableValue)c.heightProperty());
        ((IndexedCell)this.getSkinnable()).setClip((Node)rectangle);
        ReadOnlyDoubleProperty readOnlyDoubleProperty = this.columnWidthProperty();
        if (readOnlyDoubleProperty != null) {
            readOnlyDoubleProperty.addListener((InvalidationListener)this.weakColumnWidthListener);
        }
        this.registerChangeListener((ObservableValue<?>)c.visibleProperty(), "VISIBLE");
        if (c.getProperties().containsKey((Object)"deferToParentPrefWidth")) {
            this.isDeferToParentForPrefWidth = true;
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("VISIBLE".equals(string)) {
            ((IndexedCell)this.getSkinnable()).setVisible(this.columnVisibleProperty().get());
        }
    }

    @Override
    public void dispose() {
        ReadOnlyDoubleProperty readOnlyDoubleProperty = this.columnWidthProperty();
        if (readOnlyDoubleProperty != null) {
            readOnlyDoubleProperty.removeListener((InvalidationListener)this.weakColumnWidthListener);
        }
        super.dispose();
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        this.layoutLabelInArea(d, d2, d3, d4 - ((IndexedCell)this.getSkinnable()).getPadding().getBottom());
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        if (this.isDeferToParentForPrefWidth) {
            return super.computePrefWidth(d, d2, d3, d4, d5);
        }
        return this.columnWidthProperty().get();
    }
}

