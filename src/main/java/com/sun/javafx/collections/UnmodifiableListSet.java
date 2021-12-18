/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

public final class UnmodifiableListSet<E>
extends AbstractSet<E> {
    private List<E> backingList;

    public UnmodifiableListSet(List<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        this.backingList = list;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> iterator = this.backingList.iterator();
        return new Iterator<E>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int size() {
        return this.backingList.size();
    }
}

