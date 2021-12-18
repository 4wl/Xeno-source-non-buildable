/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.WeakChangeListener
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.control.FocusModel
 *  javafx.scene.control.MultipleSelectionModel
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeView
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TreeCellBehavior;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class TreeViewBehavior<T>
extends BehaviorBase<TreeView<T>> {
    protected static final List<KeyBinding> TREE_VIEW_BINDINGS = new ArrayList<KeyBinding>();
    private boolean isShiftDown = false;
    private boolean isShortcutDown = false;
    private Callback<Boolean, Integer> onScrollPageUp;
    private Callback<Boolean, Integer> onScrollPageDown;
    private Runnable onSelectPreviousRow;
    private Runnable onSelectNextRow;
    private Runnable onMoveToFirstCell;
    private Runnable onMoveToLastCell;
    private Runnable onFocusPreviousRow;
    private Runnable onFocusNextRow;
    private boolean selectionChanging = false;
    private final ListChangeListener<Integer> selectedIndicesListener = change -> {
        while (change.next()) {
            int n;
            if (change.wasReplaced() && TreeCellBehavior.hasDefaultAnchor(this.getControl())) {
                TreeCellBehavior.removeAnchor(this.getControl());
            }
            int n2 = change.wasPermutated() ? change.getTo() - change.getFrom() : 0;
            MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
            if (!this.selectionChanging) {
                if (multipleSelectionModel.isEmpty()) {
                    this.setAnchor(-1);
                } else if (this.hasAnchor() && !multipleSelectionModel.isSelected(this.getAnchor() + n2)) {
                    this.setAnchor(-1);
                }
            }
            if ((n = change.getAddedSize()) <= 0 || this.hasAnchor()) continue;
            List list = change.getAddedSubList();
            int n3 = (Integer)list.get(n - 1);
            this.setAnchor(n3);
        }
    };
    private final ChangeListener<MultipleSelectionModel<TreeItem<T>>> selectionModelListener = new ChangeListener<MultipleSelectionModel<TreeItem<T>>>(){

        public void changed(ObservableValue<? extends MultipleSelectionModel<TreeItem<T>>> observableValue, MultipleSelectionModel<TreeItem<T>> multipleSelectionModel, MultipleSelectionModel<TreeItem<T>> multipleSelectionModel2) {
            if (multipleSelectionModel != null) {
                multipleSelectionModel.getSelectedIndices().removeListener((ListChangeListener)TreeViewBehavior.this.weakSelectedIndicesListener);
            }
            if (multipleSelectionModel2 != null) {
                multipleSelectionModel2.getSelectedIndices().addListener((ListChangeListener)TreeViewBehavior.this.weakSelectedIndicesListener);
            }
        }
    };
    private final WeakListChangeListener<Integer> weakSelectedIndicesListener = new WeakListChangeListener(this.selectedIndicesListener);
    private final WeakChangeListener<MultipleSelectionModel<TreeItem<T>>> weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);

    @Override
    protected String matchActionForEvent(KeyEvent keyEvent) {
        String string = super.matchActionForEvent(keyEvent);
        if (((TreeView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
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
        if ("SelectPreviousRow".equals(string)) {
            this.selectPreviousRow();
        } else if ("SelectNextRow".equals(string)) {
            this.selectNextRow();
        } else if ("SelectFirstRow".equals(string)) {
            this.selectFirstRow();
        } else if ("SelectLastRow".equals(string)) {
            this.selectLastRow();
        } else if ("SelectAllPageUp".equals(string)) {
            this.selectAllPageUp();
        } else if ("SelectAllPageDown".equals(string)) {
            this.selectAllPageDown();
        } else if ("SelectAllToFirstRow".equals(string)) {
            this.selectAllToFirstRow();
        } else if ("SelectAllToLastRow".equals(string)) {
            this.selectAllToLastRow();
        } else if ("AlsoSelectNextRow".equals(string)) {
            this.alsoSelectNextRow();
        } else if ("AlsoSelectPreviousRow".equals(string)) {
            this.alsoSelectPreviousRow();
        } else if ("ClearSelection".equals(string)) {
            this.clearSelection();
        } else if ("SelectAll".equals(string)) {
            this.selectAll();
        } else if ("ScrollUp".equals(string)) {
            this.scrollUp();
        } else if ("ScrollDown".equals(string)) {
            this.scrollDown();
        } else if ("ExpandRow".equals(string)) {
            this.expandRow();
        } else if ("CollapseRow".equals(string)) {
            this.collapseRow();
        } else if ("ExpandAll".equals(string)) {
            this.expandAll();
        } else if ("Edit".equals(string)) {
            this.edit();
        } else if ("CancelEdit".equals(string)) {
            this.cancelEdit();
        } else if ("FocusFirstRow".equals(string)) {
            this.focusFirstRow();
        } else if ("FocusLastRow".equals(string)) {
            this.focusLastRow();
        } else if ("toggleFocusOwnerSelection".equals(string)) {
            this.toggleFocusOwnerSelection();
        } else if ("SelectAllToFocus".equals(string)) {
            this.selectAllToFocus(false);
        } else if ("SelectAllToFocusAndSetAnchor".equals(string)) {
            this.selectAllToFocus(true);
        } else if ("FocusPageUp".equals(string)) {
            this.focusPageUp();
        } else if ("FocusPageDown".equals(string)) {
            this.focusPageDown();
        } else if ("FocusPreviousRow".equals(string)) {
            this.focusPreviousRow();
        } else if ("FocusNextRow".equals(string)) {
            this.focusNextRow();
        } else if ("DiscontinuousSelectNextRow".equals(string)) {
            this.discontinuousSelectNextRow();
        } else if ("DiscontinuousSelectPreviousRow".equals(string)) {
            this.discontinuousSelectPreviousRow();
        } else if ("DiscontinuousSelectPageUp".equals(string)) {
            this.discontinuousSelectPageUp();
        } else if ("DiscontinuousSelectPageDown".equals(string)) {
            this.discontinuousSelectPageDown();
        } else if ("DiscontinuousSelectAllToLastRow".equals(string)) {
            this.discontinuousSelectAllToLastRow();
        } else if ("DiscontinuousSelectAllToFirstRow".equals(string)) {
            this.discontinuousSelectAllToFirstRow();
        } else {
            super.callAction(string);
        }
    }

    @Override
    protected void callActionForEvent(KeyEvent keyEvent) {
        this.isShiftDown = keyEvent.getEventType() == KeyEvent.KEY_PRESSED && keyEvent.isShiftDown();
        this.isShortcutDown = keyEvent.getEventType() == KeyEvent.KEY_PRESSED && keyEvent.isShortcutDown();
        super.callActionForEvent(keyEvent);
    }

    public void setOnScrollPageUp(Callback<Boolean, Integer> callback) {
        this.onScrollPageUp = callback;
    }

    public void setOnScrollPageDown(Callback<Boolean, Integer> callback) {
        this.onScrollPageDown = callback;
    }

    public void setOnSelectPreviousRow(Runnable runnable) {
        this.onSelectPreviousRow = runnable;
    }

    public void setOnSelectNextRow(Runnable runnable) {
        this.onSelectNextRow = runnable;
    }

    public void setOnMoveToFirstCell(Runnable runnable) {
        this.onMoveToFirstCell = runnable;
    }

    public void setOnMoveToLastCell(Runnable runnable) {
        this.onMoveToLastCell = runnable;
    }

    public void setOnFocusPreviousRow(Runnable runnable) {
        this.onFocusPreviousRow = runnable;
    }

    public void setOnFocusNextRow(Runnable runnable) {
        this.onFocusNextRow = runnable;
    }

    public TreeViewBehavior(TreeView<T> treeView) {
        super(treeView, TREE_VIEW_BINDINGS);
        ((TreeView)this.getControl()).selectionModelProperty().addListener(this.weakSelectionModelListener);
        if (treeView.getSelectionModel() != null) {
            treeView.getSelectionModel().getSelectedIndices().addListener(this.weakSelectedIndicesListener);
        }
    }

    @Override
    public void dispose() {
        TreeCellBehavior.removeAnchor(this.getControl());
        super.dispose();
    }

    private void setAnchor(int n) {
        TreeCellBehavior.setAnchor(this.getControl(), n < 0 ? null : Integer.valueOf(n), false);
    }

    private int getAnchor() {
        return TreeCellBehavior.getAnchor(this.getControl(), ((TreeView)this.getControl()).getFocusModel().getFocusedIndex());
    }

    private boolean hasAnchor() {
        return TreeCellBehavior.hasNonDefaultAnchor(this.getControl());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        if (!mouseEvent.isShiftDown()) {
            int n = ((TreeView)this.getControl()).getSelectionModel().getSelectedIndex();
            this.setAnchor(n);
        }
        if (!((TreeView)this.getControl()).isFocused() && ((TreeView)this.getControl()).isFocusTraversable()) {
            ((TreeView)this.getControl()).requestFocus();
        }
    }

    private void clearSelection() {
        ((TreeView)this.getControl()).getSelectionModel().clearSelection();
    }

    private void scrollUp() {
        int n = -1;
        if (this.onScrollPageUp != null) {
            n = (Integer)this.onScrollPageUp.call((Object)false);
        }
        if (n == -1) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        multipleSelectionModel.clearAndSelect(n);
    }

    private void scrollDown() {
        int n = -1;
        if (this.onScrollPageDown != null) {
            n = (Integer)this.onScrollPageDown.call((Object)false);
        }
        if (n == -1) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        multipleSelectionModel.clearAndSelect(n);
    }

    private void focusFirstRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(0);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void focusLastRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(((TreeView)this.getControl()).getExpandedItemCount() - 1);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void focusPreviousRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        focusModel.focusPrevious();
        if (!this.isShortcutDown || this.getAnchor() == -1) {
            this.setAnchor(focusModel.getFocusedIndex());
        }
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    private void focusNextRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        focusModel.focusNext();
        if (!this.isShortcutDown || this.getAnchor() == -1) {
            this.setAnchor(focusModel.getFocusedIndex());
        }
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    private void focusPageUp() {
        int n = (Integer)this.onScrollPageUp.call((Object)true);
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(n);
    }

    private void focusPageDown() {
        int n = (Integer)this.onScrollPageDown.call((Object)true);
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(n);
    }

    private void alsoSelectPreviousRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        if (this.isShiftDown && this.getAnchor() != -1) {
            int n = focusModel.getFocusedIndex() - 1;
            if (n < 0) {
                return;
            }
            int n2 = this.getAnchor();
            if (!this.hasAnchor()) {
                this.setAnchor(focusModel.getFocusedIndex());
            }
            if (multipleSelectionModel.getSelectedIndices().size() > 1) {
                this.clearSelectionOutsideRange(n2, n);
            }
            if (n2 > n) {
                multipleSelectionModel.selectRange(n2, n - 1);
            } else {
                multipleSelectionModel.selectRange(n2, n + 1);
            }
        } else {
            multipleSelectionModel.selectPrevious();
        }
        this.onSelectPreviousRow.run();
    }

    private void alsoSelectNextRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        if (this.isShiftDown && this.getAnchor() != -1) {
            int n = focusModel.getFocusedIndex() + 1;
            int n2 = this.getAnchor();
            if (!this.hasAnchor()) {
                this.setAnchor(focusModel.getFocusedIndex());
            }
            if (multipleSelectionModel.getSelectedIndices().size() > 1) {
                this.clearSelectionOutsideRange(n2, n);
            }
            if (n2 > n) {
                multipleSelectionModel.selectRange(n2, n - 1);
            } else {
                multipleSelectionModel.selectRange(n2, n + 1);
            }
        } else {
            multipleSelectionModel.selectNext();
        }
        this.onSelectNextRow.run();
    }

    private void clearSelectionOutsideRange(int n, int n2) {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        int n3 = Math.min(n, n2);
        int n4 = Math.max(n, n2);
        ArrayList arrayList = new ArrayList(multipleSelectionModel.getSelectedIndices());
        this.selectionChanging = true;
        for (int i = 0; i < arrayList.size(); ++i) {
            int n5 = (Integer)arrayList.get(i);
            if (n5 >= n3 && n5 <= n4) continue;
            multipleSelectionModel.clearSelection(n5);
        }
        this.selectionChanging = false;
    }

    private void selectPreviousRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (n <= 0) {
            return;
        }
        this.setAnchor(n - 1);
        ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(n - 1);
        this.onSelectPreviousRow.run();
    }

    private void selectNextRow() {
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (n == ((TreeView)this.getControl()).getExpandedItemCount() - 1) {
            return;
        }
        this.setAnchor(n + 1);
        ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(n + 1);
        this.onSelectNextRow.run();
    }

    private void selectFirstRow() {
        if (((TreeView)this.getControl()).getExpandedItemCount() > 0) {
            ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(0);
            if (this.onMoveToFirstCell != null) {
                this.onMoveToFirstCell.run();
            }
        }
    }

    private void selectLastRow() {
        ((TreeView)this.getControl()).getSelectionModel().clearAndSelect(((TreeView)this.getControl()).getExpandedItemCount() - 1);
        this.onMoveToLastCell.run();
    }

    private void selectAllToFirstRow() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (this.isShiftDown) {
            n = this.hasAnchor() ? this.getAnchor() : n;
        }
        multipleSelectionModel.clearSelection();
        multipleSelectionModel.selectRange(n, -1);
        focusModel.focus(0);
        if (this.isShiftDown) {
            this.setAnchor(n);
        }
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void selectAllToLastRow() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (this.isShiftDown) {
            n = this.hasAnchor() ? this.getAnchor() : n;
        }
        multipleSelectionModel.clearSelection();
        multipleSelectionModel.selectRange(n, ((TreeView)this.getControl()).getExpandedItemCount());
        if (this.isShiftDown) {
            this.setAnchor(n);
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void selectAll() {
        ((TreeView)this.getControl()).getSelectionModel().selectAll();
    }

    private void selectAllPageUp() {
        int n;
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n2 = focusModel.getFocusedIndex();
        if (this.isShiftDown) {
            n2 = this.getAnchor() == -1 ? n2 : this.getAnchor();
            this.setAnchor(n2);
        }
        int n3 = n2 < (n = ((Integer)this.onScrollPageUp.call((Object)false)).intValue()) ? 1 : -1;
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        this.selectionChanging = true;
        if (multipleSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            multipleSelectionModel.select(n);
        } else {
            multipleSelectionModel.clearSelection();
            multipleSelectionModel.selectRange(n2, n + n3);
        }
        this.selectionChanging = false;
    }

    private void selectAllPageDown() {
        int n;
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n2 = focusModel.getFocusedIndex();
        if (this.isShiftDown) {
            n2 = this.getAnchor() == -1 ? n2 : this.getAnchor();
            this.setAnchor(n2);
        }
        int n3 = n2 < (n = ((Integer)this.onScrollPageDown.call((Object)false)).intValue()) ? 1 : -1;
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        this.selectionChanging = true;
        if (multipleSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            multipleSelectionModel.select(n);
        } else {
            multipleSelectionModel.clearSelection();
            multipleSelectionModel.selectRange(n2, n + n3);
        }
        this.selectionChanging = false;
    }

    private void selectAllToFocus(boolean bl) {
        TreeView treeView = (TreeView)this.getControl();
        if (treeView.getEditingItem() != null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = treeView.getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = treeView.getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        int n2 = this.getAnchor();
        multipleSelectionModel.clearSelection();
        int n3 = n2;
        int n4 = n2 > n ? n - 1 : n + 1;
        multipleSelectionModel.selectRange(n3, n4);
        this.setAnchor(bl ? n : n2);
    }

    private void expandRow() {
        Callback callback = treeItem -> ((TreeView)this.getControl()).getRow(treeItem);
        TreeViewBehavior.expandRow(((TreeView)this.getControl()).getSelectionModel(), callback);
    }

    private void expandAll() {
        TreeViewBehavior.expandAll(((TreeView)this.getControl()).getRoot());
    }

    private void collapseRow() {
        TreeView treeView = (TreeView)this.getControl();
        TreeViewBehavior.collapseRow(treeView.getSelectionModel(), treeView.getRoot(), treeView.isShowRoot());
    }

    static <T> void expandRow(MultipleSelectionModel<TreeItem<T>> multipleSelectionModel, Callback<TreeItem<T>, Integer> callback) {
        if (multipleSelectionModel == null) {
            return;
        }
        TreeItem treeItem = (TreeItem)multipleSelectionModel.getSelectedItem();
        if (treeItem == null || treeItem.isLeaf()) {
            return;
        }
        if (treeItem.isExpanded()) {
            ObservableList observableList = treeItem.getChildren();
            if (!observableList.isEmpty()) {
                multipleSelectionModel.clearAndSelect(((Integer)callback.call(observableList.get(0))).intValue());
            }
        } else {
            treeItem.setExpanded(true);
        }
    }

    static <T> void expandAll(TreeItem<T> treeItem) {
        if (treeItem == null) {
            return;
        }
        treeItem.setExpanded(true);
        TreeViewBehavior.expandChildren(treeItem);
    }

    private static <T> void expandChildren(TreeItem<T> treeItem) {
        if (treeItem == null) {
            return;
        }
        ObservableList observableList = treeItem.getChildren();
        if (observableList == null) {
            return;
        }
        for (int i = 0; i < observableList.size(); ++i) {
            TreeItem treeItem2 = (TreeItem)observableList.get(i);
            if (treeItem2 == null || treeItem2.isLeaf()) continue;
            treeItem2.setExpanded(true);
            TreeViewBehavior.expandChildren(treeItem2);
        }
    }

    static <T> void collapseRow(MultipleSelectionModel<TreeItem<T>> multipleSelectionModel, TreeItem<T> treeItem, boolean bl) {
        if (multipleSelectionModel == null) {
            return;
        }
        TreeItem treeItem2 = (TreeItem)multipleSelectionModel.getSelectedItem();
        if (treeItem2 == null) {
            return;
        }
        if (treeItem == null) {
            return;
        }
        if (!bl && !treeItem2.isExpanded() && treeItem.equals((Object)treeItem2.getParent())) {
            return;
        }
        if (treeItem.equals((Object)treeItem2) && (!treeItem.isExpanded() || treeItem.getChildren().isEmpty())) {
            return;
        }
        if (treeItem2.isLeaf() || !treeItem2.isExpanded()) {
            multipleSelectionModel.clearSelection();
            multipleSelectionModel.select((Object)treeItem2.getParent());
        } else {
            treeItem2.setExpanded(false);
        }
    }

    private void cancelEdit() {
        ((TreeView)this.getControl()).edit(null);
    }

    private void edit() {
        TreeItem treeItem = (TreeItem)((TreeView)this.getControl()).getSelectionModel().getSelectedItem();
        if (treeItem == null) {
            return;
        }
        ((TreeView)this.getControl()).edit(treeItem);
    }

    private void toggleFocusOwnerSelection() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (multipleSelectionModel.isSelected(n)) {
            multipleSelectionModel.clearSelection(n);
            focusModel.focus(n);
        } else {
            multipleSelectionModel.select(n);
        }
        this.setAnchor(n);
    }

    private void discontinuousSelectPreviousRow() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        if (multipleSelectionModel.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectPreviousRow();
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        int n2 = n - 1;
        if (n2 < 0) {
            return;
        }
        int n3 = n;
        if (this.isShiftDown) {
            n3 = this.getAnchor() == -1 ? n : this.getAnchor();
        }
        multipleSelectionModel.selectRange(n2, n3 + 1);
        focusModel.focus(n2);
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    private void discontinuousSelectNextRow() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        if (multipleSelectionModel.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectNextRow();
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        int n2 = n + 1;
        if (n2 >= ((TreeView)this.getControl()).getExpandedItemCount()) {
            return;
        }
        int n3 = n;
        if (this.isShiftDown) {
            n3 = this.getAnchor() == -1 ? n : this.getAnchor();
        }
        multipleSelectionModel.selectRange(n3, n2 + 1);
        focusModel.focus(n2);
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    private void discontinuousSelectPageUp() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = this.getAnchor();
        int n2 = (Integer)this.onScrollPageUp.call((Object)false);
        multipleSelectionModel.selectRange(n, n2 - 1);
    }

    private void discontinuousSelectPageDown() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = this.getAnchor();
        int n2 = (Integer)this.onScrollPageDown.call((Object)false);
        multipleSelectionModel.selectRange(n, n2 + 1);
    }

    private void discontinuousSelectAllToFirstRow() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        multipleSelectionModel.selectRange(0, n);
        focusModel.focus(0);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void discontinuousSelectAllToLastRow() {
        MultipleSelectionModel multipleSelectionModel = ((TreeView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((TreeView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex() + 1;
        multipleSelectionModel.selectRange(n, ((TreeView)this.getControl()).getExpandedItemCount());
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    static {
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectAllToLastRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocus").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "FocusFirstRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "FocusLastRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection"));
        if (PlatformUtil.isMac()) {
            TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl().shortcut());
        } else {
            TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl());
        }
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.A, "SelectAll").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "FocusPreviousRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "FocusNextRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "CollapseRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "CollapseRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ExpandRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "ExpandRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.MULTIPLY, "ExpandAll"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ADD, "ExpandRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SUBTRACT, "CollapseRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "SelectPreviousRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "SelectPreviousRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "SelectNextRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "SelectNextRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "AlsoSelectPreviousRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "AlsoSelectPreviousRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "AlsoSelectNextRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "AlsoSelectNextRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Edit"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Edit"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
    }
}

