/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 */
package com.sun.javafx.scene.control;

import com.sun.javafx.collections.ListListenerHelper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ReadOnlyUnbackedObservableList<E>
implements ObservableList<E> {
    private ListListenerHelper<E> listenerHelper;

    public abstract E get(int var1);

    public abstract int size();

    public void addListener(InvalidationListener invalidationListener) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, invalidationListener);
    }

    public void addListener(ListChangeListener<? super E> listChangeListener) {
        this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, listChangeListener);
    }

    public void removeListener(ListChangeListener<? super E> listChangeListener) {
        this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, listChangeListener);
    }

    public void callObservers(ListChangeListener.Change<E> change) {
        ListListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
    }

    public int indexOf(Object object) {
        if (object == null) {
            return -1;
        }
        for (int i = 0; i < this.size(); ++i) {
            E e = this.get(i);
            if (!object.equals(e)) continue;
            return i;
        }
        return -1;
    }

    public int lastIndexOf(Object object) {
        if (object == null) {
            return -1;
        }
        for (int i = this.size() - 1; i >= 0; --i) {
            E e = this.get(i);
            if (!object.equals(e)) continue;
            return i;
        }
        return -1;
    }

    public boolean contains(Object object) {
        return this.indexOf(object) != -1;
    }

    public boolean containsAll(Collection<?> collection) {
        for (Object obj : collection) {
            if (this.contains(obj)) continue;
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public ListIterator<E> listIterator() {
        return new SelectionListIterator(this);
    }

    public ListIterator<E> listIterator(int n) {
        return new SelectionListIterator(this, n);
    }

    public Iterator<E> iterator() {
        return new SelectionListIterator(this);
    }

    public List<E> subList(int n, int n2) {
        if (n < 0 || n2 > this.size() || n > n2) {
            throw new IndexOutOfBoundsException();
        }
        ReadOnlyUnbackedObservableList readOnlyUnbackedObservableList = this;
        return new ReadOnlyUnbackedObservableList<E>((List)((Object)readOnlyUnbackedObservableList), n, n2){
            final /* synthetic */ List val$outer;
            final /* synthetic */ int val$fromIndex;
            final /* synthetic */ int val$toIndex;
            {
                this.val$outer = list;
                this.val$fromIndex = n;
                this.val$toIndex = n2;
            }

            @Override
            public E get(int n) {
                return this.val$outer.get(n + this.val$fromIndex);
            }

            @Override
            public int size() {
                return this.val$toIndex - this.val$fromIndex;
            }
        };
    }

    public Object[] toArray() {
        Object[] arrobject = new Object[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            arrobject[i] = this.get(i);
        }
        return arrobject;
    }

    public <T> T[] toArray(T[] arrT) {
        Object[] arrobject = this.toArray();
        int n = arrobject.length;
        if (arrT.length < n) {
            return Arrays.copyOf(arrobject, n, arrT.getClass());
        }
        System.arraycopy(arrobject, 0, arrT, 0, n);
        if (arrT.length > n) {
            arrT[n] = null;
        }
        return arrT;
    }

    public String toString() {
        Iterator<E> iterator = this.iterator();
        if (!iterator.hasNext()) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        while (true) {
            E e;
            stringBuilder.append((Object)((e = iterator.next()) == this ? "(this Collection)" : e));
            if (!iterator.hasNext()) {
                return stringBuilder.append(']').toString();
            }
            stringBuilder.append(", ");
        }
    }

    public boolean add(E e) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void add(int n, E e) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean addAll(int n, Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean addAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public E set(int n, E e) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean setAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean setAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public E remove(int n) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean remove(Object object) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void remove(int n, int n2) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean removeAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean retainAll(E ... arrE) {
        throw new UnsupportedOperationException("Not supported.");
    }

    private static class SelectionListIterator<E>
    implements ListIterator<E> {
        private int pos;
        private final ReadOnlyUnbackedObservableList<E> list;

        public SelectionListIterator(ReadOnlyUnbackedObservableList<E> readOnlyUnbackedObservableList) {
            this(readOnlyUnbackedObservableList, 0);
        }

        public SelectionListIterator(ReadOnlyUnbackedObservableList<E> readOnlyUnbackedObservableList, int n) {
            this.list = readOnlyUnbackedObservableList;
            this.pos = n;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.list.size();
        }

        @Override
        public E next() {
            return this.list.get(this.pos++);
        }

        @Override
        public boolean hasPrevious() {
            return this.pos > 0;
        }

        @Override
        public E previous() {
            return this.list.get(this.pos--);
        }

        @Override
        public int nextIndex() {
            return this.pos + 1;
        }

        @Override
        public int previousIndex() {
            return this.pos - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
}

