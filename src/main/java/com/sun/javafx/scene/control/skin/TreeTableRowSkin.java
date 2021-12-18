/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.css.CssMetaData
 *  javafx.css.Styleable
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableProperty
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeTableCell
 *  javafx.scene.control.TreeTableColumn
 *  javafx.scene.control.TreeTablePosition
 *  javafx.scene.control.TreeTableRow
 *  javafx.scene.control.TreeTableView
 *  javafx.scene.control.TreeTableView$TreeTableViewFocusModel
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TreeTableRowBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;
import com.sun.javafx.scene.control.skin.TableRowSkinBase;
import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

public class TreeTableRowSkin<T>
extends TableRowSkinBase<TreeItem<T>, TreeTableRow<T>, TreeTableRowBehavior<T>, TreeTableCell<T, ?>> {
    private SimpleObjectProperty<ObservableList<TreeItem<T>>> itemsProperty;
    private TreeItem<?> treeItem;
    private boolean disclosureNodeDirty = true;
    private Node graphic;
    private TreeTableViewSkin treeTableViewSkin;
    private boolean childrenDirty = false;
    private MultiplePropertyChangeListenerHandler treeItemListener = new MultiplePropertyChangeListenerHandler((Callback<String, Void>)((Callback)string -> {
        if ("GRAPHIC".equals(string)) {
            this.disclosureNodeDirty = true;
            ((TreeTableRow)this.getSkinnable()).requestLayout();
        }
        return null;
    }));
    private DoubleProperty indent = null;

    public TreeTableRowSkin(TreeTableRow<T> treeTableRow) {
        super(treeTableRow, new TreeTableRowBehavior<T>(treeTableRow));
        super.init(treeTableRow);
        this.updateTreeItem();
        this.updateTableViewSkin();
        this.registerChangeListener((ObservableValue<?>)treeTableRow.treeTableViewProperty(), "TREE_TABLE_VIEW");
        this.registerChangeListener((ObservableValue<?>)treeTableRow.indexProperty(), "INDEX");
        this.registerChangeListener((ObservableValue<?>)treeTableRow.treeItemProperty(), "TREE_ITEM");
        this.registerChangeListener((ObservableValue<?>)treeTableRow.getTreeTableView().treeColumnProperty(), "TREE_COLUMN");
    }

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
                    return TreeTableRowSkin.this;
                }

                public String getName() {
                    return "indent";
                }

                public CssMetaData<TreeTableRow<?>, Number> getCssMetaData() {
                    return StyleableProperties.INDENT;
                }
            };
        }
        return this.indent;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("TREE_ABLE_VIEW".equals(string)) {
            this.updateTableViewSkin();
        } else if ("INDEX".equals(string)) {
            this.updateCells = true;
        } else if ("TREE_ITEM".equals(string)) {
            this.updateTreeItem();
            this.isDirty = true;
        } else if ("TREE_COLUMN".equals(string)) {
            this.isDirty = true;
            ((TreeTableRow)this.getSkinnable()).requestLayout();
        }
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();
        this.updateDisclosureNodeAndGraphic();
        if (this.childrenDirty) {
            this.childrenDirty = false;
            if (this.cells.isEmpty()) {
                this.getChildren().clear();
            } else {
                this.getChildren().addAll((Collection)this.cells);
            }
        }
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        Node node;
        if (this.disclosureNodeDirty) {
            this.updateDisclosureNodeAndGraphic();
            this.disclosureNodeDirty = false;
        }
        if ((node = this.getDisclosureNode()) != null && node.getScene() == null) {
            this.updateDisclosureNodeAndGraphic();
        }
        super.layoutChildren(d, d2, d3, d4);
    }

    @Override
    protected TreeTableCell<T, ?> getCell(TableColumnBase tableColumnBase) {
        TreeTableColumn treeTableColumn = (TreeTableColumn)tableColumnBase;
        TreeTableCell treeTableCell = (TreeTableCell)treeTableColumn.getCellFactory().call((Object)treeTableColumn);
        treeTableCell.updateTreeTableColumn(treeTableColumn);
        treeTableCell.updateTreeTableView(treeTableColumn.getTreeTableView());
        return treeTableCell;
    }

    @Override
    protected void updateCells(boolean bl) {
        super.updateCells(bl);
        if (bl) {
            this.childrenDirty = true;
            this.updateChildren();
        }
    }

    @Override
    protected boolean isIndentationRequired() {
        return true;
    }

    @Override
    protected TableColumnBase getTreeColumn() {
        return ((TreeTableRow)this.getSkinnable()).getTreeTableView().getTreeColumn();
    }

    @Override
    protected int getIndentationLevel(TreeTableRow<T> treeTableRow) {
        return treeTableRow.getTreeTableView().getTreeItemLevel(treeTableRow.getTreeItem());
    }

    @Override
    protected double getIndentationPerLevel() {
        return this.getIndent();
    }

    @Override
    protected Node getDisclosureNode() {
        return ((TreeTableRow)this.getSkinnable()).getDisclosureNode();
    }

    @Override
    protected boolean isDisclosureNodeVisible() {
        return this.getDisclosureNode() != null && this.treeItem != null && !this.treeItem.isLeaf();
    }

    @Override
    protected boolean isShowRoot() {
        return ((TreeTableRow)this.getSkinnable()).getTreeTableView().isShowRoot();
    }

    @Override
    protected ObservableList<TreeTableColumn<T, ?>> getVisibleLeafColumns() {
        return ((TreeTableRow)this.getSkinnable()).getTreeTableView().getVisibleLeafColumns();
    }

    @Override
    protected void updateCell(TreeTableCell<T, ?> treeTableCell, TreeTableRow<T> treeTableRow) {
        treeTableCell.updateTreeTableRow(treeTableRow);
    }

    @Override
    protected boolean isColumnPartiallyOrFullyVisible(TableColumnBase tableColumnBase) {
        return this.treeTableViewSkin == null ? false : this.treeTableViewSkin.isColumnPartiallyOrFullyVisible(tableColumnBase);
    }

    @Override
    protected TreeTableColumn<T, ?> getTableColumnBase(TreeTableCell treeTableCell) {
        return treeTableCell.getTableColumn();
    }

    @Override
    protected ObjectProperty<Node> graphicProperty() {
        TreeTableRow treeTableRow = (TreeTableRow)this.getSkinnable();
        if (treeTableRow == null) {
            return null;
        }
        if (this.treeItem == null) {
            return null;
        }
        return this.treeItem.graphicProperty();
    }

    @Override
    protected Control getVirtualFlowOwner() {
        return ((TreeTableRow)this.getSkinnable()).getTreeTableView();
    }

    @Override
    protected DoubleProperty fixedCellSizeProperty() {
        return ((TreeTableRow)this.getSkinnable()).getTreeTableView().fixedCellSizeProperty();
    }

    private void updateTreeItem() {
        if (this.treeItem != null) {
            this.treeItemListener.unregisterChangeListener((ObservableValue<?>)this.treeItem.expandedProperty());
            this.treeItemListener.unregisterChangeListener((ObservableValue<?>)this.treeItem.graphicProperty());
        }
        this.treeItem = ((TreeTableRow)this.getSkinnable()).getTreeItem();
        if (this.treeItem != null) {
            this.treeItemListener.registerChangeListener((ObservableValue<?>)this.treeItem.graphicProperty(), "GRAPHIC");
        }
    }

    private void updateDisclosureNodeAndGraphic() {
        Node node;
        Node node2;
        if (((TreeTableRow)this.getSkinnable()).isEmpty()) {
            return;
        }
        ObjectProperty<Node> objectProperty = this.graphicProperty();
        Node node3 = node2 = objectProperty == null ? null : (Node)objectProperty.get();
        if (node2 != null) {
            if (node2 != this.graphic) {
                this.getChildren().remove((Object)this.graphic);
            }
            if (!this.getChildren().contains((Object)node2)) {
                this.getChildren().add((Object)node2);
                this.graphic = node2;
            }
        }
        if ((node = ((TreeTableRow)this.getSkinnable()).getDisclosureNode()) != null) {
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
    }

    private void updateTableViewSkin() {
        TreeTableView treeTableView = ((TreeTableRow)this.getSkinnable()).getTreeTableView();
        if (treeTableView.getSkin() instanceof TreeTableViewSkin) {
            this.treeTableViewSkin = (TreeTableViewSkin)treeTableView.getSkin();
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TreeTableRowSkin.getClassCssMetaData();
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        TreeTableView treeTableView = ((TreeTableRow)this.getSkinnable()).getTreeTableView();
        switch (accessibleAttribute) {
            case SELECTED_ITEMS: {
                ArrayList<TreeTableCell> arrayList = new ArrayList<TreeTableCell>();
                int n = ((TreeTableRow)this.getSkinnable()).getIndex();
                Iterator iterator = treeTableView.getSelectionModel().getSelectedCells().iterator();
                if (iterator.hasNext()) {
                    TreeTablePosition treeTablePosition = (TreeTablePosition)iterator.next();
                    if (treeTablePosition.getRow() == n) {
                        TreeTableCell treeTableCell;
                        TreeTableColumn treeTableColumn = treeTablePosition.getTableColumn();
                        if (treeTableColumn == null) {
                            treeTableColumn = treeTableView.getVisibleLeafColumn(0);
                        }
                        if ((treeTableCell = (TreeTableCell)((Reference)this.cellsMap.get((Object)treeTableColumn)).get()) != null) {
                            arrayList.add(treeTableCell);
                        }
                    }
                    return FXCollections.observableArrayList(arrayList);
                }
            }
            case CELL_AT_ROW_COLUMN: {
                int n = (Integer)arrobject[1];
                TreeTableColumn treeTableColumn = treeTableView.getVisibleLeafColumn(n);
                if (this.cellsMap.containsKey((Object)treeTableColumn)) {
                    return ((Reference)this.cellsMap.get((Object)treeTableColumn)).get();
                }
                return null;
            }
            case FOCUS_ITEM: {
                TreeTableView.TreeTableViewFocusModel treeTableViewFocusModel = treeTableView.getFocusModel();
                TreeTablePosition treeTablePosition = treeTableViewFocusModel.getFocusedCell();
                TreeTableColumn treeTableColumn = treeTablePosition.getTableColumn();
                if (treeTableColumn == null) {
                    treeTableColumn = treeTableView.getVisibleLeafColumn(0);
                }
                if (this.cellsMap.containsKey((Object)treeTableColumn)) {
                    return ((Reference)this.cellsMap.get((Object)treeTableColumn)).get();
                }
                return null;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    private static class StyleableProperties {
        private static final CssMetaData<TreeTableRow<?>, Number> INDENT = new CssMetaData<TreeTableRow<?>, Number>("-fx-indent", SizeConverter.getInstance(), 10.0){

            public boolean isSettable(TreeTableRow<?> treeTableRow) {
                DoubleProperty doubleProperty = ((TreeTableRowSkin)treeTableRow.getSkin()).indentProperty();
                return doubleProperty == null || !doubleProperty.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(TreeTableRow<?> treeTableRow) {
                TreeTableRowSkin treeTableRowSkin = (TreeTableRowSkin)treeTableRow.getSkin();
                return (StyleableProperty)treeTableRowSkin.indentProperty();
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

