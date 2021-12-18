/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.WeakChangeListener
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TableFocusModel
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeTableColumn
 *  javafx.scene.control.TreeTablePosition
 *  javafx.scene.control.TreeTableView
 *  javafx.scene.control.TreeTableView$TreeTableViewSelectionModel
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TableViewBehaviorBase;
import com.sun.javafx.scene.control.behavior.TreeViewBehavior;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class TreeTableViewBehavior<T>
extends TableViewBehaviorBase<TreeTableView<T>, TreeItem<T>, TreeTableColumn<T, ?>> {
    protected static final List<KeyBinding> TREE_TABLE_VIEW_BINDINGS = new ArrayList<KeyBinding>();
    private final ChangeListener<TreeTableView.TreeTableViewSelectionModel<T>> selectionModelListener = (observableValue, treeTableViewSelectionModel, treeTableViewSelectionModel2) -> {
        if (treeTableViewSelectionModel != null) {
            treeTableViewSelectionModel.getSelectedCells().removeListener((ListChangeListener)this.weakSelectedCellsListener);
        }
        if (treeTableViewSelectionModel2 != null) {
            treeTableViewSelectionModel2.getSelectedCells().addListener((ListChangeListener)this.weakSelectedCellsListener);
        }
    };
    private final WeakChangeListener<TreeTableView.TreeTableViewSelectionModel<T>> weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);

    @Override
    protected String matchActionForEvent(KeyEvent keyEvent) {
        String string = super.matchActionForEvent(keyEvent);
        if (((TreeTableView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            if ("CollapseRow".equals(string) && (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.KP_LEFT)) {
                string = "ExpandRow";
            } else if ("ExpandRow".equals(string) && (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.KP_RIGHT)) {
                string = "CollapseRow";
            }
        }
        return string;
    }

    @Override
    protected void callAction(String string) {
        if ("ExpandRow".equals(string)) {
            this.rightArrowPressed();
        } else if ("CollapseRow".equals(string)) {
            this.leftArrowPressed();
        } else if ("ExpandAll".equals(string)) {
            this.expandAll();
        } else {
            super.callAction(string);
        }
    }

    public TreeTableViewBehavior(TreeTableView<T> treeTableView) {
        super(treeTableView, TREE_TABLE_VIEW_BINDINGS);
        treeTableView.selectionModelProperty().addListener(this.weakSelectionModelListener);
        if (this.getSelectionModel() != null) {
            treeTableView.getSelectionModel().getSelectedCells().addListener(this.selectedCellsListener);
        }
    }

    @Override
    protected int getItemCount() {
        return ((TreeTableView)this.getControl()).getExpandedItemCount();
    }

    @Override
    protected TableFocusModel getFocusModel() {
        return ((TreeTableView)this.getControl()).getFocusModel();
    }

    @Override
    protected TableSelectionModel<TreeItem<T>> getSelectionModel() {
        return ((TreeTableView)this.getControl()).getSelectionModel();
    }

    @Override
    protected ObservableList<TreeTablePosition<T, ?>> getSelectedCells() {
        return ((TreeTableView)this.getControl()).getSelectionModel().getSelectedCells();
    }

    @Override
    protected TablePositionBase getFocusedCell() {
        return ((TreeTableView)this.getControl()).getFocusModel().getFocusedCell();
    }

    @Override
    protected int getVisibleLeafIndex(TableColumnBase tableColumnBase) {
        return ((TreeTableView)this.getControl()).getVisibleLeafIndex((TreeTableColumn)tableColumnBase);
    }

    protected TreeTableColumn getVisibleLeafColumn(int n) {
        return ((TreeTableView)this.getControl()).getVisibleLeafColumn(n);
    }

    @Override
    protected void editCell(int n, TableColumnBase tableColumnBase) {
        ((TreeTableView)this.getControl()).edit(n, (TreeTableColumn)tableColumnBase);
    }

    @Override
    protected ObservableList<TreeTableColumn<T, ?>> getVisibleLeafColumns() {
        return ((TreeTableView)this.getControl()).getVisibleLeafColumns();
    }

    @Override
    protected TablePositionBase<TreeTableColumn<T, ?>> getTablePosition(int n, TableColumnBase<TreeItem<T>, ?> tableColumnBase) {
        return new TreeTablePosition((TreeTableView)this.getControl(), n, (TreeTableColumn)tableColumnBase);
    }

    @Override
    protected void selectAllToFocus(boolean bl) {
        if (((TreeTableView)this.getControl()).getEditingCell() != null) {
            return;
        }
        super.selectAllToFocus(bl);
    }

    private void rightArrowPressed() {
        if (((TreeTableView)this.getControl()).getSelectionModel().isCellSelectionEnabled()) {
            if (this.isRTL()) {
                this.selectLeftCell();
            } else {
                this.selectRightCell();
            }
        } else {
            this.expandRow();
        }
    }

    private void leftArrowPressed() {
        if (((TreeTableView)this.getControl()).getSelectionModel().isCellSelectionEnabled()) {
            if (this.isRTL()) {
                this.selectRightCell();
            } else {
                this.selectLeftCell();
            }
        } else {
            this.collapseRow();
        }
    }

    private void expandRow() {
        Callback callback = treeItem -> ((TreeTableView)this.getControl()).getRow(treeItem);
        TreeViewBehavior.expandRow(((TreeTableView)this.getControl()).getSelectionModel(), callback);
    }

    private void expandAll() {
        TreeViewBehavior.expandAll(((TreeTableView)this.getControl()).getRoot());
    }

    private void collapseRow() {
        TreeTableView treeTableView = (TreeTableView)this.getControl();
        TreeViewBehavior.collapseRow(treeTableView.getSelectionModel(), treeTableView.getRoot(), treeTableView.isShowRoot());
    }

    static {
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "CollapseRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "CollapseRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ExpandRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "ExpandRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.MULTIPLY, "ExpandAll"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ADD, "ExpandRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SUBTRACT, "CollapseRow"));
        TREE_TABLE_VIEW_BINDINGS.addAll(TABLE_VIEW_BINDINGS);
    }
}

