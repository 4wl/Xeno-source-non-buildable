/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.collections.transformation.SortedList
 *  javafx.scene.control.TablePositionBase
 */
package com.sun.javafx.scene.control;

import java.util.BitSet;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TablePositionBase;

public abstract class SelectedCellsMap<T extends TablePositionBase> {
    private final ObservableList<T> selectedCells = FXCollections.observableArrayList();
    private final ObservableList<T> sortedSelectedCells = new SortedList(this.selectedCells, (tablePositionBase, tablePositionBase2) -> {
        int n = tablePositionBase.getRow() - tablePositionBase2.getRow();
        return n == 0 ? tablePositionBase.getColumn() - tablePositionBase2.getColumn() : n;
    });
    private final Map<Integer, BitSet> selectedCellBitSetMap;

    public SelectedCellsMap(ListChangeListener<T> listChangeListener) {
        this.sortedSelectedCells.addListener(listChangeListener);
        this.selectedCellBitSetMap = new TreeMap<Integer, BitSet>((n, n2) -> n.compareTo((Integer)n2));
    }

    public abstract boolean isCellSelectionEnabled();

    public int size() {
        return this.selectedCells.size();
    }

    public T get(int n) {
        if (n < 0) {
            return null;
        }
        return (T)((TablePositionBase)this.sortedSelectedCells.get(n));
    }

    public void add(T t) {
        BitSet bitSet;
        int n = t.getRow();
        int n2 = t.getColumn();
        boolean bl = false;
        if (!this.selectedCellBitSetMap.containsKey(n)) {
            bitSet = new BitSet();
            this.selectedCellBitSetMap.put(n, bitSet);
            bl = true;
        } else {
            bitSet = this.selectedCellBitSetMap.get(n);
        }
        boolean bl2 = this.isCellSelectionEnabled();
        if (bl2) {
            if (n2 >= 0) {
                boolean bl3 = bitSet.get(n2);
                if (!bl3) {
                    bitSet.set(n2);
                    this.selectedCells.add(t);
                }
            } else if (!this.selectedCells.contains(t)) {
                this.selectedCells.add(t);
            }
        } else if (bl) {
            if (n2 >= 0) {
                bitSet.set(n2);
            }
            this.selectedCells.add(t);
        }
    }

    public void addAll(Collection<T> collection) {
        for (TablePositionBase tablePositionBase : collection) {
            BitSet bitSet;
            int n = tablePositionBase.getRow();
            int n2 = tablePositionBase.getColumn();
            if (!this.selectedCellBitSetMap.containsKey(n)) {
                bitSet = new BitSet();
                this.selectedCellBitSetMap.put(n, bitSet);
            } else {
                bitSet = this.selectedCellBitSetMap.get(n);
            }
            if (n2 < 0) continue;
            bitSet.set(n2);
        }
        this.selectedCells.addAll(collection);
    }

    public void setAll(Collection<T> collection) {
        this.selectedCellBitSetMap.clear();
        for (TablePositionBase tablePositionBase : collection) {
            BitSet bitSet;
            int n = tablePositionBase.getRow();
            int n2 = tablePositionBase.getColumn();
            if (!this.selectedCellBitSetMap.containsKey(n)) {
                bitSet = new BitSet();
                this.selectedCellBitSetMap.put(n, bitSet);
            } else {
                bitSet = this.selectedCellBitSetMap.get(n);
            }
            if (n2 < 0) continue;
            bitSet.set(n2);
        }
        this.selectedCells.setAll(collection);
    }

    public void remove(T t) {
        int n = t.getRow();
        int n2 = t.getColumn();
        if (this.selectedCellBitSetMap.containsKey(n)) {
            BitSet bitSet = this.selectedCellBitSetMap.get(n);
            if (n2 >= 0) {
                bitSet.clear(n2);
            }
            if (bitSet.isEmpty()) {
                this.selectedCellBitSetMap.remove(n);
            }
        }
        this.selectedCells.remove(t);
    }

    public void clear() {
        this.selectedCellBitSetMap.clear();
        this.selectedCells.clear();
    }

    public boolean isSelected(int n, int n2) {
        if (n2 < 0) {
            return this.selectedCellBitSetMap.containsKey(n);
        }
        return this.selectedCellBitSetMap.containsKey(n) ? this.selectedCellBitSetMap.get(n).get(n2) : false;
    }

    public int indexOf(T t) {
        return this.sortedSelectedCells.indexOf(t);
    }

    public boolean isEmpty() {
        return this.selectedCells.isEmpty();
    }

    public ObservableList<T> getSelectedCells() {
        return this.selectedCells;
    }
}

