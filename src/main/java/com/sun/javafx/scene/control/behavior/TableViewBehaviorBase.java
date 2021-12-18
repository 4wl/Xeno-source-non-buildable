/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.WeakListChangeListener
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.control.Control
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.control.TableColumnBase
 *  javafx.scene.control.TableFocusModel
 *  javafx.scene.control.TablePositionBase
 *  javafx.scene.control.TableSelectionModel
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.SizeLimitedList;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public abstract class TableViewBehaviorBase<C extends Control, T, TC extends TableColumnBase<T, ?>>
extends BehaviorBase<C> {
    protected static final List<KeyBinding> TABLE_VIEW_BINDINGS = new ArrayList<KeyBinding>();
    protected boolean isShortcutDown = false;
    protected boolean isShiftDown = false;
    private boolean selectionPathDeviated = false;
    protected boolean selectionChanging = false;
    private final SizeLimitedList<TablePositionBase> selectionHistory = new SizeLimitedList(10);
    protected final ListChangeListener<TablePositionBase> selectedCellsListener = change -> {
        block0: while (change.next()) {
            if (change.wasReplaced() && TreeTableCellBehavior.hasDefaultAnchor(this.getControl())) {
                TreeTableCellBehavior.removeAnchor(this.getControl());
            }
            if (!change.wasAdded()) continue;
            TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
            if (tableSelectionModel == null) {
                return;
            }
            TablePositionBase tablePositionBase = this.getAnchor();
            boolean bl = tableSelectionModel.isCellSelectionEnabled();
            int n = change.getAddedSize();
            List list = change.getAddedSubList();
            for (TablePositionBase tablePositionBase2 : list) {
                if (this.selectionHistory.contains(tablePositionBase2)) continue;
                this.selectionHistory.add(tablePositionBase2);
            }
            if (n > 0 && !this.hasAnchor()) {
                TablePositionBase tablePositionBase3 = (TablePositionBase)list.get(n - 1);
                this.setAnchor(tablePositionBase3);
            }
            if (tablePositionBase == null || !bl || this.selectionPathDeviated) continue;
            for (int i = 0; i < n; ++i) {
                TablePositionBase tablePositionBase2;
                tablePositionBase2 = (TablePositionBase)list.get(i);
                if (tablePositionBase.getRow() == -1 || tablePositionBase2.getRow() == tablePositionBase.getRow() || tablePositionBase2.getColumn() == tablePositionBase.getColumn()) continue;
                this.setSelectionPathDeviated(true);
                continue block0;
            }
        }
    };
    protected final WeakListChangeListener<TablePositionBase> weakSelectedCellsListener = new WeakListChangeListener(this.selectedCellsListener);
    private Callback<Boolean, Integer> onScrollPageUp;
    private Callback<Boolean, Integer> onScrollPageDown;
    private Runnable onFocusPreviousRow;
    private Runnable onFocusNextRow;
    private Runnable onSelectPreviousRow;
    private Runnable onSelectNextRow;
    private Runnable onMoveToFirstCell;
    private Runnable onMoveToLastCell;
    private Runnable onSelectRightCell;
    private Runnable onSelectLeftCell;

    @Override
    protected void callAction(String string) {
        boolean bl;
        boolean bl2 = bl = this.getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        if ("SelectPreviousRow".equals(string)) {
            this.selectPreviousRow();
        } else if ("SelectNextRow".equals(string)) {
            this.selectNextRow();
        } else if ("SelectLeftCell".equals(string)) {
            if (bl) {
                this.selectRightCell();
            } else {
                this.selectLeftCell();
            }
        } else if ("SelectRightCell".equals(string)) {
            if (bl) {
                this.selectLeftCell();
            } else {
                this.selectRightCell();
            }
        } else if ("SelectFirstRow".equals(string)) {
            this.selectFirstRow();
        } else if ("SelectLastRow".equals(string)) {
            this.selectLastRow();
        } else if ("SelectAll".equals(string)) {
            this.selectAll();
        } else if ("SelectAllPageUp".equals(string)) {
            this.selectAllPageUp();
        } else if ("SelectAllPageDown".equals(string)) {
            this.selectAllPageDown();
        } else if ("SelectAllToFirstRow".equals(string)) {
            this.selectAllToFirstRow();
        } else if ("SelectAllToLastRow".equals(string)) {
            this.selectAllToLastRow();
        } else if ("AlsoSelectNext".equals(string)) {
            this.alsoSelectNext();
        } else if ("AlsoSelectPrevious".equals(string)) {
            this.alsoSelectPrevious();
        } else if ("AlsoSelectLeftCell".equals(string)) {
            if (bl) {
                this.alsoSelectRightCell();
            } else {
                this.alsoSelectLeftCell();
            }
        } else if ("AlsoSelectRightCell".equals(string)) {
            if (bl) {
                this.alsoSelectLeftCell();
            } else {
                this.alsoSelectRightCell();
            }
        } else if ("ClearSelection".equals(string)) {
            this.clearSelection();
        } else if ("ScrollUp".equals(string)) {
            this.scrollUp();
        } else if ("ScrollDown".equals(string)) {
            this.scrollDown();
        } else if ("FocusPreviousRow".equals(string)) {
            this.focusPreviousRow();
        } else if ("FocusNextRow".equals(string)) {
            this.focusNextRow();
        } else if ("FocusLeftCell".equals(string)) {
            if (bl) {
                this.focusRightCell();
            } else {
                this.focusLeftCell();
            }
        } else if ("FocusRightCell".equals(string)) {
            if (bl) {
                this.focusLeftCell();
            } else {
                this.focusRightCell();
            }
        } else if ("Activate".equals(string)) {
            this.activate();
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
        } else if ("DiscontinuousSelectNextRow".equals(string)) {
            this.discontinuousSelectNextRow();
        } else if ("DiscontinuousSelectPreviousRow".equals(string)) {
            this.discontinuousSelectPreviousRow();
        } else if ("DiscontinuousSelectNextColumn".equals(string)) {
            if (bl) {
                this.discontinuousSelectPreviousColumn();
            } else {
                this.discontinuousSelectNextColumn();
            }
        } else if ("DiscontinuousSelectPreviousColumn".equals(string)) {
            if (bl) {
                this.discontinuousSelectNextColumn();
            } else {
                this.discontinuousSelectPreviousColumn();
            }
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

    public TableViewBehaviorBase(C c) {
        this(c, null);
    }

    public TableViewBehaviorBase(C c, List<KeyBinding> list) {
        super(c, list == null ? TABLE_VIEW_BINDINGS : list);
    }

    protected void setAnchor(TablePositionBase tablePositionBase) {
        TableCellBehaviorBase.setAnchor(this.getControl(), tablePositionBase, false);
        this.setSelectionPathDeviated(false);
    }

    protected TablePositionBase getAnchor() {
        return TableCellBehaviorBase.getAnchor(this.getControl(), this.getFocusedCell());
    }

    protected boolean hasAnchor() {
        return TableCellBehaviorBase.hasNonDefaultAnchor(this.getControl());
    }

    protected abstract int getItemCount();

    protected abstract TableFocusModel getFocusModel();

    protected abstract TableSelectionModel<T> getSelectionModel();

    protected abstract ObservableList<? extends TablePositionBase> getSelectedCells();

    protected abstract TablePositionBase getFocusedCell();

    protected abstract int getVisibleLeafIndex(TableColumnBase var1);

    protected abstract TableColumnBase getVisibleLeafColumn(int var1);

    protected abstract void editCell(int var1, TableColumnBase var2);

    protected abstract ObservableList<? extends TableColumnBase> getVisibleLeafColumns();

    protected abstract TablePositionBase<TC> getTablePosition(int var1, TableColumnBase<T, ?> var2);

    protected void setAnchor(int n, TableColumnBase tableColumnBase) {
        this.setAnchor(n == -1 && tableColumnBase == null ? null : this.getTablePosition(n, tableColumnBase));
    }

    public void setOnScrollPageUp(Callback<Boolean, Integer> callback) {
        this.onScrollPageUp = callback;
    }

    public void setOnScrollPageDown(Callback<Boolean, Integer> callback) {
        this.onScrollPageDown = callback;
    }

    public void setOnFocusPreviousRow(Runnable runnable) {
        this.onFocusPreviousRow = runnable;
    }

    public void setOnFocusNextRow(Runnable runnable) {
        this.onFocusNextRow = runnable;
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

    public void setOnSelectRightCell(Runnable runnable) {
        this.onSelectRightCell = runnable;
    }

    public void setOnSelectLeftCell(Runnable runnable) {
        this.onSelectLeftCell = runnable;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        if (!this.getControl().isFocused() && this.getControl().isFocusTraversable()) {
            this.getControl().requestFocus();
        }
    }

    protected boolean isRTL() {
        return this.getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
    }

    private void setSelectionPathDeviated(boolean bl) {
        this.selectionPathDeviated = bl;
    }

    protected void scrollUp() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null || this.getSelectedCells().isEmpty()) {
            return;
        }
        TablePositionBase tablePositionBase = (TablePositionBase)this.getSelectedCells().get(0);
        int n = -1;
        if (this.onScrollPageUp != null) {
            n = (Integer)this.onScrollPageUp.call((Object)false);
        }
        if (n == -1) {
            return;
        }
        tableSelectionModel.clearAndSelect(n, tablePositionBase.getTableColumn());
    }

    protected void scrollDown() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null || this.getSelectedCells().isEmpty()) {
            return;
        }
        TablePositionBase tablePositionBase = (TablePositionBase)this.getSelectedCells().get(0);
        int n = -1;
        if (this.onScrollPageDown != null) {
            n = (Integer)this.onScrollPageDown.call((Object)false);
        }
        if (n == -1) {
            return;
        }
        tableSelectionModel.clearAndSelect(n, tablePositionBase.getTableColumn());
    }

    protected void focusFirstRow() {
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TableColumnBase tableColumnBase = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
        tableFocusModel.focus(0, tableColumnBase);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    protected void focusLastRow() {
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TableColumnBase tableColumnBase = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
        tableFocusModel.focus(this.getItemCount() - 1, tableColumnBase);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    protected void focusPreviousRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        if (tableSelectionModel.isCellSelectionEnabled()) {
            tableFocusModel.focusAboveCell();
        } else {
            tableFocusModel.focusPrevious();
        }
        if (!this.isShortcutDown || this.getAnchor() == null) {
            this.setAnchor(tableFocusModel.getFocusedIndex(), null);
        }
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    protected void focusNextRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        if (tableSelectionModel.isCellSelectionEnabled()) {
            tableFocusModel.focusBelowCell();
        } else {
            tableFocusModel.focusNext();
        }
        if (!this.isShortcutDown || this.getAnchor() == null) {
            this.setAnchor(tableFocusModel.getFocusedIndex(), null);
        }
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    protected void focusLeftCell() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        tableFocusModel.focusLeftCell();
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    protected void focusRightCell() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        tableFocusModel.focusRightCell();
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    protected void focusPageUp() {
        int n = (Integer)this.onScrollPageUp.call((Object)true);
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TableColumnBase tableColumnBase = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
        tableFocusModel.focus(n, tableColumnBase);
    }

    protected void focusPageDown() {
        int n = (Integer)this.onScrollPageDown.call((Object)true);
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TableColumnBase tableColumnBase = this.getFocusedCell() == null ? null : this.getFocusedCell().getTableColumn();
        tableFocusModel.focus(n, tableColumnBase);
    }

    protected void clearSelection() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        tableSelectionModel.clearSelection();
    }

    protected void clearSelectionOutsideRange(int n, int n2, TableColumnBase<T, ?> tableColumnBase) {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        int n3 = Math.min(n, n2);
        int n4 = Math.max(n, n2);
        ArrayList arrayList = new ArrayList(tableSelectionModel.getSelectedIndices());
        this.selectionChanging = true;
        for (int i = 0; i < arrayList.size(); ++i) {
            int n5 = (Integer)arrayList.get(i);
            if (n5 >= n3 && n5 <= n4) continue;
            tableSelectionModel.clearSelection(n5, tableColumnBase);
        }
        this.selectionChanging = false;
    }

    protected void alsoSelectPrevious() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        if (tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            this.selectPreviousRow();
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        if (tableSelectionModel.isCellSelectionEnabled()) {
            this.updateCellVerticalSelection(-1, () -> this.getSelectionModel().selectAboveCell());
        } else if (this.isShiftDown && this.hasAnchor()) {
            this.updateRowSelection(-1);
        } else {
            tableSelectionModel.selectPrevious();
        }
        this.onSelectPreviousRow.run();
    }

    protected void alsoSelectNext() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        if (tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            this.selectNextRow();
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        if (tableSelectionModel.isCellSelectionEnabled()) {
            this.updateCellVerticalSelection(1, () -> this.getSelectionModel().selectBelowCell());
        } else if (this.isShiftDown && this.hasAnchor()) {
            this.updateRowSelection(1);
        } else {
            tableSelectionModel.selectNext();
        }
        this.onSelectNextRow.run();
    }

    protected void alsoSelectLeftCell() {
        this.updateCellHorizontalSelection(-1, () -> this.getSelectionModel().selectLeftCell());
        this.onSelectLeftCell.run();
    }

    protected void alsoSelectRightCell() {
        this.updateCellHorizontalSelection(1, () -> this.getSelectionModel().selectRightCell());
        this.onSelectRightCell.run();
    }

    protected void updateRowSelection(int n) {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null || tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n2 = tableFocusModel.getFocusedIndex() + n;
        TablePositionBase tablePositionBase = this.getAnchor();
        if (!this.hasAnchor()) {
            this.setAnchor(this.getFocusedCell());
        }
        if (tableSelectionModel.getSelectedIndices().size() > 1) {
            this.clearSelectionOutsideRange(tablePositionBase.getRow(), n2, null);
        }
        if (tablePositionBase.getRow() > n2) {
            tableSelectionModel.selectRange(tablePositionBase.getRow(), n2 - 1);
        } else {
            tableSelectionModel.selectRange(tablePositionBase.getRow(), n2 + 1);
        }
    }

    protected void updateCellVerticalSelection(int n, Runnable runnable) {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null || tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        int n2 = tablePositionBase.getRow();
        if (this.isShiftDown && tableSelectionModel.isSelected(n2 + n, tablePositionBase.getTableColumn())) {
            int n3 = n2 + n;
            boolean bl = false;
            if (this.selectionHistory.size() >= 2) {
                TablePositionBase tablePositionBase2 = this.selectionHistory.get(1);
                boolean bl2 = bl = tablePositionBase2.getRow() == n3 && tablePositionBase2.getColumn() == tablePositionBase.getColumn();
            }
            int n4 = this.selectionPathDeviated ? (bl ? n2 : n3) : n2;
            tableSelectionModel.clearSelection(n4, tablePositionBase.getTableColumn());
            tableFocusModel.focus(n3, tablePositionBase.getTableColumn());
        } else if (this.isShiftDown && this.getAnchor() != null && !this.selectionPathDeviated) {
            int n5 = tableFocusModel.getFocusedIndex() + n;
            n5 = Math.max(Math.min(this.getItemCount() - 1, n5), 0);
            int n6 = Math.min(this.getAnchor().getRow(), n5);
            int n7 = Math.max(this.getAnchor().getRow(), n5);
            if (tableSelectionModel.getSelectedIndices().size() > 1) {
                this.clearSelectionOutsideRange(n6, n7, tablePositionBase.getTableColumn());
            }
            for (int i = n6; i <= n7; ++i) {
                if (tableSelectionModel.isSelected(i, tablePositionBase.getTableColumn())) continue;
                tableSelectionModel.select(i, tablePositionBase.getTableColumn());
            }
            tableFocusModel.focus(n5, tablePositionBase.getTableColumn());
        } else {
            int n8 = tableFocusModel.getFocusedIndex();
            if (!tableSelectionModel.isSelected(n8, tablePositionBase.getTableColumn())) {
                tableSelectionModel.select(n8, tablePositionBase.getTableColumn());
            }
            runnable.run();
        }
    }

    protected void updateCellHorizontalSelection(int n, Runnable runnable) {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null || tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        if (tablePositionBase == null || tablePositionBase.getTableColumn() == null) {
            return;
        }
        boolean bl = false;
        TableColumnBase tableColumnBase = this.getColumn(tablePositionBase.getTableColumn(), n);
        if (tableColumnBase == null) {
            tableColumnBase = tablePositionBase.getTableColumn();
            bl = true;
        }
        int n2 = tablePositionBase.getRow();
        if (this.isShiftDown && tableSelectionModel.isSelected(n2, tableColumnBase)) {
            TableColumnBase tableColumnBase2;
            if (bl) {
                return;
            }
            boolean bl2 = false;
            ObservableList<TablePositionBase> observableList = this.getSelectedCells();
            if (observableList.size() >= 2) {
                tableColumnBase2 = (TablePositionBase)observableList.get(observableList.size() - 2);
                boolean bl3 = bl2 = tableColumnBase2.getRow() == n2 && tableColumnBase2.getTableColumn().equals((Object)tableColumnBase);
            }
            tableColumnBase2 = this.selectionPathDeviated ? (bl2 ? tablePositionBase.getTableColumn() : tableColumnBase) : tablePositionBase.getTableColumn();
            tableSelectionModel.clearSelection(n2, tableColumnBase2);
            tableFocusModel.focus(n2, tableColumnBase);
        } else if (this.isShiftDown && this.getAnchor() != null && !this.selectionPathDeviated) {
            int n3 = this.getAnchor().getColumn();
            int n4 = this.getVisibleLeafIndex(tablePositionBase.getTableColumn()) + n;
            n4 = Math.max(Math.min(this.getVisibleLeafColumns().size() - 1, n4), 0);
            int n5 = Math.min(n3, n4);
            int n6 = Math.max(n3, n4);
            for (int i = n5; i <= n6; ++i) {
                tableSelectionModel.select(tablePositionBase.getRow(), this.getColumn(i));
            }
            tableFocusModel.focus(tablePositionBase.getRow(), this.getColumn(n4));
        } else {
            runnable.run();
        }
    }

    protected TableColumnBase getColumn(int n) {
        return this.getVisibleLeafColumn(n);
    }

    protected TableColumnBase getColumn(TableColumnBase tableColumnBase, int n) {
        return this.getVisibleLeafColumn(this.getVisibleLeafIndex(tableColumnBase) + n);
    }

    protected void selectFirstRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        ObservableList<TablePositionBase> observableList = this.getSelectedCells();
        TableColumnBase tableColumnBase = observableList.size() == 0 ? null : ((TablePositionBase)observableList.get(0)).getTableColumn();
        tableSelectionModel.clearAndSelect(0, tableColumnBase);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    protected void selectLastRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        ObservableList<TablePositionBase> observableList = this.getSelectedCells();
        TableColumnBase tableColumnBase = observableList.size() == 0 ? null : ((TablePositionBase)observableList.get(0)).getTableColumn();
        tableSelectionModel.clearAndSelect(this.getItemCount() - 1, tableColumnBase);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    protected void selectPreviousRow() {
        this.selectCell(-1, 0);
        if (this.onSelectPreviousRow != null) {
            this.onSelectPreviousRow.run();
        }
    }

    protected void selectNextRow() {
        this.selectCell(1, 0);
        if (this.onSelectNextRow != null) {
            this.onSelectNextRow.run();
        }
    }

    protected void selectLeftCell() {
        this.selectCell(0, -1);
        if (this.onSelectLeftCell != null) {
            this.onSelectLeftCell.run();
        }
    }

    protected void selectRightCell() {
        this.selectCell(0, 1);
        if (this.onSelectRightCell != null) {
            this.onSelectRightCell.run();
        }
    }

    protected void selectCell(int n, int n2) {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        int n3 = tablePositionBase.getRow();
        int n4 = this.getVisibleLeafIndex(tablePositionBase.getTableColumn());
        if (n < 0 && n3 <= 0) {
            return;
        }
        if (n > 0 && n3 >= this.getItemCount() - 1) {
            return;
        }
        if (n2 < 0 && n4 <= 0) {
            return;
        }
        if (n2 > 0 && n4 >= this.getVisibleLeafColumns().size() - 1) {
            return;
        }
        if (n2 > 0 && n4 == -1) {
            return;
        }
        TableColumnBase tableColumnBase = tablePositionBase.getTableColumn();
        tableColumnBase = this.getColumn(tableColumnBase, n2);
        int n5 = tablePositionBase.getRow() + n;
        tableSelectionModel.clearAndSelect(n5, tableColumnBase);
        this.setAnchor(n5, tableColumnBase);
    }

    protected void cancelEdit() {
        this.editCell(-1, null);
    }

    protected void activate() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        tableSelectionModel.select(tablePositionBase.getRow(), tablePositionBase.getTableColumn());
        this.setAnchor(tablePositionBase);
        if (tablePositionBase.getRow() >= 0) {
            this.editCell(tablePositionBase.getRow(), tablePositionBase.getTableColumn());
        }
    }

    protected void selectAllToFocus(boolean bl) {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        int n = tablePositionBase.getRow();
        TablePositionBase tablePositionBase2 = this.getAnchor();
        int n2 = tablePositionBase2.getRow();
        tableSelectionModel.clearSelection();
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            int n3 = n2;
            int n4 = n2 > n ? n - 1 : n + 1;
            tableSelectionModel.selectRange(n3, n4);
        } else {
            tableSelectionModel.selectRange(tablePositionBase2.getRow(), tablePositionBase2.getTableColumn(), tablePositionBase.getRow(), tablePositionBase.getTableColumn());
        }
        this.setAnchor(bl ? tablePositionBase : tablePositionBase2);
    }

    protected void selectAll() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        tableSelectionModel.selectAll();
    }

    protected void selectAllToFirstRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        boolean bl = tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE;
        TablePositionBase tablePositionBase = this.getFocusedCell();
        TableColumnBase tableColumnBase = this.getFocusedCell().getTableColumn();
        int n = tablePositionBase.getRow();
        if (this.isShiftDown) {
            n = this.getAnchor() == null ? n : this.getAnchor().getRow();
        }
        tableSelectionModel.clearSelection();
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            if (bl) {
                tableSelectionModel.select(0);
            } else {
                tableSelectionModel.selectRange(n, -1);
            }
            tableFocusModel.focus(0);
        } else {
            if (bl) {
                tableSelectionModel.select(0, tableColumnBase);
            } else {
                tableSelectionModel.selectRange(n, tableColumnBase, -1, tableColumnBase);
            }
            tableFocusModel.focus(0, tableColumnBase);
        }
        if (this.isShiftDown) {
            this.setAnchor(n, tableColumnBase);
        }
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    protected void selectAllToLastRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = this.getItemCount();
        TablePositionBase tablePositionBase = this.getFocusedCell();
        TableColumnBase tableColumnBase = this.getFocusedCell().getTableColumn();
        int n2 = tablePositionBase.getRow();
        if (this.isShiftDown) {
            n2 = this.getAnchor() == null ? n2 : this.getAnchor().getRow();
        }
        tableSelectionModel.clearSelection();
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            tableSelectionModel.selectRange(n2, n);
        } else {
            tableSelectionModel.selectRange(n2, tableColumnBase, n - 1, tableColumnBase);
        }
        if (this.isShiftDown) {
            this.setAnchor(n2, tableColumnBase);
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    protected void selectAllPageUp() {
        TableColumnBase tableColumnBase;
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = tableFocusModel.getFocusedIndex();
        TableColumnBase tableColumnBase2 = tableColumnBase = tableSelectionModel.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            n = this.getAnchor() == null ? n : this.getAnchor().getRow();
            this.setAnchor(n, tableColumnBase);
        }
        int n2 = (Integer)this.onScrollPageUp.call((Object)false);
        this.selectionChanging = true;
        if (tableSelectionModel.getSelectionMode() == null || tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            if (tableSelectionModel.isCellSelectionEnabled()) {
                tableSelectionModel.select(n2, tableColumnBase);
            } else {
                tableSelectionModel.select(n2);
            }
        } else {
            tableSelectionModel.clearSelection();
            if (tableSelectionModel.isCellSelectionEnabled()) {
                tableSelectionModel.selectRange(n, tableColumnBase, n2, tableColumnBase);
            } else {
                int n3 = n < n2 ? 1 : -1;
                tableSelectionModel.selectRange(n, n2 + n3);
            }
        }
        this.selectionChanging = false;
    }

    protected void selectAllPageDown() {
        TableColumnBase tableColumnBase;
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = tableFocusModel.getFocusedIndex();
        TableColumnBase tableColumnBase2 = tableColumnBase = tableSelectionModel.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            n = this.getAnchor() == null ? n : this.getAnchor().getRow();
            this.setAnchor(n, tableColumnBase);
        }
        int n2 = (Integer)this.onScrollPageDown.call((Object)false);
        this.selectionChanging = true;
        if (tableSelectionModel.getSelectionMode() == null || tableSelectionModel.getSelectionMode() == SelectionMode.SINGLE) {
            if (tableSelectionModel.isCellSelectionEnabled()) {
                tableSelectionModel.select(n2, tableColumnBase);
            } else {
                tableSelectionModel.select(n2);
            }
        } else {
            tableSelectionModel.clearSelection();
            if (tableSelectionModel.isCellSelectionEnabled()) {
                tableSelectionModel.selectRange(n, tableColumnBase, n2, tableColumnBase);
            } else {
                int n3 = n < n2 ? 1 : -1;
                tableSelectionModel.selectRange(n, n2 + n3);
            }
        }
        this.selectionChanging = false;
    }

    protected void toggleFocusOwnerSelection() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TablePositionBase tablePositionBase = this.getFocusedCell();
        if (tableSelectionModel.isSelected(tablePositionBase.getRow(), tablePositionBase.getTableColumn())) {
            tableSelectionModel.clearSelection(tablePositionBase.getRow(), tablePositionBase.getTableColumn());
            tableFocusModel.focus(tablePositionBase.getRow(), tablePositionBase.getTableColumn());
        } else {
            tableSelectionModel.select(tablePositionBase.getRow(), tablePositionBase.getTableColumn());
        }
        this.setAnchor(tablePositionBase.getRow(), tablePositionBase.getTableColumn());
    }

    protected void discontinuousSelectPreviousRow() {
        TableColumnBase tableColumnBase;
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        if (tableSelectionModel.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectPreviousRow();
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = tableFocusModel.getFocusedIndex();
        int n2 = n - 1;
        if (n2 < 0) {
            return;
        }
        int n3 = n;
        TableColumnBase tableColumnBase2 = tableColumnBase = tableSelectionModel.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            int n4 = n3 = this.getAnchor() == null ? n : this.getAnchor().getRow();
        }
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            tableSelectionModel.selectRange(n2, n3 + 1);
            tableFocusModel.focus(n2);
        } else {
            for (int i = n2; i < n3 + 1; ++i) {
                tableSelectionModel.select(i, tableColumnBase);
            }
            tableFocusModel.focus(n2, tableColumnBase);
        }
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    protected void discontinuousSelectNextRow() {
        TableColumnBase tableColumnBase;
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        if (tableSelectionModel.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectNextRow();
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = tableFocusModel.getFocusedIndex();
        int n2 = n + 1;
        if (n2 >= this.getItemCount()) {
            return;
        }
        int n3 = n;
        TableColumnBase tableColumnBase2 = tableColumnBase = tableSelectionModel.isCellSelectionEnabled() ? this.getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            int n4 = n3 = this.getAnchor() == null ? n : this.getAnchor().getRow();
        }
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            tableSelectionModel.selectRange(n3, n2 + 1);
            tableFocusModel.focus(n2);
        } else {
            for (int i = n3; i < n2 + 1; ++i) {
                tableSelectionModel.select(i, tableColumnBase);
            }
            tableFocusModel.focus(n2, tableColumnBase);
        }
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    protected void discontinuousSelectPreviousColumn() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null || !tableSelectionModel.isCellSelectionEnabled()) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TableColumnBase tableColumnBase = this.getColumn(this.getFocusedCell().getTableColumn(), -1);
        tableSelectionModel.select(tableFocusModel.getFocusedIndex(), tableColumnBase);
    }

    protected void discontinuousSelectNextColumn() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null || !tableSelectionModel.isCellSelectionEnabled()) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        TableColumnBase tableColumnBase = this.getColumn(this.getFocusedCell().getTableColumn(), 1);
        tableSelectionModel.select(tableFocusModel.getFocusedIndex(), tableColumnBase);
    }

    protected void discontinuousSelectPageUp() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = this.hasAnchor() ? this.getAnchor().getRow() : tableFocusModel.getFocusedIndex();
        int n2 = (Integer)this.onScrollPageUp.call((Object)false);
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            tableSelectionModel.selectRange(n, n2 - 1);
        }
    }

    protected void discontinuousSelectPageDown() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = this.hasAnchor() ? this.getAnchor().getRow() : tableFocusModel.getFocusedIndex();
        int n2 = (Integer)this.onScrollPageDown.call((Object)false);
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            tableSelectionModel.selectRange(n, n2 + 1);
        }
    }

    protected void discontinuousSelectAllToFirstRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = tableFocusModel.getFocusedIndex();
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            tableSelectionModel.selectRange(0, n);
            tableFocusModel.focus(0);
        } else {
            for (int i = 0; i < n; ++i) {
                tableSelectionModel.select(i, this.getFocusedCell().getTableColumn());
            }
            tableFocusModel.focus(0, this.getFocusedCell().getTableColumn());
        }
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    protected void discontinuousSelectAllToLastRow() {
        TableSelectionModel<T> tableSelectionModel = this.getSelectionModel();
        if (tableSelectionModel == null) {
            return;
        }
        TableFocusModel tableFocusModel = this.getFocusModel();
        if (tableFocusModel == null) {
            return;
        }
        int n = tableFocusModel.getFocusedIndex() + 1;
        if (!tableSelectionModel.isCellSelectionEnabled()) {
            tableSelectionModel.selectRange(n, this.getItemCount());
        } else {
            for (int i = n; i < this.getItemCount(); ++i) {
                tableSelectionModel.select(i, this.getFocusedCell().getTableColumn());
            }
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    static {
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraversePrevious").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "SelectLeftCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "SelectLeftCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "SelectRightCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "SelectRightCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "SelectPreviousRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "SelectPreviousRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "TraverseLeft"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "TraverseUp"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "TraverseDown"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectAllToLastRow").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "AlsoSelectPrevious").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "AlsoSelectPrevious").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "AlsoSelectNext").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "AlsoSelectNext").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocus").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "AlsoSelectLeftCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "AlsoSelectLeftCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "AlsoSelectRightCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "AlsoSelectRightCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "FocusPreviousRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "FocusNextRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "FocusRightCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "FocusRightCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "FocusLeftCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "FocusLeftCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.A, "SelectAll").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "FocusFirstRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "FocusLastRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "DiscontinuousSelectPreviousColumn").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "DiscontinuousSelectNextColumn").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow").shortcut().shift());
        if (PlatformUtil.isMac()) {
            TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl().shortcut());
        } else {
            TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl());
        }
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Activate"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "Activate"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Activate"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
    }
}

