/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.css.CssMetaData
 *  javafx.css.Styleable
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableProperty
 *  javafx.geometry.HPos
 *  javafx.geometry.VPos
 *  javafx.scene.Node
 *  javafx.scene.control.TreeCell
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeView
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TreeCellBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class TreeCellSkin<T>
extends CellSkinBase<TreeCell<T>, TreeCellBehavior<T>> {
    private static final Map<TreeView<?>, Double> maxDisclosureWidthMap = new WeakHashMap();
    private DoubleProperty indent = null;
    private boolean disclosureNodeDirty = true;
    private TreeItem<?> treeItem;
    private double fixedCellSize;
    private boolean fixedCellSizeEnabled;
    private MultiplePropertyChangeListenerHandler treeItemListener = new MultiplePropertyChangeListenerHandler((Callback<String, Void>)((Callback)string -> {
        if ("EXPANDED".equals(string)) {
            this.updateDisclosureNodeRotation(true);
        }
        return null;
    }));

    public final void setIndent(double d) {
        this.indentProperty().set(d);
    }

    public final double getIndent() {
        return this.indent == null ? 10.0 : this.indent.get();
    }

    public final DoubleProperty indentProperty() {
        if (this.indent == null) {
            this.indent = new StyleableDoubleProperty(10.0){

                public Object getBean() {
                    return TreeCellSkin.this;
                }

                public String getName() {
                    return "indent";
                }

                public CssMetaData<TreeCell<?>, Number> getCssMetaData() {
                    return StyleableProperties.INDENT;
                }
            };
        }
        return this.indent;
    }

    public TreeCellSkin(TreeCell<T> treeCell) {
        super(treeCell, new TreeCellBehavior<T>(treeCell));
        this.fixedCellSize = treeCell.getTreeView().getFixedCellSize();
        this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        this.updateTreeItem();
        this.updateDisclosureNodeRotation(false);
        this.registerChangeListener((ObservableValue<?>)treeCell.treeItemProperty(), "TREE_ITEM");
        this.registerChangeListener((ObservableValue<?>)treeCell.textProperty(), "TEXT");
        this.registerChangeListener((ObservableValue<?>)treeCell.getTreeView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("TREE_ITEM".equals(string)) {
            this.updateTreeItem();
            this.disclosureNodeDirty = true;
            ((TreeCell)this.getSkinnable()).requestLayout();
        } else if ("TEXT".equals(string)) {
            ((TreeCell)this.getSkinnable()).requestLayout();
        } else if ("FIXED_CELL_SIZE".equals(string)) {
            this.fixedCellSize = ((TreeCell)this.getSkinnable()).getTreeView().getFixedCellSize();
            this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
        }
    }

    private void updateDisclosureNodeRotation(boolean bl) {
    }

    private void updateTreeItem() {
        if (this.treeItem != null) {
            this.treeItemListener.unregisterChangeListener((ObservableValue<?>)this.treeItem.expandedProperty());
        }
        this.treeItem = ((TreeCell)this.getSkinnable()).getTreeItem();
        if (this.treeItem != null) {
            this.treeItemListener.registerChangeListener((ObservableValue<?>)this.treeItem.expandedProperty(), "EXPANDED");
        }
        this.updateDisclosureNodeRotation(false);
    }

    private void updateDisclosureNode() {
        if (((TreeCell)this.getSkinnable()).isEmpty()) {
            return;
        }
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        if (node == null) {
            return;
        }
        boolean bl = this.treeItem != null && !this.treeItem.isLeaf();
        node.setVisible(bl);
        if (!bl) {
            this.getChildren().remove((Object)node);
        } else if (node.getParent() == null) {
            this.getChildren().add((Object)node);
            node.toFront();
        } else {
            node.toBack();
        }
        if (node.getScene() != null) {
            node.applyCss();
        }
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        this.updateDisclosureNode();
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        double d5;
        TreeView treeView = ((TreeCell)this.getSkinnable()).getTreeView();
        if (treeView == null) {
            return;
        }
        if (this.disclosureNodeDirty) {
            this.updateDisclosureNode();
            this.disclosureNodeDirty = false;
        }
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        int n = treeView.getTreeItemLevel(this.treeItem);
        if (!treeView.isShowRoot()) {
            --n;
        }
        double d6 = this.getIndent() * (double)n;
        d += d6;
        boolean bl = node != null && this.treeItem != null && !this.treeItem.isLeaf();
        double d7 = d5 = maxDisclosureWidthMap.containsKey((Object)treeView) ? maxDisclosureWidthMap.get((Object)treeView) : 18.0;
        if (bl) {
            if (node == null || node.getScene() == null) {
                this.updateChildren();
            }
            if (node != null) {
                d7 = node.prefWidth(d4);
                if (d7 > d5) {
                    maxDisclosureWidthMap.put(treeView, d7);
                }
                double d8 = node.prefHeight(d7);
                node.resize(d7, d8);
                this.positionInArea(node, d, d2, d7, d8, 0.0, HPos.CENTER, VPos.CENTER);
            }
        }
        int n2 = this.treeItem != null && this.treeItem.getGraphic() == null ? 0 : 3;
        d += d7 + (double)n2;
        d3 -= d6 + d7 + (double)n2;
        Node node2 = ((TreeCell)this.getSkinnable()).getGraphic();
        if (node2 != null && !this.getChildren().contains((Object)node2)) {
            this.getChildren().add((Object)node2);
        }
        this.layoutLabelInArea(d, d2, d3, d4);
    }

    @Override
    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        double d6 = super.computeMinHeight(d, d2, d3, d4, d5);
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        return node == null ? d6 : Math.max(node.minHeight(-1.0), d6);
    }

    @Override
    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        TreeCell treeCell = (TreeCell)this.getSkinnable();
        double d6 = super.computePrefHeight(d, d2, d3, d4, d5);
        Node node = treeCell.getDisclosureNode();
        double d7 = node == null ? d6 : Math.max(node.prefHeight(-1.0), d6);
        return this.snapSize(Math.max(treeCell.getMinHeight(), d7));
    }

    @Override
    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        if (this.fixedCellSizeEnabled) {
            return this.fixedCellSize;
        }
        return super.computeMaxHeight(d, d2, d3, d4, d5);
    }

    @Override
    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        double d6 = super.computePrefWidth(d, d2, d3, d4, d5);
        double d7 = this.snappedLeftInset() + this.snappedRightInset();
        TreeView treeView = ((TreeCell)this.getSkinnable()).getTreeView();
        if (treeView == null) {
            return d7;
        }
        if (this.treeItem == null) {
            return d7;
        }
        d7 = d6;
        int n = treeView.getTreeItemLevel(this.treeItem);
        if (!treeView.isShowRoot()) {
            --n;
        }
        d7 += this.getIndent() * (double)n;
        Node node = ((TreeCell)this.getSkinnable()).getDisclosureNode();
        double d8 = node == null ? 0.0 : node.prefWidth(-1.0);
        double d9 = maxDisclosureWidthMap.containsKey((Object)treeView) ? maxDisclosureWidthMap.get((Object)treeView) : 0.0;
        return d7 += Math.max(d9, d8);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TreeCellSkin.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<TreeCell<?>, Number> INDENT = new CssMetaData<TreeCell<?>, Number>("-fx-indent", SizeConverter.getInstance(), 10.0){

            public boolean isSettable(TreeCell<?> treeCell) {
                DoubleProperty doubleProperty = ((TreeCellSkin)treeCell.getSkin()).indentProperty();
                return doubleProperty == null || !doubleProperty.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(TreeCell<?> treeCell) {
                TreeCellSkin treeCellSkin = (TreeCellSkin)treeCell.getSkin();
                return (StyleableProperty)treeCellSkin.indentProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(CellSkinBase.getClassCssMetaData());
            arrayList.add(INDENT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

