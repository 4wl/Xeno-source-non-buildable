/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.collections.ObservableSet
 *  javafx.collections.SetChangeListener
 *  javafx.collections.SetChangeListener$Change
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.SetListenerHelper;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class ObservableSetWrapper<E>
implements ObservableSet<E> {
    private final Set<E> backingSet;
    private SetListenerHelper<E> listenerHelper;

    public ObservableSetWrapper(Set<E> set) {
        this.backingSet = set;
    }

    private void callObservers(SetChangeListener.Change<E> change) {
        SetListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
    }

    public void addListener(InvalidationListener invalidationListener) {
        this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, invalidationListener);
    }

    public void addListener(SetChangeListener<? super E> setChangeListener) {
        this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, setChangeListener);
    }

    public void removeListener(SetChangeListener<? super E> setChangeListener) {
        this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, setChangeListener);
    }

    public int size() {
        return this.backingSet.size();
    }

    public boolean isEmpty() {
        return this.backingSet.isEmpty();
    }

    public boolean contains(Object object) {
        return this.backingSet.contains(object);
    }

    public Iterator iterator() {
        return new Iterator<E>(){
            private final Iterator<E> backingIt;
            private E lastElement;
            {
                this.backingIt = ObservableSetWrapper.this.backingSet.iterator();
            }

            @Override
            public boolean hasNext() {
                return this.backingIt.hasNext();
            }

            @Override
            public E next() {
                this.lastElement = this.backingIt.next();
                return this.lastElement;
            }

            @Override
            public void remove() {
                this.backingIt.remove();
                ObservableSetWrapper.this.callObservers(new SimpleRemoveChange(this.lastElement));
            }
        };
    }

    public Object[] toArray() {
        return this.backingSet.toArray();
    }

    public <T> T[] toArray(T[] arrT) {
        return this.backingSet.toArray(arrT);
    }

    public boolean add(E e) {
        boolean bl = this.backingSet.add(e);
        if (bl) {
            this.callObservers(new SimpleAddChange(e));
        }
        return bl;
    }

    public boolean remove(Object object) {
        boolean bl = this.backingSet.remove(object);
        if (bl) {
            this.callObservers(new SimpleRemoveChange(object));
        }
        return bl;
    }

    public boolean containsAll(Collection<?> collection) {
        return this.backingSet.containsAll(collection);
    }

    public boolean addAll(Collection<? extends E> collection) {
        boolean bl = false;
        for (E e : collection) {
            bl |= this.add(e);
        }
        return bl;
    }

    public boolean retainAll(Collection<?> collection) {
        return this.removeRetain(collection, false);
    }

    public boolean removeAll(Collection<?> collection) {
        return this.removeRetain(collection, true);
    }

    private boolean removeRetain(Collection<?> collection, boolean bl) {
        boolean bl2 = false;
        Iterator<E> iterator = this.backingSet.iterator();
        while (iterator.hasNext()) {
            E e = iterator.next();
            if (bl != collection.contains(e)) continue;
            bl2 = true;
            iterator.remove();
            this.callObservers(new SimpleRemoveChange(e));
        }
        return bl2;
    }

    public void clear() {
        Iterator<E> iterator = this.backingSet.iterator();
        while (iterator.hasNext()) {
            E e = iterator.next();
            iterator.remove();
            this.callObservers(new SimpleRemoveChange(e));
        }
    }

    public String toString() {
        return this.backingSet.toString();
    }

    public boolean equals(Object object) {
        return this.backingSet.equals(object);
    }

    public int hashCode() {
        return this.backingSet.hashCode();
    }

    private class SimpleRemoveChange
    extends SetChangeListener.Change<E> {
        private final E removed;

        public SimpleRemoveChange(E e) {
            super((ObservableSet)ObservableSetWrapper.this);
            this.removed = e;
        }

        public boolean wasAdded() {
            return false;
        }

        public boolean wasRemoved() {
            return true;
        }

        public E getElementAdded() {
            return null;
        }

        public E getElementRemoved() {
            return this.removed;
        }

        public String toString() {
            return "removed " + this.removed;
        }
    }

    private class SimpleAddChange
    extends SetChangeListener.Change<E> {
        private final E added;

        public SimpleAddChange(E e) {
            super((ObservableSet)ObservableSetWrapper.this);
            this.added = e;
        }

        public boolean wasAdded() {
            return true;
        }

        public boolean wasRemoved() {
            return false;
        }

        public E getElementAdded() {
            return this.added;
        }

        public E getElementRemoved() {
            return null;
        }

        public String toString() {
            return "added " + this.added;
        }
    }
}

