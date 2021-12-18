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
 *  javafx.event.EventType
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.FocusModel
 *  javafx.scene.control.ListView
 *  javafx.scene.control.MultipleSelectionModel
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import com.sun.javafx.scene.control.behavior.OrientedKeyBinding;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusListBehavior;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ListViewBehavior<T>
extends BehaviorBase<ListView<T>> {
    protected static final List<KeyBinding> LIST_VIEW_BINDINGS = new ArrayList<KeyBinding>();
    private boolean isShiftDown = false;
    private boolean isShortcutDown = false;
    private Callback<Boolean, Integer> onScrollPageUp;
    private Callback<Boolean, Integer> onScrollPageDown;
    private Runnable onFocusPreviousRow;
    private Runnable onFocusNextRow;
    private Runnable onSelectPreviousRow;
    private Runnable onSelectNextRow;
    private Runnable onMoveToFirstCell;
    private Runnable onMoveToLastCell;
    private boolean selectionChanging = false;
    private final ListChangeListener<Integer> selectedIndicesListener = change -> {
        while (change.next()) {
            int n;
            if (change.wasReplaced() && ListCellBehavior.hasDefaultAnchor(this.getControl())) {
                ListCellBehavior.removeAnchor(this.getControl());
            }
            int n2 = change.wasPermutated() ? change.getTo() - change.getFrom() : 0;
            MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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
    private final ListChangeListener<T> itemsListListener = change -> {
        while (change.next()) {
            if (change.wasAdded() && change.getFrom() <= this.getAnchor()) {
                this.setAnchor(this.getAnchor() + change.getAddedSize());
                continue;
            }
            if (!change.wasRemoved() || change.getFrom() > this.getAnchor()) continue;
            this.setAnchor(this.getAnchor() - change.getRemovedSize());
        }
    };
    private final ChangeListener<ObservableList<T>> itemsListener = new ChangeListener<ObservableList<T>>(){

        public void changed(ObservableValue<? extends ObservableList<T>> observableValue, ObservableList<T> observableList, ObservableList<T> observableList2) {
            if (observableList != null) {
                observableList.removeListener((ListChangeListener)ListViewBehavior.this.weakItemsListListener);
            }
            if (observableList2 != null) {
                observableList2.addListener((ListChangeListener)ListViewBehavior.this.weakItemsListListener);
            }
        }
    };
    private final ChangeListener<MultipleSelectionModel<T>> selectionModelListener = new ChangeListener<MultipleSelectionModel<T>>(){

        public void changed(ObservableValue<? extends MultipleSelectionModel<T>> observableValue, MultipleSelectionModel<T> multipleSelectionModel, MultipleSelectionModel<T> multipleSelectionModel2) {
            if (multipleSelectionModel != null) {
                multipleSelectionModel.getSelectedIndices().removeListener((ListChangeListener)ListViewBehavior.this.weakSelectedIndicesListener);
            }
            if (multipleSelectionModel2 != null) {
                multipleSelectionModel2.getSelectedIndices().addListener((ListChangeListener)ListViewBehavior.this.weakSelectedIndicesListener);
            }
        }
    };
    private final WeakChangeListener<ObservableList<T>> weakItemsListener = new WeakChangeListener(this.itemsListener);
    private final WeakListChangeListener<Integer> weakSelectedIndicesListener = new WeakListChangeListener(this.selectedIndicesListener);
    private final WeakListChangeListener<T> weakItemsListListener = new WeakListChangeListener(this.itemsListListener);
    private final WeakChangeListener<MultipleSelectionModel<T>> weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);
    private TwoLevelFocusListBehavior tlFocus;

    @Override
    protected String matchActionForEvent(KeyEvent keyEvent) {
        String string = super.matchActionForEvent(keyEvent);
        if (string != null) {
            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.KP_LEFT) {
                if (((ListView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    string = keyEvent.isShiftDown() ? "AlsoSelectNextRow" : (keyEvent.isShortcutDown() ? "FocusNextRow" : (((ListView)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "SelectNextRow" : "TraverseRight"));
                }
            } else if ((keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.KP_RIGHT) && ((ListView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                string = keyEvent.isShiftDown() ? "AlsoSelectPreviousRow" : (keyEvent.isShortcutDown() ? "FocusPreviousRow" : (((ListView)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "SelectPreviousRow" : "TraverseLeft"));
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
        } else if ("SelectAllToFirstRow".equals(string)) {
            this.selectAllToFirstRow();
        } else if ("SelectAllToLastRow".equals(string)) {
            this.selectAllToLastRow();
        } else if ("SelectAllPageUp".equals(string)) {
            this.selectAllPageUp();
        } else if ("SelectAllPageDown".equals(string)) {
            this.selectAllPageDown();
        } else if ("AlsoSelectNextRow".equals(string)) {
            this.alsoSelectNextRow();
        } else if ("AlsoSelectPreviousRow".equals(string)) {
            this.alsoSelectPreviousRow();
        } else if ("ClearSelection".equals(string)) {
            this.clearSelection();
        } else if ("SelectAll".equals(string)) {
            this.selectAll();
        } else if ("ScrollUp".equals(string)) {
            this.scrollPageUp();
        } else if ("ScrollDown".equals(string)) {
            this.scrollPageDown();
        } else if ("FocusPreviousRow".equals(string)) {
            this.focusPreviousRow();
        } else if ("FocusNextRow".equals(string)) {
            this.focusNextRow();
        } else if ("FocusPageUp".equals(string)) {
            this.focusPageUp();
        } else if ("FocusPageDown".equals(string)) {
            this.focusPageDown();
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

    public ListViewBehavior(ListView<T> listView) {
        super(listView, LIST_VIEW_BINDINGS);
        listView.itemsProperty().addListener(this.weakItemsListener);
        if (listView.getItems() != null) {
            listView.getItems().addListener(this.weakItemsListListener);
        }
        ((ListView)this.getControl()).selectionModelProperty().addListener(this.weakSelectionModelListener);
        if (listView.getSelectionModel() != null) {
            listView.getSelectionModel().getSelectedIndices().addListener(this.weakSelectedIndicesListener);
        }
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusListBehavior((Node)listView);
        }
    }

    @Override
    public void dispose() {
        ListCellBehavior.removeAnchor(this.getControl());
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    private void setAnchor(int n) {
        ListCellBehavior.setAnchor(this.getControl(), n < 0 ? null : Integer.valueOf(n), false);
    }

    private int getAnchor() {
        return ListCellBehavior.getAnchor(this.getControl(), ((ListView)this.getControl()).getFocusModel().getFocusedIndex());
    }

    private boolean hasAnchor() {
        return ListCellBehavior.hasNonDefaultAnchor(this.getControl());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        if (!mouseEvent.isShiftDown() && !mouseEvent.isSynthesized()) {
            int n = ((ListView)this.getControl()).getSelectionModel().getSelectedIndex();
            this.setAnchor(n);
        }
        if (!((ListView)this.getControl()).isFocused() && ((ListView)this.getControl()).isFocusTraversable()) {
            ((ListView)this.getControl()).requestFocus();
        }
    }

    private int getRowCount() {
        return ((ListView)this.getControl()).getItems() == null ? 0 : ((ListView)this.getControl()).getItems().size();
    }

    private void clearSelection() {
        ((ListView)this.getControl()).getSelectionModel().clearSelection();
    }

    private void scrollPageUp() {
        int n = -1;
        if (this.onScrollPageUp != null) {
            n = (Integer)this.onScrollPageUp.call((Object)false);
        }
        if (n == -1) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        multipleSelectionModel.clearAndSelect(n);
    }

    private void scrollPageDown() {
        int n = -1;
        if (this.onScrollPageDown != null) {
            n = (Integer)this.onScrollPageDown.call((Object)false);
        }
        if (n == -1) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        multipleSelectionModel.clearAndSelect(n);
    }

    private void focusFirstRow() {
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(0);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void focusLastRow() {
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(this.getRowCount() - 1);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void focusPreviousRow() {
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(n);
    }

    private void focusPageDown() {
        int n = (Integer)this.onScrollPageDown.call((Object)true);
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(n);
    }

    private void alsoSelectPreviousRow() {
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (n <= 0) {
            return;
        }
        this.setAnchor(n - 1);
        ((ListView)this.getControl()).getSelectionModel().clearAndSelect(n - 1);
        this.onSelectPreviousRow.run();
    }

    private void selectNextRow() {
        ListView listView = (ListView)this.getControl();
        FocusModel focusModel = listView.getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (n == this.getRowCount() - 1) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = listView.getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        this.setAnchor(n + 1);
        multipleSelectionModel.clearAndSelect(n + 1);
        if (this.onSelectNextRow != null) {
            this.onSelectNextRow.run();
        }
    }

    private void selectFirstRow() {
        if (this.getRowCount() > 0) {
            ((ListView)this.getControl()).getSelectionModel().clearAndSelect(0);
            if (this.onMoveToFirstCell != null) {
                this.onMoveToFirstCell.run();
            }
        }
    }

    private void selectLastRow() {
        ((ListView)this.getControl()).getSelectionModel().clearAndSelect(this.getRowCount() - 1);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void selectAllPageUp() {
        int n;
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n2 = focusModel.getFocusedIndex();
        if (this.isShiftDown) {
            n2 = this.getAnchor() == -1 ? n2 : this.getAnchor();
            this.setAnchor(n2);
        }
        int n3 = n2 < (n = ((Integer)this.onScrollPageUp.call((Object)false)).intValue()) ? 1 : -1;
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n2 = focusModel.getFocusedIndex();
        if (this.isShiftDown) {
            n2 = this.getAnchor() == -1 ? n2 : this.getAnchor();
            this.setAnchor(n2);
        }
        int n3 = n2 < (n = ((Integer)this.onScrollPageDown.call((Object)false)).intValue()) ? 1 : -1;
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
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

    private void selectAllToFirstRow() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
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
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        if (this.isShiftDown) {
            n = this.hasAnchor() ? this.getAnchor() : n;
        }
        multipleSelectionModel.clearSelection();
        multipleSelectionModel.selectRange(n, this.getRowCount());
        if (this.isShiftDown) {
            this.setAnchor(n);
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void selectAll() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        multipleSelectionModel.selectAll();
    }

    private void selectAllToFocus(boolean bl) {
        ListView listView = (ListView)this.getControl();
        if (listView.getEditingIndex() >= 0) {
            return;
        }
        MultipleSelectionModel multipleSelectionModel = listView.getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = listView.getFocusModel();
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

    private void cancelEdit() {
        ((ListView)this.getControl()).edit(-1);
    }

    private void activate() {
        int n = ((ListView)this.getControl()).getFocusModel().getFocusedIndex();
        ((ListView)this.getControl()).getSelectionModel().select(n);
        this.setAnchor(n);
        if (n >= 0) {
            ((ListView)this.getControl()).edit(n);
        }
    }

    private void toggleFocusOwnerSelection() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
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
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        if (multipleSelectionModel.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectPreviousRow();
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
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
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        if (multipleSelectionModel.getSelectionMode() != SelectionMode.MULTIPLE) {
            this.selectNextRow();
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex();
        int n2 = n + 1;
        if (n2 >= this.getRowCount()) {
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
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = this.getAnchor();
        int n2 = (Integer)this.onScrollPageUp.call((Object)false);
        multipleSelectionModel.selectRange(n, n2 - 1);
    }

    private void discontinuousSelectPageDown() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = this.getAnchor();
        int n2 = (Integer)this.onScrollPageDown.call((Object)false);
        multipleSelectionModel.selectRange(n, n2 + 1);
    }

    private void discontinuousSelectAllToFirstRow() {
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
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
        MultipleSelectionModel multipleSelectionModel = ((ListView)this.getControl()).getSelectionModel();
        if (multipleSelectionModel == null) {
            return;
        }
        FocusModel focusModel = ((ListView)this.getControl()).getFocusModel();
        if (focusModel == null) {
            return;
        }
        int n = focusModel.getFocusedIndex() + 1;
        multipleSelectionModel.selectRange(n, this.getRowCount());
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    static {
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectAllToLastRow").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocus").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor").shortcut().shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Activate"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "Activate"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Activate"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.A, "SelectAll").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "FocusFirstRow").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "FocusLastRow").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown").shortcut());
        if (PlatformUtil.isMac()) {
            LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl().shortcut());
        } else {
            LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl());
        }
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "SelectPreviousRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_UP, "SelectPreviousRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "SelectNextRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_DOWN, "SelectNextRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "AlsoSelectPreviousRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_UP, "AlsoSelectPreviousRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "AlsoSelectNextRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_DOWN, "AlsoSelectNextRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "FocusPreviousRow").vertical().shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "FocusNextRow").vertical().shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "SelectPreviousRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_LEFT, "SelectPreviousRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "SelectNextRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_RIGHT, "SelectNextRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "AlsoSelectPreviousRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_LEFT, "AlsoSelectPreviousRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "AlsoSelectNextRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_RIGHT, "AlsoSelectNextRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "FocusPreviousRow").shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "FocusNextRow").shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "DiscontinuousSelectPreviousRow").shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "DiscontinuousSelectNextRow").shortcut().shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.BACK_SLASH, "ClearSelection").shortcut());
    }

    private static class ListViewKeyBinding
    extends OrientedKeyBinding {
        public ListViewKeyBinding(KeyCode keyCode, String string) {
            super(keyCode, string);
        }

        public ListViewKeyBinding(KeyCode keyCode, EventType<KeyEvent> eventType, String string) {
            super(keyCode, eventType, string);
        }

        @Override
        public boolean getVertical(Control control) {
            return ((ListView)control).getOrientation() == Orientation.VERTICAL;
        }
    }
}

