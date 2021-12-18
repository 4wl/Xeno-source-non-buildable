/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ChangeHelper;
import java.util.Collections;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class NonIterableChange<E>
extends ListChangeListener.Change<E> {
    private final int from;
    private final int to;
    private boolean invalid = true;
    private static final int[] EMPTY_PERM = new int[0];

    protected NonIterableChange(int n, int n2, ObservableList<E> observableList) {
        super(observableList);
        this.from = n;
        this.to = n2;
    }

    public int getFrom() {
        this.checkState();
        return this.from;
    }

    public int getTo() {
        this.checkState();
        return this.to;
    }

    protected int[] getPermutation() {
        this.checkState();
        return EMPTY_PERM;
    }

    public boolean next() {
        if (this.invalid) {
            this.invalid = false;
            return true;
        }
        return false;
    }

    public void reset() {
        this.invalid = true;
    }

    public void checkState() {
        if (this.invalid) {
            throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
        }
    }

    public String toString() {
        boolean bl = this.invalid;
        this.invalid = false;
        String string = this.wasPermutated() ? ChangeHelper.permChangeToString(this.getPermutation()) : (this.wasUpdated() ? ChangeHelper.updateChangeToString(this.from, this.to) : ChangeHelper.addRemoveChangeToString(this.from, this.to, this.getList(), this.getRemoved()));
        this.invalid = bl;
        return "{ " + string + " }";
    }

    public static class SimpleUpdateChange<E>
    extends NonIterableChange<E> {
        public SimpleUpdateChange(int n, ObservableList<E> observableList) {
            this(n, n + 1, observableList);
        }

        public SimpleUpdateChange(int n, int n2, ObservableList<E> observableList) {
            super(n, n2, observableList);
        }

        public List<E> getRemoved() {
            return Collections.emptyList();
        }

        public boolean wasUpdated() {
            return true;
        }
    }

    public static class SimplePermutationChange<E>
    extends NonIterableChange<E> {
        private final int[] permutation;

        public SimplePermutationChange(int n, int n2, int[] arrn, ObservableList<E> observableList) {
            super(n, n2, observableList);
            this.permutation = arrn;
        }

        public List<E> getRemoved() {
            this.checkState();
            return Collections.emptyList();
        }

        @Override
        protected int[] getPermutation() {
            this.checkState();
            return this.permutation;
        }
    }

    public static class SimpleAddChange<E>
    extends NonIterableChange<E> {
        public SimpleAddChange(int n, int n2, ObservableList<E> observableList) {
            super(n, n2, observableList);
        }

        public boolean wasRemoved() {
            this.checkState();
            return false;
        }

        public List<E> getRemoved() {
            this.checkState();
            return Collections.emptyList();
        }
    }

    public static class SimpleRemovedChange<E>
    extends NonIterableChange<E> {
        private final List<E> removed;

        public SimpleRemovedChange(int n, int n2, E e, ObservableList<E> observableList) {
            super(n, n2, observableList);
            this.removed = Collections.singletonList(e);
        }

        public boolean wasRemoved() {
            this.checkState();
            return true;
        }

        public List<E> getRemoved() {
            this.checkState();
            return this.removed;
        }
    }

    public static class GenericAddRemoveChange<E>
    extends NonIterableChange<E> {
        private final List<E> removed;

        public GenericAddRemoveChange(int n, int n2, List<E> list, ObservableList<E> observableList) {
            super(n, n2, observableList);
            this.removed = list;
        }

        public List<E> getRemoved() {
            this.checkState();
            return this.removed;
        }
    }
}

