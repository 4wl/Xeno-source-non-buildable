/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.collections.ObservableListBase
 *  javafx.collections.WeakListChangeListener
 *  javafx.util.Callback
 */
package com.sun.javafx.collections;

import com.sun.javafx.collections.ElementObserver;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.WeakListChangeListener;
import javafx.util.Callback;

public final class ElementObservableListDecorator<E>
extends ObservableListBase<E>
implements ObservableList<E> {
    private final ObservableList<E> decoratedList;
    private final ListChangeListener<E> listener;
    private ElementObserver<E> observer;

    public ElementObservableListDecorator(ObservableList<E> observableList, Callback<E, Observable[]> callback) {
        this.observer = new ElementObserver<E>(callback, new Callback<E, InvalidationListener>(){

            public InvalidationListener call(final E e) {
                return new InvalidationListener(){

                    public void invalidated(Observable observable) {
                        int n;
                        ElementObservableListDecorator.this.beginChange();
                        if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                            int n2 = ElementObservableListDecorator.this.size();
                            for (n = 0; n < n2; ++n) {
                                if (ElementObservableListDecorator.this.get(n) != e) continue;
                                ElementObservableListDecorator.this.nextUpdate(n);
                            }
                        } else {
                            Iterator iterator = ElementObservableListDecorator.this.iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next() == e) {
                                    ElementObservableListDecorator.this.nextUpdate(n);
                                }
                                ++n;
                            }
                        }
                        ElementObservableListDecorator.this.endChange();
                    }
                };
            }
        }, this);
        this.decoratedList = observableList;
        int n = this.decoratedList.size();
        for (int i = 0; i < n; ++i) {
            this.observer.attachListener(this.decoratedList.get(i));
        }
        this.listener = new ListChangeListener<E>(){

            public void onChanged(ListChangeListener.Change<? extends E> change) {
                while (change.next()) {
                    int n;
                    if (!change.wasAdded() && !change.wasRemoved()) continue;
                    int n2 = change.getRemovedSize();
                    List list = change.getRemoved();
                    for (n = 0; n < n2; ++n) {
                        ElementObservableListDecorator.this.observer.detachListener(list.get(n));
                    }
                    if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                        n = change.getTo();
                        for (int i = change.getFrom(); i < n; ++i) {
                            ElementObservableListDecorator.this.observer.attachListener(ElementObservableListDecorator.this.decoratedList.get(i));
                        }
                        continue;
                    }
                    for (Object e : change.getAddedSubList()) {
                        ElementObservableListDecorator.this.observer.attachListener(e);
                    }
                }
                change.reset();
                ElementObservableListDecorator.this.fireChange(change);
            }
        };
        this.decoratedList.addListener((ListChangeListener)new WeakListChangeListener(this.listener));
    }

    public <T> T[] toArray(T[] arrT) {
        return this.decoratedList.toArray((Object[])arrT);
    }

    public Object[] toArray() {
        return this.decoratedList.toArray();
    }

    public List<E> subList(int n, int n2) {
        return this.decoratedList.subList(n, n2);
    }

    public int size() {
        return this.decoratedList.size();
    }

    public E set(int n, E e) {
        return (E)this.decoratedList.set(n, e);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.decoratedList.retainAll(collection);
    }

    public boolean removeAll(Collection<?> collection) {
        return this.decoratedList.removeAll(collection);
    }

    public E remove(int n) {
        return (E)this.decoratedList.remove(n);
    }

    public boolean remove(Object object) {
        return this.decoratedList.remove(object);
    }

    public ListIterator<E> listIterator(int n) {
        return this.decoratedList.listIterator(n);
    }

    public ListIterator<E> listIterator() {
        return this.decoratedList.listIterator();
    }

    public int lastIndexOf(Object object) {
        return this.decoratedList.lastIndexOf(object);
    }

    public Iterator<E> iterator() {
        return this.decoratedList.iterator();
    }

    public boolean isEmpty() {
        return this.decoratedList.isEmpty();
    }

    public int indexOf(Object object) {
        return this.decoratedList.indexOf(object);
    }

    public E get(int n) {
        return (E)this.decoratedList.get(n);
    }

    public boolean containsAll(Collection<?> collection) {
        return this.decoratedList.containsAll(collection);
    }

    public boolean contains(Object object) {
        return this.decoratedList.contains(object);
    }

    public void clear() {
        this.decoratedList.clear();
    }

    public boolean addAll(int n, Collection<? extends E> collection) {
        return this.decoratedList.addAll(n, collection);
    }

    public boolean addAll(Collection<? extends E> collection) {
        return this.decoratedList.addAll(collection);
    }

    public void add(int n, E e) {
        this.decoratedList.add(n, e);
    }

    public boolean add(E e) {
        return this.decoratedList.add(e);
    }

    public boolean setAll(Collection<? extends E> collection) {
        return this.decoratedList.setAll(collection);
    }

    public boolean setAll(E ... arrE) {
        return this.decoratedList.setAll((Object[])arrE);
    }

    public boolean retainAll(E ... arrE) {
        return this.decoratedList.retainAll((Object[])arrE);
    }

    public boolean removeAll(E ... arrE) {
        return this.decoratedList.removeAll((Object[])arrE);
    }

    public void remove(int n, int n2) {
        this.decoratedList.remove(n, n2);
    }

    public boolean addAll(E ... arrE) {
        return this.decoratedList.addAll((Object[])arrE);
    }
}

