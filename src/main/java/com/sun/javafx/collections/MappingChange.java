/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ChangeHelper;
import java.util.AbstractList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public final class MappingChange<E, F>
extends ListChangeListener.Change<F> {
    private final Map<E, F> map;
    private final ListChangeListener.Change<? extends E> original;
    private List<F> removed;
    public static final Map NOOP_MAP = new Map(){

        public Object map(Object object) {
            return object;
        }
    };

    public MappingChange(ListChangeListener.Change<? extends E> change, Map<E, F> map, ObservableList<F> observableList) {
        super(observableList);
        this.original = change;
        this.map = map;
    }

    public boolean next() {
        return this.original.next();
    }

    public void reset() {
        this.original.reset();
    }

    public int getFrom() {
        return this.original.getFrom();
    }

    public int getTo() {
        return this.original.getTo();
    }

    public List<F> getRemoved() {
        if (this.removed == null) {
            this.removed = new AbstractList<F>(){

                @Override
                public F get(int n) {
                    return MappingChange.this.map.map(MappingChange.this.original.getRemoved().get(n));
                }

                @Override
                public int size() {
                    return MappingChange.this.original.getRemovedSize();
                }
            };
        }
        return this.removed;
    }

    protected int[] getPermutation() {
        return new int[0];
    }

    public boolean wasPermutated() {
        return this.original.wasPermutated();
    }

    public boolean wasUpdated() {
        return this.original.wasUpdated();
    }

    public int getPermutation(int n) {
        return this.original.getPermutation(n);
    }

    public String toString() {
        int n = 0;
        while (this.next()) {
            ++n;
        }
        int n2 = 0;
        this.reset();
        while (this.next()) {
            ++n2;
        }
        this.reset();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ ");
        int n3 = 0;
        while (this.next()) {
            if (this.wasPermutated()) {
                stringBuilder.append(ChangeHelper.permChangeToString(this.getPermutation()));
            } else if (this.wasUpdated()) {
                stringBuilder.append(ChangeHelper.updateChangeToString(this.getFrom(), this.getTo()));
            } else {
                stringBuilder.append(ChangeHelper.addRemoveChangeToString(this.getFrom(), this.getTo(), this.getList(), this.getRemoved()));
            }
            if (n3 == n2) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(" }");
        this.reset();
        n3 = n2 - n;
        while (n3-- > 0) {
            this.next();
        }
        return stringBuilder.toString();
    }

    public static interface Map<E, F> {
        public F map(E var1);
    }
}

