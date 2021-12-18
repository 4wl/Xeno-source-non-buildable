/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.collections.ObservableListBase
 *  javafx.util.Callback
 */
package com.sun.javafx.collections;

import java.util.IdentityHashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableListBase;
import javafx.util.Callback;

final class ElementObserver<E> {
    private Callback<E, Observable[]> extractor;
    private final Callback<E, InvalidationListener> listenerGenerator;
    private final ObservableListBase<E> list;
    private IdentityHashMap<E, ElementsMapElement> elementsMap = new IdentityHashMap();

    ElementObserver(Callback<E, Observable[]> callback, Callback<E, InvalidationListener> callback2, ObservableListBase<E> observableListBase) {
        this.extractor = callback;
        this.listenerGenerator = callback2;
        this.list = observableListBase;
    }

    void attachListener(E e) {
        if (this.elementsMap != null && e != null) {
            if (this.elementsMap.containsKey(e)) {
                this.elementsMap.get(e).increment();
            } else {
                InvalidationListener invalidationListener = (InvalidationListener)this.listenerGenerator.call(e);
                for (Observable observable : (Observable[])this.extractor.call(e)) {
                    observable.addListener(invalidationListener);
                }
                this.elementsMap.put(e, new ElementsMapElement(invalidationListener));
            }
        }
    }

    void detachListener(E e) {
        if (this.elementsMap != null && e != null) {
            ElementsMapElement elementsMapElement = this.elementsMap.get(e);
            for (Observable observable : (Observable[])this.extractor.call(e)) {
                observable.removeListener(elementsMapElement.getListener());
            }
            if (elementsMapElement.decrement() == 0) {
                this.elementsMap.remove(e);
            }
        }
    }

    private static class ElementsMapElement {
        InvalidationListener listener;
        int counter;

        public ElementsMapElement(InvalidationListener invalidationListener) {
            this.listener = invalidationListener;
            this.counter = 1;
        }

        public void increment() {
            ++this.counter;
        }

        public int decrement() {
            return --this.counter;
        }

        private InvalidationListener getListener() {
            return this.listener;
        }
    }
}

