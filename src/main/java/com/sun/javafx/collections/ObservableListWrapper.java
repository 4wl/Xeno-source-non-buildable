/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.collections.ModifiableObservableListBase
 *  javafx.collections.ObservableList
 *  javafx.util.Callback
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ElementObserver;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import com.sun.javafx.collections.SortableList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class ObservableListWrapper<E>
extends ModifiableObservableListBase<E>
implements ObservableList<E>,
SortableList<E>,
RandomAccess {
    private final List<E> backingList;
    private final ElementObserver elementObserver;
    private SortHelper helper;

    public ObservableListWrapper(List<E> list) {
        this.backingList = list;
        this.elementObserver = null;
    }

    public ObservableListWrapper(List<E> list, Callback<E, Observable[]> callback) {
        this.backingList = list;
        this.elementObserver = new ElementObserver<E>(callback, new Callback<E, InvalidationListener>(){

            public InvalidationListener call(final E e) {
                return new InvalidationListener(){

                    public void invalidated(Observable observable) {
                        ObservableListWrapper.this.beginChange();
                        int n = ObservableListWrapper.this.size();
                        for (int i = 0; i < n; ++i) {
                            if (ObservableListWrapper.this.get(i) != e) continue;
                            ObservableListWrapper.this.nextUpdate(i);
                        }
                        ObservableListWrapper.this.endChange();
                    }
                };
            }
        }, this);
        int n = this.backingList.size();
        for (int i = 0; i < n; ++i) {
            this.elementObserver.attachListener(this.backingList.get(i));
        }
    }

    @Override
    public E get(int n) {
        return this.backingList.get(n);
    }

    @Override
    public int size() {
        return this.backingList.size();
    }

    protected void doAdd(int n, E e) {
        if (this.elementObserver != null) {
            this.elementObserver.attachListener(e);
        }
        this.backingList.add(n, e);
    }

    protected E doSet(int n, E e) {
        E e2 = this.backingList.set(n, e);
        if (this.elementObserver != null) {
            this.elementObserver.detachListener(e2);
            this.elementObserver.attachListener(e);
        }
        return e2;
    }

    protected E doRemove(int n) {
        E e = this.backingList.remove(n);
        if (this.elementObserver != null) {
            this.elementObserver.detachListener(e);
        }
        return e;
    }

    @Override
    public int indexOf(Object object) {
        return this.backingList.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.backingList.lastIndexOf(object);
    }

    @Override
    public boolean contains(Object object) {
        return this.backingList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.backingList.containsAll(collection);
    }

    @Override
    public void clear() {
        if (this.elementObserver != null) {
            int n = this.size();
            for (int i = 0; i < n; ++i) {
                this.elementObserver.detachListener(this.get(i));
            }
        }
        if (this.hasListeners()) {
            this.beginChange();
            this.nextRemove(0, this);
        }
        this.backingList.clear();
        ++this.modCount;
        if (this.hasListeners()) {
            this.endChange();
        }
    }

    public void remove(int n, int n2) {
        this.beginChange();
        for (int i = n; i < n2; ++i) {
            this.remove(n);
        }
        this.endChange();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        int n;
        this.beginChange();
        BitSet bitSet = new BitSet(collection.size());
        for (n = 0; n < this.size(); ++n) {
            if (!collection.contains(this.get(n))) continue;
            bitSet.set(n);
        }
        if (!bitSet.isEmpty()) {
            n = this.size();
            while ((n = bitSet.previousSetBit(n - 1)) >= 0) {
                this.remove(n);
            }
        }
        this.endChange();
        return !bitSet.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        int n;
        this.beginChange();
        BitSet bitSet = new BitSet(collection.size());
        for (n = 0; n < this.size(); ++n) {
            if (collection.contains(this.get(n))) continue;
            bitSet.set(n);
        }
        if (!bitSet.isEmpty()) {
            n = this.size();
            while ((n = bitSet.previousSetBit(n - 1)) >= 0) {
                this.remove(n);
            }
        }
        this.endChange();
        return !bitSet.isEmpty();
    }

    @Override
    public void sort() {
        if (this.backingList.isEmpty()) {
            return;
        }
        int[] arrn = this.getSortHelper().sort(this.backingList);
        this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size(), arrn, this));
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        if (this.backingList.isEmpty()) {
            return;
        }
        int[] arrn = this.getSortHelper().sort(this.backingList, comparator);
        this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size(), arrn, this));
    }

    private SortHelper getSortHelper() {
        if (this.helper == null) {
            this.helper = new SortHelper();
        }
        return this.helper;
    }
}

