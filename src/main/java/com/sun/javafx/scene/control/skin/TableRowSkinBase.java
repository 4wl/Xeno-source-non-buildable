/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.FadeTransition
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.css.StyleOrigin
 *  javafx.css.StyleableObjectProperty
 *  javafx.geometry.Pos
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.IndexedCell
 *  javafx.scene.control.TableColumnBase
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TableColumnBase;
import javafx.util.Duration;

public abstract class TableRowSkinBase<T, C extends IndexedCell, B extends CellBehaviorBase<C>, R extends IndexedCell>
extends CellSkinBase<C, B> {
    private static boolean IS_STUB_TOOLKIT = Toolkit.getToolkit().toString().contains("StubToolkit");
    private static boolean DO_ANIMATIONS = !IS_STUB_TOOLKIT && !PlatformUtil.isEmbedded();
    private static final Duration FADE_DURATION = Duration.millis((double)200.0);
    static final Map<Control, Double> maxDisclosureWidthMap = new WeakHashMap<Control, Double>();
    private static final int DEFAULT_FULL_REFRESH_COUNTER = 100;
    protected WeakHashMap<TableColumnBase, Reference<R>> cellsMap;
    protected final List<R> cells = new ArrayList<R>();
    private int fullRefreshCounter = 100;
    protected boolean isDirty = false;
    protected boolean updateCells = false;
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;
    private ListChangeListener<TableColumnBase> visibleLeafColumnsListener = change -> {
        this.isDirty = true;
        ((IndexedCell)this.getSkinnable()).requestLayout();
    };
    private WeakListChangeListener<TableColumnBase> weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);

    public TableRowSkinBase(C c, B b) {
        super(c, b);
    }

    protected void init(C c) {
        ((IndexedCell)this.getSkinnable()).setPickOnBounds(false);
        this.recreateCells();
        this.updateCells(true);
        this.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        c.itemProperty().addListener(observable -> this.requestCellUpdate());
        this.registerChangeListener((ObservableValue<?>)c.indexProperty(), "INDEX");
        if (this.fixedCellSizeProperty() != null) {
            this.registerChangeListener((ObservableValue<?>)this.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
            this.fixedCellSize = this.fixedCellSizeProperty().get();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    protected abstract ObjectProperty<Node> graphicProperty();

    protected abstract Control getVirtualFlowOwner();

    protected abstract ObservableList<? extends TableColumnBase> getVisibleLeafColumns();

    protected abstract void updateCell(R var1, C var2);

    protected abstract DoubleProperty fixedCellSizeProperty();

    protected abstract boolean isColumnPartiallyOrFullyVisible(TableColumnBase var1);

    protected abstract R getCell(TableColumnBase var1);

    protected abstract TableColumnBase<T, ?> getTableColumnBase(R var1);

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("INDEX".equals(string)) {
            if (((IndexedCell)this.getSkinnable()).isEmpty()) {
                this.requestCellUpdate();
            }
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.fixedCellSize = this.fixedCellSizeProperty().get();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        double d5;
        this.checkState();
        if (this.cellsMap.isEmpty()) {
            return;
        }
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        if (observableList.isEmpty()) {
            super.layoutChildren(d, d2, d3, d4);
            return;
        }
        IndexedCell indexedCell = (IndexedCell)this.getSkinnable();
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        boolean bl = this.isIndentationRequired();
        boolean bl2 = this.isDisclosureNodeVisible();
        int n = 0;
        Node node = null;
        if (bl) {
            double d9;
            TableColumnBase tableColumnBase = this.getTreeColumn();
            n = tableColumnBase == null ? 0 : observableList.indexOf((Object)tableColumnBase);
            n = n < 0 ? 0 : n;
            int n2 = this.getIndentationLevel((C)indexedCell);
            if (!this.isShowRoot()) {
                --n2;
            }
            d5 = this.getIndentationPerLevel();
            d6 = (double)n2 * d5;
            Control control = this.getVirtualFlowOwner();
            d7 = d9 = maxDisclosureWidthMap.containsKey((Object)control) ? maxDisclosureWidthMap.get((Object)control) : 0.0;
            node = this.getDisclosureNode();
            if (node != null) {
                node.setVisible(bl2);
                if (bl2 && (d7 = node.prefWidth(d4)) > d9) {
                    maxDisclosureWidthMap.put(control, d7);
                    VirtualFlow<C> virtualFlow = this.getVirtualFlow();
                    int n3 = ((IndexedCell)this.getSkinnable()).getIndex();
                    for (int i = 0; i < virtualFlow.cells.size(); ++i) {
                        IndexedCell indexedCell2 = (IndexedCell)virtualFlow.cells.get(i);
                        if (indexedCell2 == null || indexedCell2.isEmpty()) continue;
                        indexedCell2.requestLayout();
                        indexedCell2.layout();
                    }
                }
            }
        }
        double d10 = this.snappedTopInset() + this.snappedBottomInset();
        double d11 = this.snappedLeftInset() + this.snappedRightInset();
        double d12 = indexedCell.getHeight();
        int n4 = indexedCell.getIndex();
        if (n4 < 0) {
            return;
        }
        int n5 = this.cells.size();
        for (int i = 0; i < n5; ++i) {
            double d13;
            IndexedCell indexedCell3 = (IndexedCell)this.cells.get(i);
            TableColumnBase<T, ?> tableColumnBase = this.getTableColumnBase((R)indexedCell3);
            boolean bl3 = true;
            if (this.fixedCellSizeEnabled) {
                bl3 = this.isColumnPartiallyOrFullyVisible(tableColumnBase);
                d5 = this.fixedCellSize;
            } else {
                d5 = Math.max(d12, indexedCell3.prefHeight(-1.0));
                d5 = this.snapSize(d5) - this.snapSize(d10);
            }
            if (bl3) {
                if (this.fixedCellSizeEnabled && indexedCell3.getParent() == null) {
                    this.getChildren().add((Object)indexedCell3);
                }
                d13 = this.snapSize(indexedCell3.prefWidth(-1.0)) - this.snapSize(d11);
                boolean bl4 = d4 <= 24.0;
                StyleOrigin styleOrigin = ((StyleableObjectProperty)indexedCell3.alignmentProperty()).getStyleOrigin();
                if (!bl4 && styleOrigin == null) {
                    indexedCell3.setAlignment(Pos.TOP_LEFT);
                }
                if (bl && i == n) {
                    ObjectProperty<Node> objectProperty;
                    Node node2;
                    if (bl2) {
                        double d14 = node.prefHeight(d7);
                        if (d13 > 0.0 && d13 < d7 + d6) {
                            this.fadeOut(node);
                        } else {
                            this.fadeIn(node);
                            node.resize(d7, d14);
                            node.relocate(d + d6, bl4 ? d4 / 2.0 - d14 / 2.0 : d2 + indexedCell3.getPadding().getTop());
                            node.toFront();
                        }
                    }
                    Node node3 = node2 = (objectProperty = this.graphicProperty()) == null ? null : (Node)objectProperty.get();
                    if (node2 != null) {
                        d8 = node2.prefWidth(-1.0) + 3.0;
                        double d15 = node2.prefHeight(d8);
                        if (d13 > 0.0 && d13 < d7 + d6 + d8) {
                            this.fadeOut(node2);
                        } else {
                            this.fadeIn(node2);
                            node2.relocate(d + d6 + d7, bl4 ? d4 / 2.0 - d15 / 2.0 : d2 + indexedCell3.getPadding().getTop());
                            node2.toFront();
                        }
                    }
                }
                indexedCell3.resize(d13, d5);
                indexedCell3.relocate(d, this.snappedTopInset());
                indexedCell3.requestLayout();
            } else {
                if (this.fixedCellSizeEnabled) {
                    this.getChildren().remove((Object)indexedCell3);
                }
                d13 = this.snapSize(indexedCell3.prefWidth(-1.0)) - this.snapSize(d11);
            }
            d += d13;
        }
    }

    protected int getIndentationLevel(C c) {
        return 0;
    }

    protected double getIndentationPerLevel() {
        return 0.0;
    }

    protected boolean isIndentationRequired() {
        return false;
    }

    protected TableColumnBase getTreeColumn() {
        return null;
    }

    protected Node getDisclosureNode() {
        return null;
    }

    protected boolean isDisclosureNodeVisible() {
        return false;
    }

    protected boolean isShowRoot() {
        return true;
    }

    protected TableColumnBase<T, ?> getVisibleLeafColumn(int n) {
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        if (n < 0 || n >= observableList.size()) {
            return null;
        }
        return (TableColumnBase)observableList.get(n);
    }

    protected void updateCells(boolean bl) {
        if (bl) {
            if (this.fullRefreshCounter == 0) {
                this.recreateCells();
            }
            --this.fullRefreshCounter;
        }
        boolean bl2 = this.cells.isEmpty();
        this.cells.clear();
        IndexedCell indexedCell = (IndexedCell)this.getSkinnable();
        int n = indexedCell.getIndex();
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        int n2 = observableList.size();
        for (int i = 0; i < n2; ++i) {
            TableColumnBase tableColumnBase = (TableColumnBase)observableList.get(i);
            IndexedCell indexedCell2 = null;
            if (this.cellsMap.containsKey((Object)tableColumnBase) && (indexedCell2 = (IndexedCell)this.cellsMap.get((Object)tableColumnBase).get()) == null) {
                this.cellsMap.remove((Object)tableColumnBase);
            }
            if (indexedCell2 == null) {
                indexedCell2 = (IndexedCell)this.createCell(tableColumnBase);
            }
            this.updateCell((R)indexedCell2, (C)indexedCell);
            indexedCell2.updateIndex(n);
            this.cells.add((R)indexedCell2);
        }
        if (!this.fixedCellSizeEnabled && (bl || bl2)) {
            this.getChildren().setAll(this.cells);
        }
    }

    private VirtualFlow<C> getVirtualFlow() {
        for (Control control = this.getSkinnable(); control != null; control = control.getParent()) {
            if (!(control instanceof VirtualFlow)) continue;
            return (VirtualFlow)control;
        }
        return null;
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        ObservableList<TableColumnBase> observableList = this.getVisibleLeafColumns();
        int n = observableList.size();
        for (int i = 0; i < n; ++i) {
            d6 += ((TableColumnBase)observableList.get(i)).getWidth();
        }
        return d6;
    }

    @Override
    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        this.checkState();
        if (this.getCellSize() < 24.0) {
            return this.getCellSize();
        }
        double d6 = 0.0;
        int n = this.cells.size();
        for (int i = 0; i < n; ++i) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
            d6 = Math.max(d6, indexedCell.prefHeight(-1.0));
        }
        double d7 = Math.max(d6, Math.max(this.getCellSize(), ((IndexedCell)this.getSkinnable()).minHeight(-1.0)));
        return d7;
    }

    @Override
    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        this.checkState();
        if (this.getCellSize() < 24.0) {
            return this.getCellSize();
        }
        double d6 = 0.0;
        int n = this.cells.size();
        for (int i = 0; i < n; ++i) {
            IndexedCell indexedCell = (IndexedCell)this.cells.get(i);
            d6 = Math.max(d6, indexedCell.minHeight(-1.0));
        }
        return d6;
    }

    @Override
    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(d, d2, d3, d4, d5);
    }

    protected final void checkState() {
        if (this.isDirty) {
            this.updateCells(true);
            this.isDirty = false;
        } else if (this.updateCells) {
            this.updateCells(false);
            this.updateCells = false;
        }
    }

    private void requestCellUpdate() {
        this.updateCells = true;
        ((IndexedCell)this.getSkinnable()).requestLayout();
        int n = ((IndexedCell)this.getSkinnable()).getIndex();
        int n2 = this.cells.size();
        for (int i = 0; i < n2; ++i) {
            ((IndexedCell)this.cells.get(i)).updateIndex(n);
        }
    }

    private void recreateCells() {
        Reference reference;
        Iterator iterator;
        Object object;
        if (this.cellsMap != null) {
            object = this.cellsMap.values();
            iterator = object.iterator();
            while (iterator.hasNext()) {
                reference = (Reference)iterator.next();
                IndexedCell indexedCell = (IndexedCell)reference.get();
                if (indexedCell == null) continue;
                indexedCell.updateIndex(-1);
                indexedCell.getSkin().dispose();
                indexedCell.setSkin(null);
            }
            this.cellsMap.clear();
        }
        object = this.getVisibleLeafColumns();
        this.cellsMap = new WeakHashMap(object.size());
        this.fullRefreshCounter = 100;
        this.getChildren().clear();
        iterator = object.iterator();
        while (iterator.hasNext()) {
            reference = (TableColumnBase)iterator.next();
            if (this.cellsMap.containsKey(reference)) continue;
            this.createCell((TableColumnBase)reference);
        }
    }

    private R createCell(TableColumnBase tableColumnBase) {
        R r = this.getCell(tableColumnBase);
        this.cellsMap.put(tableColumnBase, new WeakReference<R>(r));
        return r;
    }

    private void fadeOut(Node node) {
        if (node.getOpacity() < 1.0) {
            return;
        }
        if (!DO_ANIMATIONS) {
            node.setOpacity(0.0);
            return;
        }
        FadeTransition fadeTransition = new FadeTransition(FADE_DURATION, node);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    private void fadeIn(Node node) {
        if (node.getOpacity() > 0.0) {
            return;
        }
        if (!DO_ANIMATIONS) {
            node.setOpacity(1.0);
            return;
        }
        FadeTransition fadeTransition = new FadeTransition(FADE_DURATION, node);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }
}

