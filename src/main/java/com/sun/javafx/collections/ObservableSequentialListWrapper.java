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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public final class ObservableSequentialListWrapper<E>
extends ModifiableObservableListBase<E>
implements ObservableList<E>,
SortableList<E> {
    private final List<E> backingList;
    private final ElementObserver elementObserver;
    private SortHelper helper;

    public ObservableSequentialListWrapper(List<E> list) {
        this.backingList = list;
        this.elementObserver = null;
    }

    public ObservableSequentialListWrapper(List<E> list, Callback<E, Observable[]> callback) {
        this.backingList = list;
        this.elementObserver = new ElementObserver<E>(callback, new Callback<E, InvalidationListener>(){

            public InvalidationListener call(final E e) {
                return new InvalidationListener(){

                    public void invalidated(Observable observable) {
                        ObservableSequentialListWrapper.this.beginChange();
                        int n = 0;
                        Iterator iterator = ObservableSequentialListWrapper.this.backingList.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next() == e) {
                                ObservableSequentialListWrapper.this.nextUpdate(n);
                            }
                            ++n;
                        }
                        ObservableSequentialListWrapper.this.endChange();
                    }
                };
            }
        }, this);
        for (E e : this.backingList) {
            this.elementObserver.attachListener(e);
        }
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
    public int indexOf(Object object) {
        return this.backingList.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return this.backingList.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator(final int n) {
        return new ListIterator<E>(){
            private final ListIterator<E> backingIt;
            private E lastReturned;
            {
                this.backingIt = ObservableSequentialListWrapper.this.backingList.listIterator(n);
            }

            @Override
            public boolean hasNext() {
                return this.backingIt.hasNext();
            }

            @Override
            public E next() {
                this.lastReturned = this.backingIt.next();
                return this.lastReturned;
            }

            @Override
            public boolean hasPrevious() {
                return this.backingIt.hasPrevious();
            }

            @Override
            public E previous() {
                this.lastReturned = this.backingIt.previous();
                return this.lastReturned;
            }

            @Override
            public int nextIndex() {
                return this.backingIt.nextIndex();
            }

            @Override
            public int previousIndex() {
                return this.backingIt.previousIndex();
            }

            @Override
            public void remove() {
                ObservableSequentialListWrapper.this.beginChange();
                int n2 = this.previousIndex();
                this.backingIt.remove();
                ObservableSequentialListWrapper.this.nextRemove(n2, this.lastReturned);
                ObservableSequentialListWrapper.this.endChange();
            }

            @Override
            public void set(E e) {
                ObservableSequentialListWrapper.this.beginChange();
                int n2 = this.previousIndex();
                this.backingIt.set(e);
                ObservableSequentialListWrapper.this.nextSet(n2, this.lastReturned);
                ObservableSequentialListWrapper.this.endChange();
            }

            @Override
            public void add(E e) {
                ObservableSequentialListWrapper.this.beginChange();
                int n2 = this.nextIndex();
                this.backingIt.add(e);
                ObservableSequentialListWrapper.this.nextAdd(n2, n2 + 1);
                ObservableSequentialListWrapper.this.endChange();
            }
        };
    }

    @Override
    public Iterator<E> iterator() {
        return this.listIterator();
    }

    @Override
    public E get(int n) {
        try {
            return this.backingList.listIterator(n).next();
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n);
        }
    }

    @Override
    public boolean addAll(int n, Collection<? extends E> collection) {
        try {
            this.beginChange();
            boolean bl = false;
            ListIterator<E> listIterator = this.listIterator(n);
            Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                listIterator.add(iterator.next());
                bl = true;
            }
            this.endChange();
            return bl;
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n);
        }
    }

    @Override
    public int size() {
        return this.backingList.size();
    }

    protected void doAdd(int n, E e) {
        try {
            this.backingList.listIterator(n).add(e);
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n);
        }
    }

    protected E doSet(int n, E e) {
        try {
            ListIterator<E> listIterator = this.backingList.listIterator(n);
            E e2 = listIterator.next();
            listIterator.set(e);
            return e2;
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n);
        }
    }

    protected E doRemove(int n) {
        try {
            ListIterator<E> listIterator = this.backingList.listIterator(n);
            E e = listIterator.next();
            listIterator.remove();
            return e;
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IndexOutOfBoundsException("Index: " + n);
        }
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

