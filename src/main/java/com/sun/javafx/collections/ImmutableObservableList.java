/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 */
package com.sun.javafx.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ImmutableObservableList<E>
extends AbstractList<E>
implements ObservableList<E> {
    private final E[] elements;

    public ImmutableObservableList(E ... arrE) {
        this.elements = arrE == null || arrE.length == 0 ? null : Arrays.copyOf(arrE, arrE.length);
    }

    public void addListener(InvalidationListener invalidationListener) {
    }

    public void removeListener(InvalidationListener invalidationListener) {
    }

    public void addListener(ListChangeListener<? super E> listChangeListener) {
    }

    public void removeListener(ListChangeListener<? super E> listChangeListener) {
    }

    public boolean addAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    public boolean setAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    public boolean setAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(E ... arrE) {
        throw new UnsupportedOperationException();
    }

    public void remove(int n, int n2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int n) {
        if (n < 0 || n >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        return this.elements[n];
    }

    @Override
    public int size() {
        return this.elements == null ? 0 : this.elements.length;
    }
}

